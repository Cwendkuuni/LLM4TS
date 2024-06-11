package org.apache.commons.jxpath.ri.compiler;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.InfoSetUtil;
import org.apache.commons.jxpath.ri.axes.InitialContext;
import org.apache.commons.jxpath.ri.axes.SelfContext;

public class CoreOperationCompareTest {

    @Test
    public void testEqual() {
        Object[] l = {1, "foo", new Boolean(true), new Pointer("1")};
        Object[] r = {1, "foo", new Boolean(true), new Pointer("1")};
        assertTrue(CoreOperationCompare.equal(null, null, null));
        assertTrue(CoreOperationCompare.equal(null, null, new Object()));
        assertTrue(CoreOperationCompare.equal(null, new Object(), null));
        assertTrue(CoreOperationCompare.equal(null, new Object(), new Object()));

        for (int i = 0; i < l.length; i++) {
            for (int j = 0; j < r.length; j++) {
                assertTrue(CoreOperationCompare.equal(l[i], r[j]));
            }
        }

        EvalContext context = new EvalContext(new Object());
        assertTrue(CoreOperationCompare.equal(
            new CoreOperationConstant(1),
            new CoreOperationConstant(1),
            context));
        assertTrue(CoreOperationCompare.equal(
            new CoreOperationConstant("foo"),
            new CoreOperationConstant("foo"),
            context));
        assertTrue(CoreOperationCompare.equal(
            new CoreOperationConstant(true),
            new CoreOperationConstant(true),
            context));
        assertTrue(CoreOperationCompare.equal(
            new CoreOperationConstant(new Pointer("1")),
            new CoreOperationConstant(new Pointer("1")),
            context));

        assertTrue(CoreOperationCompare.equal(
            new CoreOperationConstant(new HashSet<>(Arrays.asList(1, 2))),
            new CoreOperationConstant(new HashSet<>(Arrays.asList(1, 2))),
            context));

        List<Integer> list1 = Arrays.asList(1, 2, 3);
        List<Integer> list2 = Arrays.asList(1, 2, 3);
        assertTrue(CoreOperationCompare.equal(
            new CoreOperationConstant(list1.iterator()),
            new CoreOperationConstant(list2.iterator()),
            context));

        List<Integer> list3 = Arrays.asList(1, 2, 3);
        List<String> list4 = Arrays.asList("1", "2", "3");
        assertFalse(CoreOperationCompare.equal(
            new CoreOperationConstant(list1.iterator()),
            new CoreOperationConstant(list4.iterator()),
            context));

        assertTrue(CoreOperationCompare.equal(
            new CoreOperationCall("not", new CoreOperationConstant(true)),
            new CoreOperationConstant(false),
            context));

        assertTrue(CoreOperationCompare.equal(
            new CoreOperationCall("not", new CoreOperationConstant(false)),
            new CoreOperationConstant(true),
            context));

        Pointer p1 = new Pointer("1");
        Pointer p2 = new Pointer("1");
        assertTrue(CoreOperationCompare.equal(p1, p2));

        Pointer p3 = new Pointer("1");
        Pointer p4 = new Pointer("2");
        assertFalse(CoreOperationCompare.equal(p3, p4));

        InitialContext initialContext = new InitialContext();
        SelfContext selfContext = new SelfContext();
        assertTrue(CoreOperationCompare.equal(initialContext, initialContext));
        assertTrue(CoreOperationCompare.equal(initialContext, selfContext));
        assertFalse(CoreOperationCompare.equal(initialContext, p1));
        assertFalse(CoreOperationCompare.equal(selfContext, p1));

        assertTrue(CoreOperationCompare.equal(p1, p1));
        assertFalse(CoreOperationCompare.equal(p1, p2));
    }

    @Test
    public void testContains() {
        Iterator<?> it = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 1;
            }
        };
        Object[] values = {1, 2, 3};

        assertTrue(CoreOperationCompare.contains(it, null));
        assertFalse(CoreOperationCompare.contains(it, new Object()));
        for (Object value : values) {
            assertTrue(CoreOperationCompare.contains(it, value));
        }
        assertFalse(CoreOperationCompare.contains(it, new Object()));

        Iterator<?> it2 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return 2;
            }
        };
        assertFalse(CoreOperationCompare.contains(it, new Integer(1)));
        assertFalse(CoreOperationCompare.contains(it, new Integer(3)));
        assertFalse(CoreOperationCompare.contains(it, new Integer(4)));
        assertFalse(CoreOperationCompare.contains(it, new String("2")));
        assertFalse(CoreOperationCompare.contains(it, new String("3")));
        assertFalse(CoreOperationCompare.contains(it, new String("4")));

        Iterator<?> it3 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };
        Iterator<?> it4 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };
        assertTrue(CoreOperationCompare.findMatch(it3, it4));

        Iterator<?> it5 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };
        Iterator<?> it6 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(2);
            }
        };
        assertFalse(CoreOperationCompare.findMatch(it5, it6));

        Iterator<?> it7 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(1);
            }
        };
        Iterator<?> it8 = new Iterator<Object>() {
            @Override
            public boolean hasNext() {
                return true;
            }

            @Override
            public Object next() {
                return new Integer(3);
            }
        };
        assertFalse(CoreOperationCompare.findMatch(it7, it8));
    }

    @Test
    public void testFindMatch() {
        HashSet<Object> set1 = new HashSet<>();
        set1.add(1);
        set1.add(2);
        HashSet<Object> set2 = new HashSet<>();
        set2.add(1);
        set2.add(3);

        assertTrue(CoreOperationCompare.findMatch(set1.iterator(), null));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), new Object()));
        assertTrue(CoreOperationCompare.findMatch(set1.iterator(), 1));
        assertTrue(CoreOperationCompare.findMatch(set1.iterator(), 2));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), 3));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), "1"));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), "2"));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), "3"));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), new String("1")));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), new String("2")));
        assertFalse(CoreOperationCompare.findMatch(set1.iterator(), new String("3")));

        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), null));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), new Object()));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), 1));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), 2));
        assertTrue(CoreOperationCompare.findMatch(set2.iterator(), 1));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), 3));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), "1"));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), "2"));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), "3"));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), new String("1")));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), new String("2")));
        assertFalse(CoreOperationCompare.findMatch(set2.iterator(), new String("3")));
    }

    @Test
    public void testEqualWithDifferentTypes() {
        assertFalse(CoreOperationCompare.equal(1, "1"));
        assertFalse(CoreOperationCompare.equal("1", 1));
        assertFalse(CoreOperationCompare.equal(1, new Boolean(true)));
        assertFalse(CoreOperationCompare.equal(new Boolean(true), 1));
        assertFalse(CoreOperationCompare.equal(1, new Pointer("1")));
        assertFalse(CoreOperationCompare.equal(new Pointer("1"), 1));
        assertFalse(CoreOperationCompare.equal(1, new HashSet<>(Arrays.asList(1))));
        assertFalse(CoreOperationCompare.equal(new HashSet<>(Arrays.asList(1)), 1));
        assertFalse(CoreOperationCompare.equal(new CoreOperationCall("not", new CoreOperationConstant(true)), 1));
        assertFalse(CoreOperationCompare.equal(1, new CoreOperationCall("not", new CoreOperationConstant(true))));
    }
}