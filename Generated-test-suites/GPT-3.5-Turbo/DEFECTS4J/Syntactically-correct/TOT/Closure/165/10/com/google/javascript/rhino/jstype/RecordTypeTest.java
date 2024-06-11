import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeTest {
    
    private RecordType recordType;
    private JSTypeRegistry registry;
    private Map<String, RecordProperty> properties;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        properties = new HashMap<>();
        recordType = new RecordType(registry, properties);
    }

    @Test
    public void testDefineProperty() {
        String propertyName = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        boolean inferred = false;
        Node propertyNode = new Node();

        boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);

        assertTrue(result);
        assertTrue(recordType.hasProperty(propertyName));
        assertEquals(type, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testDefineProperty_Inferred() {
        String propertyName = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        boolean inferred = true;
        Node propertyNode = new Node();

        boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);

        assertTrue(result);
        assertFalse(recordType.hasProperty(propertyName));
    }

    @Test
    public void testDefineProperty_Frozen() {
        String propertyName = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        boolean inferred = false;
        Node propertyNode = new Node();

        recordType.setFrozen(true);
        boolean result = recordType.defineProperty(propertyName, type, inferred, propertyNode);

        assertFalse(result);
        assertFalse(recordType.hasProperty(propertyName));
    }

    @Test
    public void testDefineDeclaredProperty() {
        String propertyName = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node();

        recordType.defineDeclaredProperty(propertyName, type, propertyNode);

        assertTrue(recordType.hasProperty(propertyName));
        assertEquals(type, recordType.getPropertyType(propertyName));
    }

    @Test(expected = IllegalStateException.class)
    public void testDefineDeclaredProperty_NullProperty() {
        String propertyName = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node();
        RecordProperty prop = null;

        properties.put(propertyName, prop);
        recordType.defineDeclaredProperty(propertyName, type, propertyNode);
    }

    @Test
    public void testGetGreatestSubtypeHelper_BothRecordTypes() {
        RecordType thatRecord = new RecordType(registry, properties);

        JSType result = recordType.getGreatestSubtypeHelper(thatRecord);

        assertEquals(recordType, result);
    }

    @Test
    public void testGetGreatestSubtypeHelper_RecordAndUndefinedType() {
        JSType that = registry.getNativeType(JSTypeNative.UNDEFINED_TYPE);

        JSType result = recordType.getGreatestSubtypeHelper(that);

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), result);
        assertTrue(result.isEmptyType());
    }

    @Test
    public void testGetGreatestSubtypeHelper_RecordAndObjectType() {
        ObjectType that = new ObjectType(registry, null, null);

        JSType result = recordType.getGreatestSubtypeHelper(that);

        assertEquals(registry.getNativeType(JSTypeNative.NO_OBJECT_TYPE), result);
        assertTrue(result.isEmptyType());
    }

    @Test
    public void testGetGreatestSubtypeHelper_RecordAndRecordTypes_DifferentProperties() {
        String propA = "a";
        JSType propAType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        RecordProperty recordPropertyA = new RecordProperty(propAType, null);

        properties.put(propA, recordPropertyA);

        RecordType thatRecord = new RecordType(registry, properties);

        JSType result = recordType.getGreatestSubtypeHelper(thatRecord);

        assertEquals(registry.getNativeType(JSTypeNative.NO_TYPE), result);
    }

    @Test
    public void testGetGreatestSubtypeHelper_RecordAndRecordTypes_SameProperties() {
        String propA = "a";
        JSType propAType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        RecordProperty recordPropertyA = new RecordProperty(propAType, null);

        properties.put(propA, recordPropertyA);

        Map<String, RecordProperty> thatProperties = new HashMap<>();
        thatProperties.put(propA, recordPropertyA);
        RecordType thatRecord = new RecordType(registry, thatProperties);

        JSType result = recordType.getGreatestSubtypeHelper(thatRecord);

        assertEquals(recordType, result);
    }

    @Test
    public void testIsEquivalentTo_EqualRecordTypes() {
        RecordType other = new RecordType(registry, properties);

        boolean result = recordType.isEquivalentTo(other);

        assertTrue(result);
    }

    @Test
    public void testIsEquivalentTo_DifferentRecordTypes() {
        RecordType other = new RecordType(registry, new HashMap<>());

        boolean result = recordType.isEquivalentTo(other);

        assertFalse(result);
    }

    @Test
    public void testIsEquivalentTo_NotRecordType() {
        JSType other = registry.getNativeType(JSTypeNative.NUMBER_TYPE);

        boolean result = recordType.isEquivalentTo(other);

        assertFalse(result);
    }

    @Test
    public void testIsSubtype_SameRecordTypes() {
        RecordType other = new RecordType(registry, properties);

        boolean result = recordType.isSubtype(other);

        assertTrue(result);
    }

    @Test
    public void testIsSubtype_SubtypeRecordType() {
        Map<String, RecordProperty> otherProperties = new HashMap<>();
        String propA = "a";
        JSType propAType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        RecordProperty recordPropertyA = new RecordProperty(propAType, null);
        otherProperties.put(propA, recordPropertyA);

        RecordType other = new RecordType(registry, otherProperties);

        boolean result = recordType.isSubtype(other);

        assertTrue(result);
    }

    @Test
    public void testIsSubtype_NotSubtypeRecordType() {
        Map<String, RecordProperty> otherProperties = new HashMap<>();
        String propA = "a";
        JSType propAType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        RecordProperty recordPropertyA = new RecordProperty(propAType, null);
        otherProperties.put(propA, recordPropertyA);
        RecordType other = new RecordType(registry, otherProperties);

        boolean result = other.isSubtype(recordType);

        assertFalse(result);
    }

    @Test
    public void testResolveInternal_PropertyTypeResolved() {
        String propertyName = "testProperty";
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node();

        recordType.defineDeclaredProperty(propertyName, type, propertyNode);
        recordType.resolveInternal(null, null);

        assertNotSame(type, recordType.getPropertyType(propertyName));
    }

    @Test
    public void testResolveInternal_PropertyTypeNotResolved() {
        String propertyName = "testProperty";
        JSType type = registry.createGenericType("T");
        Node propertyNode = new Node();

        recordType.defineDeclaredProperty(propertyName, type, propertyNode);
        recordType.resolveInternal(null, null);

        assertSame(type, recordType.getPropertyType(propertyName));
    }
}