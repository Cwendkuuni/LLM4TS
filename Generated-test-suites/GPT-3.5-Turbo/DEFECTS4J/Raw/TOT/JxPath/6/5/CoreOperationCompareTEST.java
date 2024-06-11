import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    private CoreOperationCompare operation;

    @Test
    public void testEqual_nullValues() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertTrue(operation.equal(context, null, null));
    }

    @Test
    public void testEqual_differentTypes() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertFalse(operation.equal(context, "abc", 123));
    }

    @Test
    public void testEqual_sameType() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertTrue(operation.equal(context, "abc", "abc"));
    }

    @Test
    public void testEqual_BooleanValues_sameType() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertTrue(operation.equal(context, true, true));
    }

    @Test
    public void testEqual_BooleanValues_differentType() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertFalse(operation.equal(context, true, "true"));
    }

    @Test
    public void testEqual_DoubleValues_sameType() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertTrue(operation.equal(context, 123.45, 123.45));
    }

    @Test
    public void testEqual_DoubleValues_differentType() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertFalse(operation.equal(context, 123.45, "123.45"));
    }

    @Test
    public void testEqual_StringValues_sameType() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertTrue(operation.equal(context, "abc", "abc"));
    }

    @Test
    public void testEqual_StringValues_differentType() {
        operation = new CoreOperationCompare(null, null);
        EvalContext context = createEvalContext();
        assertFalse(operation.equal(context, "abc", true));
    }

    private EvalContext createEvalContext() {
        // implementation to create dummy EvalContext object
        // for testing purposes
        return null;
    }
}