import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.TestUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanTest {

    private InfoSet infoSet;

    @Before
    public void setUp() {
        infoSet = TestUtils.createInfoSet();
    }

    @Test
    public void testCoreOperationGreaterThan() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(5.0), new Constant(3.0));
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanZero() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(0.0), new Constant(5.0));
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.FALSE, result);
    }

    @Test
    public void testCoreOperationGreaterThanEqual() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(5.0), new Constant(5.0));
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testCoreOperationGreaterThanNaN() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(Double.NaN), new Constant(Double.NaN));
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertFalse(result instanceof Boolean);
    }

    @Test
    public void testCoreOperationGreaterThanNull() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(5.0), new Constant(null));
        EvalContext context = new EvalContext(infoSet);
        Object result = coreOperationGreaterThan.computeValue(context);
        assertNull(result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanWithInvalidArguments() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant("a"), new Constant(3.0));
        EvalContext context = new EvalContext(infoSet);
        coreOperationGreaterThan.computeValue(context);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanWithMixedTypes() {
        CoreOperationGreaterThan coreOperationGreaterThan = new CoreOperationGreaterThan(new Constant(5.0), new Constant("b"));
        EvalContext context = new EvalContext(infoSet);
        coreOperationGreaterThan.computeValue(context);
    }

    @After
    public void tearDown() {
        infoSet = null;
    }
}