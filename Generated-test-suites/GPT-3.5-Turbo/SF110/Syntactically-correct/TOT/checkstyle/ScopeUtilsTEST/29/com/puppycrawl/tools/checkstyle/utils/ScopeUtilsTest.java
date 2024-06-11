
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        // Typical use case - testing public scope
        DetailAST ast1 = new DetailAST();
        DetailAST token1 = new DetailAST();
        token1.setText("public");
        ast1.setFirstChild(token1);
        Scope expectedScope1 = Scope.PUBLIC;
        assertEquals(expectedScope1, ScopeUtils.getScopeFromMods(ast1));
        
        // Typical use case - testing protected scope
        DetailAST ast2 = new DetailAST();
        DetailAST token2 = new DetailAST();
        token2.setText("protected");
        ast2.setFirstChild(token2);
        Scope expectedScope2 = Scope.PROTECTED;
        assertEquals(expectedScope2, ScopeUtils.getScopeFromMods(ast2));
        
        // Typical use case - testing private scope
        DetailAST ast3 = new DetailAST();
        DetailAST token3 = new DetailAST();
        token3.setText("private");
        ast3.setFirstChild(token3);
        Scope expectedScope3 = Scope.PRIVATE;
        assertEquals(expectedScope3, ScopeUtils.getScopeFromMods(ast3));
        
        // Edge case - no modifiers present
        DetailAST ast4 = new DetailAST();
        Scope expectedScope4 = Scope.PACKAGE;
        assertEquals(expectedScope4, ScopeUtils.getScopeFromMods(ast4));
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Typical use case - testing parent with public modifier
        DetailAST ast1 = new DetailAST();
        ast1.setType(14);
        DetailAST mods1 = new DetailAST();
        DetailAST token1 = new DetailAST();
        token1.setText("public");
        mods1.setFirstChild(token1);
        ast1.addChild(mods1);
        Scope expectedScope1 = Scope.PUBLIC;
        assertEquals(expectedScope1, ScopeUtils.getSurroundingScope(ast1));
        
        // Typical use case - testing parent with protected modifier
        DetailAST ast2 = new DetailAST();
        ast2.setType(15);
        DetailAST mods2 = new DetailAST();
        DetailAST token2 = new DetailAST();
        token2.setText("protected");
        mods2.setFirstChild(token2);
        ast2.addChild(mods2);
        Scope expectedScope2 = Scope.PROTECTED;
        assertEquals(expectedScope2, ScopeUtils.getSurroundingScope(ast2));
        
        // Typical use case - testing parent with private modifier
        DetailAST ast3 = new DetailAST();
        ast3.setType(157);
        DetailAST mods3 = new DetailAST();
        DetailAST token3 = new DetailAST();
        token3.setText("private");
        mods3.setFirstChild(token3);
        ast3.addChild(mods3);
        Scope expectedScope3 = Scope.PRIVATE;
        assertEquals(expectedScope3, ScopeUtils.getSurroundingScope(ast3));
        
        // Typical use case - testing parent with abstract modifier
        DetailAST ast4 = new DetailAST();
        ast4.setType(154);
        DetailAST mods4 = new DetailAST();
        DetailAST token4 = new DetailAST();
        token4.setText("abstract");
        mods4.setFirstChild(token4);
        ast4.addChild(mods4);
        Scope expectedScope4 = Scope.PACKAGE;
        assertEquals(expectedScope4, ScopeUtils.getSurroundingScope(ast4));
        
        // Typical use case - testing parent with anonymous inner class
        DetailAST ast5 = new DetailAST();
        ast5.setType(136);
        Scope expectedScope5 = Scope.ANONINNER;
        assertEquals(expectedScope5, ScopeUtils.getSurroundingScope(ast5));
        
        // Edge case - no parent
        DetailAST ast6 = new DetailAST();
        Scope expectedScope6 = null;
        assertEquals(expectedScope6, ScopeUtils.getSurroundingScope(ast6));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Typical use case - testing inside an interface block
        DetailAST ast1 = new DetailAST();
        ast1.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(ast1));
        
        // Typical use case - testing not inside an interface block
        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(ast2));
        
        // Edge case - no parent
        DetailAST ast3 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceBlock(ast3));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Typical use case - testing inside an annotation block
        DetailAST ast1 = new DetailAST();
        ast1.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(ast1));
        
        // Typical use case - testing not inside an annotation block
        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(ast2));
        
        // Edge case - no parent
        DetailAST ast3 = new DetailAST();
        assertFalse(ScopeUtils.isInAnnotationBlock(ast3));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Typical use case - testing inside an interface block
        DetailAST ast1 = new DetailAST();
        ast1.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast1));
        
        // Typical use case - testing inside an annotation block
        DetailAST ast2 = new DetailAST();
        ast2.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(ast2));
        
        // Typical use case - testing not inside an interface or annotation block
        DetailAST ast3 = new DetailAST();
        ast3.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast3));
        
        // Edge case - no parent
        DetailAST ast4 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(ast4));
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Typical use case - testing inside an enum block
        DetailAST ast1 = new DetailAST();
        ast1.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(ast1));
        
        // Typical use case - testing not inside an enum block
        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(ast2));
        
        // Edge case - no parent
        DetailAST ast3 = new DetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(ast3));
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Typical use case - testing inside a code block
        DetailAST ast1 = new DetailAST();
        ast1.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(ast1));
        
        // Typical use case - testing not inside a code block
        DetailAST ast2 = new DetailAST();
        ast2.setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(ast2));
        
        // Edge case - no parent
        DetailAST ast3 = new DetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(ast3));
    }
    
    @Test
    public void testIsOuterMostType() {
        // Typical use case - testing outermost type
        DetailAST ast1 = new DetailAST();
        DetailAST parent1 = new DetailAST();
        parent1.setType(14);
        ast1.setParent(parent1);
        assertTrue(ScopeUtils.isOuterMostType(ast1));
        
        // Typical use case - testing non-outermost type
        DetailAST ast2 = new DetailAST();
        DetailAST grandparent2 = new DetailAST();
        grandparent2.setType(14);
        DetailAST parent2 = new DetailAST();
        parent2.setType(15);
        parent2.setParent(grandparent2);
        ast2.setParent(parent2);
        assertFalse(ScopeUtils.isOuterMostType(ast2));
        
        // Edge case - no parent
        DetailAST ast3 = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(ast3));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Typical use case - testing local variable definition in method
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);
        DetailAST parent1 = new DetailAST();
        parent1.setType(7);
        ast1.setParent(parent1);
        assertTrue(ScopeUtils.isLocalVariableDef(ast1));
        
        // Typical use case - testing local variable definition in lambda expression
        DetailAST ast2 = new DetailAST();
        ast2.setType(10);
        DetailAST parent2 = new DetailAST();
        parent2.setType(96);
        ast2.setParent(parent2);
        assertTrue(ScopeUtils.isLocalVariableDef(ast2));
        
        // Typical use case - testing non-local variable definition
        DetailAST ast3 = new DetailAST();
        ast3.setType(10);
        DetailAST parent3 = new DetailAST();
        parent3.setType(5);
        ast3.setParent(parent3);
        assertFalse(ScopeUtils.isLocalVariableDef(ast3));
        
        // Edge case - no parent
        DetailAST ast4 = new DetailAST();
        assertFalse(ScopeUtils.isLocalVariableDef(ast4));
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Typical use case - testing class field definition
        DetailAST ast1 = new DetailAST();
        ast1.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(ast1));
        
        // Typical use case - testing local variable definition
        DetailAST ast2 = new DetailAST();
        ast2.setType(10);
        DetailAST parent2 = new DetailAST();
        parent2.setType(7);
        ast2.setParent(parent2);
        assertTrue(ScopeUtils.isClassFieldDef(ast2));
        
        // Edge case - no parent
        DetailAST ast3 = new DetailAST();
        assertFalse(ScopeUtils.isClassFieldDef(ast3));
    }
    
    @Test
    public void testIsInScope() {
        // Typical use case - testing ast in given scope
        DetailAST ast1 = new DetailAST();
        ast1.setType(7);
        Scope scope1 = Scope.PUBLIC;
        assertTrue(ScopeUtils.isInScope(ast1, scope1));
        
        // Typical use case - testing ast not in given scope
        DetailAST ast2 = new DetailAST();
        ast2.setType(15);
        Scope scope2 = Scope.PUBLIC;
        assertFalse(ScopeUtils.isInScope(ast2, scope2));
        
        // Edge case - no surrounding scope
        DetailAST ast3 = new DetailAST();
        assertFalse(ScopeUtils.isInScope(ast3, scope1));
    }

}
