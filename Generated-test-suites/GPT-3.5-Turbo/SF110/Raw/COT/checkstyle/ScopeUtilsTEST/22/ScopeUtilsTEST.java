import com.puppycrawl.tools.checkstyle.api.DetailAST;
import org.junit.Test;
import static org.junit.Assert.*;

public class ScopeUtilsTest {

    @Test
    public void testGetScopeFromMods() {
        // Test for public scope
        DetailAST aMods1 = new DetailAST();
        aMods1.addChild(new DetailAST());
        aMods1.getFirstChild().setText("public");
        assertEquals(Scope.PUBLIC, ScopeUtils.getScopeFromMods(aMods1));

        // Test for protected scope
        DetailAST aMods2 = new DetailAST();
        aMods2.addChild(new DetailAST());
        aMods2.getFirstChild().setText("protected");
        assertEquals(Scope.PROTECTED, ScopeUtils.getScopeFromMods(aMods2));

        // Test for private scope
        DetailAST aMods3 = new DetailAST();
        aMods3.addChild(new DetailAST());
        aMods3.getFirstChild().setText("private");
        assertEquals(Scope.PRIVATE, ScopeUtils.getScopeFromMods(aMods3));

        // Test for package scope
        DetailAST aMods4 = new DetailAST();
        aMods4.addChild(new DetailAST());
        assertEquals(Scope.PACKAGE, ScopeUtils.getScopeFromMods(aMods4));
    }

    @Test
    public void testGetSurroundingScope() {
        // Test for null scope
        DetailAST node1 = new DetailAST();
        assertNull(ScopeUtils.getSurroundingScope(node1));

        // Test for anonymous inner scope
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(136);
        node2.setParent(parent2);
        assertEquals(Scope.ANONINNER, ScopeUtils.getSurroundingScope(node2));
    }

    @Test
    public void testIsInInterfaceBlock() {
        // Test for false
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceBlock(node1));

        // Test for true
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(15);
        node2.setParent(parent2);
        assertTrue(ScopeUtils.isInInterfaceBlock(node2));
    }

    @Test
    public void testIsInAnnotationBlock() {
        // Test for false
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInAnnotationBlock(node1));

        // Test for true
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(157);
        node2.setParent(parent2);
        assertTrue(ScopeUtils.isInAnnotationBlock(node2));
    }

    @Test
    public void testIsInInterfaceOrAnnotationBlock() {
        // Test for false
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInInterfaceOrAnnotationBlock(node1));

        // Test for true (interface block)
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(15);
        node2.setParent(parent2);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node2));

        // Test for true (annotation block)
        DetailAST node3 = new DetailAST();
        DetailAST parent3 = new DetailAST();
        parent3.setType(157);
        node3.setParent(parent3);
        assertTrue(ScopeUtils.isInInterfaceOrAnnotationBlock(node3));
    }

    @Test
    public void testIsInEnumBlock() {
        // Test for false
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInEnumBlock(node1));

        // Test for true
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(154);
        node2.setParent(parent2);
        assertTrue(ScopeUtils.isInEnumBlock(node2));
    }

    @Test
    public void testIsInCodeBlock() {
        // Test for false
        DetailAST node1 = new DetailAST();
        assertFalse(ScopeUtils.isInCodeBlock(node1));

        // Test for true
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(9);
        node2.setParent(parent2);
        assertTrue(ScopeUtils.isInCodeBlock(node2));
    }

    @Test
    public void testIsOuterMostType() {
        // Test for true
        DetailAST node1 = new DetailAST();
        assertTrue(ScopeUtils.isOuterMostType(node1));

        // Test for false
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(14);
        node2.setParent(parent2);
        assertFalse(ScopeUtils.isOuterMostType(node2));
    }

    @Test
    public void testIsLocalVariableDef() {
        // Test for false
        DetailAST node1 = new DetailAST();
        node1.setType(5);
        assertFalse(ScopeUtils.isLocalVariableDef(node1));

        // Test for true
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(10);
        node2.setParent(parent2);
        assertTrue(ScopeUtils.isLocalVariableDef(node2));
    }

    @Test
    public void testIsClassFieldDef() {
        // Test for false
        DetailAST node1 = new DetailAST();
        node1.setType(7);
        assertFalse(ScopeUtils.isClassFieldDef(node1));

        // Test for true
        DetailAST node2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(10);
        node2.setParent(parent2);
        assertTrue(ScopeUtils.isClassFieldDef(node2));
    }

    @Test
    public void testIsInScope() {
        // Test for true
        DetailAST ast1 = new DetailAST();
        DetailAST parent1 = new DetailAST();
        parent1.setType(14);
        ast1.setParent(parent1);

        assertEquals(true, ScopeUtils.isInScope(ast1, Scope.PACKAGE));

        // Test for false
        DetailAST ast2 = new DetailAST();
        DetailAST parent2 = new DetailAST();
        parent2.setType(15);
        ast2.setParent(parent2);

        assertEquals(false, ScopeUtils.isInScope(ast2, Scope.PACKAGE));
    }
}