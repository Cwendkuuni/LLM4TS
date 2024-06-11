package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class CoreOperationCompareTest {
    private CoreOperationCompare coreOperationCompare;

    @Before
    public void setUp() {
        coreOperationCompare = new CoreOperationCompare(new TestExpression(), new TestExpression());
    }

    @Test
    public void testGetPrecedence() {
        assertEquals(2, coreOperationCompare.getPrecedence());
    }

    @Test
    public void testIsSymmetric() {
        assertTrue(coreOperationCompare.isSymmetric());
    }

    @Test
    public void testEqual_bothNull() {
        assertTrue(coreOperationCompare.equal(null, null));
    }

    @Test
    public void testEqual_bothNonNull() {
        assertTrue(coreOperationCompare.equal("test", "test"));
    }

    @Test
    public void testEqual_nullLeft() {
        assertFalse(coreOperationCompare.equal(null, "test"));
    }

    @Test
    public void testEqual_nullRight() {
        assertFalse(coreOperationCompare.equal("test", null));
    }

    @Test
    public void testEqual_bothPointersEqual() {
        Pointer pointer = new TestPointer("test");
        assertTrue(coreOperationCompare.equal(pointer, pointer));
    }

    @Test
    public void testEqual_bothPointersNotEqual() {
        assertFalse(coreOperationCompare.equal(new TestPointer("test1"), new TestPointer("test2")));
    }

    @Test
    public void testEqual_leftPointerEqualValue() {
        Pointer pointer = new TestPointer("test");
        assertTrue(coreOperationCompare.equal(pointer, "test"));
    }

    @Test
    public void testEqual_leftPointerNotEqualValue() {
        assertFalse(coreOperationCompare.equal(new TestPointer("test1"), "test2"));
    }

    @Test
    public void testEqual_rightPointerEqualValue() {
        Pointer pointer = new TestPointer("test");
        assertTrue(coreOperationCompare.equal("test", pointer));
    }

    @Test
    public void testEqual_rightPointerNotEqualValue() {
        assertFalse(coreOperationCompare.equal("test1", new TestPointer("test2")));
    }

    @Test
    public void testEqual_booleanValuesEqual() {
        assertTrue(coreOperationCompare.equal(true, true));
        assertTrue(coreOperationCompare.equal(false, false));
    }

    @Test
    public void testEqual_booleanValuesNotEqual() {
        assertFalse(coreOperationCompare.equal(true, false));
        assertFalse(coreOperationCompare.equal(false, true));
    }

    @Test
    public void testEqual_doubleValuesEqual() {
        assertTrue(coreOperationCompare.equal(1.0, 1.0));
        assertTrue(coreOperationCompare.equal(1.5, 1.5));
    }

    @Test
    public void testEqual_doubleValuesNotEqual() {
        assertFalse(coreOperationCompare.equal(1.0, 2.0));
        assertFalse(coreOperationCompare.equal(1.5, 2.5));
    }

    @Test
    public void testEqual_stringValuesEqual() {
        assertTrue(coreOperationCompare.equal("abc", "abc"));
        assertTrue(coreOperationCompare.equal("", ""));
    }

    @Test
    public void testEqual_stringValuesNotEqual() {
        assertFalse(coreOperationCompare.equal("abc", "def"));
        assertFalse(coreOperationCompare.equal("", " "));
    }

    @Test
    public void testEqual_collectionValuesEqual() {
        List<String> list1 = new ArrayList<>();
        list1.add("test1");
        list1.add("test2");
        List<String> list2 = new ArrayList<>();
        list2.add("test1");
        list2.add("test2");

        assertTrue(coreOperationCompare.equal(list1, list2));
    }

    @Test
    public void testEqual_collectionValuesNotEqual() {
        List<String> list1 = new ArrayList<>();
        list1.add("test1");
        list1.add("test2");
        List<String> list2 = new ArrayList<>();
        list2.add("test1");
        list2.add("test3");

        assertFalse(coreOperationCompare.equal(list1, list2));
    }

    @Test
    public void testEqual_iteratorsEqual() {
        Iterator<String> iterator1 = new TestIterator("test");
        Iterator<String> iterator2 = new TestIterator("test");

        assertTrue(coreOperationCompare.equal(iterator1, iterator2));
    }

    @Test
    public void testEqual_iteratorsNotEqual() {
        Iterator<String> iterator1 = new TestIterator("test1");
        Iterator<String> iterator2 = new TestIterator("test2");

        assertFalse(coreOperationCompare.equal(iterator1, iterator2));
    }

    @Test
    public void testContains_iteratorContainsValue() {
        Iterator<String> iterator = new TestIterator("test");

        assertTrue(coreOperationCompare.contains(iterator, "test"));
    }

    @Test
    public void testContains_iteratorDoesNotContainValue() {
        Iterator<String> iterator = new TestIterator("test");

        assertFalse(coreOperationCompare.contains(iterator, "test2"));
    }

    @Test
    public void testFindMatch_iteratorsWithMatch() {
        Iterator<String> iterator1 = new TestIterator("test1", "test2");
        Iterator<String> iterator2 = new TestIterator("test2", "test3");

        assertTrue(coreOperationCompare.findMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatch_iteratorsWithoutMatch() {
        Iterator<String> iterator1 = new TestIterator("test1", "test2");
        Iterator<String> iterator2 = new TestIterator("test3", "test4");

        assertFalse(coreOperationCompare.findMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatch_iteratorAndValueWithMatch() {
        Iterator<String> iterator = new TestIterator("test1", "test2");

        assertTrue(coreOperationCompare.findMatch(iterator, "test2"));
    }

    @Test
    public void testFindMatch_iteratorAndValueWithoutMatch() {
        Iterator<String> iterator = new TestIterator("test1", "test2");

        assertFalse(coreOperationCompare.findMatch(iterator, "test3"));
    }

    private static class TestExpression extends Expression {
        @Override
        public Object compute(EvalContext context) {
            return null;
        }
    }

    private static class TestPointer implements Pointer {
        private Object value;

        public TestPointer(Object value) {
            this.value = value;
        }

        @Override
        public Object getBaseValue() {
            return value;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public int getLength() {
            if (value instanceof Collection) {
                return ((Collection<?>) value).size();
            }
            return 0;
        }

        @Override
        public Iterator<?> iteratePointedNodes() {
            if (value instanceof Collection) {
                return ((Collection<?>) value).iterator();
            } else if (value != null) {
                return new Iterator<>() {
                    private boolean hasNext = true;

                    @Override
                    public boolean hasNext() {
                        return hasNext;
                    }

                    @Override
                    public Object next() {
                        if (!hasNext) {
                            throw new NoSuchElementException();
                        }

                        hasNext = false;
                        return value;
                    }
                };
            } else {
                return null;
            }
        }
    }

    private static class TestIterator implements Iterator<String> {
        private final List<String> items;
        private int currentIndex;

        public TestIterator(String... items) {
            this.items = new ArrayList<>();
            for (String item : items) {
                this.items.add(item);
            }
            currentIndex = 0;
        }

        @Override
        public boolean hasNext() {
            return currentIndex < items.size();
        }

        @Override
        public String next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return items.get(currentIndex++);
        }
    }
}