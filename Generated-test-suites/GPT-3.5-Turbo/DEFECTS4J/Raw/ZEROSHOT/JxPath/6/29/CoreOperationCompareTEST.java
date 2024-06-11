import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {

    private CoreOperationCompare operationCompare;

    @Before
    public void setUp() {
        operationCompare = new CoreOperationCompareMock();
    }

    @Test
    public void testEqual() {
        Assert.assertTrue(operationCompare.equal(new EvalContextMock(), new ExpressionMock(), new ExpressionMock()));
    }

    @Test
    public void testEqualWithNullValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(null);
        ExpressionMock expressionMock2 = new ExpressionMock(null);

        Assert.assertTrue(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithDifferentValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock("value1");
        ExpressionMock expressionMock2 = new ExpressionMock("value2");

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(1);
        ExpressionMock expressionMock2 = new ExpressionMock("1");

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithCollectionsAndMatch() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(List.of("value1", "value2"));
        ExpressionMock expressionMock2 = new ExpressionMock(List.of("value3", "value2"));

        Assert.assertTrue(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithCollectionsAndNoMatch() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(List.of("value1", "value2"));
        ExpressionMock expressionMock2 = new ExpressionMock(List.of("value3", "value4"));

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testContains() {
        IteratorMock iteratorMock = new IteratorMock(List.of("value1", "value2").iterator());
        Assert.assertTrue(operationCompare.contains(iteratorMock, "value2"));
        Assert.assertFalse(operationCompare.contains(iteratorMock, "value3"));
    }

    @Test
    public void testFindMatch() {
        IteratorMock lit = new IteratorMock(List.of("value1", "value2").iterator());
        IteratorMock rit = new IteratorMock(List.of("value2", "value3").iterator());

        Assert.assertTrue(operationCompare.findMatch(lit, rit));
    }

    @Test
    public void testEqualWithPointers() {
        EvalContext context = new EvalContextMock();
        Pointer pointer1 = new PointerMock("value1");
        Pointer pointer2 = new PointerMock("value1");

        ExpressionMock expressionMock1 = new ExpressionMock(pointer1);
        ExpressionMock expressionMock2 = new ExpressionMock(pointer2);

        Assert.assertTrue(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithDifferentPointers() {
        EvalContext context = new EvalContextMock();
        Pointer pointer1 = new PointerMock("value1");
        Pointer pointer2 = new PointerMock("value2");

        ExpressionMock expressionMock1 = new ExpressionMock(pointer1);
        ExpressionMock expressionMock2 = new ExpressionMock(pointer2);

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithPointersAndNullValues() {
        EvalContext context = new EvalContextMock();
        Pointer pointer1 = new PointerMock(null);
        Pointer pointer2 = new PointerMock(null);

        ExpressionMock expressionMock1 = new ExpressionMock(pointer1);
        ExpressionMock expressionMock2 = new ExpressionMock(pointer2);

        Assert.assertTrue(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithDifferentClasses() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock("value1");
        ExpressionMock expressionMock2 = new ExpressionMock(1);

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithBooleanValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(true);
        ExpressionMock expressionMock2 = new ExpressionMock(Boolean.TRUE);

        Assert.assertTrue(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithDifferentBooleanValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(true);
        ExpressionMock expressionMock2 = new ExpressionMock(Boolean.FALSE);

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithNumberValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(1);
        ExpressionMock expressionMock2 = new ExpressionMock(1);

        Assert.assertTrue(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithDifferentNumberValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock(1);
        ExpressionMock expressionMock2 = new ExpressionMock(2);

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithStringValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock("value");
        ExpressionMock expressionMock2 = new ExpressionMock("value");

        Assert.assertTrue(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithDifferentStringValues() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock("value1");
        ExpressionMock expressionMock2 = new ExpressionMock("value2");

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    @Test
    public void testEqualWithStringValuesAndNull() {
        EvalContext context = new EvalContextMock();
        ExpressionMock expressionMock1 = new ExpressionMock("value");
        ExpressionMock expressionMock2 = new ExpressionMock(null);

        Assert.assertFalse(operationCompare.equal(context, expressionMock1, expressionMock2));
    }

    private class CoreOperationCompareMock extends CoreOperationCompare {

        public CoreOperationCompareMock() {
            super(null, null);
        }

        @Override
        protected boolean equal(EvalContext context, Expression left, Expression right) {
            return true;
        }
    }

    private class EvalContextMock extends EvalContext {

        @Override
        public Object getSingleNodePointer() {
            return null;
        }

        @Override
        public List getSelectedNodes() {
            return null;
        }

        @Override
        protected EvalContext createSingleNodeContext(Object child) {
            return null;
        }

        @Override
        protected boolean isNodeMatching(Object element) {
            return false;
        }

        @Override
        protected boolean isNamespaceContextChanged() {
            return false;
        }

        @Override
        protected boolean isPositionChanged() {
            return false;
        }

        @Override
        protected boolean positionMatches(Object element) {
            return false;
        }

        @Override
        protected boolean matchesNode(Object element) {
            return false;
        }

        @Override
        protected boolean matches(Object element) {
            return false;
        }
    }

    private class ExpressionMock extends Expression {

        private Object value;

        public ExpressionMock() {
            super(null);
        }

        public ExpressionMock(Object value) {
            super(null);
            this.value = value;
        }

        @Override
        public Object compute(EvalContext context) {
            return value;
        }
    }

    private class IteratorMock implements Iterator {

        private final Iterator iterator;

        public IteratorMock(Iterator iterator) {
            this.iterator = iterator;
        }

        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        @Override
        public Object next() {
            return iterator.next();
        }
    }

    private class PointerMock implements Pointer {

        private final Object value;

        public PointerMock(Object value) {
            this.value = value;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object getBaseValue() {
            return null;
        }

        @Override
        public boolean isActual() {
            return false;
        }

        @Override
        public boolean isCollection() {
            return false;
        }

        @Override
        public Iterator iteratePointers() {
            return null;
        }
    }

}