import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case
        detailAST.addChild(TokenUtils.createToken("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(detailAST));
        
        // Edge Cases
        detailAST.addChild(TokenUtils.createToken("protected"));
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(detailAST));
        
        detailAST.addChild(TokenUtils.createToken("private"));
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(detailAST));
        
        // Error Scenario
        detailAST.addChild(TokenUtils.createToken("invalid"));
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(detailAST));
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - In a class
        detailAST.setType(15); // Class type
        detailAST.addChild(TokenUtils.createToken("public"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(detailAST));
        
        // Typical Use Case - In an interface
        detailAST.setType(14); // Interface type
        detailAST.addChild(TokenUtils.createToken("public"));
        detailAST.addChild(TokenUtils.createToken("interface"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(detailAST));
        
        // Typical Use Case - In a method
        detailAST.setType(154); // Method type
        detailAST.addChild(TokenUtils.createToken("public"));
        detailAST.addChild(TokenUtils.createToken("void"));
        detailAST.addChild(TokenUtils.createToken("foo"));
        assertEquals(Scope.PUBLIC, ScopeUtils.getSurroundingScope(detailAST));

        // Typical Use Case - In an anonymous inner class
        detailAST.setType(136); // Anonymous inner class type
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(detailAST));
        
        // Edge Case - No surrounding scope
        detailAST.setType(99); // Random type
        assertEquals(null, ScopeUtils.getSurroundingScope(detailAST));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case
        detailAST.setType(14); // Interface type
        assertEquals(false, ScopeUtils.isInInterfaceBlock(detailAST));
        
        // Edge Cases
        detailAST.setType(15); // Class type
        assertEquals(true, ScopeUtils.isInInterfaceBlock(detailAST));
        
        detailAST.setType(154); // Method type
        assertEquals(false, ScopeUtils.isInInterfaceBlock(detailAST));
        
        // Error Scenario
        detailAST.setType(99); // Random type
        assertEquals(false, ScopeUtils.isInInterfaceBlock(detailAST));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case
        detailAST.setType(157); // Annotation type
        assertEquals(true, ScopeUtils.isInAnnotationBlock(detailAST));
        
        // Edge Cases
        detailAST.setType(14); // Interface type
        assertEquals(false, ScopeUtils.isInAnnotationBlock(detailAST));
        
        detailAST.setType(15); // Class type
        assertEquals(false, ScopeUtils.isInAnnotationBlock(detailAST));
        
        // Error Scenario
        detailAST.setType(99); // Random type
        assertEquals(false, ScopeUtils.isInAnnotationBlock(detailAST));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - In an interface
        detailAST.setType(14); // Interface type
        assertEquals(true, ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
        
        // Typical Use Case - In an annotation
        detailAST.setType(157); // Annotation type
        assertEquals(true, ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
        
        // Edge Case - Not in an interface or annotation
        detailAST.setType(15); // Class type
        assertEquals(false, ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
        
        // Error Scenario
        detailAST.setType(99); // Random type
        assertEquals(false, ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST));
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - In an enum
        detailAST.setType(154); // Enum type
        assertEquals(true, ScopeUtils.isInEnumBlock(detailAST));
        
        // Edge Cases
        detailAST.setType(14); // Interface type
        assertEquals(false, ScopeUtils.isInEnumBlock(detailAST));
        
        detailAST.setType(15); // Class type
        assertEquals(false, ScopeUtils.isInEnumBlock(detailAST));
        
        // Error Scenario
        detailAST.setType(99); // Random type
        assertEquals(false, ScopeUtils.isInEnumBlock(detailAST));
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - In a method
        detailAST.setType(154); // Method type
        assertEquals(true, ScopeUtils.isInCodeBlock(detailAST));
        
        // Typical Use Case - In a constructor
        detailAST.setType(136); // Constructor type
        assertEquals(true, ScopeUtils.isInCodeBlock(detailAST));
        
        // Edge Cases
        detailAST.setType(65); // Enum constant type
        assertEquals(false, ScopeUtils.isInCodeBlock(detailAST));
        
        detailAST.setType(200); // Type cast type
        assertEquals(false, ScopeUtils.isInCodeBlock(detailAST));
        
        // Error Scenario
        detailAST.setType(99); // Random type
        assertEquals(false, ScopeUtils.isInCodeBlock(detailAST));
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - Not an outermost type
        detailAST.setType(154); // Method type
        assertFalse(ScopeUtils.isOuterMostType(detailAST));
        
        // Typical Use Case - Outermost type
        detailAST.setType(99); // Some type
        assertTrue(ScopeUtils.isOuterMostType(detailAST));
        
        // Edge Case - Only inner types
        DetailAST parent = new DetailAST();
        parent.setType(154); // Method type
        detailAST.setParent(parent);
        assertTrue(ScopeUtils.isOuterMostType(detailAST));
        
        // Error Scenario
        detailAST.setParent(null);
        assertTrue(ScopeUtils.isOuterMostType(detailAST));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - Local Variable Definition
        detailAST.setType(10); // Local Variable Definition type
        DetailAST parent = new DetailAST();
        parent.setType(7); // For loop type
        detailAST.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(detailAST));
        
        // Edge Cases
        detailAST.setType(21); // Type Parameter type
        parent.setType(96); // Lambda Expression type
        assertTrue(ScopeUtils.isLocalVariableDef(detailAST));
        
        detailAST.setType(10); // Local Variable Definition type
        parent.setType(99); // Some Random type
        assertFalse(ScopeUtils.isLocalVariableDef(detailAST));
        
        // Error Scenario
        detailAST.setType(99); // Random Type
        assertFalse(ScopeUtils.isLocalVariableDef(detailAST));
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - Class Field Definition
        detailAST.setType(10); // Field Variable Definition type
        assertFalse(ScopeUtils.isClassFieldDef(detailAST));
        
        // Edge Case - Local Variable Definition
        detailAST.setType(10); // Local Variable Definition type
        assertTrue(ScopeUtils.isClassFieldDef(detailAST));
        
        // Error Scenario
        detailAST.setType(99); // Random Type
        assertFalse(ScopeUtils.isClassFieldDef(detailAST));
    }
    
    @Test
    public void testIsInScope() {
        DetailAST detailAST = new DetailAST();
        
        // Typical Use Case - In Scope
        detailAST.setType(154); // Method type
        DetailAST parent = new DetailAST();
        parent.setType(15); // Class type
        detailAST.setParent(parent);
        
        // Mock getSurroundingScope() to return Scope.PUBLIC
        ScopeUtils.getSurroundingScope = Mockito.mock(Scope.class);
        when(ScopeUtils.getSurroundingScope(detailAST)).thenReturn(Scope.PUBLIC);
        
        assertTrue(ScopeUtils.isInScope(detailAST, Scope.PUBLIC));
        
        // Typical Use Case - Not in Scope
        assertTrue(ScopeUtils.isInScope(detailAST, Scope.DEFAULT_SCOPES));
        
        // Error Scenario
        assertFalse(ScopeUtils.isInScope(detailAST, Scope.DEFAULT_SCOPES));
    }
}