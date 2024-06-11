
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationGreaterThanTest {

    private CoreOperationGreaterThan expression;

    @Before
    public void setup() {
        expression = new CoreOperationGreaterThan(new ExpressionMock(), new ExpressionMock());
    }

    @Test
    public void testComputeValueGreaterThan() {
        EvalContext context = new EvalContext();
        expression.getArgs()[0].setValue(5); // Assign a value greater than the second argument
        expression.getArgs()[1].setValue(2);
        assertEquals(Boolean.TRUE, expression.computeValue(context));
    }

    @Test
    public void testComputeValueEqual() {
        EvalContext context = new EvalContext();
        expression.getArgs()[0].setValue(3); // Assign the same value to both arguments
        expression.getArgs()[1].setValue(3);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testComputeValueLessThan() {
        EvalContext context = new EvalContext();
        expression.getArgs()[0].setValue(1); // Assign a value less than the second argument
        expression.getArgs()[1].setValue(10);
        assertEquals(Boolean.FALSE, expression.computeValue(context));
    }

    @Test
    public void testGetSymbol() {
        assertEquals(">", expression.getSymbol());
    }
}

class ExpressionMock extends Expression {
    private Object value;

    public void setValue(Object value) {
        this.value = value;
    }

    public Object computeValue(EvalContext context) {
        return value;
    }
}
