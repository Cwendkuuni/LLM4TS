import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeTest {
    
    private RecordType recordType;
    
    @Before
    public void setUp() {
        recordType = new RecordType();
    }
    
    @Test
    public void testIsSynthetic() {
        assertFalse(recordType.isSynthetic());
    }
    
    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType otherRecord = new RecordType();
        
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUALS));
    }
    
    @Test
    public void testGetImplicitPrototype() {
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), recordType.getImplicitPrototype());
    }
    
    @Test
    public void testDefineProperty() {
        assertTrue(recordType.defineProperty("propertyName", JSType, true, propertyNode));
    }
    
    @Test
    public void testGetGreatestSubtypeHelper() {
        JSType that = new JSType();
        
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), recordType.getGreatestSubtypeHelper(that));
    }
    
    @Test
    public void testToMaybeRecordType() {
        assertEquals(recordType, recordType.toMaybeRecordType());
    }
    
    @Test
    public void testIsSubtype() {
        RecordType typeA = new RecordType();
        RecordType typeB = new RecordType();
        
        assertTrue(RecordType.isSubtype(typeA, typeB));
    }
}