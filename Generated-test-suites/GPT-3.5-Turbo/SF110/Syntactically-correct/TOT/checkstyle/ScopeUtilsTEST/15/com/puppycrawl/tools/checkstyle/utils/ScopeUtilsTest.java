import org.junit.*;
import com.puppycrawl.tools.checkstyle.api.*;
import antlr.collections.*;

public class ScopeUtilsTest {

    DetailAST ast;
    ScopeUtils utils;

    @Before
    public void setup() {
        ast = new DetailAST();
        utils = new ScopeUtils();
    }

    @Test
    public void testGetScopeFromMods_package() {
        DetailAST token = new DetailAST();
        token.setText("");
        ast.addChild(token);
        Scope expected = Scope.PACKAGE;
        
        Scope result = utils.getScopeFromMods(ast);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetScopeFromMods_public() {
        DetailAST token = new DetailAST();
        token.setText("public");
        ast.addChild(token);
        Scope expected = Scope.PUBLIC;
        
        Scope result = utils.getScopeFromMods(ast);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetScopeFromMods_protected() {
        DetailAST token = new DetailAST();
        token.setText("protected");
        ast.addChild(token);
        Scope expected = Scope.PROTECTED;
        
        Scope result = utils.getScopeFromMods(ast);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetScopeFromMods_private() {
        DetailAST token = new DetailAST();
        token.setText("private");
        ast.addChild(token);
        Scope expected = Scope.PRIVATE;
        
        Scope result = utils.getScopeFromMods(ast);

        Assert.assertEquals(expected, result);
    }

    @Test
    public void testGetSurroundingScope_parentIsNull() {
        DetailAST token = new DetailAST();
        token.setText("");
        ast.setParent(null);
        Scope expected = null;
        
        Scope result = utils.getSurroundingScope(ast);

        Assert.assertEquals(expected, result);
    }

    // Continue with test cases for the remaining methods...

}