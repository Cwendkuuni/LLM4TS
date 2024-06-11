import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test adding a property
        JSType propertyType = new JSType("String");
        Node propertyNode = new Node();
        RecordTypeBuilder result = builder.addProperty("name", propertyType, propertyNode);
        assertNull(result);
        assertEquals(JSTypeNative.OBJECT_TYPE, builder.build());

        // Test adding a duplicate property
        result = builder.addProperty("name", propertyType, propertyNode);
        assertNull(result);
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Test building an empty record
        JSType emptyRecord = builder.build();
        assertTrue(emptyRecord instanceof RecordType);
        assertEquals(JSTypeNative.OBJECT_TYPE, emptyRecord);

        // Test building a non-empty record
        JSType propertyType = new JSType("String");
        Node propertyNode = new Node();
        builder.addProperty("name", propertyType, propertyNode);
        JSType record = builder.build();
        assertTrue(record instanceof RecordType);
        assertEquals(propertyType, record.getPropertyType("name"));
    }
}