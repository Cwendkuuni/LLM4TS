package gnu.trove.impl.hash;

import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {
    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
    }

    @Test
    public void testConstructor3() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f);

        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
        assertEquals(0.5f, hash.loadFactor(), 0.0);
    }

    @Test
    public void testConstructor4() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');

        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('a', hash.getNoEntryValue());
        assertEquals(10, hash.capacity());
        assertEquals(0.5f, hash.loadFactor(), 0.0);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');

        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');

        final StringBuilder sb = new StringBuilder();
        assertTrue(hash.forEach((foo) -> {
            sb.append(foo);
            return true;
        }));
        assertEquals("ab", sb.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.put(2.0f, 'b');

        hash.removeAt(0);

        assertFalse(hash.contains(1.0f));
        assertEquals(2.0f, hash.get(0), 0.0);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));

        assertEquals(0, hash.indexOf(1.0f));
        assertEquals(1, hash.indexOf(2.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.xinsertKey(1.0f);
        hash.xinsertKey(2.0f);

        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));

        assertEquals(0, hash.indexOf(1.0f));
        assertEquals(1, hash.indexOf(2.0f));
    }

    @Test
    public void testWriteExternal() {

    }

    @Test
    public void testReadExternal() {

    }
}