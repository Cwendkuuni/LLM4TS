import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationNotEqualTest {
    @Test
    public void testComputeValue() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());
        EvalContext context = new EvalContext();

        assertTrue((boolean) operation.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        CoreOperationNotEqual operation = new CoreOperationNotEqual(new Expression(), new Expression());

        assertEquals("!=", operation.getSymbol());
    }
}