
import org.junit.Assert;
import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testConstructor1() {
        TCharHash hash = new TCharHash();
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        Assert.assertArrayEquals(new char[hash.DEFAULT_CAPACITY], hash._set);
    }
    
    @Test
    public void testConstructor2() {
        TCharHash hash = new TCharHash(10);
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        Assert.assertArrayEquals(new char[10], hash._set);
    }
    
    @Test
    public void testConstructor3() {
        TCharHash hash = new TCharHash(10, 0.75f);
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash.getNoEntryValue());
        Assert.assertArrayEquals(new char[10], hash._set);
    }
    
    @Test
    public void testConstructor4() {
        TCharHash hash = new TCharHash(10, 0.75f, 'N');
        Assert.assertEquals('N', hash.getNoEntryValue());
        Assert.assertArrayEquals(new char[10], hash._set);
    }
    
    @Test
    public void testSetUp() {
        TCharHash hash = new TCharHash();
        Assert.assertEquals(hash.DEFAULT_CAPACITY, hash.setUp(10));
        Assert.assertArrayEquals(new char[10], hash._set);
    }
    
    @Test
    public void testContains() {
        TCharHash hash = new TCharHash();
        hash._set[0] = 'A';
        hash._set[1] = 'B';
        hash._set[2] = 'C';
        
        Assert.assertTrue(hash.contains('A'));
        Assert.assertTrue(hash.contains('B'));
        Assert.assertTrue(hash.contains('C'));
        Assert.assertFalse(hash.contains('D'));
    }
    
    @Test
    public void testForEach() {
        TCharHash hash = new TCharHash();
        hash._set[0] = 'A';
        hash._set[1] = 'B';
        hash._set[2] = 'C';
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        Assert.assertTrue(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash hash = new TCharHash();
        hash._set[0] = 'A';
        hash._set[1] = 'B';
        hash._set[2] = 'C';
        
        hash.removeAt(1);
        
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, hash._set[1]);
        Assert.assertEquals(0, hash._states[1]);
    }
    
    @Test
    public void testIndex() {
        TCharHash hash = new TCharHash();
        hash._set[0] = 'A';
        hash._set[1] = 'B';
        hash._states[1] = 1;
        hash._set[2] = 'C';
        hash._states[2] = 1;
        
        Assert.assertEquals(1, hash.index('B'));
        Assert.assertEquals(2, hash.index('C'));
        Assert.assertEquals(-1, hash.index('D'));
    }
    
    @Test
    public void testIndexRehashed() {
        TCharHash hash = new TCharHash();
        hash._set[0] = 'A';
        hash._set[1] = 'B';
        hash._states[1] = 1;
        hash._set[2] = 'C';
        hash._states[2] = 1;
        
        Assert.assertEquals(1, hash.indexRehashed('B', 1, 12345, (byte)1));
        Assert.assertEquals(2, hash.indexRehashed('C', 1, 12345, (byte)1));
        Assert.assertEquals(-1, hash.indexRehashed('D', 1, 12345, (byte)1));
    }
    
    @Test
    public void testInsertKey() {
        TCharHash hash = new TCharHash();
        hash._set[0] = 'A';
        hash._states[0] = 1;
        hash._set[1] = 'B';
        hash._states[1] = 2;
        hash._set[2] = 'C';
        hash._states[2] = 1;
        
        Assert.assertEquals(2, hash.insertKey('C'));
        Assert.assertEquals(-1, hash.insertKey('D'));
    }
    
    @Test
    public void testInsertKeyRehash() {
        TCharHash hash = new TCharHash();
        hash._set[0] = 'A';
        hash._states[0] = 1;
        hash._set[1] = 'B';
        hash._states[1] = 1;
        hash._set[2] = 'C';
        hash._states[2] = 2;
        
        Assert.assertEquals(2, hash.insertKeyRehash('C', 2, 12345, (byte)2));
        Assert.assertEquals(3, hash.insertKeyRehash('D', 2, 12345, (byte)2));
    }
    
    @Test
    public void testInsertKeyAt() {
        TCharHash hash = new TCharHash();
        hash.insertKeyAt(0, 'A');
        
        Assert.assertEquals('A', hash._set[0]);
        Assert.assertEquals(1, hash._states[0]);
    }
}
