import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        // Create record types
        RecordProperty prop1 = new RecordProperty("a", JSTypeNative.STRING_TYPE, null);
        RecordProperty prop2 = new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null);
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), buildProperties(prop1));
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), buildProperties(prop1, prop2));

        // Test
        assertTrue(recordType1.isEquivalentTo(recordType1));
        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        // Create record types
        RecordProperty prop1 = new RecordProperty("a", JSTypeNative.STRING_TYPE, null);
        RecordProperty prop2 = new RecordProperty("b", JSTypeNative.NUMBER_TYPE, null);
        RecordType recordType1 = new RecordType(new JSTypeRegistry(), buildProperties(prop1));
        RecordType recordType2 = new RecordType(new JSTypeRegistry(), buildProperties(prop1, prop2));

        // Test
        assertTrue(recordType1.isSubtype(new JSTypeRegistry().getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertTrue(recordType2.isSubtype(new JSTypeRegistry().getNativeObjectType(JSTypeNative.OBJECT_TYPE)));
        assertFalse(recordType1.isSubtype(recordType2));
        assertTrue(recordType2.isSubtype(recordType1));
    }

    private Map<String, RecordTypeBuilder.RecordProperty> buildProperties(RecordTypeBuilder.RecordProperty... properties) {
        Map<String, RecordTypeBuilder.RecordProperty> propMap = new HashMap<>();
        for (RecordTypeBuilder.RecordProperty property : properties) {
            propMap.put(property.getName(), property);
        }
        return propMap;
    }
}