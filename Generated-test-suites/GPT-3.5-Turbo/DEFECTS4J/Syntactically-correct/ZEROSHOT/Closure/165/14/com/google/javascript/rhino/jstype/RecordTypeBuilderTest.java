import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setup() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType expectedType = new JSType();
        String name = "testName";
        Node propertyNode = new Node();

        RecordTypeBuilder returnedBuilder = recordTypeBuilder.addProperty(name, expectedType, propertyNode);

        Assert.assertNotNull(returnedBuilder);
        Assert.assertEquals(false, returnedBuilder.isEmpty);
        Assert.assertTrue(returnedBuilder.properties.containsKey(name));
        RecordTypeBuilder.RecordProperty recordProperty = returnedBuilder.properties.get(name);
        Assert.assertEquals(expectedType, recordProperty.getType());
        Assert.assertEquals(propertyNode, recordProperty.getPropertyNode());
    }

    @Test
    public void testBuildEmpty() {
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType actualType = recordTypeBuilder.build();
        Assert.assertEquals(expectedType, actualType);
    }

    @Test
    public void testBuild() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);

        String name = "testName";
        JSType expectedType = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty(name, expectedType, propertyNode);

        JSType actualType = recordTypeBuilder.build();

        HashMap<String, RecordTypeBuilder.RecordProperty> expectedProperties = new HashMap<>();
        expectedProperties.put(name, new RecordTypeBuilder.RecordProperty(expectedType, propertyNode));
        RecordType expectedRecordType = new RecordType(registry, Collections.unmodifiableMap(expectedProperties));
        Assert.assertEquals(expectedRecordType, actualType);
    }
}