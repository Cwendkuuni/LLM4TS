import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType numberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node(Node.Token.STRING);

        // Adding a property should return the builder itself
        assertNotNull(builder.addProperty("count", numberType, propertyNode));
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType numberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node(Node.Token.STRING);

        builder.addProperty("count", numberType, propertyNode);

        // Adding a duplicate property should return null
        assertNull(builder.addProperty("count", numberType, propertyNode));
    }

    @Test
    public void testBuildEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Building an empty record should return the object type
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), builder.build());
    }

    @Test
    public void testBuildNonEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType numberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node(Node.Token.STRING);

        builder.addProperty("count", numberType, propertyNode);

        // Building a non-empty record should return a RecordType instance
        assertTrue(builder.build() instanceof RecordType);
    }

    @Test
    public void testRecordPropertyMethods() {
        JSType numberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node(Node.Token.STRING);
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(numberType, propertyNode);

        // Test RecordProperty methods
        assertEquals(numberType, recordProperty.getType());
        assertEquals(propertyNode, recordProperty.getPropertyNode());
    }
}