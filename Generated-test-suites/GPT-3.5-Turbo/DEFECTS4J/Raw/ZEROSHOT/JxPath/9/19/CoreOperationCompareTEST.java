import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Expression left = new Expression();
        Expression right = new Expression();

        EvalContext context = new EvalContext();

        CoreOperationCompare operation = new CoreOperationCompare(left, right);

        boolean result = operation.equal(context, left, right);

        Assert.assertTrue(result);
    }

    @Test
    public void testContains() {
        Expression left = new Expression();
        Object value = new Object();

        Iterator iterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        CoreOperationCompare operation = new CoreOperationCompare(left, null);

        boolean result = operation.contains(iterator, value);

        Assert.assertFalse(result);
    }

    @Test
    public void testFindMatch() {
        Iterator leftIterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        Iterator rightIterator = new Iterator() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        boolean result = operation.findMatch(leftIterator, rightIterator);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithPointers() {
        Pointer pointer1 = new Pointer();
        Pointer pointer2 = new Pointer();

        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        boolean result = operation.equal(pointer1, pointer2);

        Assert.assertTrue(result);
    }

    @Test
    public void testEqualWithBoolean() {
        Boolean l = true;
        Boolean r = false;

        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        boolean result = operation.equal(l, r);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithNumbers() {
        Number l = 1;
        Number r = 2;

        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        boolean result = operation.equal(l, r);

        Assert.assertFalse(result);
    }

    @Test
    public void testEqualWithStrings() {
        String l = "Hello";
        String r = "World";

        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        boolean result = operation.equal(l, r);

        Assert.assertFalse(result);
    }

}