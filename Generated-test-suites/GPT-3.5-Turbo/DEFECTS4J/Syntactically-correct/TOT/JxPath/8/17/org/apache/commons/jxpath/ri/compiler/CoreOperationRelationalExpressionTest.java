import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CoreOperationRelationalExpressionTest {

    private CoreOperationRelationalExpression expression;

    @Before
    public void setUp() {
        expression = new CoreOperationRelationalExpressionMock(new Expression[2]);
    }

    @Test
    public void testComputeValue_withEqualValues_shouldReturnTrue() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(10);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_withNotEqualValues_shouldReturnFalse() {
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(5);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    @Test
    public void testComputeValue_withInitialContextAsLeftOperand_shouldResetInitialContext() {
        InitialContext initialContext = new InitialContext();
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(initialContext);

        expression.computeValue(context);

        assertTrue(initialContext.isReset());
    }

    @Test
    public void testComputeValue_withInitialContextAsRightOperand_shouldResetInitialContext() {
        InitialContext initialContext = new InitialContext();
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(10);

        Object result = expression.computeValue(context);

        assertTrue(initialContext.isReset());
    }

    @Test
    public void testComputeValue_withIteratorsAsOperandsAndMatch_shouldReturnTrue() {
        IteratorMock iterator1 = new IteratorMock(new Object[]{ 1, 2, 3 });
        IteratorMock iterator2 = new IteratorMock(new Object[]{ 3, 4, 5 });
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(iterator1);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertTrue((Boolean) result);
    }

    @Test
    public void testComputeValue_withIteratorsAsOperandsAndNoMatch_shouldReturnFalse() {
        IteratorMock iterator1 = new IteratorMock(new Object[]{ 1, 2, 3 });
        IteratorMock iterator2 = new IteratorMock(new Object[]{ 4, 5, 6 });
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(iterator1);

        Object result = expression.computeValue(context);

        assertTrue(result instanceof Boolean);
        assertFalse((Boolean) result);
    }

    private static class CoreOperationRelationalExpressionMock extends CoreOperationRelationalExpression {

        public CoreOperationRelationalExpressionMock(Expression[] args) {
            super(args);
        }

        @Override
        protected boolean evaluateCompare(int compare) {
            return compare == 0;
        }
    }

    private static class IteratorMock implements Iterator<Object> {

        private Object[] array;
        private int index;

        public IteratorMock(Object[] array) {
            this.array = array;
            this.index = 0;
        }

        @Override
        public boolean hasNext() {
            return index < array.length;
        }

        @Override
        public Object next() {
            return array[index++];
        }
    }
}