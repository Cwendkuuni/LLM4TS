import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private JSTypeRegistry registry;
    private RecordType recordType;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
    }

    @Test
    public void testRecordTypeConstructor() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertNotNull(recordType);
        assertEquals(registry.getNativeObjectType("Object"), recordType.getImplicitPrototype());
        assertTrue(recordType.isDeclared());
    }

    @Test
    public void testRecordTypeConstructorWithDeclaredFalse() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties, false);

        assertNotNull(recordType);
        assertEquals(registry.getNativeObjectType("Object"), recordType.getImplicitPrototype());
        assertFalse(recordType.isDeclared());
    }

    @Test(expected = IllegalStateException.class)
    public void testRecordTypeConstructorWithNullProperty() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", null);
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put("name", new RecordProperty("John", null));
        otherProperties.put("age", new RecordProperty(25, null));
        RecordType otherRecord = new RecordType(registry, otherProperties);

        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.EQUALS));
    }

    @Test
    public void testRecordTypeIsSubtype() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertTrue(recordType.isSubtype(registry.getNativeObjectType("Object")));
    }

    @Test
    public void testRecordTypeIsSubtypeWithNonRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertFalse(recordType.isSubtype(registry.getNativeType("Number")));
    }

    @Test
    public void testRecordTypeIsSubtypeWithRecordTypeWithSameProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> subtypeProperties = new HashMap<>();
        subtypeProperties.put("name", new RecordProperty("John", null));
        subtypeProperties.put("age", new RecordProperty(25, null));
        RecordType subtypeRecord = new RecordType(registry, subtypeProperties);

        assertTrue(recordType.isSubtype(subtypeRecord));
    }

    @Test
    public void testRecordTypeIsSubtypeWithRecordTypeWithDifferentProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> subtypeProperties = new HashMap<>();
        subtypeProperties.put("name", new RecordProperty("John", null));
        subtypeProperties.put("height", new RecordProperty(180, null));
        RecordType subtypeRecord = new RecordType(registry, subtypeProperties);

        assertFalse(recordType.isSubtype(subtypeRecord));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertTrue(recordType.getGreatestSubtypeHelper(registry.getNativeObjectType("Object")).isEmptyType());
    }

    @Test
    public void testGetGreatestSubtypeHelperWithNonRecordType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertTrue(recordType.getGreatestSubtypeHelper(registry.getNativeType("Number")).isEmptyType());
    }

    @Test
    public void testGetPropertyType() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertEquals(registry.getNativeType("String"), recordType.getPropertyType("name"));
        assertEquals(registry.getNativeType("Number"), recordType.getPropertyType("age"));
    }

    @Test
    public void testIsSynthetic() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testDefinePropertyWithFrozenIsTrue() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        assertFalse(recordType.defineProperty("height", registry.getNativeType("Number"), false, null));
    }

    @Test
    public void testDefinePropertyWithFrozenIsFalse() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties, false);

        assertTrue(recordType.defineProperty("height", registry.getNativeType("Number"), false, null));
    }

    @Test
    public void testIsSubtypeWithSpecificProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> subtypeProperties = new HashMap<>();
        subtypeProperties.put("name", new RecordProperty("John", null));
        RecordType subtypeRecord = new RecordType(registry, subtypeProperties);

        assertFalse(recordType.isSubtype(subtypeRecord));
    }

    @Test
    public void testIsSubtypeWithEquivalentProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> subtypeProperties = new HashMap<>();
        subtypeProperties.put("name", new RecordProperty("John", null));
        subtypeProperties.put("age", new RecordProperty(30, null));
        RecordType subtypeRecord = new RecordType(registry, subtypeProperties);

        assertTrue(recordType.isSubtype(subtypeRecord));
    }

    @Test
    public void testIsSubtypeWithNoMatchingProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> subtypeProperties = new HashMap<>();
        subtypeProperties.put("address", new RecordProperty("123 Street", null));
        RecordType subtypeRecord = new RecordType(registry, subtypeProperties);

        assertFalse(recordType.isSubtype(subtypeRecord));
    }

    @Test
    public void testIsSubtypeWithIncompatiblePropertyTypes() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> subtypeProperties = new HashMap<>();
        subtypeProperties.put("name", new RecordProperty(30, null));
        subtypeProperties.put("age", new RecordProperty(30, null));
        RecordType subtypeRecord = new RecordType(registry, subtypeProperties);

        assertFalse(recordType.isSubtype(subtypeRecord));
    }

    @Test
    public void testIsSubtypeWithIncompatiblePropertyTypesAndNoMatchingProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("John", null));
        properties.put("age", new RecordProperty(25, null));
        recordType = new RecordType(registry, properties);

        Map<String, RecordProperty> subtypeProperties = new HashMap<>();
        subtypeProperties.put("address", new RecordProperty("123 Street", null));
        subtypeProperties.put("height", new RecordProperty(180, null));
        RecordType subtypeRecord = new RecordType(registry, subtypeProperties);

        assertFalse(recordType.isSubtype(subtypeRecord));
    }
}