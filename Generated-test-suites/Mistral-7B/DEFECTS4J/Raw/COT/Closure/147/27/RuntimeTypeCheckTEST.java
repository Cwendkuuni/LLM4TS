package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.DisableRewrittenClasses;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.Token;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testRuntimeTypeCheck() {
    String jsCode = "function Test() {}";
    Compiler compiler = new Compiler();
    Node rootNode = NodeFactory.compile(compiler, jsCode, null, null, null);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DisableRewrittenClasses());
    passManager.addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses(rootNode);

    Node functionNode = NodeUtil.findFunctionByName(rootNode, "Test");
    Node constructorNode = functionNode.getFirstChild();
    Node checkTypeCallNode = findCheckTypeCallNode(constructorNode);
    assertTrue("CheckType call expected", checkTypeCallNode != null);
  }

  @Test
  public void testRuntimeTypeCheckWithInterface() {
    String jsCode = "function Test() {}" +
        "function I {}";
    Compiler compiler = new Compiler();
    Node rootNode = NodeFactory.compile(compiler, jsCode, null, null, null);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DisableRewrittenClasses());
    passManager.addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses(rootNode);

    Node functionNode = NodeUtil.findFunctionByName(rootNode, "Test");
    Node constructorNode = functionNode.getFirstChild();
    Node checkTypeCallNode = findCheckTypeCallNode(constructorNode);
    assertTrue("CheckType call expected", checkTypeCallNode != null);

    Node interfaceCheckerCallNode = findCheckerCallNode(checkTypeCallNode, "I");
    assertTrue("Interface checker call expected", interfaceCheckerCallNode != null);
  }

  @Test
  public void testRuntimeTypeCheckWithUnionType() {
    String jsCode = "function Test(x) {}" +
        "Test.prototype.foo = function() { return 1; }";
    Compiler compiler = new Compiler();
    Node rootNode = NodeFactory.compile(compiler, jsCode, null, null, null);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DisableRewrittenClasses());
    passManager.addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses(rootNode);

    Node functionNode = NodeUtil.findFunctionByName(rootNode, "Test");
    Node constructorNode = functionNode.getFirstChild();
    Node checkTypeCallNode = findCheckTypeCallNode(constructorNode);
    assertTrue("CheckType call expected", checkTypeCallNode != null);

    Node unionTypeCheckerCallNode = findCheckerCallNode(checkTypeCallNode, "number|string");
    assertTrue("Union type checker call expected", unionTypeCheckerCallNode != null);
  }

  @Test(expected = RuntimeException.class)
  public void testRuntimeTypeCheckWithUnsupportedType() {
    String jsCode = "function Test() {}" +
        "Test.prototype.foo = function() { throw new TypeError(); }";
    Compiler compiler = new Compiler();
    Node rootNode = NodeFactory.compile(compiler, jsCode, null, null, null);
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new DisableRewrittenClasses());
    passManager.addPass(new RuntimeTypeCheck(compiler, null));
    passManager.runPasses(rootNode);

    Node functionNode = NodeUtil.findFunctionByName(rootNode, "Test");
    Node constructorNode = functionNode.getFirstChild();
    Node checkTypeCallNode = findCheckTypeCallNode(constructorNode);
    assertTrue("CheckType call expected", checkTypeCallNode != null);

    Node unsupportedTypeCheckerCallNode = findCheckerCallNode(checkTypeCallNode, "TypeError");
    assertTrue("UnsupportedType checker call not expected", unsupportedTypeCheckerCallNode == null);
  }

  private Node findCheckTypeCallNode(Node node) {
    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.CALL &&
          child.getFirstChild().getType() == Token.IDENT &&
          child.getFirstChild().getString().equals("checkType")) {
        return child;
      }
    }
    return null;
  }

  private Node findCheckerCallNode(Node checkTypeCallNode, String checkerName) {
    for (Node child = checkTypeCallNode.getLastChild(); child != null; child = child.getPrevious()) {
      if (child.getType() == Token.CALL &&
          (child.getFirstChild().getType() == Token.IDENT &&
              child.getFirstChild().getString().equals("valueChecker") ||
          child.getFirstChild().getType() == Token.IDENT &&
              child.getFirstChild().getString().equals("classChecker") ||
              child.getFirstChild().getString().equals("interfaceChecker")) &&
          NodeUtil.isStringLiteral(child.getLastChild()) &&
          child.getLastChild().getString().equals(checkerName)) {
        return child;
      }
    }
    return null;
  }
}