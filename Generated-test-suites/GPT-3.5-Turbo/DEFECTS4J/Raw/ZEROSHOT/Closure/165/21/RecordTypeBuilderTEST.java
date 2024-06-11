import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashMap;
import java.util.Map;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        String name = "testProperty";
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);
        recordTypeBuilder.addProperty(name, type, propertyNode);

        Map<String, RecordTypeBuilder.RecordProperty> properties = new HashMap<>();
        properties.put(name, recordProperty);

        assertEquals(properties, recordTypeBuilder.getProperties());
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType expectedType = new JSType();
        JSType resultType = recordTypeBuilder.build();

        assertEquals(expectedType, resultType);
    }

    @Test
    public void testBuildNonEmptyRecord() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        String name = "testProperty";
        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(type, propertyNode);
        recordTypeBuilder.addProperty(name, type, propertyNode);

        JSType expectedType = new RecordType(new JSTypeRegistry(), recordTypeBuilder.getProperties());
        JSType resultType = recordTypeBuilder.build();

        assertEquals(expectedType, resultType);
    }
}