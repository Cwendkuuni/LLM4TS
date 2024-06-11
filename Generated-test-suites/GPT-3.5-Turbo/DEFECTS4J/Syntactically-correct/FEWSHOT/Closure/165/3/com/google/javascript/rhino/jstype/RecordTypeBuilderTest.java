import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setup() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();

        assertNull(recordTypeBuilder.addProperty("foo", type, propertyNode));

        assertTrue(recordTypeBuilder.addProperty("bar", type, propertyNode) instanceof RecordTypeBuilder);

        assertNull(recordTypeBuilder.addProperty("bar", type, propertyNode));
    }

    @Test
    public void testBuild() {
        JSType objectType = new JSType();
        objectType.setNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(objectType, recordTypeBuilder.build());

        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("foo", type, propertyNode);

        RecordType recordType = new RecordTypeBuilder(new JSTypeRegistry()).addProperty("foo", type, propertyNode).build();
        assertEquals(recordType, recordTypeBuilder.build());
    }

    @Test
    public void testRecordProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();

        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);

        assertEquals(type, recordProperty.getType());
        assertEquals(propertyNode, recordProperty.getPropertyNode());
    }
}