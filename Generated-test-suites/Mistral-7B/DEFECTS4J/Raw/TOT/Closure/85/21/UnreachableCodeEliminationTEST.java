import static com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback.Scope.FunctionScope.ROOT;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.NodeFactory;

import org.junit.Before;
import org.junit.Test;

public class UnreachableCodeEliminationTest {
  private AbstractCompiler compiler;
  private CompilerOptions options;
  private Node rootNode;

  @Before
  public void setUp() {
    options = new CompilerOptions();
    options.setWarningLevel("JsDoc", CompilerOptions.WarningLevel.ERROR);
    compiler = new AbstractCompiler();
    compiler.setOptions(options);
    NodeFactory nodeFactory = compiler.getFactory();

    rootNode = nodeFactory.createScope(ROOT).getRootNode();
  }

  @Test
  public void testSimpleReturn() {
    Node returnNode = nodeFactory.createReturnStatement(null);
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode = nodeFactory.createBlock(null, returnNode);
    Node bodyNode = nodeFactory.createBlock(null, null);
    Node statementNode = nodeFactory.createExpressionStatement(returnNode);
    bodyNode.addChild(statementNode);
    functionNode.setBody(bodyNode);
    rootNode.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
  }

  @Test
  public void testDeadCodeFollowingReturn() {
    Node returnNode = nodeFactory.createReturnStatement(null);
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode1 = nodeFactory.createBlock(null, null);
    Node blockNode2 = nodeFactory.createBlock(null, null);
    Node statementNode1 = nodeFactory.createExpressionStatement(returnNode);
    Node statementNode2 = nodeFactory.createStatement(nodeFactory.createPrintStatement("Unreachable"));
    blockNode1.addChild(statementNode1);
    blockNode2.addChild(statementNode2);
    functionNode.setBody(blockNode1);
    rootNode.addChild(functionNode);
    rootNode.addChild(blockNode2);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
    assertTrue(NodeUtil.isNullOrEmpty(blockNode2.getChildren()));
  }

  @Test
  public void testDeadCodeBeforeReturn() {
    Node returnNode = nodeFactory.createReturnStatement(null);
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode1 = nodeFactory.createBlock(null, null);
    Node blockNode2 = nodeFactory.createBlock(null, null);
    Node statementNode1 = nodeFactory.createStatement(nodeFactory.createPrintStatement("Print before return"));
    Node statementNode2 = nodeFactory.createExpressionStatement(returnNode);
    blockNode1.addChild(statementNode1);
    blockNode2.addChild(statementNode2);
    functionNode.setBody(blockNode1);
    rootNode.addChild(functionNode);
    rootNode.addChild(blockNode2);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
    assertTrue(NodeUtil.isNullOrEmpty(blockNode2.getChildren()));
  }

  @Test
  public void testDeadCodeBeforeReturnWithSideEffect() {
    Node returnNode = nodeFactory.createReturnStatement(null);
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode1 = nodeFactory.createBlock(null, null);
    Node blockNode2 = nodeFactory.createBlock(null, null);
    Node statementNode1 = nodeFactory.createStatement(nodeFactory.createAssign(nodeFactory.createName("x"), nodeFactory.createStringLiteral("Hello")));
    Node statementNode2 = nodeFactory.createExpressionStatement(returnNode);
    blockNode1.addChild(statementNode1);
    blockNode2.addChild(statementNode2);
    functionNode.setBody(blockNode1);
    rootNode.addChild(functionNode);
    rootNode.addChild(blockNode2);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, true);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
    assertTrue(NodeUtil.isNullOrEmpty(blockNode2.getChildren()));
    assertTrue(NodeUtil.getStringLiteralValue(nodeFactory.getRootScope().getVariable("x")).equals("Hello"));
  }

  @Test
  public void testNoOpStatement() {
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode = nodeFactory.createBlock(null, null);
    Node statementNode = nodeFactory.createExpressionStatement(nodeFactory.createCall(null, "a.b.MyClass.prototype.propertyName"));
    blockNode.addChild(statementNode);
    functionNode.setBody(blockNode);
    rootNode.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
  }

  @Test
  public void testNoOpStatementWithSideEffect() {
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode = nodeFactory.createBlock(null, null);
    Node statementNode = nodeFactory.createExpressionStatement(nodeFactory.createCall(null, "a.b.MyClass.prototype.propertyName = 42"));
    blockNode.addChild(statementNode);
    functionNode.setBody(blockNode);
    rootNode.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
    assertTrue(NodeUtil.getIntValue(nodeFactory.getRootScope().getVariable("a.b.MyClass.prototype.propertyName")).intValue() == 42);
  }

  @Test
  public void testReturnWithSideEffect() {
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode = nodeFactory.createBlock(null, null);
    Node statementNode = nodeFactory.createExpressionStatement(nodeFactory.createCall(null, "a.b.MyClass.prototype.propertyName = 42; return"));
    blockNode.addChild(statementNode);
    functionNode.setBody(blockNode);
    rootNode.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
    assertTrue(NodeUtil.getIntValue(nodeFactory.getRootScope().getVariable("a.b.MyClass.prototype.propertyName")).intValue() == 42);
  }

  @Test(expected = RuntimeException.class)
  public void testErrorScenario() {
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode = nodeFactory.createBlock(null, null);
    Node statementNode = nodeFactory.createStatement(nodeFactory.createCall(null, "throw new Error('Test Error')"));
    blockNode.addChild(statementNode);
    functionNode.setBody(blockNode);
    rootNode.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, rootNode);
  }

  @Test
  public void testNestedFunctions() {
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode1 = nodeFactory.createBlock(null, null);
    Node statementNode1 = nodeFactory.createStatement(nodeFactory.createFunction(null, null, null, null, null, null));
    Node blockNode2 = nodeFactory.createBlock(null, null);
    Node statementNode2 = nodeFactory.createExpressionStatement(nodeFactory.createCall(null, "return"));
    blockNode1.addChild(statementNode1);
    blockNode2.addChild(statementNode2);
    functionNode.setBody(blockNode1);
    rootNode.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
  }

  @Test
  public void testNestedFunctionsWithSideEffect() {
    Node functionNode = nodeFactory.createFunction(null, null, rootNode, null, null, null);
    Node blockNode1 = nodeFactory.createBlock(null, null);
    Node statementNode1 = nodeFactory.createStatement(nodeFactory.createFunction(null, null, null, null, null, null));
    Node blockNode2 = nodeFactory.createBlock(null, null);
    Node statementNode2 = nodeFactory.createExpressionStatement(nodeFactory.createCall(null, "a.b.MyClass.prototype.propertyName = 42; return"));
    blockNode1.addChild(statementNode1);
    blockNode2.addChild(statementNode2);
    functionNode.setBody(blockNode1);
    rootNode.addChild(functionNode);

    NodeTraversal nodeTraversal = new NodeTraversal(compiler);
    UnreachableCodeElimination eliminator = new UnreachableCodeElimination(compiler, false);
    eliminator.process(null, rootNode);

    assertTrue(NodeUtil.isNullOrEmpty(functionNode.getBody()));
    assertTrue(NodeUtil.getIntValue(nodeFactory.getRootScope().getVariable("a.b.MyClass.prototype.propertyName")).intValue() == 42);
  }
}