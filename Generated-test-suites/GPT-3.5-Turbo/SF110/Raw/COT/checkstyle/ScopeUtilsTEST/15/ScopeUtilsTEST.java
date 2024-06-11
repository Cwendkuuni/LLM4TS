import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import org.junit.Assert;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("public");
        Scope scope = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(Scope.PUBLIC, scope);
    }

    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.setType(14); // Public type
        DetailAST mods = new DetailAST();
        mods.addChild(new DetailAST());
        mods.getFirstChild().setText("private");
        node.addChild(mods);
        Scope scope = ScopeUtils.getSurroundingScope(node);
        Assert.assertEquals(Scope.PRIVATE, scope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.setType(15); // Interface type
        boolean result = ScopeUtils.isInInterfaceBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(157); // Annotation type
        boolean result = ScopeUtils.isInAnnotationBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.setType(15); // Interface type
        boolean result = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.setType(154); // Enum type
        boolean result = ScopeUtils.isInEnumBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.setType(9); // If statement type
        boolean result = ScopeUtils.isInCodeBlock(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        node.setType(77); // Random type
        boolean result = ScopeUtils.isOuterMostType(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        node.setType(10); // Variable definition type
        DetailAST parent = new DetailAST();
        parent.setType(7); // Method type
        node.setParent(parent);
        boolean result = ScopeUtils.isLocalVariableDef(node);
        Assert.assertTrue(result);
    }

    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        node.setType(10); // Variable definition type
        boolean result = ScopeUtils.isClassFieldDef(node);
        Assert.assertFalse(result);
    }

    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        ast.setType(99); // Random type
        Scope scope = Scope.ANONINNER;
        boolean result = ScopeUtils.isInScope(ast, scope);
        Assert.assertFalse(result);
    }
}

