import org.junit.*;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testIsEquivalentTo() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
        prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", prop1);
        properties1.put("b", prop2);
        RecordType type1 = new RecordType(registry, properties1);

        RecordTypeBuilder.RecordProperty prop3 = new RecordTypeBuilder.RecordProperty();
        prop3.setType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        RecordTypeBuilder.RecordProperty prop4 = new RecordTypeBuilder.RecordProperty();
        prop4.setType(registry.getNativeType(JSTypeNative.NULL_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", prop3);
        properties2.put("b", prop4);
        RecordType type2 = new RecordType(registry, properties2);

        assertFalse(type1.isEquivalentTo(type2));
    }

    @Test
    public void testGetImplicitPrototype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
        prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", prop1);
        properties.put("b", prop2);
        RecordType type = new RecordType(registry, properties);

        ObjectType objType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertTrue(type.getImplicitPrototype().isEquivalentTo(objType));
    }

    @Test
    public void testDefineProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
        prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", prop1);
        properties.put("b", prop2);
        RecordType type = new RecordType(registry, properties);

        String propertyName = "c";
        JSType propertyType = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        boolean inferred = false;
        Node propertyNode = new Node();
        boolean result = type.defineProperty(propertyName, propertyType, inferred, propertyNode);

        assertTrue(result);
    }

    @Test
    public void testGetLeastSupertype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
        prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", prop1);
        properties1.put("b", prop2);
        RecordType type1 = new RecordType(registry, properties1);

        RecordTypeBuilder.RecordProperty prop3 = new RecordTypeBuilder.RecordProperty();
        prop3.setType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        RecordTypeBuilder.RecordProperty prop4 = new RecordTypeBuilder.RecordProperty();
        prop4.setType(registry.getNativeType(JSTypeNative.NULL_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", prop3);
        properties2.put("b", prop4);
        RecordType type2 = new RecordType(registry, properties2);

        JSType leastSupertype = type1.getLeastSupertype(type2);

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), leastSupertype);
    }

    @Test
    public void testToMaybeRecordType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
        prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", prop1);
        properties.put("b", prop2);
        RecordType type = new RecordType(registry, properties);

        assertTrue(type.toMaybeRecordType().isEquivalentTo(type));
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
        prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = new HashMap<>();
        properties1.put("a", prop1);
        properties1.put("b", prop2);
        RecordType type1 = new RecordType(registry, properties1);

        RecordTypeBuilder.RecordProperty prop3 = new RecordTypeBuilder.RecordProperty();
        prop3.setType(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE));
        RecordTypeBuilder.RecordProperty prop4 = new RecordTypeBuilder.RecordProperty();
        prop4.setType(registry.getNativeType(JSTypeNative.NULL_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = new HashMap<>();
        properties2.put("a", prop3);
        properties2.put("b", prop4);
        RecordType type2 = new RecordType(registry, properties2);

        assertFalse(type1.isSubtype(type2));
    }

    @Test
    public void testResolveInternal() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder.RecordProperty prop1 = new RecordTypeBuilder.RecordProperty();
        prop1.setType(registry.getNativeType(JSTypeNative.STRING_TYPE));
        RecordTypeBuilder.RecordProperty prop2 = new RecordTypeBuilder.RecordProperty();
        prop2.setType(registry.getNativeType(JSTypeNative.NUMBER_TYPE));
        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put("a", prop1);
        properties.put("b", prop2);
        RecordType type = new RecordType(registry, properties);

        StaticScope<JSType> scope = new StaticScope<>();
        ErrorReporter reporter = new ErrorReporter();
        JSType resolvedType = type.resolveInternal(reporter, scope);

        assertTrue(resolvedType.isEquivalentTo(type));
    }

}