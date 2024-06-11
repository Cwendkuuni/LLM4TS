import gnu.trove.impl.hash.TShortHash;
import gnu.trove.procedure.TShortProcedure;
import org.junit.Assert;
import org.junit.Test;

public class TShortHashTest {

    @Test
    public void testConstructor1() {
        TShortHash hash = new TShortHash();
        
        Assert.assertNotNull(hash);
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
    }
    
    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        
        TShortHash hash = new TShortHash(initialCapacity);
        
        Assert.assertNotNull(hash);
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        Assert.assertEquals(initialCapacity, hash._set.length);
    }
    
    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        
        TShortHash hash = new TShortHash(initialCapacity, loadFactor);
        
        Assert.assertNotNull(hash);
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, hash.getNoEntryValue());
        Assert.assertEquals(initialCapacity, hash._set.length);
    }
    
    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.5f;
        short noEntryValue = -1;
        
        TShortHash hash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        
        Assert.assertNotNull(hash);
        Assert.assertEquals(noEntryValue, hash.getNoEntryValue());
        Assert.assertEquals(initialCapacity, hash._set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 6);
        hash.insertKey((short) 9);
        
        Assert.assertTrue(hash.contains((short) 6));
        Assert.assertTrue(hash.contains((short) 9));
        
        Assert.assertFalse(hash.contains((short) 0));
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 6);
        hash.insertKey((short) 9);
        
        ShortHolder holder = new ShortHolder();
        
        TShortProcedure procedure = new TShortProcedure() {
            public boolean execute(short value) {
                holder.setValue(value);
                return false;
            }
        };
        
        boolean result = hash.forEach(procedure);
        
        Assert.assertFalse(result);
        Assert.assertEquals((short) 6, holder.getValue());
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 6);
        hash.insertKey((short) 9);
        
        hash.removeAt(0);
        
        Assert.assertFalse(hash.contains((short) 6));
        Assert.assertTrue(hash.contains((short) 9));
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHash();
        hash.insertKey((short) 6);
        hash.insertKey((short) 9);
        
        int index1 = hash.index((short) 6);
        int index2 = hash.index((short) 9);
        
        Assert.assertEquals(0, index1);
        Assert.assertEquals(1, index2);
        
        int index3 = hash.index((short) 0);
        
        Assert.assertEquals(-1, index3);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHash();
        
        int index = hash.insertKey((short) 6);
        
        Assert.assertEquals(0, index);
        Assert.assertTrue(hash.contains((short) 6));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHash(2);
        hash.insertKey((short) 6);
        hash.insertKey((short) 9);
        
        int index = hash.insertKeyRehash((short) 12, 0, 0, (byte) 0);
        
        Assert.assertEquals(2, index);
        Assert.assertTrue(hash.contains((short) 12));
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHash(2);
        
        hash.insertKeyAt(0, (short) 6);
        
        Assert.assertTrue(hash.contains((short) 6));
        Assert.assertEquals((short) 6, hash._set[0]);
    }
}

class ShortHolder {
    private short value;
    
    public short getValue() {
        return value;
    }
    
    public void setValue(short value) {
        this.value = value;
    }
}