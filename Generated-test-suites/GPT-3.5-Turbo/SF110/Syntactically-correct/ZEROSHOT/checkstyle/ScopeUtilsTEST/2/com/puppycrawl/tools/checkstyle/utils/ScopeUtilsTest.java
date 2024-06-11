import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test if the method returns Scope.PACKAGE
        DetailAST aMods = null;
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PACKAGE, result);
        
        // Test if the method returns Scope.PUBLIC
        aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.getFirstChild().setText("public");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PUBLIC, result);
        
        // Test if the method returns Scope.PROTECTED
        aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.getFirstChild().setText("protected");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PROTECTED, result);
        
        // Test if the method returns Scope.PRIVATE
        aMods = new DetailAST();
        aMods.addChild(new AST());
        aMods.getFirstChild().setText("private");
        result = ScopeUtils.getScopeFromMods(aMods);
        assertEquals(Scope.PRIVATE, result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsInEnumBlock() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsInCodeBlock() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsOuterMostType() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsClassFieldDef() {
        // TODO: Implement test cases for this method
    }
    
    @Test
    public void testIsInScope() {
        // TODO: Implement test cases for this method
    }
}