
  
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test typical use case for getScopeFromMods()
        DetailAST aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
        
        // Test edge case for getScopeFromMods()
        aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods));
        
        // Test error scenario for getScopeFromMods()
        aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("invalid");
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods));
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Test typical use case for getSurroundingScope()
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(14);
        node.addChild(new DetailAST());
        node.getChild(1).setType(15);
        node.addChild(new DetailAST());
        node.getChild(2).setType(157);
        assertEquals(Scope.PACKAGE, ScopeUtils.getSurroundingScope(node));
        
        // Test edge case for getSurroundingScope()
        node = new DetailAST();
        node.setType(136);
        assertNull(ScopeUtils.getSurroundingScope(node));
        
        // Test error scenario for getSurroundingScope()
        node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(100); // Invalid type
        assertNull(ScopeUtils.getSurroundingScope(node));
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Test typical use case for isInInterfaceBlock()
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(15);
        assertTrue(ScopeUtils.isInInterfaceBlock(node));
        
        // Test edge case for isInInterfaceBlock()
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
        
        // Test error scenario for isInInterfaceBlock()
        node = new DetailAST();
        node.setType(100); // Invalid type
        assertFalse(ScopeUtils.isInInterfaceBlock(node));
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Test typical use case for isInAnnotationBlock()
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(157);
        assertTrue(ScopeUtils.isInAnnotationBlock(node));
        
        // Test edge case for isInAnnotationBlock()
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
        
        // Test error scenario for isInAnnotationBlock()
        node = new DetailAST();
        node.setType(100); // Invalid type
        assertFalse(ScopeUtils.isInAnnotationBlock(node));
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test typical use case for isInInterfaceOrAnnotationBlock()
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(15);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Test edge case for isInInterfaceOrAnnotationBlock()
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
        
        // Test error scenario for isInInterfaceOrAnnotationBlock()
        node = new DetailAST();
        node.setType(100); // Invalid type
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node));
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Test typical use case for isInEnumBlock()
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(154);
        assertTrue(ScopeUtils.isInEnumBlock(node));
        
        // Test edge case for isInEnumBlock()
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInEnumBlock(node));
        
        // Test error scenario for isInEnumBlock()
        node = new DetailAST();
        node.setType(100); // Invalid type
        assertFalse(ScopeUtils.isInEnumBlock(node));
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Test typical use case for isInCodeBlock()
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(9);
        assertTrue(ScopeUtils.isInCodeBlock(node));
        
        // Test edge case for isInCodeBlock()
        node = new DetailAST();
        node.setType(14);
        assertFalse(ScopeUtils.isInCodeBlock(node));
        
        // Test error scenario for isInCodeBlock()
        node = new DetailAST();
        node.setType(100); // Invalid type
        assertFalse(ScopeUtils.isInCodeBlock(node));
    }
    
    @Test
    public void testIsOuterMostType() {
        // Test typical use case for isOuterMostType()
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        DetailAST parent = node.getFirstChild();
        parent.setType(14);
        assertTrue(ScopeUtils.isOuterMostType(node));
        
        // Test edge case for isOuterMostType()
        node = new DetailAST();
        node.addChild(new DetailAST());
        parent = node.getFirstChild();
        parent.setType(15);
        assertFalse(ScopeUtils.isOuterMostType(node));
        
        // Test error scenario for isOuterMostType()
        node = new DetailAST();
        node.addChild(new DetailAST());
        parent = node.getFirstChild();
        parent.setType(100); // Invalid type
        assertTrue(ScopeUtils.isOuterMostType(node));
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Test typical use case for isLocalVariableDef()
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        // Test edge case for isLocalVariableDef()
        node = new DetailAST();
        node.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        node.setParent(parent);
        assertTrue(ScopeUtils.isLocalVariableDef(node));
        
        // Test error scenario for isLocalVariableDef()
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(101); // Invalid type
        node.setParent(parent);
        assertFalse(ScopeUtils.isLocalVariableDef(node));
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Test typical use case for isClassFieldDef()
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        // Test edge case for isClassFieldDef()
        node = new DetailAST();
        node.setType(10);
        parent = new DetailAST();
        parent.setType(101); // Invalid parent type
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
        
        // Test error scenario for isClassFieldDef()
        node = new DetailAST();
        node.setType(11); // Invalid node type
        parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        assertFalse(ScopeUtils.isClassFieldDef(node));
    }
    
    @Test
    public void testIsInScope() {
        // Test typical use case for isInScope()
        DetailAST ast = new DetailAST();
        ast.addChild(new DetailAST());
        ast.getFirstChild().setType(14);
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(15);
        assertTrue(ScopeUtils.isInScope(ast, Scope.PACKAGE));
        
        // Test edge case for isInScope()
        ast = new DetailAST();
        ast.setType(136);
        node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(15);
        assertFalse(ScopeUtils.isInScope(ast, Scope.PUBLIC));
        
        // Test error scenario for isInScope()
        ast = new DetailAST();
        node = new DetailAST();
        node.addChild(new DetailAST());
        node.getFirstChild().setType(15);
        assertFalse(ScopeUtils.isInScope(ast, Scope.PACKAGE));
    }
}

