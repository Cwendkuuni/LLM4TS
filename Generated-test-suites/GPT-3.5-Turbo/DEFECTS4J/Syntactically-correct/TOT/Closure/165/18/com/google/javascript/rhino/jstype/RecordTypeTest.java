import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testDefineProperty() {
        // Test case for defining a property in RecordType
        // Arrange
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType = new RecordType(registry, properties);

        // Act
        recordType.defineProperty("age", registry.getNativeType(JSTypeNative.NUMBER_TYPE), false, null);

        // Assert
        assertNotNull(recordType.getPropertyType("age"));
        assertEquals(registry.getNativeType(JSTypeNative.NUMBER_TYPE), recordType.getPropertyType("age"));
    }

    @Test
    public void testIsSubtype() {
        // Test case for checking if a type is a subtype of RecordType
        // Arrange
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType = new RecordType(registry, properties);

        ObjectType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);

        // Act & Assert
        assertTrue(recordType.isSubtype(objectType));  // RecordType is a subtype of OBJECT_TYPE
        assertFalse(recordType.isSubtype(registry.getNativeType(JSTypeNative.BOOLEAN_TYPE)));  // RecordType is not a subtype of BOOLEAN_TYPE
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        // Test case for getting the greatest subtype of RecordType
        // Arrange
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("age", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("name", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties2.put("gender", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);

        // Act
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        // Assert
        assertEquals(recordType1, greatestSubtype);
    }

    @Test
    public void testIsEquivalentTo() {
        // Test case for checking if two types are equivalent
        // Arrange
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        properties1.put("age", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("age", new RecordProperty(registry.getNativeType(JSTypeNative.NUMBER_TYPE), null));
        properties2.put("name", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType2 = new RecordType(registry, properties2);

        // Act & Assert
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    @Test
    public void testResolveInternal() {
        // Test case for resolving internal properties in RecordType
        // Arrange
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty(registry.getNativeType(JSTypeNative.STRING_TYPE), null));
        RecordType recordType = new RecordType(registry, properties);
        ErrorReporter errorReporter = new ErrorReporter();

        // Act
        JSType resolvedType = recordType.resolveInternal(errorReporter, new StaticScope<JSType>());
        
        // Assert
        assertNotNull(resolvedType);
        assertEquals(recordType, resolvedType);
    }
}