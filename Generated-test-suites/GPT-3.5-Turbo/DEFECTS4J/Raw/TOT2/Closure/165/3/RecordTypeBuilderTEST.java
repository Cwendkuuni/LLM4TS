import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        RecordTypeBuilder result = recordTypeBuilder.addProperty("name", type, propertyNode);
        assertNotNull(result);
    }

    @Test
    public void testAddDuplicateProperty() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("name", type, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty("name", type, propertyNode);
        assertNull(result);
    }

    @Test
    public void testEmptyRecordBuild() {
        JSType result = recordTypeBuilder.build();
        assertTrue(result instanceof ObjectType);
    }

    @Test
    public void testNonEmptyRecordBuild() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("name", type, propertyNode);
        JSType result = recordTypeBuilder.build();
        assertTrue(result instanceof RecordType);
    }

    @Test
    public void testEmptyRecordWithGetProperty() {
        JSType result = recordTypeBuilder.build();
        assertTrue(result.getProperties().isEmpty());
    }

    @Test
    public void testNonEmptyRecordWithGetProperty() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("name", type, propertyNode);
        JSType result = recordTypeBuilder.build();
        assertEquals(1, result.getProperties().size());
    }

    @Test(expected = NullPointerException.class)
    public void testAddPropertyWithNullType() {
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("name", null, propertyNode);
    }

    @Test(expected = NullPointerException.class)
    public void testAddPropertyWithNullNode() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        recordTypeBuilder.addProperty("name", type, null);
    }

}