
import org.junit.*;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(42, "public"));
        Scope scope = ScopeUtils.getScopeFromMods(detailAST);
        assertEquals(Scope.PUBLIC, scope);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(14, "class"));
        detailAST.addChild(new DetailAST());
        detailAST.getFirstChild().addChild(new AST(42, "public"));

        Scope scope = ScopeUtils.getSurroundingScope(detailAST);
        assertEquals(Scope.PUBLIC, scope);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(15, "interface"));

        boolean isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(detailAST);
        assertTrue(isInInterfaceBlock);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(157, "annotation"));

        boolean isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(detailAST);
        assertTrue(isInAnnotationBlock);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(15, "interface"));

        boolean isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(detailAST);
        assertTrue(isInInterfaceOrAnnotationBlock);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(154, "enum"));

        boolean isInEnumBlock = ScopeUtils.isInEnumBlock(detailAST);
        assertTrue(isInEnumBlock);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(9, "if"));

        boolean isInCodeBlock = ScopeUtils.isInCodeBlock(detailAST);
        assertTrue(isInCodeBlock);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(42, "public"));

        boolean isOuterMostType = ScopeUtils.isOuterMostType(detailAST);
        assertTrue(isOuterMostType);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST detailAST = new DetailAST();
        detailAST.setType(10);
        
        DetailAST parent = new DetailAST();
        parent.setType(7);
        detailAST.setParent(parent);
        
        boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(detailAST);
        assertTrue(isLocalVariableDef);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST detailAST = new DetailAST();
        detailAST.setType(10);
        
        boolean isClassFieldDef = ScopeUtils.isClassFieldDef(detailAST);
        assertFalse(isClassFieldDef);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST detailAST = new DetailAST();
        detailAST.addChild(new AST(42, "public"));

        boolean isInScope = ScopeUtils.isInScope(detailAST, Scope.PUBLIC);
        assertTrue(isInScope);
    }
}

