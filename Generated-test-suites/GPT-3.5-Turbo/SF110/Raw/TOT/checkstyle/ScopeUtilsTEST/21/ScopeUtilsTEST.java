import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_Public() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST(123, "public"));
        Scope expected = Scope.PUBLIC;
        
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetScopeFromMods_Protected() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST(234, "protected"));
        Scope expected = Scope.PROTECTED;
        
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetScopeFromMods_Private() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new AST(345, "private"));
        Scope expected = Scope.PRIVATE;
        
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetScopeFromMods_NoModifiers() {
        DetailAST aMods = new DetailAST();
        Scope expected = Scope.PACKAGE;
        
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSurroundingScope_NoParent() {
        DetailAST node = new DetailAST();
        Scope expected = null;
        
        Scope actual = ScopeUtils.getSurroundingScope(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSurroundingScope_ParentWithModifiers() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(1, "package"));
        node.addChild(new AST(2, "public"));
        
        Scope expected = Scope.PUBLIC;
        
        Scope actual = ScopeUtils.getSurroundingScope(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSurroundingScope_ParentWithAnonymousInner() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(136, null));
        
        Scope expected = Scope.ANONINNER;
        
        Scope actual = ScopeUtils.getSurroundingScope(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceBlock_InInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(15, "interface"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceBlock_NotInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(154, "enum"));
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isInInterfaceBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInAnnotationBlock_InAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(157, "annotation"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInAnnotationBlock_NotInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "class"));
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isInAnnotationBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock_IsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(15, "interface"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock_IsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(157, "annotation"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock_NotInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(154, "enum"));
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInEnumBlock_InEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(154, "enum"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInEnumBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInEnumBlock_NotInEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "class"));
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isInEnumBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInCodeBlock_IsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(9, "if"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInCodeBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInCodeBlock_NotInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "class"));
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isInCodeBlock(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsOuterMostType_IsOuterMostType() {
        DetailAST node = new DetailAST();
        boolean expected = true;
        
        boolean actual = ScopeUtils.isOuterMostType(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsOuterMostType_NotOuterMostType() {
        DetailAST node = new DetailAST();
        node.addChild(new AST(14, "class"));
        boolean expected = false;
        
        boolean actual = ScopeUtils.isOuterMostType(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLocalVariableDef_IsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        node.addChild(new AST(7, "int"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsLocalVariableDef_IsNotLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(20);
        node.addChild(new AST(7, "String"));
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isLocalVariableDef(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsClassFieldDef_IsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10);
        node.addChild(new AST(7, "String"));
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isClassFieldDef(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsClassFieldDef_IsNotClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(20);
        node.addChild(new AST(7, "int"));
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isClassFieldDef(node);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInScope_SurroundingScopeIsMatching() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(14);
        ast.setParent(parent);
        Scope scope = Scope.PACKAGE;
        
        boolean expected = true;
        
        boolean actual = ScopeUtils.isInScope(ast, scope);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInScope_SurroundingScopeIsNotMatching() {
        DetailAST ast = new DetailAST();
        DetailAST parent = new DetailAST();
        parent.setType(15);
        ast.setParent(parent);
        Scope scope = Scope.PRIVATE;
        
        boolean expected = false;
        
        boolean actual = ScopeUtils.isInScope(ast, scope);
        
        assertEquals(expected, actual);
    }
}