package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        List<Object> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Object> list2 = new ArrayList<>(Arrays.asList(1, 2, 3));
        assertTrue(operation.equal(null, new ConstantExpression(list1), new ConstantExpression(list2)));

        Pointer pointer1 = new Pointer() {
            @Override
            public Object getValue() {
                return "pointer1";
            }
        };
        Pointer pointer2 = new Pointer() {
            @Override
            public Object getValue() {
                return "pointer2";
            }
        };
        assertTrue(operation.equal(null, new ConstantExpression(pointer1), new ConstantExpression(pointer1)));
        assertFalse(operation.equal(null, new ConstantExpression(pointer1), new ConstantExpression(pointer2)));

        assertFalse(operation.equal(null, new ConstantExpression("string1"), new ConstantExpression("string2")));
    }

    @Test
    public void testContains() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        List<Object> list = new ArrayList<>(Arrays.asList(1, 2, 3));
        Iterator iterator = list.iterator();
        assertTrue(operation.contains(iterator, 2));
        assertFalse(operation.contains(iterator, 4));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        List<Object> list1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        List<Object> list2 = new ArrayList<>(Arrays.asList(3, 4, 5));
        Iterator iterator1 = list1.iterator();
        Iterator iterator2 = list2.iterator();

        assertTrue(operation.findMatch(iterator1, iterator2));

        list2.remove(5);
        assertFalse(operation.findMatch(iterator1, iterator2));
    }

    @Test
    public void testEqualValue() {
        CoreOperationCompare operation = new CoreOperationCompare(null, null);

        assertTrue(operation.equal("string", "string"));
        assertTrue(operation.equal(123, 123));
        assertTrue(operation.equal(true, true));
    }

    @Test(expected = NoSuchElementException.class)
    public void testEqualWithException() {
        CoreOperationCompare operation = new CoreOperationCompare(null, new ConstantExpression(null));
        operation.equal(null, new ConstantExpression(new InitialContext()), new ConstantExpression(new SelfContext()));
    }
}