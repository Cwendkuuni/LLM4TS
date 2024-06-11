package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {
    
    private TFloatCharHash hash;
    
    @Before
    public void setUp(){
        hash = new TFloatCharHash();
    }
    
    @Test
    public void testGetNoEntryKey(){
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }
    
    @Test
    public void testGetNoEntryValue(){
        assertEquals('\0', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp(){
        assertEquals(10, hash.setUp(10));
        assertEquals(10, hash._set.length);
    }
    
    @Test
    public void testContains(){
        assertFalse(hash.contains(1.0f));
    }
    
    @Test
    public void testForEach(){
        TFloatProcedure procedure = new TFloatProcedure(){
            public boolean execute(float val){
                return val > 0;
            }
        };
        assertFalse(hash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt(){
        hash.removeAt(0);
    }
    
    @Test
    public void testIndex(){
        assertEquals(-1, hash.index(0.0f));
    }
    
    @Test
    public void testIndexRehashed(){
        assertEquals(-1, hash.indexRehashed(0.0f, 0, 0, (byte)0));
    }
    
    @Test
    public void testInsertKey(){
        assertEquals(0, hash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash(){
        assertEquals(0, hash.insertKeyRehash(1.0f, 0, 0, (byte)0));
    }
    
    @Test
    public void testInsertKeyAt(){
        hash.insertKeyAt(0, 1.0f);
    }
    
    @Test
    public void testXinsertKey(){
        assertEquals(0, hash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutput out = new ObjectOutputStream(baos);
        hash.writeExternal(out);
        out.flush();
        byte[] byteArray = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        ObjectInput in = new ObjectInputStream(bais);
        hash.readExternal(in);
    }
}