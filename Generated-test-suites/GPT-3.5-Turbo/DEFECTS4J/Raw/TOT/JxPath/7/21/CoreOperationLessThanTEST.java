
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CoreOperationLessThanTest {

    @Test
    public void testComputeValue_typicalCase() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext evalContext = new EvalContext();

        // Set values for arg1 and arg2
        arg1.setValue(5);
        arg2.setValue(10);

        Object result = coreOpLessThan.computeValue(evalContext);

        Assertions.assertEquals(Boolean.TRUE, result);
    }

    @Test
    public void testComputeValue_equalValues() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext evalContext = new EvalContext();

        // Set values for arg1 and arg2
        arg1.setValue(5);
        arg2.setValue(5);

        Object result = coreOpLessThan.computeValue(evalContext);

        Assertions.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_negativeValues() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext evalContext = new EvalContext();

        // Set values for arg1 and arg2
        arg1.setValue(-10);
        arg2.setValue(-5);

        Object result = coreOpLessThan.computeValue(evalContext);

        Assertions.assertEquals(Boolean.TRUE, result);
    }
    
    @Test
    public void testComputeValue_zeroValues() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext evalContext = new EvalContext();

        // Set values for arg1 and arg2
        arg1.setValue(0);
        arg2.setValue(0);

        Object result = coreOpLessThan.computeValue(evalContext);

        Assertions.assertEquals(Boolean.FALSE, result);
    }
    
    @Test
    public void testComputeValue_errorScenario() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);

        EvalContext evalContext = new EvalContext();

        // Set invalid values for arg1 and arg2 to trigger an exception
        arg1.setValue("invalid");
        arg2.setValue(10);

        Assertions.assertThrows(NumberFormatException.class, () -> {
            coreOpLessThan.computeValue(evalContext);
        });
    }

    @Test
    public void testGetSymbol() {
        Expression arg1 = new Expression();
        Expression arg2 = new Expression();
        CoreOperationLessThan coreOpLessThan = new CoreOperationLessThan(arg1, arg2);

        String symbol = coreOpLessThan.getSymbol();

        Assertions.assertEquals("<", symbol);
    }
}

