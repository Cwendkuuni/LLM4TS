import org.junit.Test;
import static org.junit.Assert.*;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourceFile;

public class ScopedAliasesTest {

  @Test
  public void testScopedAliases() {
    String input =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  var DIV = dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    Node expectedOutput = new Node("GoogDomcreateElement") {{
      setType(Token.NAME);
      setString("GoogDomcreateElement");
    }};

    // Verify that the aliases were correctly applied.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<Node> aliasUsages = traversal.getAliasUsages();
    assertEquals("Expected one alias usage.", 1, aliasUsages.size());
    Node aliasUsage = aliasUsages.get(0);
    Node aliasDefinition = aliasUsage.applyAlias().getParent();
    assertEquals("Expected alias definition to be a call to GoogDomcreateElement.",
        expectedOutput, aliasDefinition);
  }

  @Test
  public void testScopedAliasesImproperlyUsed() {
    String input =
        "goog.scope(function() {" +
        "  var dom = goog.dom;" +
        "  goog.scope(function() {" +
        "    dom.createElement('DIV');" +
        "  });" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    // Verify that the test fails due to improper use of goog.scope.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertTrue("Expected one error.", errors.size() == 1);
    DiagnosticType error = errors.get(0);
    String expectedMessage = "JSC_GOOG_SCOPE_USED_IMPROPERLY";
    assertEquals("Expected error message to be JSC_GOOG_SCOPE_USED_IMPROPERLY.",
        expectedMessage, error.getTypeName());
  }

  @Test
  public void testScopedAliasesHasBadParameters() {
    String input =
        "goog.scope(function() {" +
        "  function foo() {}" +
        "  goog.scope(foo);" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    // Verify that the test fails due to bad parameters.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertTrue("Expected one error.", errors.size() == 1);
    DiagnosticType error = errors.get(0);
    String expectedMessage = "JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS";
    assertEquals("Expected error message to be JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS.",
        expectedMessage, error.getTypeName());
  }

  @Test
  public void testScopedAliasesReferencesThis() {
    String input =
        "goog.scope(function() {" +
        "  this.foo = 'bar';" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    // Verify that the test fails due to reference to 'this'.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertTrue("Expected one error.", errors.size() == 1);
    DiagnosticType error = errors.get(0);
    String expectedMessage = "JSC_GOOG_SCOPE_REFERENCES_THIS";
    assertEquals("Expected error message to be JSC_GOOG_SCOPE_REFERENCES_THIS.",
        expectedMessage, error.getTypeName());
  }

  @Test
  public void testScopedAliasesUsesReturn() {
    String input =
        "goog.scope(function() {" +
        "  return 42;" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    // Verify that the test fails due to use of 'return'.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertTrue("Expected one error.", errors.size() == 1);
    DiagnosticType error = errors.get(0);
    String expectedMessage = "JSC_GOOG_SCOPE_USES_RETURN";
    assertEquals("Expected error message to be JSC_GOOG_SCOPE_USES_RETURN.",
        expectedMessage, error.getTypeName());
  }

  @Test
  public void testScopedAliasesUsesThrow() {
    String input =
        "goog.scope(function() {" +
        "  throw new Error('Test');" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    // Verify that the test fails due to use of 'throw'.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertTrue("Expected one error.", errors.size() == 1);
    DiagnosticType error = errors.get(0);
    String expectedMessage = "JSC_GOOG_SCOPE_USES_THROW";
    assertEquals("Expected error message to be JSC_GOOG_SCOPE_USES_THROW.",
        expectedMessage, error.getTypeName());
  }

  @Test
  public void testScopedAliasesAliasesRedefined() {
    String input =
        "goog.scope(function() {" +
        "  var foo = 'bar';" +
        "  goog.scope(function() {" +
        "    var foo = 42;" +
        "  });" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    // Verify that the test fails due to alias being redefined.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertTrue("Expected one error.", errors.size() == 1);
    DiagnosticType error = errors.get(0);
    String expectedMessage = "JSC_GOOG_SCOPE_ALIAS_REDEFINED";
    assertEquals("Expected error message to be JSC_GOOG_SCOPE_ALIAS_REDEFINED.",
        expectedMessage, error.getTypeName());
  }

  @Test
  public void testScopedAliasesNonAliasLocal() {
    String input =
        "goog.scope(function() {" +
        "  var foo = 'bar';" +
        "  var bar = 42;" +
        "});";

    SourceFile sourceFile = new SourceFile("Test.js", input);
    Compiler compiler = new Compiler();
    compiler.setOptions(new CompilerOptions() {{
      add(AliasTransformation.ENABLED, true);
    }});

    HotSwapCompilerPass scopedAliases = new ScopedAliases(compiler, null, null);
    Node root = compiler.parseTree(sourceFile);
    scopedAliases.process(null, root);

    // Verify that the test fails due to non-alias local.
    Traversal traversal = new Traversal();
    NodeTraversal.traverse(compiler, root, traversal);
    List<DiagnosticType> errors = traversal.getErrors();
    assertTrue("Expected one error.", errors.size() == 1);
    DiagnosticType error = errors.get(0);
    String expectedMessage = "JSC_GOOG_SCOPE_NON_ALIAS_LOCAL";
    assertEquals("Expected error message to be JSC_GOOG_SCOPE_NON_ALIAS_LOCAL.",
        expectedMessage, error.getTypeName());
  }
}