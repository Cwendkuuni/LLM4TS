package gnu.trove.impl.hash;

import org.junit.Test;

import static org.junit.Assert.*;

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
        
        // Test when hash does not contain the key
        assertFalse(hash.contains(1.0f));

        // Test when hash contains the key
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        
        // Test when hash is empty
        assertTrue(hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                // The hash is empty, this should not execute
                fail("Should not execute");
                return true;
            }
        }));
        
        // Test when hash is not empty
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        assertTrue(hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                // All elements in the hash should be executed
                return true;
            }
        }));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertEquals(1.0f, hash._set[0], 0.0001);

        hash.removeAt(0);
        assertEquals(0.0f, hash._set[0], 0.0001);
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        assertEquals(0, hash.index(1.0f));

        assertEquals(-1, hash.index(2.0f));
    }

    @Test
    public void testIndexRehashed() {
        // TODO: Implement this test
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(-1, hash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        // TODO: Implement this test
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, hash._set[0], 0.0001);
        assertEquals(1, hash._states[0]);
    }

    @Test
    public void testXinsertKey() {
        // TODO: Implement this test
    }

    @Test
    public void testWriteExternal() throws Exception {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        FileOutputStream fileOut = new FileOutputStream("hash.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(hash);
        out.close();
        fileOut.close();

        FileInputStream fileIn = new FileInputStream("hash.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        TFloatCharHash newHash = (TFloatCharHash) in.readObject();
        in.close();
        fileIn.close();

        assertEquals(1.0f, newHash._set[0], 0.0001);
        assertEquals(2.0f, newHash._set[1], 0.0001);
    }

    @Test
    public void testReadExternal() throws Exception {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        FileOutputStream fileOut = new FileOutputStream("hash.ser");
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(hash);
        out.close();
        fileOut.close();

        FileInputStream fileIn = new FileInputStream("hash.ser");
        ObjectInputStream in = new ObjectInputStream(fileIn);
        TFloatCharHash newHash = (TFloatCharHash) in.readObject();
        in.close();
        fileIn.close();

        assertEquals(1.0f, newHash._set[0], 0.0001);
        assertEquals(2.0f, newHash._set[1], 0.0001);
    }

}