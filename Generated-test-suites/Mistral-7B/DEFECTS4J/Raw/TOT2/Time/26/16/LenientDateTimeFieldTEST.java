package org.example.junit;

import org.junit.Test;
import static org.junit.Assert.*;

public class LenientDateTimeFieldTest {

    @Test
    public void testIsLenientMethod() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1) {
            @Override
            public int get(long millis) {
                return 0;
            }
        }, null);
        assertTrue(lenientDateTimeField.isLenient());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeValue() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1) {
            @Override
            public int get(long millis) {
                return 0;
            }
        }, null);
        lenientDateTimeField.set(0, -1);
    }

    @Test
    public void testSetLimitValue() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1) {
            @Override
            public int get(long millis) {
                return 100;
            }
        }, null);
        long limitValue = lenientDateTimeField.get(0) + lenientDateTimeField.getLimit();
        assertEquals(limitValue, lenientDateTimeField.set(0, lenientDateTimeField.getLimit()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetValueGreaterThanFieldRange() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1) {
            @Override
            public int get(long millis) {
                return 100;
            }

            @Override
            public int getLimit() {
                return 99;
            }
        }, null);
        lenientDateTimeField.set(0, 101);
    }

    @Test(expected = NullPointerException.class)
    public void testSetInstantNull() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1) {
            @Override
            public int get(long millis) {
                return 0;
            }
        }, null);
        lenientDateTimeField.set(null, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetInstantWrongChronology() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1) {
            @Override
            public int get(long millis) {
                return 0;
            }
        }, null);
        lenientDateTimeField.set(1, 0);
    }

    @Test(expected = NullPointerException.class)
    public void testSetChronologyNull() {
        LenientDateTimeField lenientDateTimeField = new LenientDateTimeField(new DateTimeField(1) {
            @Override
            public int get(long millis) {
                return 0;
            }
        }, null);
        lenientDateTimeField.set(0, 0, null);
    }
}