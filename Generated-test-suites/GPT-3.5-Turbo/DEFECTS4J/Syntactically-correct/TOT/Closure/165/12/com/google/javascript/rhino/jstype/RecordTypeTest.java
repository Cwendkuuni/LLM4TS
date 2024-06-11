import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeTest {

    // Test case for defineDeclaredProperty
    @Test
    public void testDefineDeclaredProperty() {
        RecordType recordType = new RecordType();
        boolean result = recordType.defineProperty("testProperty", "testType", false, Node.EMPTY);
        assertTrue(result);
        assertEquals("testType", recordType.getPropertyType("testProperty"));
    }

    // Test case for defineDeclaredProperty with inferred property
    @Test
    public void testDefineDeclaredPropertyInferred() {
        RecordType recordType = new RecordType();
        boolean result = recordType.defineProperty("testProperty", "testType", true, Node.EMPTY);
        assertFalse(result);
        assertNull(recordType.getPropertyType("testProperty"));
    }

    // Test case for RecordType constructor with valid properties
    @Test
    public void testConstructorValidProperties() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("propertyA", new RecordProperty("typeA", Node.EMPTY));
        properties.put("propertyB", new RecordProperty("typeB", Node.EMPTY));
        RecordType recordType = new RecordType(properties);
        assertEquals("typeA", recordType.getPropertyType("propertyA"));
        assertEquals("typeB", recordType.getPropertyType("propertyB"));
    }

    // Test case for RecordType constructor with null property
    @Test(expected = IllegalStateException.class)
    public void testConstructorNullProperty() {
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("propertyA", null);
        RecordType recordType = new RecordType(properties);
    }

    // Test case for isEquivalentTo when comparing with equivalent record type
    @Test
    public void testIsEquivalentToEquivalent() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        assertTrue(recordType1.isEquivalentTo(recordType2));
    }

    // Test case for isEquivalentTo when comparing with non-equivalent record type
    @Test
    public void testIsEquivalentToNonEquivalent() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("propertyB", "typeB", false, Node.EMPTY);
        assertFalse(recordType1.isEquivalentTo(recordType2));
    }

    // Test case for getImplicitPrototype
    @Test
    public void testGetImplicitPrototype() {
        RecordType recordType = new RecordType();
        assertEquals("Object", recordType.getImplicitPrototype().toString());
    }

    // Test case for getGreatestSubtypeHelper when given a record type
    @Test
    public void testGetGreatestSubtypeHelperRecordType() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("propertyB", "typeB", false, Node.EMPTY);
        JSType result = recordType1.getGreatestSubtypeHelper(recordType2);
        assertEquals("RecordType", result.toString());
        assertEquals("typeA", result.getPropertyType("propertyA"));
        assertEquals("typeB", result.getPropertyType("propertyB"));
    }

    // Test case for getGreatestSubtypeHelper when given an object type
    @Test
    public void testGetGreatestSubtypeHelperObjectType() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        ObjectType objectType = new ObjectType();
        objectType.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        JSType result = recordType.getGreatestSubtypeHelper(objectType);
        assertEquals("ObjectType", result.toString());
    }

    // Test case for isSubtype when given an equivalent record type
    @Test
    public void testIsSubtypeEquivalentRecordType() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        assertTrue(recordType1.isSubtype(recordType2));
    }

    // Test case for isSubtype when given a non-equivalent record type
    @Test
    public void testIsSubtypeNonEquivalentRecordType() {
        RecordType recordType1 = new RecordType();
        recordType1.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        RecordType recordType2 = new RecordType();
        recordType2.defineProperty("propertyB", "typeB", false, Node.EMPTY);
        assertFalse(recordType1.isSubtype(recordType2));
    }

    // Test case for isSubtype when given an object type
    @Test
    public void testIsSubtypeObjectType() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        ObjectType objectType = new ObjectType();
        objectType.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        assertTrue(recordType.isSubtype(objectType));
    }

    // Test case for isSubtype when given a non-subtype object type
    @Test
    public void testIsSubtypeNonSubtypeObjectType() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        ObjectType objectType = new ObjectType();
        objectType.defineProperty("propertyA", "typeB", false, Node.EMPTY);
        assertFalse(recordType.isSubtype(objectType));
    }

    // Test case for resolveInternal when type needs resolution
    @Test
    public void testResolveInternalNeedsResolution() {
        RecordType recordType = new RecordType();
        recordType.defineProperty("propertyA", "typeA", false, Node.EMPTY);
        JSType resolvedType = recordType.resolveInternal(ErrorReporter.NULL_REPORTER, null);
        assertEquals("ResolvedRecordType", resolvedType.toString());
        assertEquals("typeA", resolvedType.getPropertyType("propertyA"));
    }

    // Test case for resolveInternal when type does not need resolution
    @Test
    public void testResolveInternalDoesNotNeedResolution() {
        RecordType recordType = new RecordType();
        recordType.freeze();
        JSType resolvedType = recordType.resolveInternal(ErrorReporter.NULL_REPORTER, null);
        assertEquals("RecordType", resolvedType.toString());
    }
}