import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class RecordTypeTest {
    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>());
        assertTrue(recordType1.isEquivalentTo(recordType2));

        RecordProperty property1 = new RecordProperty();
        property1.setType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        RecordProperty property2 = new RecordProperty();
        property2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("property1", property1);
        properties.put("property2", property2);
        recordType1 = new RecordType(registry, properties);
        recordType2 = new RecordType(registry, properties);
        assertTrue(recordType1.isEquivalentTo(recordType2));

        property1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType1 = new RecordType(registry, new HashMap<String, RecordProperty>());
        RecordType recordType2 = new RecordType(registry, new HashMap<String, RecordProperty>());
        assertTrue(recordType1.isSubtype(recordType2));

        RecordProperty property1 = new RecordProperty();
        property1.setType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        RecordProperty property2 = new RecordProperty();
        property2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("property1", property1);
        properties1.put("property2", property2);
        recordType1 = new RecordType(registry, properties1);

        property1 = new RecordProperty();
        property1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        property2 = new RecordProperty();
        property2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("property1", property1);
        properties2.put("property2", property2);
        recordType2 = new RecordType(registry, properties2);

        assertTrue(recordType1.isSubtype(recordType2));
        assertFalse(recordType2.isSubtype(recordType1));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType recordType = new RecordType(registry, new HashMap<String, RecordProperty>());
        ErrorReporter errorReporter = new ErrorReporter(registry);
        StaticScope<JSType> scope = new StaticScope<>();
        JSType resolvedType = recordType.resolveInternal(errorReporter, scope);
        assertTrue(resolvedType instanceof RecordType);
        assertEquals(recordType, resolvedType);
    }
}