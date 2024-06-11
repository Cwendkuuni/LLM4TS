
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.Scope;
import antlr.collections.DetailAST;
import org.junit.Assert;
import org.junit.Test;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(new DetailAST());
        aMods.getFirstChild().setText("public");
        Scope expected = Scope.PUBLIC;
        Scope actual = ScopeUtils.getScopeFromMods(aMods);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testGetSurroundingScope() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        Scope expected = Scope.ANONINNER;
        Scope actual = ScopeUtils.getSurroundingScope(node);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        ScopeUtils.isInInterfaceBlock(node);
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        ScopeUtils.isInAnnotationBlock(node);
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        ScopeUtils.isInInterfaceOrAnnotationBlock(node);
    }
    
    @Test
    public void testIsInEnumBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        ScopeUtils.isInEnumBlock(node);
    }
    
    @Test
    public void testIsInCodeBlock() {
        DetailAST node = new DetailAST();
        node.addChild(new DetailAST());
        ScopeUtils.isInCodeBlock(node);
    }
    
    @Test
    public void testIsOuterMostType() {
        DetailAST node = new DetailAST();
        ScopeUtils.isOuterMostType(node);
    }
    
    @Test
    public void testIsLocalVariableDef() {
        DetailAST node = new DetailAST();
        ScopeUtils.isLocalVariableDef(node);
    }
    
    @Test
    public void testIsClassFieldDef() {
        DetailAST node = new DetailAST();
        ScopeUtils.isClassFieldDef(node);
    }
    
    @Test
    public void testIsInScope() {
        DetailAST ast = new DetailAST();
        Scope scope = Scope.PACKAGE;
        Scope actual = ScopeUtils.isInScope(ast, scope);
        Assert.assertTrue(actual);
    }
}
