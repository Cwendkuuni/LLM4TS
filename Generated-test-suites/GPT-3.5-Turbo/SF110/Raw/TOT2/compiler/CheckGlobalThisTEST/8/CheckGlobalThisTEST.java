package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

public class CheckGlobalThisTest {

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    assertTrue(checkGlobalThis.shouldReportThis(Node.newThis()));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    Node functionNode = new Node(Token.FUNCTION);
    JSDocInfo jsDocInfo = new JSDocInfoBuilder(true).build();
    functionNode.setJSDocInfo(jsDocInfo);

    assertEquals(jsDocInfo, checkGlobalThis.getFunctionJsDocInfo(functionNode));
  }

  @Test
  public void testVisit() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler(), new Callback());
    Node thisNode = Node.newThis();

    checkGlobalThis.visit(nodeTraversal, thisNode, null);
    // Test if the compiler error is reported for global this
  }

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checkGlobalThis = new CheckGlobalThis(new AbstractCompiler());
    NodeTraversal nodeTraversal = new NodeTraversal(new AbstractCompiler(), new Callback());
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.SCRIPT);
    assertFalse(checkGlobalThis.shouldTraverse(nodeTraversal, functionNode, parent));
  }

}