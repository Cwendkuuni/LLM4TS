import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.CoreOperationCompare;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new DummyExpression();
        Expression right = new DummyExpression();

        CoreOperationCompare coreOpCompare = new CoreOperationCompare(left, right);

        EvalContext context = new EvalContext();

        boolean result = coreOpCompare.equal(context, left, right);

        assertFalse(result);
    }

    @Test
    public void testContains() {
        Iterator it = new DummyIterator();
        Object value = new Object();

        CoreOperationCompare coreOpCompare = new CoreOperationCompare(null, null);

        boolean result = coreOpCompare.contains(it, value);

        assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new DummyIterator();
        Iterator rit = new DummyIterator();

        CoreOperationCompare coreOpCompare = new CoreOperationCompare(null, null);

        boolean result = coreOpCompare.findMatch(lit, rit);

        assertFalse(result);
    }

    @Test
    public void testEqualValues() {
        Object l = new Object();
        Object r = new Object();

        CoreOperationCompare coreOpCompare = new CoreOperationCompare(null, null);

        boolean result = coreOpCompare.equal(l, r);

        assertFalse(result);
    }

    private class DummyExpression implements Expression {
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private class DummyIterator implements Iterator {
        public boolean hasNext() {
            return false;
        }

        public Object next() {
            return null;
        }
    }
}