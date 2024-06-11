import org.junit.Test;
import static org.junit.Assert.*;

public class ExpressionTest {

    @Test
    public void testIsContextDependent() {
        Expression expression = new ConcreteExpression();
        assertTrue(expression.isContextDependent());
    }

    @Test
    public void testComputeValue() {
        Expression expression = new ConcreteExpression();
        EvalContext context = new MockEvalContext();
        Object result = expression.computeValue(context);
        assertNotNull(result);
    }

    @Test
    public void testCompute() {
        Expression expression = new ConcreteExpression();
        EvalContext context = new MockEvalContext();
        Object result = expression.compute(context);
        assertNotNull(result);
    }

    @Test
    public void testIterate() {
        Expression expression = new ConcreteExpression();
        EvalContext context = new MockEvalContext();
        Iterator iterator = expression.iterate(context);
        assertNotNull(iterator);
    }

    @Test
    public void testIteratePointers() {
        Expression expression = new ConcreteExpression();
        EvalContext context = new MockEvalContext();
        Iterator iterator = expression.iteratePointers(context);
        assertNotNull(iterator);
    }
}

class ConcreteExpression extends Expression {

    @Override
    public boolean computeContextDependent() {
        return true;
    }

    @Override
    public Object computeValue(EvalContext context) {
        return new Object();
    }

    @Override
    public Object compute(EvalContext context) {
        return new Object();
    }
}

class MockEvalContext extends EvalContext {

    @Override
    public EvalContext getClone() {
        return this;
    }
}