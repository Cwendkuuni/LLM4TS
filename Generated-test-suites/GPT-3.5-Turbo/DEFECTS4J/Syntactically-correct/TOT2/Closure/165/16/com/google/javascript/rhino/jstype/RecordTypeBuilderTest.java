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
        RecordTypeBuilder result = recordTypeBuilder.addProperty("propertyName", type, propertyNode);
        assertNotNull(result);
    }

    @Test
    public void testAddDuplicateProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("duplicateName", new JSType(), new Node());
        RecordTypeBuilder result = recordTypeBuilder.addProperty("duplicateName", type, propertyNode);
        assertNull(result);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType result = recordTypeBuilder.build();
        assertNotNull(result);
    }

    @Test
    public void testBuildNonEmptyRecord() {
        recordTypeBuilder.addProperty("propertyName", new JSType(), new Node());
        JSType result = recordTypeBuilder.build();
        assertNotNull(result);
    }

    @Test
    public void testNativeObjectType() {
        JSType result = recordTypeBuilder.build();
        assertEquals(JSTypeNative.OBJECT_TYPE, result.getNativeType());
    }

    @Test
    public void testUnmodifiablePropertiesMap() {
        recordTypeBuilder.addProperty("propertyName", new JSType(), new Node());
        JSType result = recordTypeBuilder.build();
        assertTrue(result.getProperties().isUnmodifiable());
    }

}