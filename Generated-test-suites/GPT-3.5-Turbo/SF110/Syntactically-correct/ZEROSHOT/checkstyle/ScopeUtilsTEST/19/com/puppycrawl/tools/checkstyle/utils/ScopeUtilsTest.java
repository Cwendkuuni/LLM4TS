import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test case 1
        DetailAST aModsCase1 = new DetailAST();
        Scope expectedResultCase1 = Scope.PACKAGE;
        Scope actualResultCase1 = ScopeUtils.getScopeFromMods(aModsCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST aModsCase2 = new DetailAST();
        AST tokenCase2 = new AST();
        tokenCase2.setText("public");
        aModsCase2.addChild(tokenCase2);
        Scope expectedResultCase2 = Scope.PUBLIC;
        Scope actualResultCase2 = ScopeUtils.getScopeFromMods(aModsCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST aModsCase3 = new DetailAST();
        AST tokenCase3 = new AST();
        tokenCase3.setText("protected");
        aModsCase3.addChild(tokenCase3);
        Scope expectedResultCase3 = Scope.PROTECTED;
        Scope actualResultCase3 = ScopeUtils.getScopeFromMods(aModsCase3);
        assertEquals(expectedResultCase3, actualResultCase3);

        // Test case 4
        DetailAST aModsCase4 = new DetailAST();
        AST tokenCase4 = new AST();
        tokenCase4.setText("private");
        aModsCase4.addChild(tokenCase4);
        Scope expectedResultCase4 = Scope.PRIVATE;
        Scope actualResultCase4 = ScopeUtils.getScopeFromMods(aModsCase4);
        assertEquals(expectedResultCase4, actualResultCase4);
    }

    @Test
    public void testGetSurroundingScope() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(14);
        DetailAST tokenCase1 = new DetailAST();
        tokenCase1.setType(5);
        Scope modScopeCase1 = Scope.PUBLIC;
        tokenCase1.addChild(modScopeCase1);
        nodeCase1.addChild(tokenCase1);

        Scope expectedResultCase1 = Scope.PUBLIC;
        Scope actualResultCase1 = ScopeUtils.getSurroundingScope(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(15);
        nodeCase2.setParent(null);

        Scope expectedResultCase2 = null;
        Scope actualResultCase2 = ScopeUtils.getSurroundingScope(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(15);
        DetailAST tokenCase3 = new DetailAST();
        tokenCase3.setType(5);
        Scope modScopeCase3 = Scope.PRIVATE;
        tokenCase3.addChild(modScopeCase3);
        nodeCase3.addChild(tokenCase3);

        Scope expectedResultCase3 = Scope.PRIVATE;
        Scope actualResultCase3 = ScopeUtils.getSurroundingScope(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsInInterfaceBlock() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(15);

        boolean expectedResultCase1 = true;
        boolean actualResultCase1 = ScopeUtils.isInInterfaceBlock(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(14);

        boolean expectedResultCase2 = false;
        boolean actualResultCase2 = ScopeUtils.isInInterfaceBlock(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(136);

        boolean expectedResultCase3 = false;
        boolean actualResultCase3 = ScopeUtils.isInInterfaceBlock(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsInAnnotationBlock() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(157);

        boolean expectedResultCase1 = true;
        boolean actualResultCase1 = ScopeUtils.isInAnnotationBlock(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(15);

        boolean expectedResultCase2 = false;
        boolean actualResultCase2 = ScopeUtils.isInAnnotationBlock(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(136);

        boolean expectedResultCase3 = false;
        boolean actualResultCase3 = ScopeUtils.isInAnnotationBlock(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(15);

        boolean expectedResultCase1 = true;
        boolean actualResultCase1 = ScopeUtils.isInInterfaceOrAnnotationBlock(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(157);

        boolean expectedResultCase2 = true;
        boolean actualResultCase2 = ScopeUtils.isInInterfaceOrAnnotationBlock(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(136);

        boolean expectedResultCase3 = false;
        boolean actualResultCase3 = ScopeUtils.isInInterfaceOrAnnotationBlock(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsInEnumBlock() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(154);

        boolean expectedResultCase1 = true;
        boolean actualResultCase1 = ScopeUtils.isInEnumBlock(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(15);

        boolean expectedResultCase2 = false;
        boolean actualResultCase2 = ScopeUtils.isInEnumBlock(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(136);

        boolean expectedResultCase3 = false;
        boolean actualResultCase3 = ScopeUtils.isInEnumBlock(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsInCodeBlock() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(9);

        boolean expectedResultCase1 = true;
        boolean actualResultCase1 = ScopeUtils.isInCodeBlock(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(136);

        boolean expectedResultCase2 = false;
        boolean actualResultCase2 = ScopeUtils.isInCodeBlock(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);
    }

    @Test
    public void testIsOuterMostType() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(7);
        DetailAST parentCase1 = new DetailAST();
        parentCase1.setType(14);
        nodeCase1.setParent(parentCase1);

        boolean expectedResultCase1 = false;
        boolean actualResultCase1 = ScopeUtils.isOuterMostType(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(7);
        DetailAST parentCase2 = new DetailAST();
        parentCase2.setType(15);
        nodeCase2.setParent(parentCase2);

        boolean expectedResultCase2 = false;
        boolean actualResultCase2 = ScopeUtils.isOuterMostType(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(7);
        DetailAST parentCase3 = new DetailAST();
        parentCase3.setType(157);
        nodeCase3.setParent(parentCase3);

        boolean expectedResultCase3 = false;
        boolean actualResultCase3 = ScopeUtils.isOuterMostType(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsLocalVariableDef() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(10);
        DetailAST parentCase1 = new DetailAST();
        parentCase1.setType(7);
        nodeCase1.setParent(parentCase1);

        boolean expectedResultCase1 = true;
        boolean actualResultCase1 = ScopeUtils.isLocalVariableDef(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(10);
        DetailAST parentCase2 = new DetailAST();
        parentCase2.setType(15);
        nodeCase2.setParent(parentCase2);

        boolean expectedResultCase2 = false;
        boolean actualResultCase2 = ScopeUtils.isLocalVariableDef(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(21);
        DetailAST parentCase3 = new DetailAST();
        parentCase3.setType(96);
        nodeCase3.setParent(parentCase3);

        boolean expectedResultCase3 = true;
        boolean actualResultCase3 = ScopeUtils.isLocalVariableDef(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsClassFieldDef() {
        // Test case 1
        DetailAST nodeCase1 = new DetailAST();
        nodeCase1.setType(10);
        DetailAST parentCase1 = new DetailAST();
        parentCase1.setType(7);
        nodeCase1.setParent(parentCase1);

        boolean expectedResultCase1 = false;
        boolean actualResultCase1 = ScopeUtils.isClassFieldDef(nodeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST nodeCase2 = new DetailAST();
        nodeCase2.setType(10);
        DetailAST parentCase2 = new DetailAST();
        parentCase2.setType(15);
        nodeCase2.setParent(parentCase2);

        boolean expectedResultCase2 = true;
        boolean actualResultCase2 = ScopeUtils.isClassFieldDef(nodeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);

        // Test case 3
        DetailAST nodeCase3 = new DetailAST();
        nodeCase3.setType(10);
        DetailAST parentCase3 = new DetailAST();
        parentCase3.setType(21);
        nodeCase3.setParent(parentCase3);

        boolean expectedResultCase3 = true;
        boolean actualResultCase3 = ScopeUtils.isClassFieldDef(nodeCase3);
        assertEquals(expectedResultCase3, actualResultCase3);
    }

    @Test
    public void testIsInScope() {
        // Test case 1
        DetailAST astCase1 = new DetailAST();
        Scope scopeCase1 = Scope.PUBLIC;
        Scope surroundingScopeOfAstTokenCase1 = Scope.PUBLIC;

        boolean expectedResultCase1 = true;
        boolean actualResultCase1 = ScopeUtils.isInScope(astCase1, scopeCase1);
        assertEquals(expectedResultCase1, actualResultCase1);

        // Test case 2
        DetailAST astCase2 = new DetailAST();
        Scope scopeCase2 = Scope.PUBLIC;
        Scope surroundingScopeOfAstTokenCase2 = Scope.PRIVATE;

        boolean expectedResultCase2 = false;
        boolean actualResultCase2 = ScopeUtils.isInScope(astCase2, scopeCase2);
        assertEquals(expectedResultCase2, actualResultCase2);
    }

}