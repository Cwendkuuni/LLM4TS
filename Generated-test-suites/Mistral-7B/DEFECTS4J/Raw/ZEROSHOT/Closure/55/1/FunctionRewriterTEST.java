import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.FunctionRewriter;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import java.util.List;

public class FunctionRewriterTest {

  private FunctionRewriter functionRewriter;
  private NodeFactory nodeFactory;

  @org.junit.Before
  public void setUp() {
    nodeFactory = new NodeFactory();
    functionRewriter = new FunctionRewriter(null);
  }

  @org.junit.Test
  public void testEmptyFunctionReducer() {
    Node emptyFunctionNode = nodeFactory.createFunctionDeclaration(
        "emptyFunction", null, null, null);

    Node reducedNode = functionRewriter.reduce(emptyFunctionNode);
    assertNull(reducedNode);

    Node helperNode = functionRewriter.parseHelperCode(EmptyFunctionReducer.class);
    assertNull(helperNode);

    Node rootNode = nodeFactory.createBlock(null);
    rootNode.addChildToBack(helperNode);
    NodeTraversal.traverse(functionRewriter, rootNode, null);

    assertSame(helperNode, emptyFunctionNode.getParent());
  }

  @org.junit.Test
  public void testIdentityReducer() {
    Node identityFunctionNode = nodeFactory.createFunctionDeclaration(
        "identityFunction", null, null,
        nodeFactory.createReturnStatement(
            nodeFactory.createName("this"), null));

    Node reducedNode = functionRewriter.reduce(identityFunctionNode);
    Node helperNode = functionRewriter.parseHelperCode(IdentityReducer.class);

    assertNull(reducedNode);
    assertNull(helperNode);

    Node rootNode = nodeFactory.createBlock(null);
    rootNode.addChildToBack(helperNode);
    NodeTraversal.traverse(functionRewriter, rootNode, null);

    Node expectedHelperNode = nodeFactory.createFunctionDeclaration(
        "identityFunctionHelper", null, null, null);
    expectedHelperNode.addChildToBack(
        nodeFactory.createVariableDeclarationList(
            ImmutableList.of(
                nodeFactory.createVariableDeclaration(
                    "JSCompiler_value", null, null, null)),
            null));
    expectedHelperNode.addChildToBack(
        nodeFactory.createFunctionExpression(
            null,
            ImmutableList.of(new Node(Token.IDENT, "JSCompiler_value")),
            nodeFactory.createReturnStatement(
                nodeFactory.createName("JSCompiler_value"), null),
            null));

    assertSame(expectedHelperNode, helperNode);
    assertSame(helperNode, identityFunctionNode.getParent());
  }

  @org.junit.Test
  public void testReturnConstantReducer() {
    Node constantFunctionNode = nodeFactory.createFunctionDeclaration(
        "constantFunction", null, null,
        nodeFactory.createReturnStatement(
            nodeFactory.createNumberLiteral(10), null));

    Node reducedNode = functionRewriter.reduce(constantFunctionNode);
    Node helperNode = functionRewriter.parseHelperCode(ReturnConstantReducer.class);

    assertNull(reducedNode);
    assertNull(helperNode);

    Node rootNode = nodeFactory.createBlock(null);
    rootNode.addChildToBack(helperNode);
    NodeTraversal.traverse(functionRewriter, rootNode, null);

    Node expectedHelperNode = nodeFactory.createFunctionDeclaration(
        "returnArgHelper", null, null, null);
    expectedHelperNode.addChildToBack(
        nodeFactory.createVariableDeclarationList(
            ImmutableList.of(
                nodeFactory.createVariableDeclaration(
                    "JSCompiler_value", null, null, null)),
            null));
    expectedHelperNode.addChildToBack(
        nodeFactory.createFunctionExpression(
            null,
            ImmutableList.of(new Node(Token.IDENT, "JSCompiler_value")),
            nodeFactory.createBlock(
                nodeFactory.createReturnStatement(
                    nodeFactory.createName("JSCompiler_value"), null)),
            null));

    assertSame(expectedHelperNode, helperNode);
    assertSame(helperNode, constantFunctionNode.getParent());
  }

  @org.junit.Test
  public void testGetterReducer() {
    Node getterFunctionNode = nodeFactory.createFunctionDeclaration(
        "getProperty", null, null,
        nodeFactory.createReturnStatement(
            nodeFactory.createGetProp(
                nodeFactory.createName("this"),
                nodeFactory.createName("propertyName")),
            null));

    Node reducedNode = functionRewriter.reduce(getPropertyFunctionNode);
    Node helperNode = functionRewriter.parseHelperCode(GetterReducer.class);

    assertNull(reducedNode);
    assertNull(helperNode);

    Node rootNode = nodeFactory.createBlock(null);
    rootNode.addChildToBack(helperNode);
    NodeTraversal.traverse(functionRewriter, rootNode, null);

    Node expectedHelperNode = nodeFactory.createFunctionDeclaration(
        "getHelper", null, null, null);
    expectedHelperNode.addChildToBack(
        nodeFactory.createVariableDeclarationList(
            ImmutableList.of(
                nodeFactory.createVariableDeclaration(
                    "JSCompiler_name", null, null, null)),
            null));
    expectedHelperNode.addChildToBack(
        nodeFactory.createFunctionExpression(
            null,
            ImmutableList.of(new Node(Token.IDENT, "JSCompiler_name")),
            nodeFactory.createBlock(
                nodeFactory.createReturnStatement(
                    nodeFactory.createGetProp(
                        nodeFactory.createThis(),
                        nodeFactory.createName("JSCompiler_name")),
                    null)),
            null));

    assertSame(expectedHelperNode, helperNode);
    assertSame(helperNode, getterFunctionNode.getParent());
  }

  @org.junit.Test
  public void testSetterReducer() {
    Node setterFunctionNode = nodeFactory.createFunctionDeclaration(
        "setProperty", null, null,
        nodeFactory.createStatementList(
            ImmutableList.of(
                nodeFactory.createExpressionStatement(
                    nodeFactory.createAssign(
                        nodeFactory.createGetProp(
                            nodeFactory.createThis(),
                            nodeFactory.createName("propertyName")),
                        nodeFactory.createName("value"))))));

    Node reducedNode = functionRewriter.reduce(setterFunctionNode);
    Node helperNode = functionRewriter.parseHelperCode(SetterReducer.class);

    assertNull(reducedNode);
    assertNull(helperNode);

    Node rootNode = nodeFactory.createBlock(null);
    rootNode.addChildToBack(helperNode);
    NodeTraversal.traverse(functionRewriter, rootNode, null);

    Node expectedHelperNode = nodeFactory.createFunctionDeclaration(
        "setHelper", null, null, null);
    expectedHelperNode.addChildToBack(
        nodeFactory.createVariableDeclarationList(
            ImmutableList.of(
                nodeFactory.createVariableDeclaration(
                    "JSCompiler_name", null, null, null),
                nodeFactory.createVariableDeclaration(
                    "JSCompiler_value", null, null, null)),
            null));
    expectedHelperNode.addChildToBack(
        nodeFactory.createFunctionExpression(
            null,
            ImmutableList.of(
                new Node(Token.IDENT, "JSCompiler_name"),
                new Node(Token.IDENT, "JSCompiler_value")),
            nodeFactory.createBlock(
                nodeFactory.createStatementList(
                    ImmutableList.of(
                        nodeFactory.createExpressionStatement(
                            nodeFactory.createAssign(
                                nodeFactory.createGetProp(
                                    nodeFactory.createThis(),
                                    nodeFactory.createName("JSCompiler_name")),
                                nodeFactory.createName("JSCompiler_value")))))),
            null));

    assertSame(expectedHelperNode, helperNode);
    assertSame(helperNode, setterFunctionNode.getParent());
  }
}