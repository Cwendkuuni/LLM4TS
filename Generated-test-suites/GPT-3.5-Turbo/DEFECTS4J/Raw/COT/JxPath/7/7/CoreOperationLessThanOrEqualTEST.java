import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.ri.compiler.CoreOperationLessThanOrEqual;
import org.apache.commons.jxpath.ri.EvalContext;
import org.junit.Test;

public class CoreOperationLessThanOrEqualTest {

    @Test
    public void testComputeValue() {
        CoreOperationLessThanOrEqual expression = new CoreOperationLessThanOrEqual(null, null);
        EvalContext context = null;
        
        // Test when l <= r
        expression.args[0] = new ExpressionMock(2.0);
        expression.args[1] = new ExpressionMock(3.0);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
        
        // Test when l > r
        expression.args[0] = new ExpressionMock(3.0);
        expression.args[1] = new ExpressionMock(2.0);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    private class ExpressionMock extends Expression {
        private Object value;
        
        public ExpressionMock(Object value) {
            this.value = value;
        }
        
        public Object computeValue(EvalContext context) {
            return value;
        }
    }
}