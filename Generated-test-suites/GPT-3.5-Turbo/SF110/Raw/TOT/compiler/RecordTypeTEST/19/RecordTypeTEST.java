import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.RecordProperty;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {

    private RecordType recordType;

    @Before
    public void setUp() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Map<String, RecordProperty> properties = new HashMap<>();

        // Test 1: Test with empty properties
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSynthetic() {
        // Test 2: Test when the record type is synthetic
        assertTrue(recordType.isSynthetic());
    }

    @Test
    public void testGetPropertyType() {
        // Test 3: Test getPropertyType when the property exists
        String property = "name";
        JSType expectedType = new JSType(registry, "string");
        properties.put(property, new RecordProperty(expectedType, null));
        JSType actualType = recordType.getPropertyType(property);
        assertEquals(expectedType, actualType);

        // Test 4: Test getPropertyType when the property does not exist
        String nonExistingProperty = "age";
        assertNull(recordType.getPropertyType(nonExistingProperty));
    }

    @Test
    public void testIsSubtype() {
        // Test 5: Test isSubtype when the types are the same
        RecordType sameType = new RecordType(registry, properties);
        assertTrue(recordType.isSubtype(sameType));

        // Test 6: Test isSubtype when the types have the same properties and types
        String property = "name";
        JSType propertyType = new JSType(registry, "string");
        properties.put(property, new RecordProperty(propertyType, null));
        RecordType samePropertiesType = new RecordType(registry, properties);
        assertTrue(recordType.isSubtype(samePropertiesType));

        // Test 7: Test isSubtype when the types do not have the same properties
        Map<String, RecordProperty> otherProperties = new HashMap<>();
        String otherProperty = "age";
        JSType otherPropertyType = new JSType(registry, "number");
        otherProperties.put(otherProperty, new RecordProperty(otherPropertyType, null));
        RecordType differentPropertiesType = new RecordType(registry, otherProperties);
        assertFalse(recordType.isSubtype(differentPropertiesType));

        // Test 8: Test isSubtype when the types have different property types
        properties.put(otherProperty, new RecordProperty(propertyType, null));
        properties.put(property, new RecordProperty(otherPropertyType, null));
        RecordType differentPropertyTypesType = new RecordType(registry, properties);
        assertFalse(recordType.isSubtype(differentPropertyTypesType));
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        // Test 9: Test checkRecordEquivalenceHelper with equivalent records
        String property1 = "name";
        JSType property1Type = new JSType(registry, "string");
        properties.put(property1, new RecordProperty(property1Type, null));

        String property2 = "age";
        JSType property2Type = new JSType(registry, "number");
        properties.put(property2, new RecordProperty(property2Type, null));

        RecordType otherRecord = new RecordType(registry, properties);
        assertTrue(recordType.checkRecordEquivalenceHelper(otherRecord, EquivalenceMethod.DEFAULT));

        // Test 10: Test checkRecordEquivalenceHelper with non-equivalent records
        Map<String, RecordProperty> otherProperties = new HashMap<>();
        otherProperties.put(property1, new RecordProperty(property2Type, null));
        RecordType nonEquivalentRecord = new RecordType(registry, otherProperties);
        assertFalse(recordType.checkRecordEquivalenceHelper(nonEquivalentRecord, EquivalenceMethod.DEFAULT));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        // Test 11: Test getGreatestSubtypeHelper with that.isRecordType() true
        RecordType thatRecord = new RecordType(registry, properties);
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(thatRecord);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

        // Test 12: Test getGreatestSubtypeHelper with that.isRecordType() false
        JSType that = new JSType(registry, "string");
        greatestSubtype = recordType.getGreatestSubtypeHelper(that);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
    }

    @Test
    public void testIsFrozen() {
        // Test 13: Test isFrozen when the record type is frozen
        assertTrue(recordType.isFrozen());

        // Test 14: Test isFrozen when the record type is not frozen
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType notFrozenRecordType = new RecordType(registry, properties, false);
        assertFalse(notFrozenRecordType.isFrozen());
    }

    @Test
    public void testDefineProperty() {
        // Test 15: Test defineProperty when the record type is frozen
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordType frozenRecordType = new RecordType(registry, properties);
        boolean isPropertyDefined = frozenRecordType.defineProperty("age", new JSType(registry, "number"), false, null);
        assertFalse(isPropertyDefined);

        // Test 16: Test defineProperty when the record type is not frozen
        boolean notFrozenIsPropertyDefined = notFrozenRecordType.defineProperty("age", new JSType(registry, "number"), false, null);
        assertTrue(notFrozenIsPropertyDefined);
    }

    @Test
    public void testToMaybeRecordType() {
        // Test 17: Test toMaybeRecordType when the object is a record type
        assertEquals(recordType, recordType.toMaybeRecordType());

        // Test 18: Test toMaybeRecordType when the object is not a record type
        assertNull(notFrozenRecordType.toMaybeRecordType());
    }

    @Test
    public void testGetImplicitPrototype() {
        // Test 19: Test getImplicitPrototype
        ObjectType expectedPrototype = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        ObjectType actualPrototype = recordType.getImplicitPrototype();
        assertEquals(expectedPrototype, actualPrototype);
    }

    @Test
    public void testDefineDeclaredProperty() {
        // Test 20: Test defineDeclaredProperty
        String property = "name";
        JSType propertyType = new JSType(registry, "string");
        Node propertyNode = new Node();
        boolean isPropertyDefined = recordType.defineDeclaredProperty(property, propertyType, propertyNode);
        assertTrue(isPropertyDefined);
    }

    @Test
    public void testDefineSynthesizedProperty() {
        // Test 21: Test defineSynthesizedProperty
        String property = "age";
        JSType propertyType = new JSType(registry, "number");
        Node propertyNode = new Node();
        boolean isPropertyDefined = recordType.defineSynthesizedProperty(property, propertyType, propertyNode);
        assertTrue(isPropertyDefined);
    }

    @Test
    public void testGetOwnPropertyNames() {
        // Test 22: Test getOwnPropertyNames
        Set<String> expectedPropertyNames = properties.keySet();
        Set<String> actualPropertyNames = recordType.getOwnPropertyNames();
        assertEquals(expectedPropertyNames, actualPropertyNames);
    }

    @Test
    public void testHasProperty() {
        // Test 23: Test hasProperty when the property exists
        String property = "name";
        properties.put(property, new RecordProperty(new JSType(registry, "string"), null));
        assertTrue(recordType.hasProperty(property));

        // Test 24: Test hasProperty when the property does not exist
        String nonExistingProperty = "age";
        assertFalse(recordType.hasProperty(nonExistingProperty));
    }

    @Test
    public void testIsPropertyTypeDeclared() {
        // Test 25: Test isPropertyTypeDeclared when the property type is declared
        String property = "name";
        properties.put(property, new RecordProperty(new JSType(registry, "string"), null));
        assertTrue(recordType.isPropertyTypeDeclared(property));

        // Test 26: Test isPropertyTypeDeclared when the property type is not declared
        String nonDeclaredProperty = "age";
        assertFalse(recordType.isPropertyTypeDeclared(nonDeclaredProperty));
    }

    @Test
    public void testIsEquivalent() {
        // Test 27: Test isEquivalent when the types are equivalent
        RecordType equivalentType = new RecordType(registry, properties);
        assertTrue(recordType.isEquivalent(equivalentType));

        // Test 28: Test isEquivalent when the types are not equivalent
        String property = "name";
        JSType propertyType = new JSType(registry, "string");
        properties.put(property, new RecordProperty(propertyType, null));
        RecordType nonEquivalentType = new RecordType(registry, properties);
        assertFalse(recordType.isEquivalent(nonEquivalentType));
    }

    @Test
    public void testIsEmptyType() {
        // Test 29: Test isEmptyType when the type is empty
        RecordType emptyType = new RecordType(registry, new HashMap<>());
        assertTrue(emptyType.isEmptyType());

        // Test 30: Test isEmptyType when the type is not empty
        assertFalse(recordType.isEmptyType());
    }

    @Test
    public void testGetTypeOfProperty() {
        // Test 31: Test getTypeOfProperty when the property exists
        String property = "name";
        JSType expectedPropertyType = new JSType(registry, "string");
        properties.put(property, new RecordProperty(expectedPropertyType, null));
        JSType actualPropertyType = recordType.getTypeOfProperty(property);
        assertEquals(expectedPropertyType, actualPropertyType);

        // Test 32: Test getTypeOfProperty when the property does not exist
        String nonExistingProperty = "age";
        assertNull(recordType.getTypeOfProperty(nonExistingProperty));
    }

    @Test
    public void testGetGreatestSubtype() {
        // Test 33: Test getGreatestSubtype with that.isRecordType() true
        RecordType thatRecord = new RecordType(registry, properties);
        JSType greatestSubtype = recordType.getGreatestSubtype(thatRecord);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE), greatestSubtype);

        // Test 34: Test getGreatestSubtype with that.isRecordType() false
        JSType that = new JSType(registry, "string");
        greatestSubtype = recordType.getGreatestSubtype(that);
        assertEquals(registry.getNativeObjectType(JSTypeNative.NO_OBJECT_TYPE), greatestSubtype);
    }
}