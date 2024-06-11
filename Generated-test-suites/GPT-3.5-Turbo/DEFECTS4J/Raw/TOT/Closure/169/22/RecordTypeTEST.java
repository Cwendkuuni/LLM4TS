import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeTest {

    @Test
    public void testRecordType() {
        // Create a record type with properties a and b
        RecordType recordType = new RecordType();
        recordType.addProperty("a", Type.INT);
        recordType.addProperty("b", Type.STRING);

        // Test if recordType is an instance of PrototypeObjectType
        assertTrue(recordType instanceof PrototypeObjectType);

        // Test if recordType is an instance of RecordType
        assertTrue(recordType instanceof RecordType);

        // Test if recordType has property a
        assertTrue(recordType.hasProperty("a"));

        // Test if recordType does not have property c
        assertFalse(recordType.hasProperty("c"));

        // Test if property a has type INT
        JSType propertyType = recordType.getPropertyType("a");
        assertEquals(Type.INT, propertyType);

        // Test if property b has type STRING
        propertyType = recordType.getPropertyType("b");
        assertEquals(Type.STRING, propertyType);
    }
    
    @Test
    public void testDefineProperty() {
        // Create a record type with property a
        RecordType recordType = new RecordType();
        recordType.defineProperty("a", Type.INT, true, null);

        // Test if recordType has property a
        assertTrue(recordType.hasProperty("a"));

        // Test if property a has type INT
        JSType propertyType = recordType.getPropertyType("a");
        assertEquals(Type.INT, propertyType);

        // Test if recordType has property b
        assertFalse(recordType.hasProperty("b"));

        // Test if property b is not defined
        propertyType = recordType.getPropertyType("b");
        assertNull(propertyType);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testNullRecordProperty() {
        // Create a record type with a null RecordProperty
        Map<String, RecordProperty> properties = new HashMap<>();
        properties.put("a", null);
        RecordType recordType = new RecordType(properties);

        // This should throw an IllegalStateException
    }

    @Test
    public void testIsSubtype() {
        // Create two record types
        RecordType recordType1 = new RecordType();
        recordType1.addProperty("a", Type.NUMBER);
        recordType1.addProperty("b", Type.STRING);

        RecordType recordType2 = new RecordType();
        recordType2.addProperty("a", Type.NUMBER);

        // Test if recordType2 is a subtype of recordType1
        assertTrue(recordType2.isSubtype(recordType1));

        // Test if recordType1 is not a subtype of recordType2
        assertFalse(recordType1.isSubtype(recordType2));
    }

    @Test
    public void testGetGreatestSubtypeHelper() {
        // Create two record types
        RecordType recordType1 = new RecordType();
        recordType1.addProperty("a", Type.NUMBER);
        recordType1.addProperty("b", Type.STRING);

        RecordType recordType2 = new RecordType();
        recordType2.addProperty("a", Type.NUMBER);
        recordType2.addProperty("c", Type.BOOLEAN);

        // Get the greatest subtype of recordType1 and recordType2
        JSType greatestSubtype = recordType1.getGreatestSubtypeHelper(recordType2);

        // Test if the greatest subtype has property a
        assertTrue(greatestSubtype.hasProperty("a"));

        // Test if the greatest subtype does not have property c
        assertFalse(greatestSubtype.hasProperty("c"));
    }

}