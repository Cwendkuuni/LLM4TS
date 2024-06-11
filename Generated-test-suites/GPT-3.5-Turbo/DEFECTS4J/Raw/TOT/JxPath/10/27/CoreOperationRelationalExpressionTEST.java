import org.junit.Test;
import static org.junit.Assert.*;

public class CoreOperationRelationalExpressionTest {

    @Test
    public void testComputeValue() {
        // Test for greater than operator
        CoreOperationRelationalExpression expression = new ConcreteOperationRelationalExpression();
        EvalContext context = new EvalContext();
        context.setSingleNodePointer(10);
        expression.computeValue(context);
        assertFalse((Boolean) expression.computeValue(context));

        context.setSingleNodePointer(20);
        assertTrue((Boolean) expression.computeValue(context));

        // Test for greater than or equal to operator
        expression = new ConcreteOperationRelationalExpression();
        context.setSingleNodePointer(5);
        expression.computeValue(context);
        assertFalse((Boolean) expression.computeValue(context));

        context.setSingleNodePointer(10);
        assertTrue((Boolean) expression.computeValue(context));

        // Test for less than operator
        expression = new ConcreteOperationRelationalExpression();
        context.setSingleNodePointer(20);
        expression.computeValue(context);
        assertFalse((Boolean) expression.computeValue(context));

        context.setSingleNodePointer(10);
        assertTrue((Boolean) expression.computeValue(context));

        // Test for less than or equal to operator
        expression = new ConcreteOperationRelationalExpression();
        context.setSingleNodePointer(10);
        expression.computeValue(context);
        assertTrue((Boolean) expression.computeValue(context));

        context.setSingleNodePointer(20);
        assertFalse((Boolean) expression.computeValue(context));
    }

    @Test
    public void testCompute() {
        // Test for both objects not being iterators
        CoreOperationRelationalExpression expression = new ConcreteOperationRelationalExpression();
        assertFalse(expression.compute(10, 20));
        assertFalse(expression.compute(20, 10));
        assertTrue(expression.compute(10, 10));

        // Test for left object being an iterator
        expression = new ConcreteOperationRelationalExpression();
        Iterator<String> iterator1 = new ArrayList<String>().iterator();
        iterator1.add("a");
        iterator1.add("b");
        iterator1.add("c");
        assertTrue(expression.compute(iterator1, "c"));
        assertFalse(expression.compute(iterator1, "d"));

        // Test for right object being an iterator
        expression = new ConcreteOperationRelationalExpression();
        Iterator<String> iterator2 = new ArrayList<String>().iterator();
        iterator2.add("x");
        iterator2.add("y");
        iterator2.add("z");
        assertFalse(expression.compute("w", iterator2));
        assertTrue(expression.compute("y", iterator2));

        // Test for both objects being iterators
        expression = new ConcreteOperationRelationalExpression();
        Iterator<String> iterator3 = new ArrayList<String>().iterator();
        iterator3.add("a");
        iterator3.add("b");
        iterator3.add("c");

        Iterator<String> iterator4 = new ArrayList<String>().iterator();
        iterator4.add("x");
        iterator4.add("y");
        iterator4.add("z");

        assertTrue(expression.compute(iterator3, iterator4));
        assertFalse(expression.compute(iterator3, iterator2));
    }

    @Test
    public void testReduce() {
        CoreOperationRelationalExpression expression = new ConcreteOperationRelationalExpression();
        SelfContext selfContext = new SelfContext();
        Collection<String> collection = new ArrayList<String>();
        collection.add("a");
        collection.add("b");
        collection.add("c");

        Iterator<String> iterator = expression.reduce(selfContext);
        assertEquals("SelfContext", iterator);
        iterator = expression.reduce(collection);
        assertEquals("Iterator", iterator);
        iterator = expression.reduce(iterator);
        assertEquals("Iterator", iterator);
    }

    @Test
    public void testContainsMatch() {
        CoreOperationRelationalExpression expression = new ConcreteOperationRelationalExpression();
        Iterator<String> iterator = new ArrayList<String>().iterator();
        iterator.add("a");
        iterator.add("b");
        iterator.add("c");

        assertTrue(expression.containsMatch(iterator, "b"));
        assertFalse(expression.containsMatch(iterator, "x"));
    }

    @Test
    public void testFindMatch() {
        CoreOperationRelationalExpression expression = new ConcreteOperationRelationalExpression();
        Iterator<String> iterator1 = new ArrayList<String>().iterator();
        iterator1.add("a");
        iterator1.add("b");
        iterator1.add("c");

        Iterator<String> iterator2 = new ArrayList<String>().iterator();
        iterator2.add("x");
        iterator2.add("y");
        iterator2.add("z");

        assertTrue(expression.findMatch(iterator1, iterator2));

        iterator2.add("b");
        iterator2.add("c");
        assertTrue(expression.findMatch(iterator1, iterator2));

        iterator2.add("m");
        iterator2.add("n");
        assertFalse(expression.findMatch(iterator1, iterator2));
    }
}

class ConcreteOperationRelationalExpression extends CoreOperationRelationalExpression {
    public ConcreteOperationRelationalExpression() {
        super(null);
    }

    @Override
    protected boolean evaluateCompare(int compare) {
        // Implementation not required for test
        return false;
    }

    @Override
    protected int getPrecedence() {
        // Implementation not required for test
        return 0;
    }

    @Override
    protected boolean isSymmetric() {
        // Implementation not required for test
        return false;
    }
}