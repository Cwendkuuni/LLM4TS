import static org.junit.Assert.*;
import org.junit.Test;
import com.puppycrawl.tools.checkstyle.utils.ScopeUtils;
import com.puppycrawl.tools.checkstyle.api.DetailAST;

public class ScopeUtilsTest {
    
    @Test
    public void testGetScopeFromMods() {
        DetailAST aMods = new DetailAST();
        aMods.addChild(token("public"));
        aMods.addChild(token("protected"));
        aMods.addChild(token("private"));
        aMods.addChild(token("default"));
        
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods));
        
        DetailAST bMods = new DetailAST();
        bMods.addChild(token("protected"));
        bMods.addChild(token("private"));
        
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(bMods));
        
        DetailAST cMods = new DetailAST();
        cMods.addChild(token("private"));
        cMods.addChild(token("default"));
        
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(cMods));
        
        DetailAST dMods = new DetailAST();
        dMods.addChild(token("default"));
        
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(dMods));
        
    }
    
    @Test
    public void testGetSurroundingScope() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsInInterfaceBlock() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsInAnnotationBlock() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsInEnumBlock() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsInCodeBlock() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsOuterMostType() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsLocalVariableDef() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsClassFieldDef() {
        // Add test cases here
        
    }
    
    @Test
    public void testIsInScope() {
        // Add test cases here
        
    }
    
    private DetailAST token(String text) {
        DetailAST token = new DetailAST();
        token.setText(text);
        return token;
    }
}