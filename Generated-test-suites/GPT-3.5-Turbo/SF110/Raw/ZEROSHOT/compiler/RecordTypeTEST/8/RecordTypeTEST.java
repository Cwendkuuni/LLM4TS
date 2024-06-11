import org.junit.Assert;
import org.junit.Test;

public class RecordTypeTest {

    @Test
    public void testConstructor1() {
        // Create a RecordType with empty properties
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);

        // Verify that the RecordType is declared and not synthetic
        Assert.assertTrue(recordType.isDeclared());
        Assert.assertFalse(recordType.isSynthetic());
    }

    @Test
    public void testConstructor2() {
        // Create a RecordType with properties
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("string", propertyNode));
        properties.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType = new RecordType(registry, properties);

        // Verify that the RecordType is not declared and not synthetic
        Assert.assertFalse(recordType.isDeclared());
        Assert.assertFalse(recordType.isSynthetic());

        // Verify that the properties are defined correctly
        Assert.assertEquals("string", recordType.getPropertyType("name"));
        Assert.assertEquals("number", recordType.getPropertyType("age"));
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorNullProperty() {
        // Create a RecordType with a null property
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", null);
        RecordType recordType = new RecordType(registry, properties);
    }

    @Test
    public void testIsSubtype() {
        // Create two RecordTypes with the same properties
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty("string", propertyNode));
        properties1.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("name", new RecordProperty("string", propertyNode));
        properties2.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType2 = new RecordType(registry, properties2);

        // Verify that recordType2 is a subtype of recordType1
        Assert.assertTrue(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeEmptyRecord() {
        // Create a RecordType with empty properties
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);

        // Create a record type with one property
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("string", propertyNode));
        RecordType recordType2 = new RecordType(registry, properties);

        // Verify that recordType2 is a subtype of the empty record
        Assert.assertTrue(recordType.isSubtype(recordType2));
    }

    @Test
    public void testIsSubtypeObjectType() {
        // Create a RecordType with properties
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("string", propertyNode));
        properties.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType = new RecordType(registry, properties);

        // Create an ObjectType
        ObjectType objectType = new ObjectType(registry, null, null);

        // Verify that the ObjectType is a subtype of the RecordType
        Assert.assertTrue(recordType.isSubtype(objectType));
    }

    @Test
    public void testDefineProperty() {
        // Create a RecordType with empty properties
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);

        // Define a new property with a type and a property node
        boolean defined = recordType.defineProperty("name", "string", true, propertyNode);

        // Verify that the property is defined correctly
        Assert.assertTrue(defined);
        Assert.assertEquals("string", recordType.getPropertyType("name"));
    }

    @Test
    public void testDefinePropertyFrozen() {
        // Create a RecordType with empty properties and freeze it
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);
        recordType.setFrozen(true);

        // Define a new property with a type and a property node
        boolean defined = recordType.defineProperty("name", "string", true, propertyNode);

        // Verify that the property is not defined since the record type is frozen
        Assert.assertFalse(defined);
    }

    @Test
    public void testGetGreatestSubtypeHelperRecordType() {
        // Create two RecordTypes with properties
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty("string", propertyNode));
        properties1.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("name", new RecordProperty("string", propertyNode));
        properties2.put("city", new RecordProperty("string", propertyNode));
        RecordType recordType2 = new RecordType(registry, properties2);

        // Get the greatest subtype of recordType1 and recordType2
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        // Verify that the greatest subtype contains the common properties
        Assert.assertEquals("string", greatestSubtype.getPropertyType("name"));
    }

    @Test
    public void testGetGreatestSubtypeHelperObjectType() {
        // Create a RecordType with properties
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", new RecordProperty("string", propertyNode));
        properties.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType = new RecordType(registry, properties);

        // Create an ObjectType
        ObjectType objectType = new ObjectType(registry, null, null);
        objectType.defineProperty("address", "string", true, propertyNode);

        // Get the greatest subtype of recordType and objectType
        JSType greatestSubtype = recordType.getGreatestSubtypeHelper(objectType);

        // Verify that the greatest subtype contains the properties of both types
        Assert.assertEquals("string", greatestSubtype.getPropertyType("name"));
        Assert.assertEquals("number", greatestSubtype.getPropertyType("age"));
        Assert.assertEquals("string", greatestSubtype.getPropertyType("address"));
    }

    @Test
    public void testToMaybeRecordType() {
        // Create a RecordType with empty properties
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);

        // Cast the RecordType to a MaybeRecordType
        MaybeRecordType maybeRecordType = recordType.toMaybeRecordType();

        // Verify that the maybeRecordType is the original RecordType
        Assert.assertEquals(recordType, maybeRecordType);
    }

    @Test
    public void testIsDeclared() {
        // Create a RecordType with empty properties
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);

        // Verify that the RecordType is declared
        Assert.assertTrue(recordType.isDeclared());
    }

    @Test
    public void testIsSynthetic() {
        // Create a RecordType with empty properties
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);

        // Verify that the RecordType is not synthetic
        Assert.assertFalse(recordType.isSynthetic());
    }

    @Test(expected = IllegalStateException.class)
    public void testIsInvariantNullProperty() {
        // Create a RecordType with empty properties
        RecordType recordType = new RecordType(registry, new HashMap<>(), true);

        // Create a RecordType with a null property
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("name", null);
        RecordType recordType2 = new RecordType(registry, properties);

        // Check if recordType2 is invariant to recordType
        recordType2.isInvariant(recordType);
    }

    @Test
    public void testCheckRecordEquivalenceHelper() {
        // Create two RecordTypes with the same properties
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty("string", propertyNode));
        properties1.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("name", new RecordProperty("string", propertyNode));
        properties2.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType2 = new RecordType(registry, properties2);

        // Check record equivalence helper
        boolean equivalent = recordType1.checkRecordEquivalenceHelper(recordType2, eqMethod);

        // Verify that the two RecordTypes are equivalent
        Assert.assertTrue(equivalent);
    }

    @Test
    public void testCheckRecordEquivalenceHelperDifferentProperties() {
        // Create two RecordTypes with different properties
        Map<String, RecordProperty> properties1 = new HashMap<>();
        properties1.put("name", new RecordProperty("string", propertyNode));
        properties1.put("age", new RecordProperty("number", propertyNode));
        RecordType recordType1 = new RecordType(registry, properties1);

        Map<String, RecordProperty> properties2 = new HashMap<>();
        properties2.put("name", new RecordProperty("string", propertyNode));
        properties2.put("city", new RecordProperty("string", propertyNode));
        RecordType recordType2 = new RecordType(registry, properties2);

        // Check record equivalence helper
        boolean equivalent = recordType1.checkRecordEquivalenceHelper(recordType2, eqMethod);

        // Verify that the two RecordTypes are not equivalent
        Assert.assertFalse(equivalent);
    }


}