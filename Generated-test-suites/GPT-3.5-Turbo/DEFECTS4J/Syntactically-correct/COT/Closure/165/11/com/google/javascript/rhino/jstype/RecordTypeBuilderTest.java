import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("propertyName", type, propertyNode);
        // Verify that the property has been added to the record type builder
        assertNotNull(recordTypeBuilder.properties.get("propertyName"));
    }

    @Test
    public void testBuild_emptyRecord() {
        JSType recordType = recordTypeBuilder.build();
        // Verify that an empty record type returns the object type
        assertEquals(JSTypeNative.OBJECT_TYPE, recordType);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("propertyName", type, propertyNode);
        JSType recordType = recordTypeBuilder.build();
        // Verify that a non-empty record type has been created
        assertNotNull(recordType);
        // Verify that the record type contains the added property
        assertEquals(type, recordType.getProperties().get("propertyName").getType());
    }
}