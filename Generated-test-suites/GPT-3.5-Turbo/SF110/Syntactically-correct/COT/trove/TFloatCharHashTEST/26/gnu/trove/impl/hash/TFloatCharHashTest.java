import static org.junit.Assert.*;
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals('\0', hash.getNoEntryValue());
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
        int[] count = { 0 };
        hash.forEach((float key) -> {
            count[0]++;
            return true;
        });
        assertEquals(2, count[0]);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        hash.removeAt(0);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.XinsertKey(1.0f);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectStream = new ObjectOutputStream(outputStream);
            hash.writeExternal(objectStream);
            objectStream.close();

            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream inObjectStream = new ObjectInputStream(inputStream);

            hash.readExternal(inObjectStream);

            inObjectStream.close();
            inputStream.close();

            assertTrue(hash.contains(1.0f));
            assertEquals('a', hash.get(1.0f));

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
    // Additional test cases for potential edge cases and exceptions

    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(10, hash._set.length);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        int index = hash.index(1.0f);
        assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.put(1.0f, 'a');
        int index = hash.indexRehashed(1.0f, 0, 0, (byte) 1);
        assertEquals(0, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte) 0);
        assertEquals(0, index);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        assertTrue(hash.contains(1.0f));
    }
}