import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, Maps.newTreeMap());
        RecordType recordType2 = new RecordType(registry, Maps.newTreeMap());
        assertTrue(recordType1.isEquivalentTo(recordType2));

        // Test case 2
        Map<String, RecordProperty> properties1 = Maps.newTreeMap();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType3 = new RecordType(registry, properties1);
        assertFalse(recordType1.isEquivalentTo(recordType3));
        assertFalse(recordType3.isEquivalentTo(recordType1));

        // Test case 3
        Map<String, RecordProperty> properties2 = Maps.newTreeMap();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType4 = new RecordType(registry, properties2);
        assertTrue(recordType3.isEquivalentTo(recordType4));
    }

    @Test
    public void testGetLeastSupertype() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, Maps.newTreeMap());
        RecordType recordType2 = new RecordType(registry, Maps.newTreeMap());
        assertEquals(recordType1.toMaybeObjectType(), recordType1.getLeastSupertype(recordType2));

        // Test case 2
        Map<String, RecordProperty> properties1 = Maps.newTreeMap();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType3 = new RecordType(registry, properties1);
        assertEquals(recordType1.toMaybeObjectType(), recordType1.getLeastSupertype(recordType3));
        assertEquals(recordType3.toMaybeObjectType(), recordType3.getLeastSupertype(recordType1));

        // Test case 3
        Map<String, RecordProperty> properties2 = Maps.newTreeMap();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("c", new RecordProperty(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE), null));
        RecordType recordType4 = new RecordType(registry, properties2);
        Map<String, RecordProperty> properties3 = Maps.newTreeMap();
        properties3.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties3.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType5 = new RecordType(registry, properties3);
        assertEquals(recordType5.toMaybeObjectType(), recordType3.getLeastSupertype(recordType4));
        assertEquals(recordType5.toMaybeObjectType(), recordType4.getLeastSupertype(recordType3));
    }

    @Test
    public void testIsSubtype() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, Maps.newTreeMap());
        RecordType recordType2 = new RecordType(registry, Maps.newTreeMap());
        assertTrue(recordType1.isSubtype(recordType2));

        // Test case 2
        Map<String, RecordProperty> properties1 = Maps.newTreeMap();
        properties1.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties1.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType3 = new RecordType(registry, properties1);
        assertFalse(recordType1.isSubtype(recordType3));
        assertTrue(recordType3.isSubtype(recordType1));

        // Test case 3
        Map<String, RecordProperty> properties2 = Maps.newTreeMap();
        properties2.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties2.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType4 = new RecordType(registry, properties2);
        assertTrue(recordType3.isSubtype(recordType4));
        assertFalse(recordType4.isSubtype(recordType3));
    }

    @Test
    public void testResolveInternal() {
        // Test case 1
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, Maps.newTreeMap());
        assertEquals(recordType1, recordType1.resolveInternal(null, null));

        // Test case 2
        Map<String, RecordProperty> properties = Maps.newTreeMap();
        properties.put("a", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties.put("b", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties);
        assertEquals(recordType2, recordType2.resolveInternal(null, null));
    }
}