
import org.junit.Assert;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        EvalContext context = new EvalContext(null, null);
        Expression left = new Expression(null);
        Expression right = new Expression(null);

        boolean result = coreOperationCompare.equal(context, left, right);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator<Object> it = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Object value = new Object();

        boolean result = coreOperationCompare.contains(it, value);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Iterator<Object> lit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };
        Iterator<Object> rit = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Object next() {
                return null;
            }
        };

        boolean result = coreOperationCompare.findMatch(lit, rit);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testEqualWithPointer() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Pointer l = new Pointer();
        Pointer r = new Pointer();

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertEquals(false, result);
    }

    @Test
    public void testEqualWithBoolean() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Boolean l = true;
        Boolean r = true;

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testEqualWithNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Number l = 1;
        Number r = 1;

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testEqualWithString() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        String l = "test";
        String r = "test";

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertEquals(true, result);
    }

    @Test
    public void testEqualWithNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(null, null);
        Object l = null;
        Object r = null;

        boolean result = coreOperationCompare.equal(l, r);

        Assert.assertEquals(true, result);
    }
}
