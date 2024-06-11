package org.apache.commons.jxpath.ri.compiler;

import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class CoreOperationCompareTest {
    private CoreOperationCompare operationCompare;
    private Expression left;
    private Expression right;

    @Before
    public void setUp() {
        left = new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        right = new Expression() {
            @Override
            public Object compute(EvalContext evalContext) {
                return null;
            }
        };

        operationCompare = new CoreOperationCompare(left, right) {
            @Override
            protected boolean equal(EvalContext context, Expression left, Expression right) {
                return false;
            }

            @Override
            protected boolean contains(Iterator it, Object value) {
                return false;
            }

            @Override
            protected boolean findMatch(Iterator lit, Iterator rit) {
                return false;
            }

            @Override
            protected boolean equal(Object l, Object r) {
                return false;
            }
        };
    }

    @Test
    public void testEqual_SingleObjects_ShouldReturnTrue() {
        Object obj1 = new Object();
        Object obj2 = obj1;

        assertTrue(operationCompare.equal(obj1, obj2));
    }

    @Test
    public void testEqual_CompareBooleans_ShouldReturnTrue() {
        boolean bool1 = true;
        boolean bool2 = true;

        assertTrue(operationCompare.equal(bool1, bool2));
    }

    @Test
    public void testEqual_CompareIntegers_ShouldReturnTrue() {
        int int1 = 10;
        int int2 = 10;

        assertTrue(operationCompare.equal(int1, int2));
    }

    @Test
    public void testEqual_CompareStrings_ShouldReturnTrue() {
        String str1 = "test";
        String str2 = "test";

        assertTrue(operationCompare.equal(str1, str2));
    }

    @Test
    public void testEqual_ComparePointers_ShouldReturnTrue() {
        Pointer pointer1 = new Pointer() {
            @Override
            public Object getValue() {
                return null;
            }

            @Override
            public boolean isLeaf() {
                return false;
            }
        };
        Pointer pointer2 = pointer1;

        assertTrue(operationCompare.equal(pointer1, pointer2));
    }

    @Test
    public void testEqual_CompareCollectionsWithSameValues_ShouldReturnTrue() {
        Collection<Integer> collection1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collection<Integer> collection2 = new ArrayList<>(Arrays.asList(1, 2, 3));

        assertTrue(operationCompare.equal(collection1, collection2));
    }

    @Test
    public void testEqual_CompareCollectionsWithDifferentValues_ShouldReturnFalse() {
        Collection<Integer> collection1 = new ArrayList<>(Arrays.asList(1, 2, 3));
        Collection<Integer> collection2 = new ArrayList<>(Arrays.asList(4, 5, 6));

        assertFalse(operationCompare.equal(collection1, collection2));
    }

    @Test
    public void testEqual_CompareIteratorsWithSameValues_ShouldReturnTrue() {
        Iterator<Integer> iterator1 = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();
        Iterator<Integer> iterator2 = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();

        assertTrue(operationCompare.equal(iterator1, iterator2));
    }

    @Test
    public void testEqual_CompareIteratorsWithDifferentValues_ShouldReturnFalse() {
        Iterator<Integer> iterator1 = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();
        Iterator<Integer> iterator2 = new ArrayList<>(Arrays.asList(4, 5, 6)).iterator();

        assertFalse(operationCompare.equal(iterator1, iterator2));
    }

    @Test
    public void testContains_CollectionContainsValue_ShouldReturnTrue() {
        Collection<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3));
        Object value = 2;

        assertTrue(operationCompare.contains(collection.iterator(), value));
    }

    @Test
    public void testContains_CollectionDoesNotContainValue_ShouldReturnFalse() {
        Collection<Integer> collection = new ArrayList<>(Arrays.asList(1, 2, 3));
        Object value = 4;

        assertFalse(operationCompare.contains(collection.iterator(), value));
    }

    @Test
    public void testFindMatch_IteratorsContainMatchingValues_ShouldReturnTrue() {
        Iterator<Integer> iterator1 = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();
        Iterator<Integer> iterator2 = new ArrayList<>(Arrays.asList(3, 4, 5)).iterator();

        assertTrue(operationCompare.findMatch(iterator1, iterator2));
    }

    @Test
    public void testFindMatch_IteratorsDoNotContainMatchingValues_ShouldReturnFalse() {
        Iterator<Integer> iterator1 = new ArrayList<>(Arrays.asList(1, 2, 3)).iterator();
        Iterator<Integer> iterator2 = new ArrayList<>(Arrays.asList(4, 5, 6)).iterator();

        assertFalse(operationCompare.findMatch(iterator1, iterator2));
    }
    
}