import org.junit.Test;

import static org.junit.Assert.*;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext context = new EvalContext();

        // Test when l < r -> computeValue returns Boolean.TRUE
        Object l = 10.0;
        Object r = 20.0;
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(context));

        // Test when l >= r -> computeValue returns Boolean.FALSE
        l = 20.0;
        r = 10.0;
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));

        // Test when l = r -> computeValue returns Boolean.FALSE
        l = 20.0;
        r = 20.0;
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));

        // Test when l < r as strings -> computeValue returns Boolean.TRUE
        l = "10";
        r = "20";
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(context));

        // Test when l >= r as strings -> computeValue returns Boolean.FALSE
        l = "20";
        r = "10";
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));

        // Test when l = r as strings -> computeValue returns Boolean.FALSE
        l = "20";
        r = "20";
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));

        // Test when l < r as strings and numbers -> computeValue returns Boolean.TRUE
        l = 10.0;
        r = "20";
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.TRUE, coreOperationLessThan.computeValue(context));

        // Test when l >= r as strings and numbers -> computeValue returns Boolean.FALSE
        l = 20.0;
        r = "10";
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));

        // Test when l = r as strings and numbers -> computeValue returns Boolean.FALSE
        l = 20.0;
        r = "20";
        context.setValue("arg1", l);
        context.setValue("arg2", r);
        assertEquals(Boolean.FALSE, coreOperationLessThan.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOperationLessThan = new CoreOperationLessThan(arg1, arg2);

        assertEquals("<", coreOperationLessThan.getSymbol());
    }
}