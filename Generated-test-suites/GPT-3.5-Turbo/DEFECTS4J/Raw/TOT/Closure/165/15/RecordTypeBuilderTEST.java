import org.junit.Test;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty_typicalUseCase() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Add a property with typical use case values
        String name = "property";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node(Node.TYPEOF, new Node(Node.NAME, "property"));

        RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);

        assertNotNull(result);
        assertEquals(1, builder.getProperties().size());
        assertTrue(builder.getProperties().containsKey(name));
        assertEquals(type, builder.getProperties().get(name).getType());
        assertEquals(propertyNode, builder.getProperties().get(name).getPropertyNode());
    }

    @Test
    public void testAddProperty_duplicateProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Add a property
        String name = "property";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node(Node.TYPEOF, new Node(Node.NAME, "property"));
        builder.addProperty(name, type, propertyNode);

        // Try to add a duplicate property
        RecordTypeBuilder result = builder.addProperty(name, type, propertyNode);

        assertNull(result);
        assertEquals(1, builder.getProperties().size());
        assertTrue(builder.getProperties().containsKey(name));
        assertEquals(type, builder.getProperties().get(name).getType());
        assertEquals(propertyNode, builder.getProperties().get(name).getPropertyNode());
    }

    @Test
    public void testBuild_emptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType result = builder.build();

        assertNotNull(result);
        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), result);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        // Add a property to the builder
        String name = "property";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node(Node.TYPEOF, new Node(Node.NAME, "property"));
        builder.addProperty(name, type, propertyNode);

        JSType result = builder.build();

        assertNotNull(result);
        assertTrue(result instanceof RecordType);
        assertEquals(type, result.getPropertyType(name));
    }
}