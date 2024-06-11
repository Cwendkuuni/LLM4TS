import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;

import java.util.List;
import java.util.Set;

public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;
  private CompilerOptions options;
  private Node root;
  private Scope scope;
  private NodeTraversal nodeTraversal;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    options = new CompilerOptions();
    root = new Node(Token.PROGRAM);
    scope = new Scope(root, new Node(Token.GLOBAL, ""));
    nodeTraversal = new NodeTraversal(compiler, root, options);
  }

  @org.junit.Test
  public void testCollapseSimpleVarDeclarations() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node varA = findVarNode("var A;");
    Node varB = findVarNode("var B;");
    Node varC = findVarNode("var C;");

    List<Node> varNodes = Lists.newArrayList(varA, varB, varC);
    List<Node> collapsedVarNodes = Lists.newArrayList(
        findVarNode("var A, B = null, C;")
    );

    for (int i = 0; i < varNodes.size(); i++) {
      assertEquals(collapsedVarNodes.get(0).getParent(), varNodes.get(i).getParent());
      assertEquals(collapsedVarNodes.get(0), varNodes.get(i).getNext());
    }
  }

  @org.junit.Test
  public void testCollapseVarWithAssignment() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node varA = findVarNode("var A = null;");
    Node varB = findVarNode("var B = null;");
    Node varC = findVarNode("var C = null;");

    List<Node> varNodes = Lists.newArrayList(varA, varB, varC);
    List<Node> collapsedVarNodes = Lists.newArrayList(
        findVarNode("var A, B = null, C;")
    );

    for (int i = 0; i < varNodes.size(); i++) {
      assertEquals(collapsedVarNodes.get(0).getParent(), varNodes.get(i).getParent());
      assertEquals(collapsedVarNodes.get(0), varNodes.get(i).getNext());
    }
  }

  @org.junit.Test
  public void testCollapseVarWithExpression() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node varA = findVarNode("var A = 1;");
    Node varB = findVarNode("var B = 2;");
    Node varC = findVarNode("var C = 3;");

    List<Node> varNodes = Lists.newArrayList(varA, varB, varC);
    List<Node> collapsedVarNodes = Lists.newArrayList(
        findVarNode("var A, B = 2, C = 3;")
    );

    for (int i = 0; i < varNodes.size(); i++) {
      assertEquals(collapsedVarNodes.get(0).getParent(), varNodes.get(i).getParent());
      assertEquals(collapsedVarNodes.get(0), varNodes.get(i).getNext());
    }
  }

  @org.junit.Test
  public void testCollapseVarWithFunction() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node function = new Node(Token.FUNCTION, "function Foo() { }");
    Node varA = new Node(Token.VAR, "var A;");
    Node varB = new Node(Token.VAR, "var B;");
    Node varC = new Node(Token.VAR, "var C;");

    function.addChildToBack(varA);
    function.addChildToBack(varB);
    function.addChildToBack(varC);
    root.addChildToBack(function);

    Node functionCollapsed = findFunctionNode("function Foo() { var A, B = null, C; }");

    assertEquals(functionCollapsed, function);
  }

  @org.junit.Test
  public void testCollapseVarWithFunctionAndVar() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node function = new Node(Token.FUNCTION, "function Foo() { }");
    Node varA = new Node(Token.VAR, "var A;");
    Node varB = new Node(Token.VAR, "var B;");
    Node varC = new Node(Token.VAR, "var C;");

    function.addChildToBack(varA);
    function.addChildToBack(varB);
    function.addChildToBack(new Node(Token.VAR, "var D;"));
    root.addChildToBack(function);

    Node functionCollapsed = findFunctionNode("function Foo() { var A, B = null, C, D; }");

    assertEquals(functionCollapsed, function);
  }

  @org.junit.Test
  public void testCollapseVarWithFunctionAndAssign() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node function = new Node(Token.FUNCTION, "function Foo() { }");
    Node varA = new Node(Token.VAR, "var A;");
    Node varB = new Node(Token.VAR, "var B;");
    Node assignA = new Node(Token.ASSIGN, "A = 1;");
    Node assignB = new Node(Token.ASSIGN, "B = 2;");

    function.addChildToBack(varA);
    function.addChildToBack(assignA);
    function.addChildToBack(varB);
    function.addChildToBack(assignB);
    root.addChildToBack(function);

    Node functionCollapsed = findFunctionNode("function Foo() { var A, B = 2, A = 1; }");

    assertEquals(functionCollapsed, function);
  }

  @org.junit.Test
  public void testCollapseVarWithFunctionAndAssignAndVar() {
    CollapseVariableDeclarations pass = new CollapseVariableDeclarations(compiler);
    pass.process(null, root);

    Node function = new Node(Token.FUNCTION, "function Foo() { }");
    Node varA = new Node(Token.VAR, "var A;");
    Node varB = new Node(Token.VAR, "var B;");
    Node assignA = new Node(Token.ASSIGN, "A = 1;");
    Node assignB = new Node(Token.ASSIGN, "B = 2;");
    Node varC = new Node(Token.VAR, "var C;");

    function.addChildToBack(varA);
    function.addChildToBack(assignA);
    function.addChildToBack(varB);
    function.addChildToBack(assignB);
    function.addChildToBack(varC);
    root.addChildToBack(function);

    Node functionCollapsed = findFunctionNode("function Foo() { var A, B = 2, A = 1, C; }");

    assertEquals(functionCollapsed, function);
  }

  private Node findVarNode(String nodeString) {
    Node varNode = new Node(Token.FROM_JSON, "{" + nodeString + "}");
    Node parent = root.getFirstChild();
    while (parent != null) {
      for (Node child = parent.getFirstChild(); child != null; child = child.getNext()) {
        if (child.equals(varNode)) {
          return child;
        }
      }
      parent = parent.getNext();
    }
    throw new RuntimeException("Variable node not found: " + nodeString);
  }

  private Node findFunctionNode(String functionString) {
    Node functionNode = new Node(Token.FROM_JSON, "{" + functionString + "}");
    for (Node child = root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getFirstChild() != null && child.getFirstChild().equals(functionNode)) {
        return child;
      }
    }
    throw new RuntimeException("Function node not found: " + functionString);
  }
}