import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;

import java.util.Arrays;
import java.util.List;

public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;
  private Node rootNode;
  private Scope scope;

  @org.junit.Before
  public void setUp() {
    compiler = mock(AbstractCompiler.class);
    scope = mock(Scope.class);
    rootNode = new Node(Token.ROOT);
  }

  @org.junit.Test
  public void testSimpleCollapse() {
    when(compiler.getLifeCycleStage()).thenReturn(null);
    when(compiler.getScope()).thenReturn(scope);

    Node varA = new Node(Token.VAR, "a");
    Node varB = new Node(Token.VAR, "b");
    Node varC = new Node(Token.VAR, "c");

    varA.setParent(rootNode);
    varB.setParent(rootNode);
    varC.setParent(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, new CollapseVariableDeclarations(compiler));

    traversal.traverse();

    assertEquals(2, rootNode.getChildCount());

    Node collapsedVar = rootNode.getChild(0);
    assertEquals(Token.VAR, collapsedVar.getTokenType());
    assertEquals("a", collapsedVar.getString());

    Node children = collapsedVar.getFirstChild();
    List<Node> collapsedChildren = Arrays.asList(varB, varC);

    for (int i = 0; i < collapsedChildren.size(); ++i) {
      Node child = collapsedChildren.get(i);
      Node actualChild = children.getNext();

      assertEquals(child.getTokenType(), actualChild.getTokenType());
      assertEquals(child.getString(), actualChild.getString());
    }
  }

  @org.junit.Test
  public void testCollapseWithAssignments() {
    when(compiler.getLifeCycleStage()).thenReturn(null);
    when(compiler.getScope()).thenReturn(scope);

    Node varA = new Node(Token.VAR, "a");
    Node assignA = new Node(Token.ASSIGN, null);
    Node varB = new Node(Token.VAR, "b");
    Node assignB = new Node(Token.ASSIGN, null);
    Node varC = new Node(Token.VAR, "c");

    assignA.addChildToBack(varA);
    assignB.addChildToBack(varB);

    varA.setParent(rootNode);
    assignA.setParent(rootNode);
    varB.setParent(rootNode);
    assignB.setParent(rootNode);
    varC.setParent(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, new CollapseVariableDeclarations(compiler));

    traversal.traverse();

    assertEquals(1, rootNode.getChildCount());

    Node collapsedVar = rootNode.getChild(0);
    assertEquals(Token.VAR, collapsedVar.getTokenType());
    assertEquals("a", collapsedVar.getString());

    Node children = collapsedVar.getFirstChild();
    Node collapsedAssign = children.getNext();
    List<Node> collapsedAssignChildren = Arrays.asList(assignB, varC);

    for (int i = 0; i < collapsedAssignChildren.size(); ++i) {
      Node child = collapsedAssignChildren.get(i);
      Node actualChild = collapsedAssign.getNext();

      assertEquals(child.getTokenType(), actualChild.getTokenType());
      assertEquals(child.getString(), actualChild.getString());
    }
  }

  @org.junit.Test
  public void testNoCollapse() {
    when(compiler.getLifeCycleStage()).thenReturn(null);
    when(compiler.getScope()).thenReturn(scope);

    Node varA = new Node(Token.VAR, "a");
    Node varB = new Node(Token.VAR, "b");

    varA.setParent(rootNode);
    varB.setParent(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, new CollapseVariableDeclarations(compiler));

    traversal.traverse();

    assertEquals(2, rootNode.getChildCount());
  }

  @org.junit.Test
  public void testCollapseMultipleAssignments() {
    when(compiler.getLifeCycleStage()).thenReturn(null);
    when(compiler.getScope()).thenReturn(scope);

    Node varA = new Node(Token.VAR, "a");
    Node assignA = new Node(Token.ASSIGN, null);
    Node varB = new Node(Token.VAR, "b");
    Node assignB = new Node(Token.ASSIGN, null);
    Node varC = new Node(Token.VAR, "c");
    Node assignC = new Node(Token.ASSIGN, null);

    assignA.addChildToBack(varA);
    assignB.addChildToBack(varB);
    assignC.addChildToBack(varC);

    varA.setParent(rootNode);
    assignA.setParent(rootNode);
    varB.setParent(rootNode);
    assignB.setParent(rootNode);
    varC.setParent(rootNode);
    assignC.setParent(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, new CollapseVariableDeclarations(compiler));

    traversal.traverse();

    assertEquals(1, rootNode.getChildCount());

    Node collapsedVar = rootNode.getChild(0);
    assertEquals(Token.VAR, collapsedVar.getTokenType());
    assertEquals("a", collapsedVar.getString());

    Node children = collapsedVar.getFirstChild();

    Node collapsedAssign = children;
    List<Node> collapsedAssignChildren = Arrays.asList(assignB, assignC);

    for (int i = 0; i < collapsedAssignChildren.size(); ++i) {
      Node child = collapsedAssignChildren.get(i);
      Node actualChild = collapsedAssign.getNext();

      assertEquals(child.getTokenType(), actualChild.getTokenType());
      assertEquals(child.getString(), actualChild.getString());
    }
  }

  @org.junit.Test
  public void testCollapseMultipleAssignmentsWithStatements() {
    when(compiler.getLifeCycleStage()).thenReturn(null);
    when(compiler.getScope()).thenReturn(scope);

    Node varA = new Node(Token.VAR, "a");
    Node assignA = new Node(Token.ASSIGN, null);
    Node stmtA = new Node(Token.STMT, null);
    stmtA.addChildToBack(new Node(Token.PRINT, null));
    Node varB = new Node(Token.VAR, "b");
    Node assignB = new Node(Token.ASSIGN, null);
    Node stmtB = new Node(Token.STMT, null);
    stmtB.addChildToBack(new Node(Token.PRINT, null));
    Node varC = new Node(Token.VAR, "c");
    Node assignC = new Node(Token.ASSIGN, null);
    Node stmtC = new Node(Token.STMT, null);
    stmtC.addChildToBack(new Node(Token.PRINT, null));

    assignA.addChildToBack(varA);
    assignB.addChildToBack(varB);
    assignC.addChildToBack(varC);

    stmtA.addChildToBack(assignA);
    stmtB.addChildToBack(assignB);
    stmtC.addChildToBack(assignC);

    varA.setParent(rootNode);
    assignA.setParent(rootNode);
    varB.setParent(rootNode);
    assignB.setParent(rootNode);
    varC.setParent(rootNode);
    assignC.setParent(rootNode);
    stmtA.setParent(rootNode);
    stmtB.setParent(rootNode);
    stmtC.setParent(rootNode);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, new CollapseVariableDeclarations(compiler));

    traversal.traverse();

    assertEquals(1, rootNode.getChildCount());

    Node collapsedVar = rootNode.getChild(0);
    assertEquals(Token.VAR, collapsedVar.getTokenType());
    assertEquals("a", collapsedVar.getString());

    Node children = collapsedVar.getFirstChild();

    Node collapsedStmt = children;
    List<Node> collapsedAssignChildren = Arrays.asList(stmtB, stmtC);

    for (int i = 0; i < collapsedAssignChildren.size(); ++i) {
      Node child = collapsedAssignChildren.get(i);
      Node actualChild = collapsedStmt.getNext();

      assertEquals(child.getTokenType(), actualChild.getTokenType());
      assertEquals(Token.STMT, child.getTokenType());

      Node childStmt = child.getFirstChild();
      Node actualStmt = actualChild.getFirstChild();

      assertEquals(childStmt.getTokenType(), actualStmt.getTokenType());
      assertEquals(childStmt.getLastChild().getTokenType(), actualStmt.getLastChild().getTokenType());
    }
  }

  @org.junit.Test
  public void testCollapseFunctionVariableDeclarations() {
    when(compiler.getLifeCycleStage()).thenReturn(null);
    when(compiler.getScope()).thenReturn(scope);

    Node function = new Node(Token.FUNCTION, null);
    Node body = new Node(Token.BLOCK, null);
    Node varA = new Node(Token.VAR, "a");
    Node varB = new Node(Token.VAR, "b");
    Node varC = new Node(Token.VAR, "c");

    varA.setParent(body);
    varB.setParent(body);
    varC.setParent(body);

    function.addChildToBack(body);

    NodeTraversal traversal = new NodeTraversal(compiler, rootNode, new CollapseVariableDeclarations(compiler));

    traversal.traverse();

    assertEquals(1, function.getChildCount());
    Node collapsedVar = function.getChild(0);

    assertEquals(Token.VAR, collapsedVar.getTokenType());
    assertEquals("function", collapsedVar.getString());

    Node children = collapsedVar.getFirstChild();
    assertEquals(Token.BLOCK, children.getTokenType());

    Node collapsedBody = children;

    List<Node> collapsedVarChildren = Arrays.asList(varA, varB, varC);

    for (int i = 0; i < collapsedVarChildren.size(); ++i) {
      Node child = collapsedVarChildren.get(i);
      Node actualChild = collapsedBody.getNext();

      assertEquals(child.getTokenType(), actualChild.getTokenType());
      assertEquals(child.getString(), actualChild.getString());
    }
  }
}