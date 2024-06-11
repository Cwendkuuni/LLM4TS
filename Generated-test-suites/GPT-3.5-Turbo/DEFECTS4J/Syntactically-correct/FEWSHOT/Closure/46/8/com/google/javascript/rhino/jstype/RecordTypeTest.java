
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeTest {
    
    @Test
    public void testIsEquivalentTo() {
        RecordType type1 = createRecordType();
        RecordType type2 = createRecordType();
        RecordType type3 = createDifferentRecordType();
        
        assertEquals(true, type1.isEquivalentTo(type2));
        assertEquals(false, type1.isEquivalentTo(type3));
    }
    
    @Test
    public void testGetLeastSupertype() {
        RecordType type1 = createRecordType();
        RecordType type2 = createSubtypeRecordType();
        RecordType type3 = createDifferentRecordType();
        
        assertEquals(type1, type1.getLeastSupertype(type2));
        assertEquals(null, type1.getLeastSupertype(type3));
    }
    
    @Test
    public void testGetGreatestSubtype() {
        RecordType type1 = createRecordType();
        RecordType type2 = createSubtypeRecordType();
        RecordType type3 = createDifferentRecordType();
        
        assertEquals(type2, type1.getGreatestSubtype(type2));
        assertEquals(null, type1.getGreatestSubtype(type3));
    }
    
    @Test
    public void testIsSubtype() {
        RecordType type1 = createRecordType();
        RecordType type2 = createSubtypeRecordType();
        RecordType type3 = createDifferentRecordType();
        
        assertEquals(true, type1.isSubtype(type1));
        assertEquals(true, type2.isSubtype(type1));
        assertEquals(false, type1.isSubtype(type3));
    }
    
    private RecordType createRecordType() {
        RecordTypeBuilder builder = new RecordTypeBuilder();
        builder.addProperty("a", JSTypeNative.UNKNOWN_TYPE);
        builder.addProperty("b", JSTypeNative.UNKNOWN_TYPE);
        
        return builder.build();
    }
    
    private RecordType createSubtypeRecordType() {
        RecordTypeBuilder builder = new RecordTypeBuilder();
        builder.addProperty("a", JSTypeNative.UNKNOWN_TYPE);
        builder.addProperty("b", JSTypeNative.UNKNOWN_TYPE);
        builder.addProperty("c", JSTypeNative.UNKNOWN_TYPE);
        
        return builder.build();
    }
    
    private RecordType createDifferentRecordType() {
        RecordTypeBuilder builder = new RecordTypeBuilder();
        builder.addProperty("a", JSTypeNative.UNKNOWN_TYPE);
        builder.addProperty("d", JSTypeNative.UNKNOWN_TYPE);
        
        return builder.build();
    }
}
