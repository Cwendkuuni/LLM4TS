import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.Scope;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import org.junit.Assert;
import org.junit.Test;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test typical use case
        DetailAST aMods = new DetailAST();
        DetailAST publicToken = new DetailAST();
        publicToken.setText("public");
        aMods.addChild(publicToken);
        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(expectedScope, actualScope);

        // Test edge case
        aMods = new DetailAST();
        expectedScope = Scope.PACKAGE;
        actualScope = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testGetSurroundingScope() {
        // Test typical use case
        DetailAST node = new DetailAST();
        DetailAST parentToken = new DetailAST();
        parentToken.setType(14);
        node.setParent(parentToken);
        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getSurroundingScope(node);
        Assert.assertEquals(expectedScope, actualScope);

        // Test edge case
        node = new DetailAST();
        expectedScope = null;
        actualScope = ScopeUtils.getSurroundingScope(node);
        Assert.assertEquals(expectedScope, actualScope);
    }

    @Test
    public void testIsInInterfaceBlock() {
        // Test typical use case
        DetailAST node = new DetailAST();
        DetailAST parentToken = new DetailAST();
        parentToken.setType(15);
        node.setParent(parentToken);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInInterfaceBlock(node);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        node = new DetailAST();
        expectedValue = false;
        actualValue = ScopeUtils.isInInterfaceBlock(node);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsInAnnotationBlock() {
        // Test typical use case
        DetailAST node = new DetailAST();
        DetailAST parentToken = new DetailAST();
        parentToken.setType(157);
        node.setParent(parentToken);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInAnnotationBlock(node);
        Assert.assertEquals(expectedValue, actualValue);
        
        // Test edge case
        node = new DetailAST();
        expectedValue = false;
        actualValue = ScopeUtils.isInAnnotationBlock(node);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test typical use case
        DetailAST node = new DetailAST();
        DetailAST parentToken = new DetailAST();
        parentToken.setType(15);
        node.setParent(parentToken);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        node = new DetailAST();
        expectedValue = false;
        actualValue = ScopeUtils.isInInterfaceOrAnnotationBlock(node);
        Assert.assertEquals(expectedValue, actualValue);
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Test typical use case
        DetailAST node = new DetailAST();
        DetailAST parentToken = new DetailAST();
        parentToken.setType(154);
        node.setParent(parentToken);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInEnumBlock(node);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        node = new DetailAST();
        expectedValue = false;
        actualValue = ScopeUtils.isInEnumBlock(node);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsInCodeBlock() {
        // Test typical use case
        DetailAST node = new DetailAST();
        DetailAST parentToken = new DetailAST();
        parentToken.setType(9);
        node.setParent(parentToken);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInCodeBlock(node);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        node = new DetailAST();
        expectedValue = false;
        actualValue = ScopeUtils.isInCodeBlock(node);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsOuterMostType() {
        // Test typical use case
        DetailAST node = new DetailAST();
        DetailAST parentToken = new DetailAST();
        parentToken.setType(16);
        node.setParent(parentToken);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isOuterMostType(node);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        node = new DetailAST();
        expectedValue = false;
        actualValue = ScopeUtils.isOuterMostType(node);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsLocalVariableDef() {
        // Test typical use case
        DetailAST node = new DetailAST();
        node.setType(10);
        DetailAST parentToken = new DetailAST();
        parentToken.setType(7);
        node.setParent(parentToken);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isLocalVariableDef(node);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        node = new DetailAST();
        node.setType(10);
        expectedValue = false;
        actualValue = ScopeUtils.isLocalVariableDef(node);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsClassFieldDef() {
        // Test typical use case
        DetailAST node = new DetailAST();
        node.setType(10);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isClassFieldDef(node);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        node = new DetailAST();
        node.setType(9);
        expectedValue = false;
        actualValue = ScopeUtils.isClassFieldDef(node);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testIsInScope() {
        // Test typical use case
        DetailAST ast = new DetailAST();
        DetailAST surroundingScopeToken = new DetailAST();
        Scope expectedScope = Scope.PUBLIC;
        Scope actualScope = ScopeUtils.getSurroundingScope(ast);
        boolean expectedValue = true;
        boolean actualValue = ScopeUtils.isInScope(ast, expectedScope);
        Assert.assertEquals(expectedValue, actualValue);

        // Test edge case
        ast = new DetailAST();
        expectedScope = Scope.PACKAGE;
        actualScope = ScopeUtils.getSurroundingScope(ast);
        expectedValue = false;
        actualValue = ScopeUtils.isInScope(ast, expectedScope);
        Assert.assertEquals(expectedValue, actualValue);
    }
}