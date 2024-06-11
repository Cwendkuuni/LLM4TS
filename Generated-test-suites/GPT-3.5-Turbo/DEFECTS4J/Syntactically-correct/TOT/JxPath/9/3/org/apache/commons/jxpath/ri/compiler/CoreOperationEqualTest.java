import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.compiler.Expression;
import org.apache.commons.jxpath.ri.compiler.CoreOperationEqual;
import org.apache.commons.jxpath.ri.EvalContext;

public class CoreOperationEqualTest {

    private CoreOperationEqual operation;

    @Before
    public void setup() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        operation = new CoreOperationEqual(arg1, arg2);
    }

    @Test
    public void testComputeValueEqual() {
        EvalContext context = new EvalContext();
        boolean result = (boolean) operation.computeValue(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValueNotEqual() {
        EvalContext context = new EvalContext();
        context.setNode(new Object());  // Set a different node to make the equality check fail
        boolean result = (boolean) operation.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetSymbol() {
        String symbol = operation.getSymbol();
        Assert.assertEquals("=", symbol);
    }

    // Additional test cases to cover typical use cases, edge cases, and error scenarios

    @Test
    public void testComputeValueEqualOnNullArguments() {
        CoreOperationEqual operation = new CoreOperationEqual(null, null);
        EvalContext context = new EvalContext();
        boolean result = (boolean) operation.computeValue(context);
        Assert.assertTrue(result);
    }

    @Test
    public void testComputeValueNotEqualOnNullArgument() {
        Expression arg1 = new MockExpression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, null);
        EvalContext context = new EvalContext();
        boolean result = (boolean) operation.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testComputeValueNotEqualOnDifferentTypes() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression("2");
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        EvalContext context = new EvalContext();
        boolean result = (boolean) operation.computeValue(context);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetSymbolNotNull() {
        Expression arg1 = new MockExpression();
        Expression arg2 = new MockExpression();
        CoreOperationEqual operation = new CoreOperationEqual(arg1, arg2);
        String symbol = operation.getSymbol();
        Assert.assertNotNull(symbol);
    }

    // Mock expression class for testing

    private class MockExpression implements Expression {
        private Object value;

        public MockExpression() {
            this.value = "1";
        }

        public MockExpression(Object value) {
            this.value = value;
        }

        public Object computeValue(EvalContext evalContext){
            return value;
        }

        public String toString(){
            return value.toString();
        }
    }
}