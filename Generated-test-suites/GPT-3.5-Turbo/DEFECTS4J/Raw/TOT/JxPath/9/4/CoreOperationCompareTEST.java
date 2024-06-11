import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        // Typical use case
        Expression arg1 = new TestExpression("value1");
        Expression arg2 = new TestExpression("value1");
        CoreOperationCompare operation = new TestCoreOperationCompare(arg1, arg2);
        assertTrue(operation.equal(context, arg1, arg2));

        // Edge case - comparing two null values
        arg1 = new TestExpression(null);
        arg2 = new TestExpression(null);
        operation = new TestCoreOperationCompare(arg1, arg2);
        assertTrue(operation.equal(context, arg1, arg2));

        // Edge case - comparing two different objects
        arg1 = new TestExpression("value1");
        arg2 = new TestExpression("value2");
        operation = new TestCoreOperationCompare(arg1, arg2);
        assertFalse(operation.equal(context, arg1, arg2));
    }

    @Test
    public void testContains() {
        // Typical use case - iterator contains the value
        Expression arg1 = new TestExpression(new String[]{"value1", "value2", "value3"});
        Expression arg2 = new TestExpression("value2");
        CoreOperationCompare operation = new TestCoreOperationCompare(arg1, arg2);
        assertTrue(operation.contains(context, arg1, arg2));

        // Typical use case - iterator does not contain the value
        arg1 = new TestExpression(new String[]{"value1", "value2", "value3"});
        arg2 = new TestExpression("value4");
        operation = new TestCoreOperationCompare(arg1, arg2);
        assertFalse(operation.contains(context, arg1, arg2));
    }

    @Test
    public void testFindMatch() {
        // Typical use case - the match is found
        Expression arg1 = new TestExpression(new String[]{"value1", "value2", "value3"});
        Expression arg2 = new TestExpression(new String[]{"value4", "value2", "value6"});
        CoreOperationCompare operation = new TestCoreOperationCompare(arg1, arg2);
        assertTrue(operation.findMatch(context, arg1, arg2));

        // Typical use case - the match is not found
        arg1 = new TestExpression(new String[]{"value1", "value2", "value3"});
        arg2 = new TestExpression(new String[]{"value4", "value5", "value6"});
        operation = new TestCoreOperationCompare(arg1, arg2);
        assertFalse(operation.findMatch(context, arg1, arg2));
    }

    @Test
    public void testEqualObjects() {
        // Typical use case - two pointers are equal
        Object l = new TestPointer("value1");
        Object r = new TestPointer("value1");
        CoreOperationCompare operation = new TestCoreOperationCompare(null, null);
        assertTrue(operation.equal(l, r));

        // Typical use case - two pointers are not equal
        l = new TestPointer("value1");
        r = new TestPointer("value2");
        operation = new TestCoreOperationCompare(null, null);
        assertFalse(operation.equal(l, r));

        // Typical use case - comparing a pointer to null
        l = new TestPointer("value1");
        r = null;
        operation = new TestCoreOperationCompare(null, null);
        assertFalse(operation.equal(l, r));

        // Typical use case - comparing two null values
        l = null;
        r = null;
        operation = new TestCoreOperationCompare(null, null);
        assertTrue(operation.equal(l, r));

        // Typical use case - comparing two boolean values
        l = true;
        r = true;
        operation = new TestCoreOperationCompare(null, null);
        assertTrue(operation.equal(l, r));

        // Typical use case - comparing two double values
        l = 1.0;
        r = 1.0;
        operation = new TestCoreOperationCompare(null, null);
        assertTrue(operation.equal(l, r));

        // Typical use case - comparing two string values
        l = "value1";
        r = "value1";
        operation = new TestCoreOperationCompare(null, null);
        assertTrue(operation.equal(l, r));

        // Typical use case - comparing two different string values
        l = "value1";
        r = "value2";
        operation = new TestCoreOperationCompare(null, null);
        assertFalse(operation.equal(l, r));
    }

    private class TestExpression extends Expression {
        private Object value;

        public TestExpression(Object value) {
            this.value = value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }
    }

    private class TestCoreOperationCompare extends CoreOperationCompare {
        public TestCoreOperationCompare(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }
    }

    private class TestPointer extends Pointer {
        public TestPointer(Object value) {
            super(value);
        }

        @Override
        public Object getValue() {
            return value;
        }
    }
}