package org.apache.commons.cli;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import java.util.Arrays;

import org.junit.Test;

public class MultiKeyTest {

    @Test
    public void testMultiKeyConstruction() {
        K key1 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };
        K key2 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };

        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey2 = new MultiKey<>(new Object[] { key1, key2 });

        assertArrayEquals(new Object[] { key1, key2 }, multiKey1.getKeys());
        assertArrayEquals(new Object[] { key1, key2 }, multiKey2.getKeys());

        MultiKey<Object> multiKey3 = new MultiKey<>(new Object[] { key1, key2 }, false);
        assertSame(multiKey2, multiKey3);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullKeys() {
        new MultiKey<>(null);
    }

    @Test
    public void testGetKey() {
        K key1 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };
        K key2 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };

        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);

        assertEquals(key1, multiKey.getKey(0));
        assertEquals(key2, multiKey.getKey(1));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetKeyInvalidIndex() {
        K key1 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };
        K key2 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };

        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);

        multiKey.getKey(2);
    }

    @Test
    public void testSize() {
        K key1 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };
        K key2 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };

        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);

        assertEquals(2, multiKey.size());
    }

    @Test
    public void testEquals() {
        K key1 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };
        K key2 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };
        K key3 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }
        };

        MultiKey<Object> multiKey1 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey2 = new MultiKey<>(key1, key2);
        MultiKey<Object> multiKey3 = new MultiKey<>(new Object[] { key1, key2 });

        MultiKey<Object> multiKey4 = new MultiKey<>(key1, key3);
        MultiKey<Object> multiKey5 = new MultiKey<>(key2, key1);

        assertEquals(multiKey1, multiKey1);
        assertEquals(multiKey1, multiKey2);
        assertEquals(multiKey1, multiKey3);
        assertNotSame(multiKey1, multiKey4);
        assertNotSame(multiKey1, multiKey5);
    }

    @Test
    public void testHashCode() {
        K key1 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }

            @Override
            public int hashCode() {
                return 1;
            }
        };
        K key2 = new Object() {
            @Override
            public boolean equals(Object o) {
                return true;
            }

            @Override
            public int hashCode() {
                return 2;
            }
        };

        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);

        assertEquals(32 * (1 + 1), multiKey.hashCode());
    }

    @Test
    public void testToString() {
        K key1 = new Object() {
            @Override
            public String toString() {
                return "key1";
            }
        };
        K key2 = new Object() {
            @Override
            public String toString() {
                return "key2";
            }
        };

        MultiKey<Object> multiKey = new MultiKey<>(key1, key2);

        assertEquals("MultiKey[key1, key2]", multiKey.toString());
    }
}