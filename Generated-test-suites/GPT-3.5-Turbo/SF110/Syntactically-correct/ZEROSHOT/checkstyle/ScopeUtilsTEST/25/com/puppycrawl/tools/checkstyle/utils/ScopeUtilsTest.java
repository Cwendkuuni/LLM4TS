import org.junit.Assert;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST mods = new DetailAST();
        mods.addChild(createToken("public"));
        Scope scope = ScopeUtils.getScopeFromMods(mods);
        Assert.assertEquals(Scope.PUBLIC, scope);

        mods = new DetailAST();
        mods.addChild(createToken("private"));
        scope = ScopeUtils.getScopeFromMods(mods);
        Assert.assertEquals(Scope.PRIVATE, scope);

        mods = new DetailAST();
        mods.addChild(createToken("protected"));
        scope = ScopeUtils.getScopeFromMods(mods);
        Assert.assertEquals(Scope.PROTECTED, scope);

        mods = new DetailAST();
        mods.addChild(createToken("package"));
        scope = ScopeUtils.getScopeFromMods(mods);
        Assert.assertEquals(Scope.PACKAGE, scope);

        mods = new DetailAST();
        scope = ScopeUtils.getScopeFromMods(mods);
        Assert.assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        Scope scope = ScopeUtils.getSurroundingScope(node);
        Assert.assertNull(scope);

        node = new DetailAST();
        node.setType(14);
        node.addChild(createToken(""));
        node.addChild(createToken(""));
        node.addChild(createToken("public"));
        scope = ScopeUtils.getSurroundingScope(node);
        Assert.assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        boolean isInInterface = ScopeUtils.isInInterfaceBlock(node);
        Assert.assertFalse(isInInterface);

        node = new DetailAST();
        node.setType(15);
        isInInterface = ScopeUtils.isInInterfaceBlock(node);
        Assert.assertTrue(isInInterface);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean isInAnnotation = ScopeUtils.isInAnnotationBlock(node);
        Assert.assertFalse(isInAnnotation);

        node = new DetailAST();
        node.setType(157);
        isInAnnotation = ScopeUtils.isInAnnotationBlock(node);
        Assert.assertTrue(isInAnnotation);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        boolean isInInterfaceOrAnnotation = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertFalse(isInInterfaceOrAnnotation);

        node = new DetailAST();
        node.setType(15);
        isInInterfaceOrAnnotation = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertTrue(isInInterfaceOrAnnotation);

        node = new DetailAST();
        node.setType(157);
        isInInterfaceOrAnnotation = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertTrue(isInInterfaceOrAnnotation);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        boolean isInEnum = ScopeUtils.isInEnumBlock(node);
        Assert.assertFalse(isInEnum);

        node = new DetailAST();
        node.setType(154);
        isInEnum = ScopeUtils.isInEnumBlock(node);
        Assert.assertTrue(isInEnum);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        boolean isInCodeBlock = ScopeUtils.isInCodeBlock(node);
        Assert.assertFalse(isInCodeBlock);

        node = new DetailAST();
        node.setType(9);
        isInCodeBlock = ScopeUtils.isInCodeBlock(node);
        Assert.assertTrue(isInCodeBlock);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        boolean isOuterMost = ScopeUtils.isOuterMostType(node);
        Assert.assertTrue(isOuterMost);

        node = new DetailAST();
        node.setType(14);
        node.addChild(createToken(""));
        node.addChild(createToken(""));
        node.addChild(createToken("public"));
        isOuterMost = ScopeUtils.isOuterMostType(node);
        Assert.assertFalse(isOuterMost);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        boolean isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        Assert.assertFalse(isLocalVariableDef);

        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        isLocalVariableDef = ScopeUtils.isLocalVariableDef(node);
        Assert.assertTrue(isLocalVariableDef);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        boolean isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        Assert.assertFalse(isClassFieldDef);

        node = new DetailAST();
        node.setType(10);
        DetailAST parent = new DetailAST();
        parent.setType(7);
        node.setParent(parent);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        Assert.assertFalse(isClassFieldDef);

        node = new DetailAST();
        node.setType(10);
        isClassFieldDef = ScopeUtils.isClassFieldDef(node);
        Assert.assertTrue(isClassFieldDef);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = ScopeUtils.getSurroundingScope(ast);
        boolean inScope = ScopeUtils.isInScope(ast, scope);
        Assert.assertTrue(inScope);
    }

    private static DetailAST createToken(String text) {
        DetailAST token = new DetailAST();
        token.setText(text);
        return token;
    }
}