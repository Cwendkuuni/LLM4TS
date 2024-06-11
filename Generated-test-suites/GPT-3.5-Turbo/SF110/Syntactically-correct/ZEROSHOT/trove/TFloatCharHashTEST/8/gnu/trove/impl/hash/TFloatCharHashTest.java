import org.junit.*;
import static org.junit.Assert.*;
import gnu.trove.procedure.TFloatProcedure;
 
public class TFloatCharHashTest {
    
    private TFloatCharHash floatCharHash;
 
    @Before
    public void setUp() {
        floatCharHash = new TFloatCharHash();
    }
    
    @After
    public void tearDown() {
        floatCharHash = null;
    }
    
    @Test
    public void testContains() {
        assertFalse(floatCharHash.contains(1.0f));
        floatCharHash.insertKey(1.0f);
        assertTrue(floatCharHash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.insertKey(3.0f);
        
        final StringBuilder stringBuilder = new StringBuilder();
        
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                stringBuilder.append(value);
                return true;
            }
        };
        
        assertTrue(floatCharHash.forEach(procedure));
        assertEquals("1.02.03.0", stringBuilder.toString());
    }
    
    @Test
    public void testRemoveAt() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.removeAt(0);
        assertFalse(floatCharHash.contains(1.0f));
        assertTrue(floatCharHash.contains(2.0f));
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', floatCharHash.getNoEntryValue());
    }
    
    @Test
    public void testInsertKey() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        assertTrue(floatCharHash.contains(1.0f));
        assertTrue(floatCharHash.contains(2.0f));
    }
    
    @Test
    public void testXinsertKey() {
        floatCharHash.XinsertKey(1.0f);
        floatCharHash.XinsertKey(2.0f);
        assertTrue(floatCharHash.contains(1.0f));
        assertTrue(floatCharHash.contains(2.0f));
    }
    
    @Test
    public void testWriteExternal() {
        // TODO: Implement this test
    }
    
    @Test
    public void testReadExternal() {
        // TODO: Implement this test
    }
}