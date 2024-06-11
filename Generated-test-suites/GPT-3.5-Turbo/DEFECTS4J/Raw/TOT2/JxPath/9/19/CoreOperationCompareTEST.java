import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare();
        // Test basic equal scenario
        assertTrue(compare.equal(new EvalContext(), new Expression(), new Expression()));
        // Test scenario with different objects
        assertFalse(compare.equal(new EvalContext(), new Expression(), new Expression()));
        // Test scenario with null objects
        assertTrue(compare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testContains() {
        CoreOperationCompare compare = new CoreOperationCompare();
        // Test basic contains scenario
        assertFalse(compare.contains(new Iterator(), new Object()));
        // Test scenario with matching element
        assertTrue(compare.contains(new Iterator(), new Object()));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare compare = new CoreOperationCompare();
        // Test basic find match scenario
        assertFalse(compare.findMatch(new Iterator(), new Iterator()));
        // Test scenario with matching elements
        assertTrue(compare.findMatch(new Iterator(), new Iterator()));
    }

    @Test
    public void testGetPrecedence() {
        CoreOperationCompare compare = new CoreOperationCompare();
        // Test get precedence method
        assertEquals(2, compare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        CoreOperationCompare compare = new CoreOperationCompare();
        // Test is symmetric method
        assertTrue(compare.isSymmetric());
    }

}