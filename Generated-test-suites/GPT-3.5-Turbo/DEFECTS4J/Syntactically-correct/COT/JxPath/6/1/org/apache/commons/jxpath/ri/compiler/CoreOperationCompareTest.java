import org.junit.Assert;
import org.junit.Test;
import java.util.Iterator;
import java.util.Collection;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        EvalContext context = new EvalContext();
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(arg1, arg2);
        boolean result = coreOperationCompare.equal(context, arg1, arg2);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testContains() {
        Iterator it = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.contains(it, value);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        Iterator lit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator rit = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.findMatch(lit, rit);
        Assert.assertEquals(false, result);
    }

    @Test
    public void testEqual2() {
        Object l = null;
        Object r = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.equal(l, r);
        Assert.assertEquals(true, result);
    }

    @Test
    public void testEqual3() {
        Object l = null;
        Object r = null;
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare();
        boolean result = coreOperationCompare.equal(l, r);
        Assert.assertEquals(true, result);
    }
}