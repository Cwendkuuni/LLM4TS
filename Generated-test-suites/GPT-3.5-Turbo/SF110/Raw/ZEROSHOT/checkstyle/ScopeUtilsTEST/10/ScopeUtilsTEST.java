import com.puppycrawl.tools.checkstyle.api.DetailAST;
import junit.framework.Assert;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST ast = new DetailAST();
        Scope scope = ScopeUtils.getScopeFromMods(ast);
        Assert.assertEquals(Scope.PACKAGE, scope);

        ast.addChild(createAST("public"));
        scope = ScopeUtils.getScopeFromMods(ast);
        Assert.assertEquals(Scope.PUBLIC, scope);

        ast.addChild(createAST("protected"));
        scope = ScopeUtils.getScopeFromMods(ast);
        Assert.assertEquals(Scope.PROTECTED, scope);

        ast.addChild(createAST("private"));
        scope = ScopeUtils.getScopeFromMods(ast);
        Assert.assertEquals(Scope.PRIVATE, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST ast = new DetailAST();
        Scope scope = ScopeUtils.getSurroundingScope(ast);
        Assert.assertNull(scope);

        ast.setParent(createAST(14));
        Scope modScope = ScopeUtils.getScopeFromMods(ast.findFirstToken(5));
        scope = ScopeUtils.getSurroundingScope(ast);
        Assert.assertEquals(modScope, scope);

        ast.setParent(createAST(136));
        scope = ScopeUtils.getSurroundingScope(ast);
        Assert.assertEquals(Scope.ANONINNER, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST ast = new DetailAST();
        boolean isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        Assert.assertFalse(isInInterfaceBlock);

        ast.setParent(createAST(15));
        isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        Assert.assertTrue(isInInterfaceBlock);

        ast.setParent(createAST(14));
        isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        Assert.assertFalse(isInInterfaceBlock);

        ast.setParent(createAST(136));
        isInInterfaceBlock = ScopeUtils.isInInterfaceBlock(ast);
        Assert.assertFalse(isInInterfaceBlock);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST ast = new DetailAST();
        boolean isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        Assert.assertFalse(isInAnnotationBlock);

        ast.setParent(createAST(157));
        isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        Assert.assertTrue(isInAnnotationBlock);

        ast.setParent(createAST(14));
        isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        Assert.assertFalse(isInAnnotationBlock);

        ast.setParent(createAST(15));
        isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        Assert.assertFalse(isInAnnotationBlock);

        ast.setParent(createAST(136));
        isInAnnotationBlock = ScopeUtils.isInAnnotationBlock(ast);
        Assert.assertFalse(isInAnnotationBlock);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST ast = new DetailAST();
        boolean isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        Assert.assertFalse(isInInterfaceOrAnnotationBlock);

        ast.setParent(createAST(15));
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        Assert.assertTrue(isInInterfaceOrAnnotationBlock);

        ast.setParent(createAST(154));
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        Assert.assertTrue(isInInterfaceOrAnnotationBlock);

        ast.setParent(createAST(157));
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        Assert.assertTrue(isInInterfaceOrAnnotationBlock);

        ast.setParent(createAST(14));
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        Assert.assertFalse(isInInterfaceOrAnnotationBlock);

        ast.setParent(createAST(136));
        isInInterfaceOrAnnotationBlock = ScopeUtils.isInInterfaceOrAnnotationBlock(ast);
        Assert.assertFalse(isInInterfaceOrAnnotationBlock);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST ast = new DetailAST();
        boolean isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        Assert.assertFalse(isInEnumBlock);

        ast.setParent(createAST(154));
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        Assert.assertTrue(isInEnumBlock);

        ast.setParent(createAST(15));
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        Assert.assertFalse(isInEnumBlock);

        ast.setParent(createAST(157));
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        Assert.assertFalse(isInEnumBlock);

        ast.setParent(createAST(14));
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        Assert.assertFalse(isInEnumBlock);

        ast.setParent(createAST(136));
        isInEnumBlock = ScopeUtils.isInEnumBlock(ast);
        Assert.assertFalse(isInEnumBlock);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST ast = new DetailAST();
        boolean isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        Assert.assertFalse(isInCodeBlock);

        ast.setParent(createAST(9));
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        Assert.assertTrue(isInCodeBlock);

        ast.setParent(createAST(8));
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        Assert.assertTrue(isInCodeBlock);

        ast.setParent(createAST(11));
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        Assert.assertTrue(isInCodeBlock);

        ast.setParent(createAST(12));
        isInCodeBlock = ScopeUtils.isInCodeBlock(ast);
        Assert.assertTrue(isInCodeBlock);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST ast = new DetailAST();
        boolean isOuterMostType = ScopeUtils.isOuterMostType(ast);
        Assert.assertTrue(isOuterMostType);

        ast.setParent(createAST(14));
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        Assert.assertFalse(isOuterMostType);

        ast.setParent(createAST(15));
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        Assert.assertFalse(isOuterMostType);

        ast.setParent(createAST(157));
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        Assert.assertFalse(isOuterMostType);

        ast.setParent(createAST(154));
        isOuterMostType = ScopeUtils.isOuterMostType(ast);
        Assert.assertFalse(isOuterMostType);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST ast = new DetailAST();
        boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        Assert.assertFalse(isLocalVariableDef);

        ast.setType(10);
        DetailAST parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(7);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        Assert.assertTrue(isLocalVariableDef);

        parent.setType(35);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        Assert.assertTrue(isLocalVariableDef);

        parent.setType(156);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        Assert.assertTrue(isLocalVariableDef);

        ast.setType(21);
        parent.setType(96);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        Assert.assertTrue(isLocalVariableDef);

        parent.setType(35);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(ast);
        Assert.assertFalse(isLocalVariableDef);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST ast = new DetailAST();
        boolean isClassFieldDef = ScopeUtils.isClassFieldDef(ast);
        Assert.assertFalse(isClassFieldDef);

        ast.setType(10);
        DetailAST parent = new DetailAST();
        ast.setParent(parent);
        parent.setType(7);
        isClassFieldDef = ScopeUtils.isClassFieldDef(ast);
        Assert.assertFalse(isClassFieldDef);

        parent.setType(35);
        isClassFieldDef = ScopeUtils.isClassFieldDef(ast);
        Assert.assertFalse(isClassFieldDef);

        parent.setType(156);
        isClassFieldDef = ScopeUtils.isClassFieldDef(ast);
        Assert.assertFalse(isClassFieldDef);

        parent.setType(89);
        isClassFieldDef = ScopeUtils.isClassFieldDef(ast);
        Assert.assertTrue(isClassFieldDef);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        boolean isInScope = ScopeUtils.isInScope(ast, Scope.PACKAGE);
        Assert.assertFalse(isInScope);

        ast.setParent(createAST(14));
        Scope surroundingScopeOfAstToken = ScopeUtils.getSurroundingScope(ast);
        isInScope = ScopeUtils.isInScope(ast, surroundingScopeOfAstToken);
        Assert.assertTrue(isInScope);
    }

    // Helper method to create a DetailAST node with given type
    private DetailAST createAST(int type) {
        DetailAST ast = new DetailAST();
        ast.setType(type);
        return ast;
    }

    // Helper method to create a DetailAST node with given text
    private DetailAST createAST(String text) {
        DetailAST ast = new DetailAST();
        ast.setText(text);
        return ast;
    }
}