
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        // Test case for adding a property
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(new JSType(), new Node());
        builder.addProperty("property1", property.getType(), property.getPropertyNode());
        assertNotNull(builder.build());
    }

    @Test
    public void testAddPropertyDuplicate() {
        // Test case for adding a duplicate property
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(new JSType(), new Node());
        builder.addProperty("property1", property.getType(), property.getPropertyNode());
        assertNull(builder.addProperty("property1", property.getType(), property.getPropertyNode()));
    }

    @Test
    public void testBuildEmptyRecord() {
        // Test case for building an empty record
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        JSType recordType = builder.build();
        assertTrue(recordType instanceof ObjectType);
        assertEquals(JSTypeNative.OBJECT_TYPE, ((ObjectType) recordType).getNativeObjectType());
    }

    @Test
    public void testBuildNonEmptyRecord() {
        // Test case for building a non-empty record
        RecordTypeBuilder builder = new RecordTypeBuilder(new JSTypeRegistry());
        RecordTypeBuilder.RecordProperty property = new RecordTypeBuilder.RecordProperty(new JSType(), new Node());
        builder.addProperty("property1", property.getType(), property.getPropertyNode());
        JSType recordType = builder.build();
        assertTrue(recordType instanceof RecordType);
        assertEquals(1, ((RecordType) recordType).getProperties().size());
    }
}
