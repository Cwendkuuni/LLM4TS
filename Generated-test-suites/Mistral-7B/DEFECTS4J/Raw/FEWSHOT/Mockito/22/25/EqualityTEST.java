package org.mockito.internal.matchers;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class EqualityTest {

    @Test
    public void testAreEqual() {
        Object o1 = "foo";
        Object o2 = "foo";

        assertTrue(Equality.areEqual(o1, o2));

        o1 = new Integer(1);
        o2 = new Integer(1);

        assertTrue(Equality.areEqual(o1, o2));

        o1 = new Object()[1];
        o2 = new Object()[1];

        Object o1Element = new Integer(1);
        Object o2Element = new Integer(1);
        assertArrayEquals(new Object[]{o1Element}, (Object[]) o1);
        assertArrayEquals(new Object[]{o2Element}, (Object[]) o2);
        assertTrue(Equality.areEqual(o1, o2));

        o1 = new String[]{"foo"};
        o2 = new String[]{"foo"};
        assertTrue(Equality.areEqual(o1, o2));

        o1 = new int[1];
        o2 = new int[1];
        o1[0] = 1;
        o2[0] = 1;
        assertTrue(Equality.areEqual(o1, o2));

        o1 = null;
        o2 = null;
        assertTrue(Equality.areEqual(o1, o2));

        o1 = new Object();
        assertFalse(Equality.areEqual(o1, new Object()));

        o1 = new Integer(1);
        o2 = new Long(1);
        assertFalse(Equality.areEqual(o1, o2));

        o1 = new Object();
        o2 = new String(o1.toString());
        assertFalse(Equality.areEqual(o1, o2));
    }

    @Test
    public void testAreArraysEqual() {
        Object o1 = new Integer[1];
        Object o2 = new Integer[1];

        Integer o1Element = new Integer(1);
        Integer o2Element = new Integer(1);
        o1[0] = o1Element;
        o2[0] = o2Element;

        assertTrue(Equality.areArraysEqual(o1, o2));

        o1 = new Integer[]{1, 2, 3};
        o2 = new Integer[]{1, 2, 3};
        assertTrue(Equality.areArraysEqual(o1, o2));

        o1 = new String[]{"foo", "bar"};
        o2 = new String[]{"foo", "bar"};
        assertTrue(Equality.areArraysEqual(o1, o2));

        o1 = new Integer[1][1];
        Object o1SubArray = o1[0];
        o2 = new Integer[1][1];
        Object o2SubArray = o2[0];
        o1SubArray[0] = new Integer(1);
        o2SubArray[0] = new Integer(1);
        assertTrue(Equality.areArraysEqual(o1, o2));

        o1 = new Integer[]{1, 1, 1};
        o2 = new Integer[]{1, 2, 1};
        assertFalse(Equality.areArraysEqual(o1, o2));

        o1 = new Integer[1];
        o2 = new String[1];
        assertFalse(Equality.areArraysEqual(o1, o2));

        o1 = null;
        o2 = null;
        assertTrue(Equality.areArraysEqual(o1, o2));

        o1 = new Integer[1];
        o2 = new Object[1];
        assertFalse(Equality.areArraysEqual(o1, o2));
    }

    @Test
    public void testIsArray() {
        assertTrue(Equality.isArray(new int[0]));
        assertTrue(Equality.isArray(new int[1]));
        assertTrue(Equality.isArray(new Integer[0]));
        assertTrue(Equality.isArray(new Integer[1]));
        assertTrue(Equality.isArray(new String[0]));
        assertTrue(Equality.isArray(new String[1]));
        assertTrue(Equality.isArray(new Object[0]));
        assertTrue(Equality.isArray(new Object[1]));

        assertFalse(Equality.isArray(null));
        assertFalse(Equality.isArray(new Object()));
        assertFalse(Equality.isArray(new String("foo")));
        assertFalse(Equality.isArray(new Integer(1)));
    }
}