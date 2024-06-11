import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test case 1: No mods
        DetailAST aMods1 = null;
        Scope expected1 = Scope.PACKAGE;
        Scope actual1 = ScopeUtils.getScopeFromMods(aMods1);
        assertEquals(expected1, actual1);
        
        // Test case 2: One mod
        DetailAST aMods2 = new DetailAST();
        aMods2.addChild(new AST("public"));
        Scope expected2 = Scope.PUBLIC;
        Scope actual2 = ScopeUtils.getScopeFromMods(aMods2);
        assertEquals(expected2, actual2);
        
        // Test case 3: Multiple mods
        DetailAST aMods3 = new DetailAST();
        aMods3.addChild(new AST("public"));
        aMods3.addChild(new AST("protected"));
        Scope expected3 = Scope.PUBLIC;
        Scope actual3 = ScopeUtils.getScopeFromMods(aMods3);
        assertEquals(expected3, actual3);
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Test case 1: No surrounding scope
        DetailAST node1 = new DetailAST();
        Scope expected1 = null;
        Scope actual1 = ScopeUtils.getSurroundingScope(node1);
        assertEquals(expected1, actual1);
        
        // Test case 2: With surrounding scope
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.addChild(new AST("public"));
        node2.setParent(parent2);
        Scope expected2 = Scope.PUBLIC;
        Scope actual2 = ScopeUtils.getSurroundingScope(node2);
        assertEquals(expected2, actual2);
    }
    
    // Add test methods for the remaining public methods in the class
    
}

