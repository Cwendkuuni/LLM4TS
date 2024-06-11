import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        // Typical use case
        String name = "property1";
        JSType type = new JSType();
        Node propertyNode = new Node();

        RecordTypeBuilder builder = recordTypeBuilder.addProperty(name, type, propertyNode);

        assertNotNull(builder);
        assertTrue(builder.isEmpty);

        // Edge case - adding a duplicate property
        builder = builder.addProperty(name, type, propertyNode);

        assertNull(builder);

        // Error scenario - adding a null property name should return null
        builder = recordTypeBuilder.addProperty(null, type, propertyNode);

        assertNull(builder);

        // Error scenario - adding a null JSType should return null
        builder = recordTypeBuilder.addProperty(name, null, propertyNode);

        assertNull(builder);
    }

    @Test
    public void testBuild() {
        // Typical use case
        RecordTypeBuilder builder = recordTypeBuilder.addProperty("property1", new JSType(), new Node());
        JSType result = builder.build();

        assertNotNull(result);
        assertTrue(result instanceof RecordType);

        // Edge case - build an empty record
        RecordTypeBuilder emptyBuilder = new RecordTypeBuilder(registry);
        JSType emptyResult = emptyBuilder.build();

        assertNotNull(emptyResult);
        assertTrue(emptyResult instanceof JSType);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), emptyResult);
    }
}