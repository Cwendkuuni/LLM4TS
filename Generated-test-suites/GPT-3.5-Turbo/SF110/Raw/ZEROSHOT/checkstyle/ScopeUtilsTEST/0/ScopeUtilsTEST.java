package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;
import static org.junit.Assert.*;

import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = null;
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, result);

        aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("public");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, result);

        aMods.addChild(new DetailAST());
        aMods.getFirstChild().getNextSibling().setText("protected");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, result);

        aMods.addChild(new DetailAST());
        aMods.getFirstChild().getNextSibling().getNextSibling().setText("private");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, result);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = null;
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(null, result);

        node = new DetailAST();
        node.setType(14);
        Scope result = ScopeUtils.getSurroundingScope(node);
        assertEquals(null, result);

        // Add more test cases as needed
    }

    // Add more test methods for the remaining methods in ScopeUtils class

}