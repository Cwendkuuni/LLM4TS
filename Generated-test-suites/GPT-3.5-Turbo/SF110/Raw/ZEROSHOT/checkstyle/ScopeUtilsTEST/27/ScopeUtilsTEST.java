import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        DetailAST token1 = new DetailAST();
        token1.setText("public");
        DetailAST token2 = new DetailAST();
        token2.setText("protected");
        DetailAST token3 = new DetailAST();
        token3.setText("private");
        aMods.setFirstChild(token1);
        token1.setNextSibling(token2);
        token2.setNextSibling(token3);
        
        Scope result = ScopeUtils.getScopeFromMods(aMods);
        
        assertEquals(Scope.PRIVATE, result);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        DetailAST mods = new DetailAST();
        DetailAST token1 = new DetailAST();
        token1.setText("public");
        DetailAST token2 = new DetailAST();
        token2.setText("protected");
        mods.setFirstChild(token1);
        token1.setNextSibling(token2);
        parent.setFirstChild(mods);
        node.setParent(parent);
        
        Scope result = ScopeUtils.getSurroundingScope(node);
        
        assertEquals(Scope.PUBLIC, result);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlockWhenInInterfaceBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlockWhenInAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(157);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlockWhenNotInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(154);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isInEnumBlock(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(9);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isInCodeBlock(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        DetailAST grandparent = new DetailAST();
        grandparent.setType(14);
        parent.setParent(grandparent);
        node.setParent(parent);
        
        boolean result = ScopeUtils.isOuterMostType(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent1 = new DetailAST();
        parent1.setType(7);
        DetailAST parent2 = new DetailAST();
        parent2.setType(35);
        parent1.setParent(parent2);
        node.setParent(parent1);
        
        boolean result = ScopeUtils.isLocalVariableDef(node);
        
        assertTrue(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent1 = new DetailAST();
        parent1.setType(7);
        DetailAST parent2 = new DetailAST();
        parent2.setType(35);
        parent1.setParent(parent2);
        node.setParent(parent1);
        
        boolean result = ScopeUtils.isClassFieldDef(node);
        
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PUBLIC;
        
        boolean result = ScopeUtils.isInScope(ast, scope);
        
        assertFalse(result);
    }
}