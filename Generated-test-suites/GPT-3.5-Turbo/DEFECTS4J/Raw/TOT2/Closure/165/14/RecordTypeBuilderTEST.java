import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        builder.addProperty("name", new JSType("String"), new Node(NodeType.PROPERTY));

        RecordTypeBuilder.RecordProperty property = builder.getProperties().get("name");

        assertNotNull(property);
        assertEquals("String", property.getType().getName());
    }

    @Test
    public void testEmptyRecordBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        JSType type = builder.build();

        assertEquals(JSTypeNative.OBJECT_TYPE, type.getName());
    }

    @Test
    public void testNonEmptyRecordBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);

        builder.addProperty("name", new JSType("String"), new Node(NodeType.PROPERTY));
        builder.addProperty("age", new JSType("Number"), new Node(NodeType.PROPERTY));

        JSType type = builder.build();

        RecordType recordType = (RecordType) type;

        assertNotNull(recordType.getProperty("name"));
        assertNotNull(recordType.getProperty("age"));
    }
}