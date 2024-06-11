import static org.junit.Assert.*;
import static com.google.javascript.jscomp.DiagnosticType.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;
import static com.google.javascript.jscomp.Scope.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticListener;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.rhino.NodeTraversal.ScopedCallback;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test for ScopedAliases class.
 */
public class ScopedAliasesTest {

  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private CompilerOptions options;
  private DiagnosticListener diagnostics;

  @Before
  public void setUp() {
    compiler = new AbstractCompiler() {};
    preprocessorSymbolTable = new PreprocessorSymbolTable() {};
    options = new CompilerOptions() {};
    diagnostics = new DiagnosticListener() {};
  }

  @Test
  public void testValidScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    // Test a valid scoped alias
    Node root = Node.newBlock("goog.scope(function() {" +
        "  var goog = {x: 1}; " +
        "  var y = goog.x; " +
        "});");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    assertEquals("var x = goog.x;", aliasDefinitionsInOrder.get(0).toString());

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    assertEquals("goog.scope(function() {", scopeCalls.get(0).toString());

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    assertEquals("y", aliasUsages.get(0).toString());

    assertFalse(scopedAliases.hasErrors());
  }

  @Test
  public void testInvalidScopedAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    // Test a scope alias with more than one parameter
    Node root = Node.newBlock("goog.scope(function(x, y) {" +
        "});");

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias without a function body
    root = Node.newBlock("goog.scope()");

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_USED_IMPROPERLY,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias with a return statement
    root = Node.newBlock("goog.scope(function() {" +
        "  return 1; " +
        "});");

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_USES_RETURN,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias with a throw statement
    root = Node.newBlock("goog.scope(function() {" +
        "  throw new Error(); " +
        "});");

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_USES_THROW,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias with a name already defined in the scope
    root = Node.newBlock("goog.scope(function() {" +
        "  var x = 1; " +
        "  var x = goog.x; " +
        "});");

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_ALIAS_REDEFINED,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias with a non-alias local variable
    root = Node.newBlock("goog.scope(function() {" +
        "  var x = 1; " +
        "});");

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_NON_ALIAS_LOCAL,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias nested in a function
    root = Node.newBlock("function test() {" +
        "  goog.scope(function() {" +
        "    var x = 1; " +
        "  });" +
        "}");

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_USED_IMPROPERLY,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias with a method reference (this is complicated because
    // the method reference is treated as a property reference and the property
    // reference has to be converted to a method reference)
    root = Node.newBlock("goog.scope(function() {" +
        "  this.method = function() {}; " +
        "  var m = goog.method; " +
        "});");

    Node methodNode = Node.newName("method");
    methodNode.setType(Token.FUNCTION);
    methodNode.setJSDocInfo(new JSDocInfo() {
      @Override
      public Node[] getTypeNodes() {
        return new Node[] {Node.newQualifiedName("Function")};
      }
    });
    root = root.replaceChild(root.getLastChild(), methodNode);

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_USES_THIS,
        scopedAliases.getDiagnostics().get(0).getType());

    // Test a scope alias with a this reference in the function body
    root = Node.newBlock("goog.scope(function() {" +
        "  function test() {" +
        "    this.x = 1; " +
        "  } " +
        "  test(); " +
        "});");

    Node testFunctionNode = Node.newName("test");
    testFunctionNode.setType(Token.FUNCTION);
    Node testFunctionBodyNode = Node.newBlock("this.x = 1;");
    testFunctionNode.setBody(testFunctionBodyNode);

    root = root.replaceChild(root.getLastChild(), testFunctionNode);

    scopedAliases.process(null, root);

    assertTrue(scopedAliases.hasErrors());
    assertEquals(1, scopedAliases.getDiagnostics().size());
    assertEquals(JSC_GOOG_SCOPE_REFERENCES_THIS,
        scopedAliases.getDiagnostics().get(0).getType());
  }

  @Test
  public void testTraversalOrder() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    Node root = Node.newBlock("goog.scope(function() {" +
        "  var a = 1; " +
        "  var b = 2; " +
        "  var c = 3; " +
        "  var d = 4; " +
        "  var goog = {x: goog.a}; " +
        "});");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    List<String> expectedAliasesOrder = Arrays.asList("a", "goog.a", "x");
    assertEquals(expectedAliasesOrder, getAliasesFromNodes(aliasDefinitionsInOrder));
  }

  private List<String> getAliasesFromNodes(List<Node> nodes) {
    List<String> aliases = Lists.newArrayList();
    for (Node node : nodes) {
      if (node instanceof Var && ((Var) node).isAlias()) {
        aliases.add(node.getName());
      }
    }
    return aliases;
  }

  @Test
  public void testTypeNodeAliases() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    Node root = Node.newBlock("goog.scope(function() {" +
        "  var goog = {googType: goog.googType}; " +
        "});");

    scopedAliases.process(null, root);

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    List<String> expectedAliasesOrder = Arrays.asList("goog.googType", "googType");
    assertEquals(expectedAliasesOrder, getAliasesFromNodes(aliasUsages));
  }

  @Test
  public void testNoAliasDefinitions() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    Node root = Node.newBlock("goog.scope(function() {" +
        "});");

    scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = scopedAliases.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());
  }

  @Test
  public void testNoAliasUsages() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    Node root = Node.newBlock("goog.scope(function() {" +
        "  function test() {} " +
        "});");

    scopedAliases.process(null, root);

    List<Node> aliasUsages = scopedAliases.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());
  }

  @Test
  public void testNoScopeCalls() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    Node root = Node.newBlock("function test() {}");

    scopedAliases.process(null, root);

    List<Node> scopeCalls = scopedAliases.getScopeCalls();
    assertTrue(scopeCalls.isEmpty());
  }

  @Test
  public void testErrorReporting() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    Node root = Node.newBlock("goog.scope(function(x) {" +
        "});");

    scopedAliases.process(null, root);

    List<DiagnosticType> expectedDiagnosticTypes = Arrays.asList(
        JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS);
    List<DiagnosticType> actualDiagnosticTypes = ImmutableList.copyOf(scopedAliases.getDiagnostics());
    assertEquals(expectedDiagnosticTypes, actualDiagnosticTypes);
  }

  @Test
  public void testNoErrors() {
    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, ALWAYS);

    Node root = Node.newBlock("goog.scope(function() {" +
        "  var a = 1; " +
        "  var b = goog.a; " +
        "});");

    scopedAliases.process(null, root);

    assertFalse(scopedAliases.hasErrors());
  }
}