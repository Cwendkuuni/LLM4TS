import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType type = new JSType();
        Node node = new Node();

        assertTrue(builder.addProperty("name", type, node) != null);
        assertTrue(builder.addProperty("age", type, node) != null);
        assertTrue(builder.addProperty("name", type, node) == null);
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder emptyBuilder = new RecordTypeBuilder(registry);
        RecordTypeBuilder nonEmptyBuilder = new RecordTypeBuilder(registry);

        JSType expectedEmptyType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType expectedNonEmptyType = new RecordType(registry, Collections.unmodifiableMap(new HashMap<String, RecordTypeBuilder.RecordProperty>() {{
            put("name", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
            put("age", new RecordTypeBuilder.RecordProperty(new JSType(), new Node()));
        }}));

        assertEquals(expectedEmptyType, emptyBuilder.build());
        assertEquals(expectedNonEmptyType, nonEmptyBuilder.addProperty("name", new JSType(), new Node()).addProperty("age", new JSType(), new Node()).build());
    }
}