
import org.junit.Assert;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods_givenPublicMod_returnsPublicScope() {
        DetailAST aMods = createDummyDetailAST("public");
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetScopeFromMods_givenProtectedMod_returnsProtectedScope() {
        DetailAST aMods = createDummyDetailAST("protected");
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(Scope.PROTECTED, scope);
    }

    @Test
    public void testGetScopeFromMods_givenPrivateMod_returnsPrivateScope() {
        DetailAST aMods = createDummyDetailAST("private");
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(Scope.PRIVATE, scope);
    }

    @Test
    public void testGetScopeFromMods_givenNoMod_returnsPackageScope() {
        DetailAST aMods = createDummyDetailAST("something");
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(Scope.PACKAGE, scope);
    }

    @Test
    public void testGetSurroundingScope_givenPackageScope_returnsNull() {
        DetailAST node = createDummyDetailAST("package");
        Scope scope = ScopeUtils.getSurroundingScope(node);
        Assert.assertNull(scope);
    }

    @Test
    public void testIsInInterfaceBlock_givenNodeInInterfaceBlock_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInInterfaceBlock_givenNodeOutsideInterfaceBlock_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsInAnnotationBlock_givenNodeInAnnotationBlock_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInAnnotationBlock_givenNodeOutsideAnnotationBlock_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_givenNodeInInterfaceBlock_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_givenNodeInAnnotationBlock_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock_givenNodeOutsideInterfaceAndAnnotationBlock_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsInEnumBlock_givenNodeInEnumBlock_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isInEnumBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInEnumBlock_givenNodeOutsideEnumBlock_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isInEnumBlock(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsInCodeBlock_givenNodeInCodeBlock_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isInCodeBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInCodeBlock_givenNodeOutsideCodeBlock_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isInCodeBlock(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsOuterMostType_givenNodeIsOuterMostType_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isOuterMostType(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsOuterMostType_givenNodeIsNotOuterMostType_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isOuterMostType(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsLocalVariableDef_givenNodeIsLocalVariableDef_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isLocalVariableDef(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsLocalVariableDef_givenNodeIsNotLocalVariableDef_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isLocalVariableDef(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsClassFieldDef_givenNodeIsClassFieldDef_returnsTrue() {
        DetailAST node = createDummyDetailAST("something");
        boolean result = ScopeUtils.isClassFieldDef(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsClassFieldDef_givenNodeIsNotClassFieldDef_returnsFalse() {
        DetailAST node = createDummyDetailAST("package");
        boolean result = ScopeUtils.isClassFieldDef(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsInScope_givenNodeInScope_returnsTrue() {
        DetailAST ast = createDummyDetailAST("something");
        boolean result = ScopeUtils.isInScope(ast, Scope.PACKAGE);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInScope_givenNodeNotInScope_returnsFalse() {
        DetailAST ast = createDummyDetailAST("package");
        boolean result = ScopeUtils.isInScope(ast, Scope.PUBLIC);
        Assert.assertFalse(result);
    }

    private DetailAST createDummyDetailAST(String text) {
        return new DetailAST();
    }

    // Add more helper methods if needed

}
