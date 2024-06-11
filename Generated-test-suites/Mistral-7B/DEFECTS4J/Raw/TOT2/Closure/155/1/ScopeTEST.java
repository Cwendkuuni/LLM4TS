import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Scope.*;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Test;

public class ScopeTest {

 // Test constructor with valid arguments
 @Test
 public void testScopeConstructor() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 assertEquals(0, scope.getDepth());
 assertNull(scope.getParent());
 assertNotNull(scope.getTypeOfThis());
 assertFalse(scope.isBottom());
 }

 // Test constructor with null rootNode
 @Test(expected = IllegalArgumentException.class)
 public void testScopeConstructorWithNullRootNode() {
 new Scope(null, null);
 }

 // Test constructor with null parent
 @Test
 public void testScopeConstructorWithNullParent() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Scope parent = new Scope(null, new Node(Token.BLOCK, null));
 scope = new Scope(parent, new Node(Token.FUNCTION, null));
 assertEquals(1, scope.getDepth());
 assertSame(parent, scope.getParent());
 assertNotNull(scope.getTypeOfThis());
 assertFalse(scope.isBottom());
 }

 // Test constructor with bottom scope
 @Test
 public void testScopeConstructorWithBottomScope() {
 Scope scope = new Scope(new Node(Token.BLOCK, null), new ObjectType(String.class));
 assertEquals(0, scope.getDepth());
 assertNull(scope.getParent());
 assertTrue(scope.isBottom());
 }

 // Test create a variable with valid arguments
 @Test
 public void testDeclareVariable() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var = scope.declare("test", new Node(Token.NAME, "test"), String.class, new CompilerInput("test.js"));
 assertEquals("test", var.getName());
 assertEquals(Integer.TYPE, var.getType().getBaseType());
 assertNotNull(var.getParentScope());
 assertEquals(scope, var.getScope());
 }

 // Test create a variable with null nameNode
 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullNameNode() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 scope.declare("test", null, String.class, new CompilerInput("test.js"));
 }

 // Test create a variable with null type
 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullType() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 scope.declare("test", new Node(Token.NAME, "test"), null, new CompilerInput("test.js"));
 }

 // Test create a variable with null input
 @Test(expected = IllegalArgumentException.class)
 public void testDeclareVariableWithNullInput() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 scope.declare("test", new Node(Token.NAME, "test"), String.class, null);
 }

 // Test create a variable with duplicate name
 @Test(expected = IllegalStateException.class)
 public void testDeclareVariableWithDuplicateName() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 scope.declare("test", new Node(Token.NAME, "test"), String.class, new CompilerInput("test.js"));
 scope.declare("test", new Node(Token.NAME, "test"), String.class, new CompilerInput("test.js"));
 }

 // Test getVar with valid variable name
 @Test
 public void testGetVarWithValidName() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 Var var = scope.getVar("test1");
 assertEquals(var1, var);
 }

 // Test getVar with null variable name
 @Test(expected = IllegalArgumentException.class)
 public void testGetVarWithNullName() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 scope.getVar(null);
 }

 // Test getVar with non-existent variable name
 @Test
 public void testGetVarWithNonExistentName() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 Var var = scope.getVar("test3");
 assertNull(var);
 }

 // Test getVar with parent scope
 @Test
 public void testGetVarWithParentScope() {
 Scope parentScope = new Scope(null, new Node(Token.FUNCTION, null));
 Var parentVar = parentScope.declare("parent", new Node(Token.NAME, "parent"), String.class, new CompilerInput("parent.js"));
 Scope scope = new Scope(parentScope, new Node(Token.FUNCTION, null));
 Var var = scope.getVar("parent");
 assertEquals(parentVar, var);
 }

 // Test getVar with grandparent scope
 @Test
 public void testGetVarWithGrandparentScope() {
 Scope grandparentScope = new Scope(null, new Node(Token.FUNCTION, null));
 Var grandparentVar = grandparentScope.declare("grandparent", new Node(Token.NAME, "grandparent"), String.class, new CompilerInput("grandparent.js"));
 Scope parentScope = new Scope(grandparentScope, new Node(Token.FUNCTION, null));
 Var parentVar = parentScope.declare("parent", new Node(Token.NAME, "parent"), String.class, new CompilerInput("parent.js"));
 Scope scope = new Scope(parentScope, new Node(Token.FUNCTION, null));
 Var var = scope.getVar("grandparent");
 assertEquals(grandparentVar, var);
 }

 // Test getVar with global scope
 @Test
 public void testGetVarWithGlobalScope() {
 Scope globalScope = new Scope(null, null);
 Var globalVar = globalScope.declare("global", new Node(Token.NAME, "global"), String.class, null);
 Scope scope = new Scope(globalScope, new Node(Token.FUNCTION, null));
 Var var = scope.getVar("global");
 assertEquals(globalVar, var);
 }

 // Test undeclare variable
 @Test
 public void testUndeclareVariable() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var = scope.declare("test", new Node(Token.NAME, "test"), String.class, new CompilerInput("test.js"));
 scope.undeclare(var);
 assertNull(scope.getVar("test"));
 }

 // Test getVars with empty scope
 @Test
 public void testGetVarsWithEmptyScope() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Iterator<Var> it = scope.getVars();
 assertFalse(it.hasNext());
 }

 // Test getVars with scope containing variables
 @Test
 public void testGetVarsWithScopeContainingVariables() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 Iterator<Var> it = scope.getVars();
 assertTrue(it.hasNext());
 Var var = it.next();
 assertEquals(var1, var);
 assertTrue(it.hasNext());
 var = it.next();
 assertEquals(var2, var);
 }

 // Test getVarCount with empty scope
 @Test
 public void testGetVarCountWithEmptyScope() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 assertEquals(0, scope.getVarCount());
 }

 // Test getVarCount with scope containing variables
 @Test
 public void testGetVarCountWithScopeContainingVariables() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 assertEquals(2, scope.getVarCount());
 }

 // Test isDeclared with valid name and recurse
 @Test
 public void testIsDeclaredWithValidNameAndRecurse() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 Var var3 = scope.declare("test1", new Node(Token.NAME, "test1"), Double.class, new CompilerInput("test.js"));
 assertTrue(scope.isDeclared("test1", true));
 }

 // Test isDeclared with null name
 @Test(expected = IllegalArgumentException.class)
 public void testIsDeclaredWithNullName() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 scope.isDeclared(null, true);
 }

 // Test isDeclared with non-existent name
 @Test
 public void testIsDeclaredWithNonExistentName() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 assertFalse(scope.isDeclared("test3", true));
 }

 // Test isDeclared with parent scope
 @Test
 public void testIsDeclaredWithParentScope() {
 Scope parentScope = new Scope(null, new Node(Token.FUNCTION, null));
 Var parentVar = parentScope.declare("parent", new Node(Token.NAME, "parent"), String.class, new CompilerInput("parent.js"));
 Scope scope = new Scope(parentScope, new Node(Token.FUNCTION, null));
 assertTrue(scope.isDeclared("parent", true));
 }

 // Test isDeclared with grandparent scope
 @Test
 public void testIsDeclaredWithGrandparentScope() {
 Scope grandparentScope = new Scope(null, new Node(Token.FUNCTION, null));
 Var grandparentVar = grandparentScope.declare("grandparent", new Node(Token.NAME, "grandparent"), String.class, new CompilerInput("grandparent.js"));
 Scope parentScope = new Scope(grandparentScope, new Node(Token.FUNCTION, null));
 Var parentVar = parentScope.declare("parent", new Node(Token.NAME, "parent"), String.class, new CompilerInput("parent.js"));
 Scope scope = new Scope(parentScope, new Node(Token.FUNCTION, null));
 assertTrue(scope.isDeclared("grandparent", true));
 }

 // Test isDeclared with global scope
 @Test
 public void testIsDeclaredWithGlobalScope() {
 Scope globalScope = new Scope(null, null);
 Var globalVar = globalScope.declare("global", new Node(Token.NAME, "global"), String.class, null);
 Scope scope = new Scope(globalScope, new Node(Token.FUNCTION, null));
 assertTrue(scope.isDeclared("global", true));
 }

 // Test isLocal with scope containing local variables
 @Test
 public void testIsLocalWithScopeContainingLocalVariables() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 assertTrue(scope.isLocal());
 }

 // Test isLocal with scope containing global variables
 @Test
 public void testIsLocalWithScopeContainingGlobalVariables() {
 Scope globalScope = new Scope(null, null);
 Var globalVar1 = globalScope.declare("global1", new Node(Token.NAME, "global1"), String.class, null);
 Var globalVar2 = globalScope.declare("global2", new Node(Token.NAME, "global2"), Integer.class, null);
 Scope scope = new Scope(globalScope, new Node(Token.FUNCTION, null));
 assertFalse(scope.isLocal());
 }

 // Test isLocal with empty scope
 @Test
 public void testIsLocalWithEmptyScope() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 assertTrue(scope.isLocal());
 }

 // Test isGlobal with scope containing local variables
 @Test
 public void testIsGlobalWithScopeContainingLocalVariables() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 Var var1 = scope.declare("test1", new Node(Token.NAME, "test1"), String.class, new CompilerInput("test.js"));
 Var var2 = scope.declare("test2", new Node(Token.NAME, "test2"), Integer.class, new CompilerInput("test.js"));
 assertFalse(scope.isGlobal());
 }

 // Test isGlobal with scope containing global variables
 @Test
 public void testIsGlobalWithScopeContainingGlobalVariables() {
 Scope globalScope = new Scope(null, null);
 Var globalVar1 = globalScope.declare("global1", new Node(Token.NAME, "global1"), String.class, null);
 Var globalVar2 = globalScope.declare("global2", new Node(Token.NAME, "global2"), Integer.class, null);
 Scope scope = new Scope(globalScope, new Node(Token.FUNCTION, null));
 assertTrue(scope.isGlobal());
 }

 // Test isGlobal with empty scope
 @Test
 public void testIsGlobalWithEmptyScope() {
 Scope scope = new Scope(null, new Node(Token.FUNCTION, null));
 assertTrue(scope.isGlobal());
 }
}