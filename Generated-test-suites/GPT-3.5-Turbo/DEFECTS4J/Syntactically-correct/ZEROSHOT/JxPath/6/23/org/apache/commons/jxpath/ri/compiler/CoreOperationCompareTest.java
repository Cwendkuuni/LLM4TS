import static org.junit.Assert.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.apache.commons.jxpath.Pointer;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare compare = new CoreOperationCompare(MockExpression.INSTANCE, MockExpression.INSTANCE);
        EvalContext context = new EvalContext();
        Object result = compare.equal(context, MockExpression.INSTANCE, MockExpression.INSTANCE);
        assertTrue(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare compare = new CoreOperationCompare(MockExpression.INSTANCE, MockExpression.INSTANCE);
        Iterator<Object> it = new HashSet<Object>().iterator();
        boolean result = compare.contains(it, MockExpression.INSTANCE);
        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare compare = new CoreOperationCompare(MockExpression.INSTANCE, MockExpression.INSTANCE);
        Iterator<Object> lit = new HashSet<Object>().iterator();
        Iterator<Object> rit = new HashSet<Object>().iterator();
        boolean result = compare.findMatch(lit, rit);
        assertFalse(result);
    }

    @Test
    public void testEquals() {
        Object l = MockExpression.INSTANCE;
        Object r = MockExpression.INSTANCE;
        CoreOperationCompare compare = new CoreOperationCompare(MockExpression.INSTANCE, MockExpression.INSTANCE);
        boolean result = compare.equal(l, r);
        assertTrue(result);
    }

    @Test
    public void testValues() {
        CoreOperationCompare compare = new CoreOperationCompare(MockExpression.INSTANCE, MockExpression.INSTANCE);
        Object l = MockExpression.INSTANCE;
        Object r = MockExpression.INSTANCE;
        boolean result = compare.equal(l, r);
        assertTrue(result);
    }
}