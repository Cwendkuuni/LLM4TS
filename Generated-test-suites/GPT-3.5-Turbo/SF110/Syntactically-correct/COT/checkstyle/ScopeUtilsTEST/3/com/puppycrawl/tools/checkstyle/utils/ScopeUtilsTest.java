package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Assert;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test case 1: No modifiers
        DetailAST aMods1 = new DetailAST();
        Assert.assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods1));
        
        // Test case 2: Public modifier
        DetailAST aMods2 = new DetailAST();
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        aMods2.addChild(publicToken);
        Assert.assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods2));
        
        // Test case 3: Protected modifier
        DetailAST aMods3 = new DetailAST();
        DetailAST protectedToken = new DetailAST();
        protectedToken.setText("protected");
        aMods3.addChild(protectedToken);
        Assert.assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods3));
        
        // Test case 4: Private modifier
        DetailAST aMods4 = new DetailAST();
        DetailAST privateToken = new DetailAST();
        privateToken.setText("private");
        aMods4.addChild(privateToken);
        Assert.assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods4));
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Test case 1: No parent node
        DetailAST node1 = new DetailAST();
        Assert.assertNull(ScopeUtils.getSurroundingScope(node1));
        
        // Test case 2: Parent node is of type 14
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(14);
        node2.setParent(parent2);
        Assert.assertNull(ScopeUtils.getSurroundingScope(node2));
        
        // Test case 3: Parent node is of type 15
        DetailAST node3 = new DetailAST();
        DetailAST parent3 = new DetailAST();
        parent3.setType(15);
        node3.setParent(parent3);
        Assert.assertEquals(ScopeUtils.getScopeFromMods(parent3.findFirstToken(5)), ScopeUtils.getSurroundingScope(node3));
        
        // Test case 4: Parent node is of type 157
        DetailAST node4 = new DetailAST();
        DetailAST parent4 = new DetailAST();
        parent4.setType(157);
        node4.setParent(parent4);
        Assert.assertEquals(ScopeUtils.getScopeFromMods(parent4.findFirstToken(5)), ScopeUtils.getSurroundingScope(node4));
        
        // Test case 5: Parent node is of type 154
        DetailAST node5 = new DetailAST();
        DetailAST parent5 = new DetailAST();
        parent5.setType(154);
        node5.setParent(parent5);
        Assert.assertEquals(ScopeUtils.getScopeFromMods(parent5.findFirstToken(5)), ScopeUtils.getSurroundingScope(node5));
        
        // Test case 6: Parent node is of type 136
        DetailAST node6 = new DetailAST();
        DetailAST parent6 = new DetailAST();
        parent6.setType(136);
        node6.setParent(parent6);
        Assert.assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node6));
    }
    
    // Additional test cases for the remaining methods in the ScopeUtils class.
    // ...
}