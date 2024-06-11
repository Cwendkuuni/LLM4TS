package org.apache.commons.jxpath.ri.compiler;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

import static org.junit.Assert.*;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        CoreOperationCompare coreOperation = new CoreOperationCompareMock(null, null);

        // Test for null values
        assertTrue(coreOperation.equal(null, null));
        assertFalse(coreOperation.equal(null, "test"));
        assertFalse(coreOperation.equal("test", null));

        // Test for boolean values
        assertTrue(coreOperation.equal(true, true));
        assertFalse(coreOperation.equal(true, false));

        // Test for numeric values
        assertTrue(coreOperation.equal(10, 10));
        assertFalse(coreOperation.equal(10, 20));

        // Test for string values
        assertTrue(coreOperation.equal("hello", "hello"));
        assertFalse(coreOperation.equal("hello", "world"));
    }

    @Test
    public void testContains() {
        CoreOperationCompare coreOperation = new CoreOperationCompareMock(null, null);

        Collection<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3));

        // Test if collection contains value
        assertTrue(coreOperation.contains(collection.iterator(), 2));
        assertFalse(coreOperation.contains(collection.iterator(), 4));
    }

    @Test
    public void testFindMatch() {
        CoreOperationCompare coreOperation = new CoreOperationCompareMock(null, null);

        Collection<Integer> collection1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collection<Integer> collection2 = new ArrayList<>(Arrays.asList(3, 4, 5));

        // Test if two collections have a matching element
        assertTrue(coreOperation.findMatch(collection1.iterator(), collection2.iterator()));
    }

    // Custom Mock class for testing
    private class CoreOperationCompareMock extends CoreOperationCompare {
        public CoreOperationCompareMock(Expression arg1, Expression arg2) {
            super(arg1, arg2);
        }

        @Override
        protected boolean equal(Object l, Object r) {
            return super.equal(l, r);
        }

        @Override
        protected boolean contains(Iterator it, Object value) {
            return super.contains(it, value);
        }

        @Override
        protected boolean findMatch(Iterator lit, Iterator rit) {
            return super.findMatch(lit, rit);
        }
    }
}