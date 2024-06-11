import static org.junit.Assert.*;
import static com.google.javascript.jscomp.NodeTraversal.AbstractNodeTraversal.TRAVERSE;
import static com.google.javascript.rhino.NodeFactory.createIdentifier;
import static com.google.javascript.rhino.NodeFactory.createNode;
import static com.google.javascript.rhino.NodeFactory.createReturnStatement;
import static com.google.javascript.rhino.NodeFactory.createStatement;
import static com.google.javascript.rhino.NodeFactory.createUnconditionalBranch;
import static com.google.javascript.rhino.Token.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.UnreachableCodeElimination;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private Compiler compiler;
  private Node rootNode;

  @Before
  public void setup() {
    compiler = new Compiler();
    CompilerOptions options = new CompilerOptions();
    rootNode = createExampleNode();
    compiler.compile(ImmutableList.of(), rootNode, options);
  }

  @Test
  public void testUnreachableCodeAfterReturn() {
    Node returnNode = findNodeByType(rootNode, TOKEN, Return);
    Node unreachableNode = findNodeByType(returnNode.getNext(), TOKEN, Identifier, "unreachable");

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    Node result = findNodeByType(rootNode, TOKEN, Return);
    assertNull(findNodeByType(result.getNext(), TOKEN, Identifier, "unreachable"));
  }

  @Test
  public void testRemoveNoOpStatements() {
    Node functionNode = findNodeByType(rootNode, TOKEN, FunctionDeclaration);
    Node statementNode = functionNode.getFirstChild().getFirstChild();

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    Node result = functionNode;
    Node currentNode = statementNode;
    while (currentNode != null) {
      result = currentNode;
      currentNode = currentNode.getNext();
    }

    Node expected = createStatement(Token.ASSIGN, createIdentifier("x"), createNode(Token.NUMBER, 1));
    assertEquals(expected, result);
  }

  @Test(expected = AssertionError.class)
  public void testRemoveFunctionBody() {
    Node functionNode = findNodeByType(rootNode, TOKEN, FunctionDeclaration);
    Node bodyNode = functionNode.getFirstChild();

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    Node function = findNodeByType(rootNode, TOKEN, FunctionDeclaration);
    assertNull(function.getFirstChild());
  }

  @Test
  public void testRemoveBreakStatement() {
    Node functionNode = createExampleBreakNode();
    Node breakNode = findNodeByType(functionNode, TOKEN, Break);

    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, functionNode);

    Node result = findNodeByType(functionNode, TOKEN, Return);
    assertNull(findNodeByType(result.getNext(), TOKEN, Break));
  }

  private Node createExampleNode() {
    Node root = createNode(TOKEN, FunctionDeclaration, "test", ImmutableList.of());
    Node body = createNode(TOKEN, Block, null);
    root.setFirstChild(body);

    Node statement1 = createStatement(TOKEN, Return, createNode(TOKEN, NumberLiteral, 1));
    body.setFirstChild(statement1);
    Node unreachableStatement = createStatement(TOKEN, Identifier, "unreachable");
    body.setNext(unreachableStatement);

    return root;
  }

  private Node createExampleBreakNode() {
    Node root = createNode(TOKEN, FunctionDeclaration, "test", ImmutableList.of());
    Node body = createNode(TOKEN, Block, null);
    root.setFirstChild(body);

    Node loop = createNode(TOKEN, While, null, createNode(Token.IDENTIFIER, "i", 0), null);
    body.setFirstChild(loop);

    Node bodyStatement = createStatement(TOKEN, Identifier, "i");
    loop.setFirstChild(bodyStatement);

    Node breakStatement = createUnconditionalBranch(createNode(Token.BREAK));
    bodyStatement.setNext(breakStatement);

    Node returnStatement = createStatement(TOKEN, Return, createNode(TOKEN, NumberLiteral, 1));
    loop.setNext(returnStatement);

    return root;
  }

  private Node findNodeByType(Node node, int type, Class<?>... classes) {
    while (node != null && node.getType() != type) {
      if (classes != null) {
        for (Class<?> clazz : classes) {
          if (node.getClass().isInstance(clazz)) {
            return node;
          }
        }
      }
      node = node.getNext();
    }
    return null;
  }
}