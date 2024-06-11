import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.AliasTransformationHandler;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;

import java.util.List;

public class ScopedAliasesTest {

  private static final String CLASS_NAME = "ScopedAliases";

  @SuppressWarnings("unchecked")
  private static final AliasTransformationHandler HANDLER = new AliasTransformationHandler() {
    @Override
    public void logAliasTransformation(String fileName, SourcePosition position) {
      System.out.println("Logging alias transformation: " + fileName + ":" + position);
    }
  };

  @SuppressWarnings("unchecked")
  private static HotSwapCompilerPass createScopedAliases() {
    return new ScopedAliases(
        new Compiler(),
        null,
        HANDLER);
  }

  @Test
  public void testScopingMethodName() {
    String sourceCode = "function test() {" +
        "goog.scope(function() {" +
        "goog.scope = function() {};" +
        "});" +
        "assert(goog.scope === undefined);" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testMultipleAliases() {
    String sourceCode = "function test() {" +
        "goog.scope(function() {" +
        "var x = goog.x;" +
        "var y = goog.y;" +
        "});" +
        "assert(typeof x === 'function')";
        // TODO: Add assertions for 'y' as well
        +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testScopeCallWithNoParameters() {
    String sourceCode = "function test() {" +
        "goog.scope();" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = ((ScopedAliases) scopedAliases).getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node functionNode = scopeCall.getChildAtIndex(1);
    assertTrue(functionNode.isFunction());
    assertEquals(0, functionNode.getChildCount());

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testScopeCallWithParameters() {
    String sourceCode = "function test() {" +
        "goog.scope(function(a, b) {" +
        "});" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = ((ScopedAliases) scopedAliases).getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals(2, scopeCall.getChildCount());
    Node functionNode = scopeCall.getChildAtIndex(1);
    assertTrue(functionNode.isFunction());
    assertEquals(2, functionNode.getChildCount());
    Node paramA = functionNode.getChildAtIndex(0);
    assertEquals(Token.VAR, paramA.getType());
    Node paramB = functionNode.getChildAtIndex(1);
    assertEquals(Token.VAR, paramB.getType());

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testScopeCallWithBadCode() {
    String sourceCode = "function test() {" +
        "goog.scope();" +
        "goog.scope(function() {})";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<Node> scopeCalls = ((ScopedAliases) scopedAliases).getScopeCalls();
    assertEquals(2, scopeCalls.size());

    String expectedError = "JSC_GOOG_SCOPE_USED_IMPROPERLY";
    List<String> expectedErrors = ImmutableList.of(
        "test.js:1:1: " + expectedError,
        "test.js:3:1: " + expectedError
    );
    List<String> actualErrors = compiler.getDiagnostics();
    assertTrue(actualErrors.equals(expectedErrors));

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testScopeCallWithThisKeyword() {
    String sourceCode = "function test() {" +
        "goog.scope(function() {" +
        "this.x = 1;" +
        "});" +
        "assert(typeof test.x === 'number');" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<String> expectedErrors = ImmutableList.of(
        "test.js:3:1: " + GOOG_SCOPE_REFERENCES_THIS
    );
    List<String> actualErrors = compiler.getDiagnostics();
    assertTrue(actualErrors.equals(expectedErrors));

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testScopeCallWithReturnStatement() {
    String sourceCode = "function test() {" +
        "goog.scope(function() {" +
        "return 1;" +
        "});" +
        "assert(typeof test() === 'number');" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<String> expectedErrors = ImmutableList.of(
        "test.js:3:1: " + GOOG_SCOPE_USES_RETURN
    );
    List<String> actualErrors = compiler.getDiagnostics();
    assertTrue(actualErrors.equals(expectedErrors));

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testScopeCallWithThrowStatement() {
    String sourceCode = "function test() {" +
        "goog.scope(function() {" +
        "throw new Error('Test Error');" +
        "});" +
        "assert(false);" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<String> expectedErrors = ImmutableList.of(
        "Test Error"
    );
    List<String> actualErrors = compiler.getErrorMessages();
    assertTrue(actualErrors.equals(expectedErrors));

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testAliasRedefined() {
    String sourceCode = "function test() {" +
        "goog.scope(function() {" +
        "var x = 1;" +
        "goog.x = 2;" +
        "});" +
        "assert(typeof test.x === 'number')" +
        "assert(typeof test.x === 'number')" +
        "assert(test.x !== test.x);" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<String> expectedErrors = ImmutableList.of(
        "JSC_GOOG_SCOPE_ALIAS_REDEFINED: x"
    );
    List<String> actualErrors = compiler.getDiagnostics();
    assertTrue(actualErrors.equals(expectedErrors));

    assertTrue(warnings.isEmpty());
  }

  @Test
  public void testNonAliasLocalInScope() {
    String sourceCode = "function test() {" +
        "goog.scope(function() {" +
        "var x = 1;" +
        "var y = x;" +
        "});" +
        "assert(typeof test.x === 'number')" +
        "assert(typeof test.y === 'number')" +
        "assert(test.x !== test.y);" +
        "}";

    Compiler compiler = new Compiler();
    HotSwapCompilerPass scopedAliases = createScopedAliases();

    List<String> warnings = ImmutableList.<String>of();
    Node rootNode = compiler.parse(sourceCode, warnings);
    scopedAliases.process(null, rootNode);

    List<String> expectedErrors = ImmutableList.of(
        "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL: x"
    );
    List<String> actualErrors = compiler.getDiagnostics();
    assertTrue(actualErrors.equals(expectedErrors));

    assertTrue(warnings.isEmpty());
  }
}