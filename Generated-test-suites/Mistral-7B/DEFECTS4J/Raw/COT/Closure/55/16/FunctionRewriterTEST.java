import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal.Callback;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

import org.junit.Test;

public class FunctionRewriterTest {

  private static final FunctionRewriter TESTEE = new FunctionRewriter(null);

  @Test
  public void testProcess_EmptyProgram() {
    Node root = NodeFactory.createCompilationUnit();
    TESTEE.process(null, root);
  }

  @Test
  public void testProcess_NoReducibleFunctions() {
    Node root = NodeFactory.createCompilationUnit();
    Node functionNode1 = NodeFactory.createFunctionDeclaration(
        "func1", null, NodeFactory.createBlock(null));
    root.addChildToBack(functionNode1);
    Node functionNode2 = NodeFactory.createFunctionDeclaration(
        "func2", null, NodeFactory.createBlock(null));
    root.addChildToBack(functionNode2);

    TESTEE.process(null, root);

    assertNull(TESTEE.parseHelperCode(EmptyFunctionReducer.class));
    assertNull(TESTEE.parseHelperCode(IdentityReducer.class));
    assertNull(TESTEE.parseHelperCode(GetterReducer.class));
    assertNull(TESTEE.parseHelperCode(SetterReducer.class));
  }

  @Test
  public void testProcess_EmptyFunctionReducer() {
    Node root = NodeFactory.createCompilationUnit();
    Node emptyFunctionNode = NodeFactory.createFunctionDeclaration(
        "func", null, NodeFactory.createBlock(null));
    emptyFunctionNode.getLastChild().putBooleanProp(Token.IS_EMPTY, true);
    root.addChildToBack(emptyFunctionNode);

    TESTEE.process(null, root);

    Node helperCode = TESTEE.parseHelperCode(EmptyFunctionReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcess_IdentityFunctionReducer() {
    Node root = NodeFactory.createCompilationUnit();
    Node identityFunctionNode = NodeFactory.createFunctionDeclaration(
        "func", null,
        NodeFactory.createBlock(
            NodeFactory.createExpressionStatement(
                NodeFactory.createReturnStatement(
                    NodeFactory.newName("a")))));
    root.addChildToBack(identityFunctionNode);

    TESTEE.process(null, root);

    Node helperCode = TESTEE.parseHelperCode(IdentityReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcess_GetterFunctionReducer() {
    Node root = NodeFactory.createCompilationUnit();
    Node getterFunctionNode = NodeFactory.createFunctionDeclaration(
        "getC", null, NodeFactory.createBlock(
            NodeFactory.createExpressionStatement(
                NodeFactory.createReturnStatement(
                    NodeFactory.createGetProp(
                        NodeFactory.newThis(), NodeFactory.newName("c"))))));
    Node propertyNameNode = NodeFactory.newName("c");
    root.addChildToBack(getPropertyNameNode);
    root.addChildToBack(getNodeForFunctionDeclaration(
        "getC", NodeFactory.createEmptyFunctionBody()));
    root.addChildToBack(getFunctionNodeWithPropertyGet(
        "getC", NodeFactory.newName("getter"), propertyNameNode));

    TESTEE.process(null, root);

    Node helperCode = TESTEE.parseHelperCode(GetterReducer.class);
    assertNotNull(helperCode);
  }

  @Test
  public void testProcess_SetterFunctionReducer() {
    Node root = NodeFactory.createCompilationUnit();
    Node setterFunctionNode = NodeFactory.createFunctionDeclaration(
        "setC", null, NodeFactory.createBlock(
            NodeFactory.createStatement(
                NodeFactory.createAssign(
                    NodeFactory.newThis(),
                    NodeFactory.newName("c"),
                    NodeFactory.newNumberLiteral(10)))));
    Node propertyNameNode = NodeFactory.newName("c");
    root.addChildToBack(getPropertyNameNode);
    root.addChildToBack(getNodeForFunctionDeclaration(
        "setC", NodeFactory.createEmptyFunctionBody()));
    root.addChildToBack(getFunctionNodeWithPropertySet(
        "setC", NodeFactory.newName("setter"), propertyNameNode));

    TESTEE.process(null, root);

    Node helperCode = TESTEE.parseHelperCode(SetterReducer.class);
    assertNotNull(helperCode);
  }

  private Node getNodeForFunctionDeclaration(
      String name, Node body) {
    return NodeFactory.createFunctionDeclaration(
        name, null, body);
  }

  private Node getFunctionNodeWithPropertyGet(
      String name, Node getterName, Node propertyNameNode) {
    Node functionNode = NodeFactory.createFunctionDeclaration(
        name, null, NodeFactory.createBlock(null));
    Node getPropertyNode = NodeFactory.newGetProp(
        NodeFactory.newThis(), NodeFactory.newName(getterName));
    Node getPropertyCallNode = NodeFactory.createCall(
        getPropertyNode, NodeFactory.newArray(propertyNameNode));
    Node assignmentNode = NodeFactory.createStatement(
        NodeFactory.createAssign(
            NodeFactory.newThis(), propertyNameNode,
            getPropertyCallNode));
    Node functionBody = NodeFactory.createBlock(assignmentNode);
    functionNode.setBody(functionBody);
    return functionNode;
  }

  private Node getFunctionNodeWithPropertySet(
      String name, Node setterName, Node propertyNameNode) {
    Node functionNode = NodeFactory.createFunctionDeclaration(
        name, null, NodeFactory.createBlock(null));
    Node getterNameNode = NodeFactory.newName(setterName);
    Node getThisNode = NodeFactory.newThis();
    Node getPropertyNameNode = NodeFactory.newGetProp(getThisNode, propertyNameNode);
    Node setPropertyNode = NodeFactory.newAssign(
        getPropertyNameNode, NodeFactory.newNumberLiteral(10));
    Node functionBody = NodeFactory.createBlock(setPropertyNode);
    Node argumentList = NodeFactory.createList(
        NodeFactory.newName(getterName), NodeFactory.newName("value"));
    Node functionCallNode = NodeFactory.createCall(
        NodeFactory.newName("JSCompiler_apply"),
        NodeFactory.newArray(functionNode, NodeFactory.newArray(argumentList)));
    Node statementNode = NodeFactory.createStatement(functionCallNode);
    functionBody.addChildToBack(statementNode);
    functionNode.setBody(functionBody);
    return functionNode;
  }
}