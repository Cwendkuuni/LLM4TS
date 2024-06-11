import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordTypeCreation() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("String", null));
        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        
        assertNotNull(recordType);
        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testRecordEquivalence() {
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("age", new RecordProperty("Number", null));
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("age", new RecordProperty("Number", null));
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, false));
    }

    @Test
    public void testRecordSubtyping() {
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("color", new RecordProperty("String", null));
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("color", new RecordProperty("String", null));
        properties2.put("size", new RecordProperty("String", null));
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), properties2);

        assertTrue(RecordType.isSubtype(recordType1, recordType2));
    }

    @Test
    public void testRecordTypeProperties() {
        RecordType recordType = new RecordType(new JSTypeRegistry(), new HashMap<>());
        recordType.defineProperty("name", registry.getNativeType(JSTypeNative.STRING_TYPE), false, null);

        assertNotNull(recordType.getPropertyType("name"));
        assertEquals("String", recordType.getPropertyType("name").toString());
    }

    @Test
    public void testGreatestSubtype() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("length", new RecordProperty("Number", null));
        RecordType recordType = new RecordType(new JSTypeRegistry(), properties);
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        assertEquals(objectType, recordType.getGreatestSubtypeHelper(objectType));
    }

}