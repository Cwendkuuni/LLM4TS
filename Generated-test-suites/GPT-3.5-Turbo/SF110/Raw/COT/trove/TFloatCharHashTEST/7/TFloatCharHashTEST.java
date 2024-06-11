import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatCharHash hash = new TFloatCharHash();
        float expected = 0.0f;
        float result = hash.getNoEntryKey();
        assertEquals(expected, result, 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatCharHash hash = new TFloatCharHash();
        char expected = '\0';
        char result = hash.getNoEntryValue();
        assertEquals(expected, result);
    }

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        boolean result = hash.contains(1.0f);
        assertTrue(result);
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                // do something
                return true;
            }
        };
        boolean result = hash.forEach(procedure);
        assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.removeAt(0);
        float result = hash.get(0);
        assertEquals(0.0f, result, 0.0f);
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int result = hash.insertKey(1.0f);
        assertEquals(0, result);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.8f);
        for (int i = 0; i < 10; i++) {
            hash.insertKey((float) i);
        }
        int result = hash.insertKeyRehash(10.0f, 0, 0, (byte) 1);
        assertEquals(10, result);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 1.0f);
        float result = hash.get(0);
        assertEquals(1.0f, result, 0.0f);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        int result = hash.XinsertKey(1.0f);
        assertEquals(0, result);
    }

    @Test
    public void testWriteExternal() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        
        try {
            FileOutputStream fileOut = new FileOutputStream("hash.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(hash);
            out.close();
            fileOut.close();
        } catch(IOException i) {
            i.printStackTrace();
        }
    }

    @Test
    public void testReadExternal() {
        TFloatCharHash hash = null;
        
        try {
            FileInputStream fileIn = new FileInputStream("hash.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            hash = (TFloatCharHash) in.readObject();
            in.close();
            fileIn.close();
        } catch(IOException i) {
            i.printStackTrace();
            return;
        } catch(ClassNotFoundException c) {
            System.out.println("TFloatCharHash class not found");
            c.printStackTrace();
            return;
        }
        
        float result = hash.get(0);
        assertEquals(1.0f, result, 0.0f);
    }
}