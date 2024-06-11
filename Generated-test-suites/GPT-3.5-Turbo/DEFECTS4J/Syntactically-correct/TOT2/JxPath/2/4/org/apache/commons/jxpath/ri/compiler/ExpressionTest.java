import static org.junit.Assert.*;
import org.junit.Test;

public class ExpressionTest {
    
    @Test
    public void testIsContextDependent() {
        Expression exp = new ConcreteExpression();
        assertFalse(exp.isContextDependent());
    }
    
    @Test
    public void testComputeValue() {
        Expression exp = new ConcreteExpression();
        EvalContext context = new EvalContext();
        Object result = exp.computeValue(context);
        assertNull(result);
    }
    
    @Test
    public void testCompute() {
        Expression exp = new ConcreteExpression();
        EvalContext context = new EvalContext();
        Object result = exp.compute(context);
        assertNull(result);
    }

}

class ConcreteExpression extends Expression {
    
    public boolean computeContextDependent() {
        return false;
    }
    
    public Object computeValue(EvalContext context) {
        return null;
    }
    
    public Object compute(EvalContext context) {
        return null;
    }
}