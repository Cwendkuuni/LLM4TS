import static org.junit.Assert.*;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Testcase 1
        DetailAST aMods1 = new DetailAST();
        assertFalse(ScopeUtils.getScopeFromMods(aMods1) == Scope.PUBLIC);
        
        // Testcase 2
        DetailAST aMods2 = new DetailAST();
        aMods2.addChild(new AST("public"));
        assertTrue(ScopeUtils.getScopeFromMods(aMods2) == Scope.PUBLIC);
        
        // Testcase 3
        DetailAST aMods3 = new DetailAST();
        aMods3.addChild(new AST("protected"));
        assertTrue(ScopeUtils.getScopeFromMods(aMods3) == Scope.PROTECTED);
        
        // Testcase 4
        DetailAST aMods4 = new DetailAST();
        aMods4.addChild(new AST("private"));
        assertTrue(ScopeUtils.getScopeFromMods(aMods4) == Scope.PRIVATE);
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.getSurroundingScope(node1) == Scope.ANONINNER);
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        node2.addChild(new DetailAST());
        node2.addChild(new DetailAST());
        assertFalse(ScopeUtils.getSurroundingScope(node2) == Scope.PUBLIC);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceBlock(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertTrue(ScopeUtils.isInInterfaceBlock(node2));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInAnnotationBlock(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertTrue(ScopeUtils.isInAnnotationBlock(node2));
    }
    
    @Test
    public void testisInInterfaceOrAnnotationBlock() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node2));
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertTrue(ScopeUtils.isInEnumBlock(node2));
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertTrue(ScopeUtils.isInCodeBlock(node2));
    }
    
    @Test
    public void testIsOuterMostType() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isOuterMostType(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertFalse(ScopeUtils.isOuterMostType(node2));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isLocalVariableDef(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertFalse(ScopeUtils.isLocalVariableDef(node2));
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Testcase 1
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isClassFieldDef(node1));
        
        // Testcase 2
        DetailAST node2 = new DetailAST();
        node2.addChild(new DetailAST());
        assertFalse(ScopeUtils.isClassFieldDef(node2));
    }
    
    @Test
    public void testIsInScope() {
        // Testcase 1
        DetailAST ast1 = new DetailAST();
        DetailAST ast2 = new DetailAST();
        assertFalse(ScopeUtils.isInScope(ast1, Scope.PUBLIC));
        
        // Testcase 2
        DetailAST ast = new DetailAST();
        assertFalse(ScopeUtils.isInScope(ast, Scope.PROTECTED));
    }
}