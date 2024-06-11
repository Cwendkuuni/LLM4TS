package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.InfoSet;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object left = new Integer(1);
        Object right = new Integer(1);
        EvalContext context = new EvalContext(new InfoSet() {});

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new ConstantExpression(left), new ConstantExpression(right));

        assertTrue(coreOperationCompare.evaluate(context));

        left = new Boolean(true);
        right = new Boolean(true);
        assertTrue(coreOperationCompare.evaluate(context));

        left = "foo";
        right = "foo";
        assertTrue(coreOperationCompare.evaluate(context));

        left = new Pointer("foo");
        right = new Pointer("foo");
        assertTrue(coreOperationCompare.evaluate(context));

        left = new Integer(1);
        right = new Integer(2);
        assertFalse(coreOperationCompare.evaluate(context));

        left = new Boolean(true);
        right = new Boolean(false);
        assertFalse(coreOperationCompare.evaluate(context));

        left = "foo";
        right = "bar";
        assertFalse(coreOperationCompare.evaluate(context));

        left = new Pointer("foo");
        right = new Pointer("bar");
        assertFalse(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testContains() {
        Iterator iterator = Arrays.asList(1, 2, 3).iterator();
        Object value = new Integer(2);

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new ConstantExpression(iterator), new ConstantExpression(value));

        assertTrue(coreOperationCompare.evaluate(new EvalContext(new InfoSet() {})));
    }

    @Test
    public void testFindMatch() {
        Iterator iterator1 = Arrays.asList("apple", "banana", "cherry").iterator();
        Iterator iterator2 = Arrays.asList("apple", "date", "cherry").iterator();

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new ConstantExpression(iterator1), new ConstantExpression(iterator2));

        assertTrue(coreOperationCompare.evaluate(new EvalContext(new InfoSet() {})));

        iterator1 = Arrays.asList("apple", "banana", "cherry").iterator();
        iterator2 = Arrays.asList("date", "cherry", "apple").iterator();

        assertFalse(coreOperationCompare.evaluate(new EvalContext(new InfoSet() {})));
    }

    @Test
    public void testEqualWithInitialContext() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator getIterator(Pointer pointer) {
                return Arrays.asList(new Pointer("apple"), new Pointer("banana")).iterator();
            }
        });

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new InitialContext(new Pointer("apple")), new InitialContext(new Pointer("apple")));

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithSelfContext() {
        EvalContext context = new EvalContext(new InfoSet() {
            @Override
            public Iterator getIterator(Pointer pointer) {
                return Arrays.asList(new Pointer("apple"), new Pointer("banana"), new Pointer("cherry")).iterator();
            }
        });

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new SelfContext(new Pointer("apple")), new SelfContext(new Pointer("apple")));

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        EvalContext context = new EvalContext(new InfoSet() {});

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new ConstantExpression(new Integer(1)), new ConstantExpression(new String("1")));

        assertTrue(coreOperationCompare.evaluate(context));
    }

    @Test
    public void testEqualWithCollections() {
        Collection collection1 = new HashSet() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        Collection collection2 = new HashSet() {{
            add(new Integer(1));
            add(new Integer(2));
            add(new Integer(3));
        }};

        CoreOperationCompare coreOperationCompare = new CoreOperationCompare(
                new ConstantExpression(collection1), new ConstantExpression(collection2));

        assertFalse(coreOperationCompare.evaluate(new EvalContext(new InfoSet() {})));

        collection1 = new HashSet() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        collection2 = new HashSet() {{
            addAll(collection1);
            add(new Integer(3));
        }};

        assertFalse(coreOperationCompare.evaluate(new EvalContext(new InfoSet() {})));

        collection1 = new HashSet() {{
            add(new Integer(1));
            add(new Integer(2));
        }};
        collection2 = new HashSet() {{
            addAll(collection2);
            remove(new Integer(3));
        }};

        assertTrue(coreOperationCompare.evaluate(new EvalContext(new InfoSet() {})));
    }
}