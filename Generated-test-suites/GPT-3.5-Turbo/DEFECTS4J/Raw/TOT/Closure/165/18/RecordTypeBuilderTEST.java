import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty_existingName() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        builder.addProperty("name", new JSType(), new Node());
        assertNull(builder.addProperty("name", new JSType(), new Node()));
    }

    @Test
    public void testAddProperty_validInput() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType expectedType = new JSType();
        Node expectedNode = new Node();
        builder.addProperty("property", expectedType, expectedNode);
        RecordTypeBuilder.RecordProperty property = builder.getProperties().get("property");
        assertNotNull(property);
        assertEquals(expectedType, property.getType());
        assertEquals(expectedNode, property.getPropertyNode());
    }

    @Test
    public void testBuild_emptyType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(expectedType, builder.build());
    }

    @Test
    public void testBuild_nonEmptyType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType expectedType = new RecordType(registry, Collections.unmodifiableMap(builder.getProperties()));
        builder.addProperty("property", new JSType(), new Node());
        assertEquals(expectedType, builder.build());
    }
}