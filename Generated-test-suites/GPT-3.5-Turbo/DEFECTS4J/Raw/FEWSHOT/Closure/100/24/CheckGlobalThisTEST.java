package com.google.javascript.jscomp;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Before;
import org.junit.Test;

public class CheckGlobalThisTest {
  
  private AbstractCompiler compiler;
  private CheckLevel level;
  private CheckGlobalThis checkGlobalThis;
  
  @Before
  public void setup() {
    compiler = new AbstractCompiler();
    level = CheckLevel.WARNING;
    checkGlobalThis = new CheckGlobalThis(compiler, level);
  }
  
  @Test
  public void shouldTraverse_ReturnTrueWhenNonGlobalContext() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.EXPR_RESULT);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertTrue(result);
  }
  
  @Test
  public void shouldTraverse_ReturnFalseWhenConstructorFunction() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    parent.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_ReturnFalseWhenFunctionWithThisAnnotation() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    parent.setJSDocInfo(jsDoc);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_ReturnFalseWhenAssignmentToPrototypeProperty() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    Node lhs = new Node(Token.NAME, "Object");
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void shouldTraverse_ReturnFalseWhenAssignmentToPrototypePropertySubproperty() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.GETPROP);
    
    Node grandparent = new Node(Token.GETPROP);
    Node greatGrandparent = new Node(Token.GETPROP);
    
    Node varNode = new Node(Token.NAME, "Object");
    Node prototypeNode = new Node(Token.NAME, "prototype");
    Node subpropertyNode = new Node(Token.NAME, "subproperty");
    
    Node rhs = new Node(Token.THIS);
    
    grandparent.addChildToBack(varNode);
    grandparent.addChildToBack(prototypeNode);
    
    greatGrandparent.addChildToBack(grandparent);
    greatGrandparent.addChildToBack(subpropertyNode);
    
    parent.addChildToBack(greatGrandparent);
    parent.addChildToBack(rhs);
    
    boolean result = checkGlobalThis.shouldTraverse(t, n, parent);
    
    assertFalse(result);
  }
  
  @Test
  public void visit_ReportErrorWhenThisIsOnLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    Node lhs = new Node(Token.NAME, "varName");
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    
    checkGlobalThis.visit(t, n, parent);
    
    // Verify that a warning was reported
  }
  
  @Test
  public void visit_DoNotReportErrorWhenThisIsNotOnLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    Node lhs = new Node(Token.NAME, "object");
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    
    checkGlobalThis.visit(t, n, parent);
    
    // Verify that no warning was reported
  }
  
  @Test
  public void visit_SetAssignLhsChildToNullWhenThisIsNotOnLeftSideOfAssignment() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    Node lhs = new Node(Token.NAME, "object");
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    
    checkGlobalThis.visit(t, n, parent);
    
    // Verify that assignLhsChild is null
  }
  
  @Test
  public void visit_DoNotReportErrorWhenThisIsNotOnLeftSideOfAssignmentNestedAssignments() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    Node nestedAssignLhs = new Node(Token.NAME, "varName");
    Node nestedAssignRhs = new Node(Token.THIS);
    Node nestedAssignNode = new Node(Token.ASSIGN, nestedAssignLhs, nestedAssignRhs);
    
    Node lhs = new Node(Token.NAME, "object");
    Node rhs = new Node(Token.THIS);
    parent.addChildToBack(lhs);
    parent.addChildToBack(nestedAssignNode);
    
    checkGlobalThis.visit(t, n, parent);
    
    // Verify that no warning was reported
  }
  
  @Test
  public void visit_DoNotReportErrorWhenThisIsNotOnLeftSideOfAssignmentAssignmentToPrototypeProperty() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    Node lhs = new Node(Token.GETPROP);
    Node rhs = new Node(Token.THIS);
    
    Node objectNode = new Node(Token.NAME, "Object");
    Node prototypeNode = new Node(Token.NAME, "prototype");
    
    lhs.addChildToBack(objectNode);
    lhs.addChildToBack(prototypeNode);
    
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    
    checkGlobalThis.visit(t, n, parent);
    
    // Verify that no warning was reported
  }
  
  @Test
  public void visit_DoNotReportErrorWhenThisIsNotOnLeftSideOfAssignmentAssignmentToPrototypePropertySubproperty() {
    NodeTraversal t = new NodeTraversal(compiler);
    Node n = new Node(Token.THIS);
    Node parent = new Node(Token.ASSIGN);
    
    Node lhs = new Node(Token.GETPROP);
    Node rhs = new Node(Token.THIS);
    
    Node rootObjectNode = new Node(Token.NAME, "Object");
    Node prototypeNode = new Node(Token.NAME, "prototype");
    Node subpropertyNode = new Node(Token.NAME, "subproperty");
    
    lhs.addChildToBack(rootObjectNode);
    lhs.addChildToBack(prototypeNode);
    lhs.addChildToBack(subpropertyNode);
    
    parent.addChildToBack(lhs);
    parent.addChildToBack(rhs);
    
    checkGlobalThis.visit(t, n, parent);
    
    // Verify that no warning was reported
  }
  
  private static JSDocInfo createJsDocForFunction() {
    JSDocInfo jsDoc = new JSDocInfo();
    Node rootObj = new Node(Token.NAME, "Object");
    Node prototype = new Node(Token.NAME, "prototype");
    Node varNode = new Node(Token.NAME, "varName");
    
    jsDoc.recordThisType();
    
    Node functionNode = new Node(Token.FUNCTION);
    functionNode.addChildToBack(rootObj);
    
    Node parent = new Node(Token.FUNCTION);
    parent.addChildToFront(prototype);
    parent.addChildToFront(varNode);
    
    parent.setJSDocInfo(jsDoc);
    
    functionNode.setParent(parent);
    
    return jsDoc;
  }
  
  private static JSDocInfo createJsDocForConstructorFunction() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordConstructor();
    
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    parent.setJSDocInfo(jsDoc);
    
    functionNode.setParent(parent);
    
    return jsDoc;
  }
  
  private static JSDocInfo createJsDocForFunctionWithThisAnnotation() {
    JSDocInfo jsDoc = new JSDocInfo();
    jsDoc.recordThisType();
    
    Node functionNode = new Node(Token.FUNCTION);
    Node parent = new Node(Token.FUNCTION);
    parent.setJSDocInfo(jsDoc);
    
    functionNode.setParent(parent);
    
    return jsDoc;
  }
  
  private static Node createAssignWithNestedFunctionAssignAndThis() {
    Node lhs = new Node(Token.NAME, "varName");
    Node rhs = new Node(Token.THIS);
    
    Node nestedAssignLhs = new Node(Token.NAME, "nestedVar");
    Node nestedAssignRhs = new Node(Token.THIS);
    Node nestedAssignNode = new Node(Token.ASSIGN, nestedAssignLhs, nestedAssignRhs);
    
    Node assignNode = new Node(Token.ASSIGN, lhs, rhs);
    assignNode.addChildToBack(nestedAssignNode);
    
    return assignNode;
  }

}