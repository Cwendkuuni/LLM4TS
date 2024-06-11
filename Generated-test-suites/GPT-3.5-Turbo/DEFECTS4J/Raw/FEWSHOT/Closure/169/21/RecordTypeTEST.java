import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType(null, null);
        assertFalse(recordType.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    }
    
    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(null, null);
        
        assertFalse(recordType.defineProperty("property", null, false, null));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType = new RecordType(null, null);
        
        assertNull(recordType.getGreatestSubtypeHelper(null));
    }
    
    @Test
    public void testIsSubtype() {
        RecordType recordType = new RecordType(null, null);
        
        assertFalse(recordType.isSubtype(null));
    }
    
    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(null, null);
        
        assertNull(recordType.resolveInternal(null, null));
    }
}