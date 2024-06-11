
package com.puppycrawl.tools.checkstyle.utils;

import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // typical use case
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(createAST("public")));
        
        // edge cases
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(createAST("default")));
    }
    
    @Test
    public void testGetSurroundingScope() {
        // typical use case
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(createAST(14)));
        
        // edge cases
        assertEquals(Scope.PRIVATE, ScopeUtils.getSurroundingScope(createAST(136)));
    }
    
    // remaining test methods for the other public methods...
    
    private DetailAST createAST(int type) {
        DetailAST ast = new DetailAST();
        ast.setType(type);
        return ast;
    }
}

