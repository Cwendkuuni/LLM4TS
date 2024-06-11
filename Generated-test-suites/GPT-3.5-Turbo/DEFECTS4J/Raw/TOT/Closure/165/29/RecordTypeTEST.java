import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        // Create an instance of the JSTypeRegistry
        JSTypeRegistry registry = new JSTypeRegistry();

        // Create a map of properties for the RecordType
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", new RecordProperty(JSTypeNative.STRING_TYPE, null));
        properties.put("b", new RecordProperty(JSTypeNative.NUMBER_TYPE, null));

        // Create an instance of the RecordType
        RecordType recordType = new RecordType(registry, properties);

        // Test getPropertyType() method
        assertEquals(recordType.getPropertyType("a"), JSTypeNative.STRING_TYPE);
        assertEquals(recordType.getPropertyType("b"), JSTypeNative.NUMBER_TYPE);

        // Test isEquivalentTo() method
        assertTrue(recordType.isEquivalentTo(recordType));

        // Test getImplicitPrototype() method
        assertEquals(recordType.getImplicitPrototype(), registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

        // Test isSubtype() method
        assertTrue(recordType.isSubtype(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)));

        // Test resolveInternal() method
        assertEquals(recordType.resolveInternal(), recordType);

    }

}