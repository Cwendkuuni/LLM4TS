import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setup() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        String name = "property1";
        JSType type = new JSType();
        Node propertyNode = new Node();
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);

        assertNull(recordTypeBuilder.addProperty(name, type, propertyNode));
        assertFalse(recordTypeBuilder.isEmpty);
        assertTrue(recordTypeBuilder.properties.containsKey(name));
        assertEquals(recordProperty, recordTypeBuilder.properties.get(name));
    }

    @Test
    public void testAddDuplicateProperty() {
        String name = "property1";
        JSType type = new JSType();
        Node propertyNode = new Node();

        assertNull(recordTypeBuilder.addProperty(name, type, propertyNode));
        assertNull(recordTypeBuilder.addProperty(name, type, propertyNode));
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType objectType = new JSType();
        objectType.setNativeObjectType(JSTypeNative.OBJECT_TYPE);

        assertTrue(recordTypeBuilder.isEmpty);
        assertEquals(objectType, recordTypeBuilder.build());
    }

    @Test
    public void testBuildNonEmptyRecord() {
        String name = "property1";
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty(name, type, propertyNode);

        HashMap<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put(name, new RecordTypeBuilder.RecordProperty(type, propertyNode));

        assertFalse(recordTypeBuilder.isEmpty);
        assertEquals(properties, ((RecordType)recordTypeBuilder.build()).getProperties());
    }
}