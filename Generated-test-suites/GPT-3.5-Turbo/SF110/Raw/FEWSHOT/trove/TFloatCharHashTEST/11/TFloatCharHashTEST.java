import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('a', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.setUp(10);
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        
        assertTrue(hash.contains(2.0f));
        assertFalse(hash.contains(4.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            
            public boolean execute(float value) {
                sum += value;
                return true;
            }
            
            float getSum() {
                return sum;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(6.0f, procedure.getSum(), 0.0f);
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash.removeAt(1);
        
        assertEquals(1.0f, hash._set[1], 0.0f);
        assertEquals(0, hash._states[1]);
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 1, 0};
        
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 1, 0};
        
        assertEquals(1, hash.indexRehashed(2.0f, 1, 1, (byte)1));
        assertEquals(-1, hash.indexRehashed(4.0f, 1, 1, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._states = new byte[] {0, 1, 0};
        
        hash.insertKey(2.0f);
        assertEquals(2.0f, hash._set[0], 0.0f);
        assertEquals(1, hash._states[0]);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        hash._states = new byte[] {1, 1, 0};
        
        hash.insertKeyRehash(2.0f, 1, 1, (byte)1);
        assertEquals(2.0f, hash._set[0], 0.0f);
        assertEquals(1, hash._states[0]);
    }
    
    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._states = new byte[] {0, 1, 0};
        hash._set = new float[] {1.0f, 2.0f, 3.0f};
        
        hash.XinsertKey(2.0f);
        assertEquals(2.0f, hash._set[0], 0.0f);
        assertEquals(1, hash._states[0]);
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        
        TFloatCharHash hash = new TFloatCharHash();
        hash.no_entry_key = 1.0f;
        hash.no_entry_value = 'a';

        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        
        byte[] byteArray = outputStream.toByteArray();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        
        TFloatCharHash deserializedHash = new TFloatCharHash();
        deserializedHash.readExternal(objectInputStream);
        
        assertEquals(1.0f, deserializedHash.no_entry_key, 0.0f);
        assertEquals('a', deserializedHash.no_entry_value);
        
        objectOutputStream.close();
        objectInputStream.close();
    }
    
    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        String input = "aced00057372001d6772752e74726f76652e696d706c2e686173682e5430666c6f6174436861724861736831"
                + "ffd5ebf115d89602000078707372004f6772752e74726f76652e696d706c2e686173682e5430666c6f61744368"
                + "6172486173680000000000000000000200007870ffffffffffffff70740072440001411c00183fc0000000724400"
                + "01410107800000007244000141020678ffffff7870ffffffffffffff7878ffffffffffffff781700000000000007"
                + "7878000000000008787878000000000878787878ffffffff787878787878787872";
        byte[] byteArray = hexStringToByteArray(input);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        
        TFloatCharHash hash = new TFloatCharHash();
        hash.readExternal(objectInputStream);
        
        assertEquals(1.0f, hash.no_entry_key, 0.0f);
        assertEquals('a', hash.no_entry_value);
        
        objectInputStream.close();
    }
    
    private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
}