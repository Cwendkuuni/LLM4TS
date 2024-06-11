package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.junit.Test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class CoreOperationCompareTest {

    @Test
    public void testEqualNullEqualsNull() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(null),
            new ConstantExpression(null)
        );
        assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualBooleanTrueAndTrue() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(true),
            new ConstantExpression(true)
        );
        assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualBooleanTrueAndFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(true),
            new ConstantExpression(false)
        );
        assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualBooleanFalseAndFalse() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(false),
            new ConstantExpression(false)
        );
        assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualBooleanTrueAndNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(true),
            new ConstantExpression(new Double(42.0))
        );
        assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualStringAndString() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression("foo"),
            new ConstantExpression("foo")
        );
        assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualStringAndNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression("foo"),
            new ConstantExpression(new Double(42.0))
        );
        assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualNumberAndString() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(new Double(42.0)),
            new ConstantExpression("foo")
        );
        assertFalse(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualNumberAndNumber() {
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new ConstantExpression(new Double(42.0)),
            new ConstantExpression(new Double(42.0))
        );
        assertTrue(coreOperationCompare.equal(new EvalContext(), null, null));
    }

    @Test
    public void testEqualCollectionWithSameElements() {
        Collection<Object> collection = new HashSet<>();
        collection.add("foo");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$a"),
            new ConstantExpression(collection)
        );
        EvalContext context = new EvalContext(null, null, null, null);
        context.setVariable("a", collection);
        assertTrue(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testEqualCollectionWithDifferentElements() {
        Collection<Object> collectionA = new HashSet<>();
        collectionA.add("foo");
        Collection<Object> collectionB = new HashSet<>();
        collectionB.add("bar");
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$a"),
            new ConstantExpression(collectionB)
        );
        EvalContext context = new EvalContext(null, null, null, null);
        context.setVariable("a", collectionA);
        assertFalse(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testContainsIteratorWithSameElements() {
        Object[] elements = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        Iterator<Object> iterator = Arrays.asList(elements).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$a"),
            new ConstantExpression(iterator)
        );
        EvalContext context = new EvalContext(null, null, null, null);
        context.setVariable("a", Arrays.asList(elements).iterator());
        assertTrue(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testContainsIteratorWithDifferentElements() {
        Object[] elementsA = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        Iterator<Object> iteratorA = Arrays.asList(elementsA).iterator();
        Object[] elementsB = new Object[]{new Integer(1), new Integer(3), new Integer(4)};
        Iterator<Object> iteratorB = Arrays.asList(elementsB).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$a"),
            new ConstantExpression(iteratorB)
        );
        EvalContext context = new EvalContext(null, null, null, null);
        context.setVariable("a", iteratorA);
        assertFalse(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testFindMatch() {
        Object[] elementsA = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        Iterator<Object> iteratorA = Arrays.asList(elementsA).iterator();
        Object[] elementsB = new Object[]{new Integer(1), new Integer(3), new Integer(4)};
        Iterator<Object> iteratorB = Arrays.asList(elementsB).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$a"),
            new VariableExpression("$b")
        );
        EvalContext context = new EvalContext(null, null, null, null);
        context.setVariable("a", iteratorA);
        context.setVariable("b", iteratorB);
        assertTrue(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testFindMatchDifferentCollectionSizes() {
        Object[] elementsA = new Object[]{new Integer(1), new Integer(2), new Integer(3)};
        Iterator<Object> iteratorA = Arrays.asList(elementsA).iterator();
        Object[] elementsB = new Object[]{new Integer(1), new Integer(3)};
        Iterator<Object> iteratorB = Arrays.asList(elementsB).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$a"),
            new VariableExpression("$b")
        );
        EvalContext context = new EvalContext(null, null, null, null);
        context.setVariable("a", iteratorA);
        context.setVariable("b", iteratorB);
        assertFalse(coreOperationCompare.equal(context, null, null));
    }

    @Test
    public void testFindMatchNullIterator() {
        Object[] elementsA = null;
        Iterator<Object> iteratorA = Arrays.asList(elementsA).iterator();
        Object[] elementsB = new Object[]{new Integer(1), new Integer(3)};
        Iterator<Object> iteratorB = Arrays.asList(elementsB).iterator();
        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
            new VariableExpression("$a"),
            new VariableExpression("$b")
        );
        EvalContext context = new EvalContext(null, null, null, null);
        context.setVariable("a", iteratorA);
        context.setVariable("b", iteratorB);
        assertFalse(coreOperationCompare.equal(context, null, null));
    }
}