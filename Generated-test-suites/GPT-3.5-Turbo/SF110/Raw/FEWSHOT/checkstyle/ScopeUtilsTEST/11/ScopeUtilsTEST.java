
package com.puppycrawl.tools.checkstyle.utils;

import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(null));
        
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(getFirstToken("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(detailAST));
        
        detailAST.addChild(getFirstToken("protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(detailAST));
        
        detailAST.addChild(getFirstToken("private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(detailAST));
        
        //Case where multiple modifiers are present
        detailAST = new DetailAST();
        detailAST.addChild(getFirstToken("protected"));
        detailAST.addChild(getFirstToken("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(detailAST));
    }

    @Test
    public void testGetSurroundingScope() {
        assertNull(ScopeUtils.getSurroundingScope(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where surrounding scope is not found
        detailAST.setType(18);
        assertNull(ScopeUtils.getSurroundingScope(detailAST));
        
        //Case where surrounding scope is found
        detailAST.setType(14);
        DetailAST parentToken = new DetailAST();
        parentToken.setType(5);
        parentToken.addChild(getFirstToken("public"));
        detailAST.setParent(parentToken);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(detailAST));
        
        //Case where surrounding scope is found and already set
        DetailAST grandParentToken = new DetailAST();
        grandParentToken.setType(19);
        grandParentToken.addChild(getFirstToken("protected"));
        parentToken.setParent(grandParentToken);
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(detailAST));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        assertFalse(ScopeUtils.isInInterfaceBlock(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not inside an interface block
        detailAST.setType(7);
        assertFalse(ScopeUtils.isInInterfaceBlock(detailAST));
        
        detailAST.setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(detailAST));
        
        detailAST.setType(19);
        assertFalse(ScopeUtils.isInInterfaceBlock(detailAST));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        assertFalse(ScopeUtils.isInAnnotationBlock(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not inside an annotation block
        detailAST.setType(7);
        assertFalse(ScopeUtils.isInAnnotationBlock(detailAST));
        
        detailAST.setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(detailAST));
        
        detailAST.setType(19);
        assertFalse(ScopeUtils.isInAnnotationBlock(detailAST));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not inside an interface or annotation block
        detailAST.setType(7);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
        
        detailAST.setType(157);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
        
        detailAST.setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
        
        detailAST.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
    }
    
    @Test
    public void testIsInEnumBlock() {
        assertFalse(ScopeUtils.isInEnumBlock(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not inside an enum block
        detailAST.setType(7);
        assertFalse(ScopeUtils.isInEnumBlock(detailAST));
        
        detailAST.setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(detailAST));
        
        detailAST.setType(15);
        assertFalse(ScopeUtils.isInEnumBlock(detailAST));
    }
    
    @Test
    public void testIsInCodeBlock() {
        assertFalse(ScopeUtils.isInCodeBlock(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not inside a code block
        detailAST.setType(7);
        assertFalse(ScopeUtils.isInCodeBlock(detailAST));
        
        detailAST.setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(detailAST));
        
        detailAST.setType(111);
        assertFalse(ScopeUtils.isInCodeBlock(detailAST));
    }
    
    @Test
    public void testIsOuterMostType() {
        assertTrue(ScopeUtils.isOuterMostType(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not the outermost type
        detailAST.setType(10);
        assertFalse(ScopeUtils.isOuterMostType(detailAST));
        
        detailAST.setType(154);
        assertTrue(ScopeUtils.isOuterMostType(detailAST));
        
        detailAST.setType(19);
        assertFalse(ScopeUtils.isOuterMostType(detailAST));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        assertFalse(ScopeUtils.isLocalVariableDef(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not a local variable definition
        detailAST.setType(7);
        assertFalse(ScopeUtils.isLocalVariableDef(detailAST));
        
        detailAST.setType(10);
        assertFalse(ScopeUtils.isLocalVariableDef(detailAST));
        
        //Case where a local variable definition
        detailAST.setType(10);
        DetailAST parentToken = new DetailAST();
        parentToken.setType(7);
        detailAST.setParent(parentToken);
        assertTrue(ScopeUtils.isLocalVariableDef(detailAST));
        
        detailAST.setType(21);
        parentToken.setType(96);
        assertTrue(ScopeUtils.isLocalVariableDef(detailAST));
    }
    
    @Test
    public void testIsClassFieldDef() {
        assertFalse(ScopeUtils.isClassFieldDef(null));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where not a class field definition
        detailAST.setType(7);
        assertFalse(ScopeUtils.isClassFieldDef(detailAST));
        
        detailAST.setType(10);
        assertFalse(ScopeUtils.isClassFieldDef(detailAST));
        
        //Case where a class field definition
        detailAST.setType(10);
        DetailAST parentToken = new DetailAST();
        parentToken.setType(7);
        detailAST.setParent(parentToken);
        assertFalse(ScopeUtils.isClassFieldDef(detailAST));
    }
    
    @Test
    public void testIsInScope() {
        assertFalse(ScopeUtils.isInScope(null, Scope.PUBLIC));
        
        DetailAST detailAST = new DetailAST();
        
        //Case where scope does not match
        detailAST.setType(7);
        assertFalse(ScopeUtils.isInScope(detailAST, Scope.PACKAGE));
        
        //Case where scope matches
        detailAST.setType(14);
        DetailAST parentToken = new DetailAST();
        parentToken.setType(5);
        parentToken.addChild(getFirstToken("public"));
        detailAST.setParent(parentToken);
        assertTrue(ScopeUtils.isInScope(detailAST, Scope.PUBLIC));
    }
    
    private DetailAST getFirstToken(String tokenText) {
        DetailAST detailAST = new DetailAST();
        detailAST.setType(1);
        detailAST.setText(tokenText);
        return detailAST;
    }
}
