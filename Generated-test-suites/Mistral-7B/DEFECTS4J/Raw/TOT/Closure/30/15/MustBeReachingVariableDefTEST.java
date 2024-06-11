import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import com.google.javascript.jscomp.MustBeReachingVariableDef;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

import java.util.Set;

public class MustBeReachingVariableDefTest {

  @org.junit.Test
  public void testEmptyFunction() {
    Scope testScope = new Scope();
    ControlFlowGraph<Node> testCfg = new ControlFlowGraph<Node>(testScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(testCfg, testScope, null);

    Node root = new Node(Token.BLOCK);
    testCfg.setRootNode(root);

    MustDef result = analyzer.run(root);
    assertNull(result.reachingDef.get("x"));
  }

  @org.junit.Test
  public void testSimpleAssignment() {
    Scope testScope = new Scope();
    ControlFlowGraph<Node> testCfg = new ControlFlowGraph<Node>(testScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(testCfg, testScope, null);

    Node root = new Node(Token.BLOCK);
    Node assign = new Node(Token.ASSIGN, new Node("x"), new Node(Token.NUM, 1));
    root.addChild(assign);

    testCfg.setRootNode(root);

    MustDef result = analyzer.run(root);
    Definition expected = new Definition(assign);
    assertSame(expected, result.reachingDef.get("x"));
  }

  @org.junit.Test
  public void testComplexFunction() {
    Scope testScope = new Scope();
    ControlFlowGraph<Node> testCfg = new ControlFlowGraph<Node>(testScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(testCfg, testScope, null);

    Node root = new Node(Token.FUNCTION);

    // x = 1;
    Node xAssign = new Node(Token.ASSIGN, new Node("x"), new Node(Token.NUM, 1));
    root.addChild(xAssign);

    // if (condition) {
    Node ifNode = new Node(Token.IF, null, null, null, null);
    root.addChild(ifNode);

    Node condition = new Node(Token.BOOL, Token.TRUE);
    ifNode.setCondition(condition);

    // y = 2;
    Node yAssign = new Node(Token.ASSIGN, new Node("y"), new Node(Token.NUM, 2));
    Node ifBody = new Node(Token.BLOCK);
    ifNode.setTrueQuad(new Node[] { ifBody });
    ifBody.addChild(yAssign);

    // z = x + y;
    Node add = new Node(Token.PLUS, new Node("x"), new Node("y"));
    Node zAssign = new Node(Token.ASSIGN, new Node("z"), add);
    ifBody.addChild(zAssign);

    // } else {
    Node elseNode = new Node(Token.ELSE, null, null, null, null);
    ifNode.setFalseQuad(new Node[] { elseNode });

    Node elseBody = new Node(Token.BLOCK);
    elseNode.setBody(elseBody);

    // w = 3;
    Node wAssign = new Node(Token.ASSIGN, new Node("w"), new Node(Token.NUM, 3));
    elseBody.addChild(wAssign);

    testCfg.setRootNode(root);

    MustDef result = analyzer.run(root);

    // x and y are defined in the if block, so they should only be defined there
    assertSame(xAssign, result.reachingDef.get("x").node);
    assertSame(yAssign, result.reachingDef.get("y").node);

    // z is defined in the if block, so it should be defined there
    assertSame(zAssign, result.reachingDef.get("z").node);

    // w is defined in the else block, so it should only be defined there
    assertSame(wAssign, result.reachingDef.get("w").node);
  }

  @org.junit.Test
  public void testEscapedVariable() {
    Scope testScope = new Scope();
    ControlFlowGraph<Node> testCfg = new ControlFlowGraph<Node>(testScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(testCfg, testScope, null);

    Node root = new Node(Token.FUNCTION);

    // x = 1;
    Node xAssign = new Node(Token.ASSIGN, new Node("x"), new Node(Token.NUM, 1));
    root.addChild(xAssign);

    // if (x > 0) {
    Node ifNode = new Node(Token.IF, null, null, null, null);
    root.addChild(ifNode);

    Node condition = new Node(Token.GREATER, new Node("x"), new Node(Token.NUM, 0));
    ifNode.setCondition(condition);

    // y = 2;
    Node yAssign = new Node(Token.ASSIGN, new Node("y"), new Node(Token.NUM, 2));
    Node ifBody = new Node(Token.BLOCK);
    ifNode.setTrueQuad(new Node[] { ifBody });
    ifBody.addChild(yAssign);

    // z = x + y;
    Node add = new Node(Token.PLUS, new Node("x"), new Node("y"));
    Node zAssign = new Node(Token.ASSIGN, new Node("z"), add);
    ifBody.addChild(zAssign);

    // } else {
    Node elseNode = new Node(Token.ELSE, null, null, null, null);
    ifNode.setFalseQuad(new Node[] { elseNode });

    Node elseBody = new Node(Token.BLOCK);
    elseNode.setBody(elseBody);

    // this.x = 3;
    Node thisAssign = new Node(Token.ASSIGN, new Node("this.x"), new Node("x"));
    elseBody.addChild(thisAssign);

    // w = 3;
    Node wAssign = new Node(Token.ASSIGN, new Node("w"), new Node(Token.NUM, 3));
    elseBody.addChild(wAssign);

    testCfg.setRootNode(root);

    MustDef result = analyzer.run(root);

    // x is defined in the if block, but it is not the only definition of x
    // because "this.x" is defined in the else block, so x's definition is marked
    // as BOTTOM
    assertNull(result.reachingDef.get("x"));

    // y is defined in the if block, so it is defined there
    assertSame(yAssign, result.reachingDef.get("y").node);

    // z is defined in the if block, so it is defined there
    assertSame(zAssign, result.reachingDef.get("z").node);

    // w is defined in the else block, so it is defined there
    assertSame(wAssign, result.reachingDef.get("w").node);

    // "this.x" is defined in the else block, but it is not reachable from the use
    // of "x" in the if block, so it should not be in the result
    assertNull(result.reachingDef.get("this.x"));
  }

  @org.junit.Test
  public void testVariableInOuterScope() {
    Scope testScope = new Scope();
    ControlFlowGraph<Node> testCfg = new ControlFlowGraph<Node>(testScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(testCfg, testScope, null);

    Node root = new Node(Token.FUNCTION);

    // x = 1;
    Node xAssign = new Node(Token.ASSIGN, new Node("x"), new Node(Token.NUM, 1));
    root.addChild(xAssign);

    // if (x > 0) {
    Node ifNode = new Node(Token.IF, null, null, null, null);
    root.addChild(ifNode);

    Node condition = new Node(Token.GREATER, new Node("x"), new Node(Token.NUM, 0));
    ifNode.setCondition(condition);

    // y = 2;
    Node yAssign = new Node(Token.ASSIGN, new Node("y"), new Node(Token.NUM, 2));
    Node ifBody = new Node(Token.BLOCK);
    ifNode.setTrueQuad(new Node[] { ifBody });
    ifBody.addChild(yAssign);

    // z = x + y;
    Node add = new Node(Token.PLUS, new Node("x"), new Node("y"));
    Node zAssign = new Node(Token.ASSIGN, new Node("z"), add);
    ifBody.addChild(zAssign);

    // }
    Node elseNode = new Node(Token.ELSE, null, null, null, null);
    ifNode.setFalseQuad(new Node[] { elseNode });

    Node elseBody = new Node(Token.BLOCK);
    elseNode.setBody(elseBody);

    // outerScopeVar = 3;
    Node outerScopeVarAssign = new Node(Token.ASSIGN, new Node("outerScopeVar"), new Node(Token.NUM, 3));
    elseBody.addChild(outerScopeVarAssign);

    testCfg.setRootNode(root);

    MustDef result = analyzer.run(root);

    // x is defined in the if block, but it does not depend on outerScopeVar
    // so it should still be defined there
    assertSame(xAssign, result.reachingDef.get("x").node);

    // y is defined in the if block, so it is defined there
    assertSame(yAssign, result.reachingDef.get("y").node);

    // z is defined in the if block, so it is defined there
    assertSame(zAssign, result.reachingDef.get("z").node);

    // outerScopeVar is defined in the else block, but it is not reachable from the use
    // of x in the if block, so it should not be in the result
    assertNull(result.reachingDef.get("outerScopeVar"));
  }

  @org.junit.Test
  public void testVariableInCatchBlock() {
    Scope testScope = new Scope();
    ControlFlowGraph<Node> testCfg = new ControlFlowGraph<Node>(testScope);
    MustBeReachingVariableDef analyzer = new MustBeReachingVariableDef(testCfg, testScope, null);

    Node root = new Node(Token.FUNCTION, null, null, null, null);

    // try {
    Node tryBlock = new Node(Token.BLOCK, null, null, null, null);
    root.addChild(tryBlock);

    // x = 1;
    Node xAssign = new Node(Token.ASSIGN, new Node("x"), new Node(Token.NUM, 1));
    tryBlock.addChild(xAssign);

    // throw new Error();
    Node throwNode = new Node(Token.THROW, null, null, null, null);
    tryBlock.addChild(throwNode);

    // } catch (Error e) {
    Node catchNode = new Node(Token.CATCH, null, null, null, null);
    root.addChild(catchNode);

    Node catchBlock = new Node(Token.BLOCK, null, null, null, null);
    catchNode.setBody(catchBlock);

    // y = 2;
    Node yAssign = new Node(Token.ASSIGN, new Node("y"), new Node(Token.NUM, 2));
    catchBlock.addChild(yAssign);

    testCfg.setRootNode(root);

    MustDef result = analyzer.run(root);

    // x is defined before the throw statement, but it is not reachable from the catch block
    // so it should not be in the result
    assertNull(result.reachingDef.get("x"));

    // y is defined in the catch block, so it is defined there
    assertSame(yAssign, result.reachingDef.get("y").node);
  }
}