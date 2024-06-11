import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Kind.TRAVERSE;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.TraversalFactory;
import com.google.javascript.rhino.JSDocInfo;

import java.util.List;
import java.util.Set;

/**
 * Tests for the CollapseVariableDeclarations class
 */
public class CollapseVariableDeclarationsTest {

  private AbstractCompiler compiler;

  @Before
  public void setUp() {
    compiler = new Compiler();
  }

  @Test
  public void testCollapsingVariableDeclarations() {
    Node root = new Node(Token.PROGRAM);

    Node varA = new Node(Token.VAR, "a");
    Node varB = new Node(Token.VAR, "b");
    Node varC = new Node(Token.VAR, "c");

    Node assignAB = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAB.addChild(varB);
    assignAB.addChild(new Node(Token.INT_LITERAL, 1));

    Node assignAC = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAC.addChild(varC);
    assignAC.addChild(new Node(Token.INT_LITERAL, 2));

    Node assignA = new Node(Token.ASSIGN, Token); // Token.ASSIGN_EQUALS for JUnit 5
    assignA.addChild(varA);
    assignA.addChild(assignAB);
    assignA.addChild(assignAC);

    root.addChild(assignA);

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    TraversalFactory tf = compiler.getTraversalFactory();
    NodeTraversal nt = tf.create(root, TRAVERSE);
    nt.traverse(new CollapseVariableDeclarationsTest.CheckCollapsedVariables(varA, varB, varC));
  }

  @Test
  public void testCollapsingVariableDeclarationsWithAssignments() {
    Node root = new Node(Token.PROGRAM);

    Node varA = new Node(Token.VAR, "a");
    Node assignAA = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAA.addChild(varA);
    assignAA.addChild(new Node(Token.BOOL_LITERAL, true));

    Node varB = new Node(Token.VAR, "b");
    Node assignAB = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAB.addChild(varB);
    assignAB.addChild(new Node(Token.BOOL_LITERAL, true));

    Node varC = new Node(Token.VAR, "c");
    Node assignAC = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAC.addChild(varC);
    assignAC.addChild(new Node(Token.BOOL_LITERAL, true));

    Node assignABC = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignABC.addChild(assignAA);
    assignABC.addChild(assignAB);
    assignABC.addChild(assignAC);

    root.addChild(assignABC);

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    TraversalFactory tf = compiler.getTraversalFactory();
    NodeTraversal nt = tf.create(root, TRAVERSE);
    nt.traverse(new CollapseVariableDeclarationsTest.CheckCollapsedVariables(varA, varB, varC));
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingAdjacentIFs() {
    Node root = new Node(Token.PROGRAM);

    Node ifNode = new Node(Token.IF, null);
    Node ifStatement = new Node(Token.BLOCK);
    Node varA = new Node(Token.VAR, "a");
    Node assignAA = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAA.addChild(varA);
    assignAA.addChild(new Node(Token.INT_LITERAL, 1));
    ifStatement.addChild(assignAA);

    Node elseNode = new Node(Token.ELSE, null);
    Node elseStatement = new Node(Token.BLOCK);
    Node varB = new Node(Token.VAR, "b");
    Node assignAB = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAB.addChild(varB);
    assignAB.addChild(new Node(Token.INT_LITERAL, 1));
    elseStatement.addChild(assignAB);

    ifNode.addChild(ifStatement);
    elseNode.addChild(elseStatement);
    root.addChild(ifNode);
    root.addChild(elseNode);

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    TraversalFactory tf = compiler.getTraversalFactory();
    NodeTraversal nt = tf.create(root, TRAVERSE);
    nt.traverse(new CollapseVariableDeclarationsTest.CheckCollapsedVariables(varA, varB));
  }

  @Test(expected = AssertionError.class)
  public void testCollapsingDuplicateVariableDeclarations() {
    Node root = new Node(Token.PROGRAM);

    Node varA = new Node(Token.VAR, "a");
    Node assignAA = new Node(Token.ASSIGN, Token.ASSIGN_EQUALS);
    assignAA.addChild(varA);
    assignAA.addChild(new Node(Token.INT_LITERAL, 1));

    Node varA2 = new Node(Token.VAR, "a");
    root.addChild(assignAA);
    root.addChild(new Node(Token.BLOCK, null).addChild(varA2));

    CollapseVariableDeclarations collapseVariableDeclarations = new CollapseVariableDeclarations(compiler);
    collapseVariableDeclarations.process(null, root);

    TraversalFactory tf = compiler.getTraversalFactory();
    NodeTraversal nt = tf.create(root, TRAVERSE);
    nt.traverse(new CollapseVariableDeclarationsTest.CheckCollapsedVariables(varA, varA2));
  }

  private static class CheckCollapsedVariables implements NodeTraversal.AbstractPostOrderCallback {
    private final Node expectedA;
    private final Node expectedB;
    private final Node expectedC;

    CheckCollapsedVariables(Node a, Node b, Node c) {
      this.expectedA = a;
      this.expectedB = b;
      this.expectedC = c;
    }

    @Override
    public void visit(NodeTraversal nt, Node current) {
      if (current.isVar()) {
        Var var = compiler.getEnvironment().getScope().getVar(current.getString());
        assertEquals(expectedA, var);
        expectedA = null;
        assertEquals(expectedB, null);
        expectedB = var;
      }
    }
  }
}