import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    // 

    @Test
    public void testRecordType() {
        RecordType recordType = new RecordType(registry, properties);
        assertNotNull(recordType);
    }

    @Test
    public void testIsSynthetic() {
        RecordType recordType = new RecordType(registry, properties);
        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, true));
    }

    @Test
    public void testGetPropertyType() {
        RecordType recordType = new RecordType(registry, properties);
        JSType propertyType = recordType.getPropertyType("property");
        assertNotNull(propertyType);
    }

    @Test
    public void testIsSubtype() {
        RecordType recordType1 = new RecordType(registry, properties);
        RecordType recordType2 = new RecordType(registry, properties);
        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testResolveInternal() {
        RecordType recordType = new RecordType(registry, properties);
        JSType resolvedType = recordType.resolveInternal(t, scope);
        assertNotNull(resolvedType);
    }

    // 
}