import org.junit.Test;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testConstructor1() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        assertNotNull(hash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TShortHash hash = new TShortHash(initialCapacity) {
            @Override
            public int capacity() {
                return 0;
            }
        };

        assertNotNull(hash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor) {
            @Override
            public int capacity() {
                return 0;
            }
        };

        assertNotNull(hash);
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short no_entry_value = 0;
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, no_entry_value) {
            @Override
            public int capacity() {
                return 0;
            }
        };

        assertNotNull(hash);
        assertEquals(no_entry_value, hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TShortHash hash = new TShortHash(initialCapacity) {
            @Override
            public int capacity() {
                return 0;
            }
        };

        int capacity = hash.setUp(initialCapacity);

        assertEquals(initialCapacity, capacity);
        assertNotNull(hash._set);
        assertEquals(initialCapacity, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        assertFalse(hash.contains((short) 1));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        assertTrue(hash.forEach(val -> true));
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        hash.removeAt(0);

        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash._set[0]);
        //assertions for other properties
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        int index = hash.index((short) 1);

        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        int index = hash.indexRehashed((short) 1, 0, 0, (byte) 0);

        assertEquals(-1, index);
    }
    
    @Test
    public void testInsertKey1() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        int index = hash.insertKey((short) 1);

        assertEquals(0, index);
        assertEquals((short) 1, hash._set[0]);
    }
    
    @Test
    public void testInsertKey2() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };
        hash.insertKey((short) 1);

        int index = hash.insertKey((short) 1);

        assertEquals(-1, index);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        hash.insertKeyRehash((short) 1, 0, 0, (byte) 0);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash() {
            @Override
            public int capacity() {
                return 0;
            }
        };

        hash.insertKeyAt(0, (short) 1);

        assertEquals((short) 1, hash._set[0]);
        assertEquals((byte) 1, hash._states[0]);
    }
}