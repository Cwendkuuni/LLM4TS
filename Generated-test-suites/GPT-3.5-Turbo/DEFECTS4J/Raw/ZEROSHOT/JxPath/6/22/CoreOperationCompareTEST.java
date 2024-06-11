import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null; // Not implemented for test
            }
        };

        EvalContext context = new EvalContext();
        Expression left = new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null; // Not implemented for test
            }
        };
        Expression right = new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null; // Not implemented for test
            }
        };

        boolean result = operation.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null; // Not implemented for test
            }
        };

        Iterator it = new Iterator() {
            private int index = 0;
            private Object[] values = new Object[]{1, 2, 3};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };
        Object value = 2;

        boolean result = operation.contains(it, value);

        Assert.assertTrue(result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null; // Not implemented for test
            }
        };

        Iterator leftIterator = new Iterator() {
            private int index = 0;
            private Object[] values = new Object[]{1, 2, 3};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };
        Iterator rightIterator = new Iterator() {
            private int index = 0;
            private Object[] values = new Object[]{2, 3, 4};

            @Override
            public boolean hasNext() {
                return index < values.length;
            }

            @Override
            public Object next() {
                return values[index++];
            }
        };

        boolean result = operation.findMatch(leftIterator, rightIterator);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualValues() {
        CoreOperationCompare operation = new CoreOperationCompare() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null; // Not implemented for test
            }
        };

        boolean result = operation.equal("abc", "abc");

        Assert.assertTrue(result);
    }

}