package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;
import org.junit.Test;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Test cases for CoreOperationCompare class.
 */
public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object l = 1;
        Object r = 1;
        assertTrue(CoreOperationCompare.equal(null, l, r));
        assertTrue(CoreOperationCompare.equal(l, l));
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));
        assertTrue(CoreOperationCompare.equal(l, null));
        assertTrue(CoreOperationCompare.equal(r, null));

        l = "foo";
        r = "foo";
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));

        l = new Integer(1);
        r = new Integer(1);
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));

        l = new Double(1.0);
        r = new Double(1.0);
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));

        l = new Boolean(true);
        r = new Boolean(true);
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));

        l = new Pointer("", null, null);
        r = new Pointer("", null, null);
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));

        l = new Pointer("foo", null, null);
        r = new Pointer("foo", null, null);
        assertTrue(CoreOperationCompare.equal(l, r));
        assertTrue(CoreOperationCompare.equal(r, l));

        l = new Pointer("foo", null, null);
        r = new Pointer("bar", null, null);
        assertFalse(CoreOperationCompare.equal(l, r));
        assertFalse(CoreOperationCompare.equal(r, l));

        l = new InitialContext();
        r = new InitialContext();
        assertFalse(CoreOperationCompare.equal(l, r));
        assertFalse(CoreOperationCompare.equal(r, l));

        l = new SelfContext();
        r = new SelfContext();
        assertFalse(CoreOperationCompare.equal(l, r));
        assertFalse(CoreOperationCompare.equal(r, l));

        l = new String("foo");
        r = new String("bar");
        assertFalse(CoreOperationCompare.equal(l, r));
        assertFalse(CoreOperationCompare.equal(r, l));

        l = new Boolean(true);
        r = new Boolean(false);
        assertFalse(CoreOperationCompare.equal(l, r));
        assertFalse(CoreOperationCompare.equal(r, l));

        l = new Integer(1);
        r = new Integer(2);
        assertFalse(CoreOperationCompare.equal(l, r));
        assertFalse(CoreOperationCompare.equal(r, l));

        l = new Double(1.0);
        r = new Double(2.0);
        assertFalse(CoreOperationCompare.equal(l, r));
        assertFalse(CoreOperationCompare.equal(r, l));
    }

    @Test
    public void testContains() {
        List list = Arrays.asList(new Object[]{"foo", "bar", "baz"});
        Iterator it = list.iterator();

        assertTrue(CoreOperationCompare.contains(it, null));
        assertTrue(CoreOperationCompare.contains(it, "foo"));
        assertTrue(CoreOperationCompare.contains(it, "bar"));
        assertTrue(CoreOperationCompare.contains(it, "baz"));
        assertFalse(CoreOperationCompare.contains(it, "qux"));
        assertFalse(CoreOperationCompare.contains(it, new Object(){
            @Override
            public String toString() {
                return "qux";
            }
        }));

        assertFalse(CoreOperationCompare.contains(null, null));
        assertFalse(CoreOperationCompare.contains(null, "foo"));
        assertFalse(CoreOperationCompare.contains("foo", null));
    }

    @Test
    public void testFindMatch() {
        List list1 = Arrays.asList(new Object[]{"foo", "bar", "baz"});
        List list2 = Arrays.asList(new Object[]{"foo", "qux", "baz"});

        HashSet set = new HashSet(list1);

        assertTrue(CoreOperationCompare.findMatch(list1.iterator(), list2.iterator()));
        assertFalse(CoreOperationCompare.findMatch(list2.iterator(), list1.iterator()));

        assertTrue(CoreOperationCompare.findMatch(list1.iterator(), list1.iterator()));
        assertTrue(CoreOperationCompare.findMatch(list2.iterator(), list2.iterator()));
        assertFalse(CoreOperationCompare.findMatch(list1.iterator(), list2.iterator()));
        assertFalse(CoreOperationCompare.findMatch(list2.iterator(), list1.iterator()));

        assertTrue(CoreOperationCompare.findMatch(list1.iterator(), null));
        assertFalse(CoreOperationCompare.findMatch(null, list1.iterator()));
        assertFalse(CoreOperationCompare.findMatch(list1.iterator(), list2.iterator()));
    }

    @Test
    public void testEqualWithIterators() {
        List list1 = Arrays.asList(new Object[]{"foo", "bar", "baz"});
        List list2 = Arrays.asList(new Object[]{"foo", "qux", "baz"});

        Iterator it1 = list1.iterator();
        Iterator it2 = list2.iterator();

        assertFalse(CoreOperationCompare.equal(it1, it2));
        assertFalse(CoreOperationCompare.equal(it2, it1));

        it1 = list1.iterator();
        it2 = list1.iterator();
        assertTrue(CoreOperationCompare.equal(it1, it2));
        assertTrue(CoreOperationCompare.equal(it2, it1));

        it1 = list1.iterator();
        it2 = list2.iterator();
        assertFalse(CoreOperationCompare.equal(it1, it2));
        assertFalse(CoreOperationCompare.equal(it2, it1));

        it1 = null;
        it2 = list1.iterator();
        assertFalse(CoreOperationCompare.equal(it1, it2));
        assertFalse(CoreOperationCompare.equal(it2, it1));

        it1 = list1.iterator();
        it2 = null;
        assertFalse(CoreOperationCompare.equal(it1, it2));
        assertFalse(CoreOperationCompare.equal(it2, it1));
    }
}