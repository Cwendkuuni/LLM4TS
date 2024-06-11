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
        // Typical use case
        assertNotNull(recordTypeBuilder.addProperty("property1", new JSType(), new Node()));

        // Edge case - Adding duplicate property
        assertNull(recordTypeBuilder.addProperty("property1", new JSType(), new Node()));
    }

    @Test
    public void testBuild() {
        // Typical use case
        assertNotNull(recordTypeBuilder.build());

        // Edge case - Building empty record
        assertNotNull(recordTypeBuilder.build());
    }

    @Test
    public void testRecordPropertyGetType() {
        // Typical use case
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(new JSType(), new Node());
        assertEquals(new JSType(), recordProperty.getType());
    }

    @Test
    public void testRecordPropertyGetPropertyNode() {
        // Typical use case
        Node node = new Node();
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(new JSType(), node);
        assertEquals(node, recordProperty.getPropertyNode());
    }
}