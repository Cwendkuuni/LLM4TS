import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSet;
import org.apache.commons.jxpath.ri.InfSetUtil;
import org.apache.commons.jxpath.ri.compiler.CoreOperationGreaterThanOrEqual;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationGreaterThanOrEqualTest {

    @Test
    public void testCoreOperationGreaterThanOrEqual() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(5.0), new CoreConstant(5.0));
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithEdgeCases() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(5.0), new CoreConstant(6.0));
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(4.0), new CoreConstant(5.0));
        result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(5.0), new CoreConstant(4.0));
        result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(Double.MIN_VALUE), new CoreConstant(Double.MIN_VALUE));
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(Double.MIN_VALUE), new CoreConstant(Double.MIN_VALUE + 0.0001));
        result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test(expected = ClassCastException.class)
    public void testCoreOperationGreaterThanOrEqualWithStringArguments() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant("5.0"), new CoreConstant("4.0"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCoreOperationGreaterThanOrEqualWithNullArg() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(5.0), null);
    }

    @Test
    public void testCoreOperationGreaterThanOrEqualWithEdgeCasesAndNaN() {
        CoreOperationGreaterThanOrEqual coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(Double.NaN), new CoreConstant(Double.NaN));
        EvalContext context = new EvalContext();
        InfoSet infoSet = new InfoSet();
        context.setInfoSet(infoSet);
        Object result = coreOperation.computeValue(context);
        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);

        coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(Double.NaN), new CoreConstant(Double.NEGATIVE_INFINITY));
        result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);

        coreOperation = new CoreOperationGreaterThanOrEqual(new CoreConstant(Double.NEGATIVE_INFINITY), new CoreConstant(Double.NaN));
        result = coreOperation.computeValue(context);
        assertFalse(result instanceof Boolean);
        assertFalse((Boolean) result);
    }
}