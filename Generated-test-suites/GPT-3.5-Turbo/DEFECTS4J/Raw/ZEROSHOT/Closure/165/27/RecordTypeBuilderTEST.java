import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder builder;

    @Before
    public void setUp() {
        builder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node();

        // Test adding a property that doesn't exist
        RecordTypeBuilder returnedBuilder = builder.addProperty("testProperty", type, propertyNode);
        assertNotNull(returnedBuilder);
        assertFalse(builder.isEmpty);

        // Test adding a duplicate property
        returnedBuilder = builder.addProperty("testProperty", type, propertyNode);
        assertNull(returnedBuilder);
    }

    @Test
    public void testBuild() {
        // Test building an empty record
        JSType emptyRecord = builder.build();
        assertNotNull(emptyRecord);
        assertTrue(emptyRecord instanceof ObjectType);

        // Test building a non-empty record
        JSTypeRegistry registry = new JSTypeRegistry();
        JSType type = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        Node propertyNode = new Node();
        builder.addProperty("testProperty", type, propertyNode);
        JSType nonEmptyRecord = builder.build();
        assertNotNull(nonEmptyRecord);
        assertTrue(nonEmptyRecord instanceof RecordType);
    }
}