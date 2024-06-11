import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);

        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testRecordTypeDeclared() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties, true);

        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        RecordType recordType1 = new RecordType(registry, properties1);
        Map<String, RecordProperty> properties2 = new HashMap<>();
        RecordType recordType2 = new RecordType(registry, properties2);
        assertTrue(recordType1.isSubtype(recordType2));
        // Add properties to recordType1 and retest
        properties1.put("prop1", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeWithProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);
        assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeNotEqualProperties() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop2", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);
        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop2", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);
        assertTrue(greatestSubtype.isEmptyType());
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties);
        assertTrue(recordType.defineProperty("prop1", JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), false, null));
    }

    @Test
    public void testDefinePropertyFrozen() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        RecordType recordType = new RecordType(registry, properties, true);
        assertFalse(recordType.defineProperty("prop1", JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), false, null));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("prop1", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("prop1", new RecordProperty(JSTypeRegistry.nativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);
        assertTrue(recordType1.checkRecordEquivalenceHelper(recordType2, null));
    }
}