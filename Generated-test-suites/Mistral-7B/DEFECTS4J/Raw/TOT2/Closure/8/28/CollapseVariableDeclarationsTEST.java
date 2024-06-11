import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.*;
import static com.google.javascript.rhino.NodeType.*;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.JSDocInfo;

public class CollapseVariableDeclarationsTest {

  @Test
  public void testCollapseVariableDeclarations() {
    Compiler compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    options.setWarningLevel(Compiler.WarningLevel.VERBOSE);
    compiler.setOptions(options);

    String sourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d;\n" +
        "  a = true;\n" +
        "  b = true;\n" +
        "}";

    Node root = new Node(ScriptNode);
    NodeAST visitor = new NodeAST(root);
    visitor.parse(sourceCode);

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    // Check that variables were collapsed correctly
    assertVariableCollapsed("foo.a", "foo.a, b = 1, c = 2");;
    assertVariableCollapsed("foo.b", "foo.b = 1");;
    assertVariableCollapsed("foo.c", "foo.c = 2");;

    // Check that assign statements were collapsed correctly
    assertAssignCollapsed("foo.a = true", "foo.a, b = 1, c = 2");;
    assertAssignCollapsed("foo.b = true", "foo.b = 1");;
    assertAssignCollapsed("foo.c = true", "foo.c = 2");;

    // Check that variables inside a function are collapsed together
    String functionSourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d;\n" +
        "  function bar() {\n" +
        "    var e;\n" +
        "    var f = 3;\n" +
        "    var g = 4;\n" +
        "  }";

    Node functionRoot = new Node(ScriptNode);
    NodeAST functionVisitor = new NodeAST(functionRoot);
    functionVisitor.parse(functionSourceCode);

    collapseVariableDeclarations.process(null, functionRoot);

    assertVariableCollapsed("foo.a", "foo.a, b = 1, c = 2, e = ") ;
    assertVariableCollapsed("foo.b", "foo.b = 1") ;
    assertVariableCollapsed("foo.c", "foo.c = 2") ;
    assertVariableCollapsed("foo.d", "foo.d") ;
    assertVariableCollapsed("function foo.bar.e", "function foo.bar.e = ") ;
    assertVariableCollapsed("function foo.bar.f", "function foo.bar.f = 3") ;
    assertVariableCollapsed("function foo.bar.g", "function foo.bar.g = 4") ;

    // Check that variables are not collapsed across function boundaries
    String functionWithFunctionCallSourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  function bar() {\n" +
        "    var b = 1;\n" +
        "  }\n" +
        "  var c = 2;\n" +
        "}";

    Node functionWithFunctionCallRoot = new Node(ScriptNode);
    NodeAST functionWithFunctionCallVisitor = new NodeAST(functionWithFunctionCallRoot);
    functionWithFunctionCallVisitor.parse(functionWithFunctionCallSourceCode);

    collapseVariableDeclarations.process(null, functionWithFunctionCallRoot);

    assertVariableCollapsed("foo.a", "foo.a") ;
    assertVariableCollapsed("function foo.bar.b", "function foo.bar.b = 1") ;
    assertVariableCollapsed("foo.c", "foo.c = 2") ;

    // Check that variables are not collapsed with non-variables
    String nonVariableCollapseSourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var b = 1;\n" +
        "  var c = 2;\n" +
        "  var d;\n" +
        "  a = 1;\n" +
        "  b = 1;\n" +
        "  c = 1;\n" +
        "  d = true;\n" +
        "}";

    Node nonVariableCollapseRoot = new Node(ScriptNode);
    NodeAST nonVariableCollapseVisitor = new NodeAST(nonVariableCollapseRoot);
    nonVariableCollapseVisitor.parse(nonVariableCollapseSourceCode);

    collapseVariableDeclarations.process(null, nonVariableCollapseRoot);

    assertVariableCollapsed("foo.a", "foo.a") ;
    assertAssignCollapsed("foo.b = 1", "foo.b = 1") ;
    assertAssignCollapsed("foo.c = 1", "foo.c = 1") ;
    assertVariableCollapsed("foo.d", "foo.d") ;

    // Check that redeclared variables are suppressed
    String redeclaredVariableSourceCode = "function foo() {\n" +
        "  var a;\n" +
        "  var a = 1;\n" +
        "}";

    Node redeclaredVariableRoot = new Node(ScriptNode);
    NodeAST redeclaredVariableVisitor = new NodeAST(redeclaredVariableRoot);
    redeclaredVariableVisitor.parse(redeclaredVariableSourceCode);

    collapseVariableDeclarations.process(null, redeclaredVariableRoot);

    Node foo = redeclaredVariableRoot.lookupVar("foo");
    JSDocInfo info = foo.getJSDocInfo();
    assertTrue(info.hasSuppression("duplicate"));

    // Check that collapsed variables are accessible
    String collapsedVariableAccessSourceCode = "function foo() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "  var d;\n" +
        "  a = 1;\n" +
        "  b = 2;\n" +
        "  c = 3;\n" +
        "  console.log(a, b, c);\n" +
        "}";

    Node collapsedVariableAccessRoot = new Node(ScriptNode);
    NodeAST collapsedVariableAccessVisitor = new NodeAST(collapsedVariableAccessRoot);
    collapsedVariableAccessVisitor.parse(collapsedVariableAccessSourceCode);

    collapseVariableDeclarations.process(null, collapsedVariableAccessRoot);

    Node function = collapsedVariableAccessRoot.lookupFunction("foo");
    Node consoleLog = function.getLastChild();
    List<Node> args = consoleLog.getArguments();

    assertEquals(3, args.size());
    assertEquals("1", args.get(0).toString());
    assertEquals("2", args.get(1).toString());
    assertEquals("3", args.get(2).toString());

    // Check that collapsed variables are accessible in nested scopes
    String nestedScopeCollapsedVariableAccessSourceCode = "function foo() {\n" +
        "  var a, b = 1, c = 2;\n" +
        "  function bar() {\n" +
        "    var d;\n" +
        "    console.log(a, b, c);\n" +
        "  }\n" +
        "  bar();\n" +
        "}";

    Node nestedScopeCollapsedVariableAccessRoot = new Node(ScriptNode);
    NodeAST nestedScopeCollapsedVariableAccessVisitor = new NodeAST(nestedScopeCollapsedVariableAccessRoot);
    nestedScopeCollapsedVariableAccessVisitor.parse(nestedScopeCollapsedVariableAccessSourceCode);

    collapseVariableDeclarations.process(null, nestedScopeCollapsedVariableAccessRoot);

    Node functionBar = nestedScopeCollapsedVariableAccessRoot.lookupFunction("bar");
    Node consoleLog = functionBar.getLastChild();
    List<Node> args = consoleLog.getArguments();

    assertEquals(3, args.size());
    assertEquals("1", args.get(0).toString());
    assertEquals("2", args.get(1).toString());
    assertEquals("3", args.get(2).toString());
  }

  private void assertVariableCollapsed(String expectedName, String expectedCollapsedDeclaration) {
    Node root = extractCollapsedVariable(CollapseVariableDeclarationsTest.class.getResource("/testCollapseVariableDeclarations.js").getFile(), "foo");
    Node variable = extractNodeByName(root, expectedName);

    Node parent = variable.getParent();
    assertEquals(ASSIGN, parent.getType());
    Node assignment = parent;

    Node collapsedDeclaration = assignment.getFirstChild();

    assertEquals(VAR_DECL, collapsedDeclaration.getType());
    assertEquals(expectedCollapsedDeclaration, collapsedDeclaration.toString());
  }

  private void assertAssignCollapsed(String expectedName, String expectedCollapsedDeclaration) {
    Node root = extractCollapsedVariable(CollapseVariableDeclarationsTest.class.getResource("/testCollapseVariableDeclarations.js").getFile(), "foo");
    Node assignment = extractNodeByName(root, expectedName);

    Node parent = assignment.getParent();
    assertEquals(ASSIGN, parent.getType());
    Node collapsedDeclaration = parent;

    assertEquals(VAR_DECL, collapsedDeclaration.getType());
    assertEquals(expectedCollapsedDeclaration, collapsedDeclaration.toString());
  }

  private Node extractCollapsedVariable(String resourcePath, String functionName) {
    Node root = NodeUtil.parse(CollapseVariableDeclarationsTest.class.getResourceAsStream(resourcePath), true);
    Node function = extractNodeByName(root, functionName);
    return function;
  }

  private Node extractNodeByName(Node root, String name) {
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      Node node = child;
      if (node.isName() && node.getString().equals(name)) {
        return node;
      }
      if (node.getFirstChild() != null) {
        node = extractNodeByName(node.getFirstChild(), name);
        if (node != null) {
          return node;
        }
      }
    }
    return null;
  }
}