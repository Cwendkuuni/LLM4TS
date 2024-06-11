import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;

public class ScopeUtilsTest {
  
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        // add test case specific assertions
        
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        Scope expected = Scope.PACKAGE;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        Scope actual = ScopeUtils.getSurroundingScope(node);
        Scope expected = null;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isInEnumBlock(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isInCodeBlock(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isOuterMostType(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        // add test case specific assertions
        
        boolean actual = ScopeUtils.isClassFieldDef(node);
        boolean expected = false;
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;
        
        boolean actual = ScopeUtils.isInScope(ast, scope);
        boolean expected = false;
        assertEquals(expected, actual);
    }
}


