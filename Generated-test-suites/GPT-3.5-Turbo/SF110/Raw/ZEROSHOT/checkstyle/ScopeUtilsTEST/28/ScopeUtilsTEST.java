import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import antlr.collections.*;

public class ScopeUtilsTest {
    @Test
    public void testGetScopeFromMods() {
        DetailAST detailAST = new DetailAST();
        Scope scope = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PACKAGE, scope);
        
        detailAST.addChild(new DetailAST());
        detailAST.getFirstChild().setText("public");
        scope = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PUBLIC, scope);
        
        detailAST.addChild(new DetailAST());
        detailAST.getFirstChild().setText("protected");
        scope = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PROTECTED, scope);
        
        detailAST.addChild(new DetailAST());
        detailAST.getFirstChild().setText("private");
        scope = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PRIVATE, scope);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST detailAST = new DetailAST();
        Scope scope = ScopeUtils.getSurroundingScope(detailAST);
        assertNull(scope);
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        parent.addChild(new DetailAST());
        parent.getFirstChild().setType(5);
        parent.getFirstChild().addChild(new DetailAST());
        parent.getFirstChild().getFirstChild().setText("public");
        detailAST.setParent(parent);
        detailAST.setType(1);
        
        scope = ScopeUtils.getSurroundingScope(detailAST);
        assertEquals(Scope.PUBLIC, scope);
        
        parent.setType(157);
        scope = ScopeUtils.getSurroundingScope(detailAST);
        assertEquals(Scope.PUBLIC, scope);
        
        parent.setType(136);
        scope = ScopeUtils.getSurroundingScope(detailAST);
        assertEquals(Scope.ANONINNER, scope);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceBlock(detailAST);
        assertFalse(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        detailAST.setParent(parent);
        detailAST.setType(15);
        
        result = ScopeUtils.isInInterfaceBlock(detailAST);
        assertTrue(result);
        
        parent.setType(157);
        result = ScopeUtils.isInInterfaceBlock(detailAST);
        assertFalse(result);
        
        parent.setType(136);
        result = ScopeUtils.isInInterfaceBlock(detailAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isInAnnotationBlock(detailAST);
        assertFalse(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        detailAST.setParent(parent);
        detailAST.setType(157);
        
        result = ScopeUtils.isInAnnotationBlock(detailAST);
        assertTrue(result);
        
        parent.setType(15);
        result = ScopeUtils.isInAnnotationBlock(detailAST);
        assertFalse(result);
        
        parent.setType(136);
        result = ScopeUtils.isInAnnotationBlock(detailAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertFalse(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(157);
        detailAST.setParent(parent);
        detailAST.setType(15);
        
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertTrue(result);
        
        parent.setType(14);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertFalse(result);
        
        parent.setType(136);
        result = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isInEnumBlock(detailAST);
        assertFalse(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(157);
        detailAST.setParent(parent);
        detailAST.setType(154);
        
        result = ScopeUtils.isInEnumBlock(detailAST);
        assertTrue(result);
        
        parent.setType(15);
        result = ScopeUtils.isInEnumBlock(detailAST);
        assertFalse(result);
        
        parent.setType(136);
        result = ScopeUtils.isInEnumBlock(detailAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isInCodeBlock(detailAST);
        assertFalse(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(9);
        detailAST.setParent(parent);
        detailAST.setType(1);
        
        result = ScopeUtils.isInCodeBlock(detailAST);
        assertTrue(result);
        
        parent.setType(14);
        result = ScopeUtils.isInCodeBlock(detailAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isOuterMostType(detailAST);
        assertTrue(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(14);
        detailAST.setParent(parent);
        detailAST.setType(1);
        
        result = ScopeUtils.isOuterMostType(detailAST);
        assertFalse(result);
        
        parent.setType(15);
        result = ScopeUtils.isOuterMostType(detailAST);
        assertFalse(result);
        
        parent.setType(157);
        result = ScopeUtils.isOuterMostType(detailAST);
        assertFalse(result);
        
        parent.setType(136);
        result = ScopeUtils.isOuterMostType(detailAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isLocalVariableDef(detailAST);
        assertFalse(result);
        
        DetailAST parent = new DetailAST();
        parent.setType(7);
        detailAST.setParent(parent);
        detailAST.setType(10);
        
        result = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(result);
        
        parent.setType(35);
        result = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(result);
        
        parent.setType(156);
        result = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(result);
        
        detailAST.setType(21);
        parent = new DetailAST();
        parent.setType(96);
        detailAST.setParent(parent);
        
        result = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(result);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(result);
        
        DetailAST parent = new DetailAST();
        detailAST.setParent(parent);
        detailAST.setType(10);
        
        result = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(result);
        
        parent.setType(7);
        result = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(result);
        
        parent.setType(35);
        result = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(result);
        
        parent.setType(156);
        result = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(result);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST detailAST = new DetailAST();
        boolean result = ScopeUtils.isInScope(detailAST, Scope.PACKAGE);
        assertFalse(result);
        
        ScopeUtils.getSurroundingScope(detailAST);
        result = ScopeUtils.isInScope(detailAST, Scope.PACKAGE);
        assertTrue(result);
    }
}