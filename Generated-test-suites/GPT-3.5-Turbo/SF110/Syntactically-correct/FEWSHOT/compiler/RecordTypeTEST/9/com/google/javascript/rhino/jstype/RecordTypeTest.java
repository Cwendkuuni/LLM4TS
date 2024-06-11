import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsSynthetic() {
        RecordType recordType1 = new RecordType(null, null);
        assertTrue(recordType1.isSynthetic());
        
        RecordType recordType2 = new RecordType(null, null, false);
        assertFalse(recordType2.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, null));
        
        RecordType recordType3 = new RecordType(null, null);
        RecordType recordType4 = new RecordType(null, null);
        assertFalse(recordType3.checkRecordEquivalenceHelper(recordType4, null));
    }
    
    @Test
    public void testDefineProperty() {
        RecordType recordType = new RecordType(null, null);
        assertTrue(recordType.defineProperty("test", null, false, null));
        assertFalse(recordType.defineProperty("test", null, false, null));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        RecordType recordType1 = new RecordType(null, null);
        JSType jsType1 = new JSType();
        assertTrue(recordType1.getGreatestSubtypeHelper(jsType1).isInvariant(jsType1));
        
        RecordType recordType2 = new RecordType(null, null);
        JSType jsType2 = new JSType();
        assertFalse(recordType2.getGreatestSubtypeHelper(jsType2).isInvariant(jsType2));
    }
    
    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        assertTrue(recordType1.isSubtype(recordType2));
        
        RecordType recordType3 = new RecordType(null, null);
        ObjectType objectType = new ObjectType();
        assertTrue(recordType3.isSubtype(objectType));
    }
    
    @Test
    public void testIsSubtype_2() {
        RecordType recordType1 = new RecordType(null, null);
        RecordType recordType2 = new RecordType(null, null);
        assertTrue(RecordType.isSubtype(recordType1, recordType2));
        
        RecordType recordType3 = new RecordType(null, null);
        RecordType recordType4 = new RecordType(null, null);
        assertFalse(RecordType.isSubtype(recordType3, recordType4));
    }
}