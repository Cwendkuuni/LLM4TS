import static org.junit.Assert.*;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        EvalContext context = new EvalContext();
        Expression left = new MockExpression();
        Expression right = new MockExpression();
        
        // Typical use case
        assertTrue(coreOperationCompare.equal(context, left, right));
        
        // Edge case with empty collections
        context.addData("left", new HashSet());
        context.addData("right", new HashSet());
        left = new VariableExpression("left");
        right = new VariableExpression("right");
        assertFalse(coreOperationCompare.equal(context, left, right));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator it = new MockIterator();
        Object value = new Object();
        
        // Typical use case
        assertFalse(coreOperationCompare.contains(it, value));
        
        // Edge case with iterator containing value
        MockIterator mockIterator = new MockIterator();
        mockIterator.add(value);
        assertTrue(coreOperationCompare.contains(mockIterator, value));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Iterator lit = new MockIterator();
        Iterator rit = new MockIterator();
        
        // Typical use case
        assertFalse(coreOperationCompare.findMatch(lit, rit));
        
        // Edge case with matching elements
        MockIterator leftIter = new MockIterator();
        MockIterator rightIter = new MockIterator();
        leftIter.add("a");
        rightIter.add("a");
        assertTrue(coreOperationCompare.findMatch(leftIter, rightIter));
    }

    @Test
    public void testEqualObjects() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        Object l = "test";
        Object r = "test";
        
        // Typical use case
        assertTrue(coreOperationCompare.equal(l, r));
        
        // Edge case with different objects
        assertFalse(coreOperationCompare.equal(l, new Object()));
    }
}