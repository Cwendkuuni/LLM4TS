
package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class VarCheckTest {
  
  @Test
  public void testUndefinedVarError() {
    VarCheck varCheck = new VarCheck();
    
    // Create a scope with a variable that is not declared
    Scope.Var var = new Scope.Var("undefinedVar");
    Scope scope = new Scope(null);
    scope.declare("undefinedVar", null, null, null);
    
    NodeTraversal traversal = new NodeTraversal(varCheck, null);
    traversal.getScopeCreator().push(scope);
    
    // Test that the UNDEFINED_VAR_ERROR diagnostic message is thrown
    try {
        varCheck.visit(traversal, var, null);
        fail("Expected IllegalStateException to be thrown");
    } catch(IllegalStateException e) {
        assertEquals("Unexpected variable undefinedVar", e.getMessage());
    }
  }
  
  @Test
  public void testViolatedModuleDepError() {
    VarCheck varCheck = new VarCheck();
    
    // Create two modules and a variable declaration in each module
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    Node localVar1 = new Node(Token.NAME, "localVar1");
    Node localVar2 = new Node(Token.NAME, "localVar2");
    NodeDeclaration diagnosis1 = new NodeDeclaration(localVar1, null, module1);
    NodeDeclaration diagnosis2 = new NodeDeclaration(localVar2, null, module2);
    localVar1.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    localVar2.putBooleanProp(Node.IS_CONSTANT_NAME, false);
    module1.getVars().add(diagnosis1);
    module2.getVars().add(diagnosis2);
    root.addChildToFront(localVar1);
    root.addChildToFront(localVar2);
    
    // Create a traversal object
    NodeTraversal traversal = new NodeTraversal(varCheck, null);
    traversal.getScopeCreator().pushScope(module1, root, externs);
    traversal.getScopeCreator().pushScope(module2, root, externs);
    
    // Test that an error is thrown when a variable is referenced in the wrong module
    try {
        varCheck.visit(traversal, localVar1, null);
        fail("Expected IllegalStateException to be thrown");
    } catch(IllegalStateException e) {
        assertEquals("Unexpected variable localVar1", e.getMessage());
    }
  }
  
  @Test
  public void testMissingModuleDepError() {
    VarCheck varCheck = new VarCheck();
    
    // Create two modules and a variable declaration in each module
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    Node localVar1 = new Node(Token.NAME, "localVar1");
    Node localVar2 = new Node(Token.NAME, "localVar2");
    NodeDeclaration diagnosis1 = new NodeDeclaration(localVar1, null, module1);
    NodeDeclaration diagnosis2 = new NodeDeclaration(localVar2, null, module2);
    localVar1.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    localVar2.putBooleanProp(Node.IS_CONSTANT_NAME, false);
    module1.getVars().add(diagnosis1);
    module2.getVars().add(diagnosis2);
    root.addChildToFront(localVar1);
    root.addChildToFront(localVar2);
    
    // Create a traversal object
    NodeTraversal traversal = new NodeTraversal(varCheck, null);
    traversal.getScopeCreator().pushScope(module1, root, externs);
    traversal.getScopeCreator().pushScope(module2, root, externs);
    
    // Test that an error is thrown when a variable is referenced without a proper module dependency
    try {
        varCheck.visit(traversal, localVar2, null);
        fail("Expected IllegalStateException to be thrown");
    } catch(IllegalStateException e) {
        assertEquals("Unexpected variable localVar2", e.getMessage());
    }
  }
  
  @Test
  public void testStrictModuleDepError() {
    VarCheck varCheck = new VarCheck();
    
    // Create two modules and a variable declaration in each module
    JSModule module1 = new JSModule("module1");
    JSModule module2 = new JSModule("module2");
    Node externs = new Node(Token.BLOCK);
    Node root = new Node(Token.BLOCK);
    Node localVar1 = new Node(Token.NAME, "localVar1");
    Node localVar2 = new Node(Token.NAME, "localVar2");
    NodeDeclaration diagnosis1 = new NodeDeclaration(localVar1, null, module1);
    NodeDeclaration diagnosis2 = new NodeDeclaration(localVar2, null, module2);
    localVar1.putBooleanProp(Node.IS_CONSTANT_NAME, true);
    localVar2.putBooleanProp(Node.IS_CONSTANT_NAME, false);
    module1.getVars().add(diagnosis1);
    module2.getVars().add(diagnosis2);
    root.addChildToFront(localVar1);
    root.addChildToFront(localVar2);
    
    // Create a traversal object
    NodeTraversal traversal = new NodeTraversal(varCheck, null);
    traversal.getScopeCreator().pushScope(module1, root, externs);
    traversal.getScopeCreator().pushScope(module2, root, externs);
    
    // Test that an error is thrown when a variable is referenced without a proper module dependency
    try {
        varCheck.visit(traversal, localVar2, null);
        fail("Expected IllegalStateException to be thrown");
    } catch(IllegalStateException e) {
        assertEquals("Unexpected variable localVar2", e.getMessage());
    }
  }
  
  @Test
  public void testInvalidFunctionDecl() {
    VarCheck varCheck = new VarCheck();
    
    // Create a function with an empty name
    Node func = new Node(Token.FUNCTION, new Node(Token.NAME));
    NodeTraversal traversal = new NodeTraversal(varCheck, null);
    
    // Test that an error is thrown when a function declaration has no name
    try {
        varCheck.visit(traversal, func, null);
        fail("Expected IllegalStateException to be thrown");
    } catch(IllegalStateException e) {
        assertEquals("Syntax error: function declaration must have a name", e.getMessage());
    }
  }
}
