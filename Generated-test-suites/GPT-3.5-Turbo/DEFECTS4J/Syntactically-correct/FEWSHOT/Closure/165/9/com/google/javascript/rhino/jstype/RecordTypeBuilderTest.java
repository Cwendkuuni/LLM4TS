import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {
    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry jsTypeRegistry;

    @Before
    public void setUp() {
        jsTypeRegistry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(jsTypeRegistry);
    }

    @Test
    public void testAddProperty() {
        String name = "property1";
        JSType type = jsTypeRegistry.getNativeNumberType();
        Node propertyNode = new Node(1); // Dummy node

        // Add first property
        RecordTypeBuilder result1 = recordTypeBuilder.addProperty(name, type, propertyNode);
        assertEquals(recordTypeBuilder, result1);
        assertEquals(type, recordTypeBuilder.properties.get(name).getType());
        assertEquals(propertyNode, recordTypeBuilder.properties.get(name).getPropertyNode());

        // Add second property with the same name, expect null return value
        RecordTypeBuilder result2 = recordTypeBuilder.addProperty(name, type, propertyNode);
        assertNull(result2);
        assertEquals(type, recordTypeBuilder.properties.get(name).getType());
        assertEquals(propertyNode, recordTypeBuilder.properties.get(name).getPropertyNode());
    }

    @Test
    public void testBuildWithEmptyRecord() {
        JSType expectedResult = jsTypeRegistry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType result = recordTypeBuilder.build();
        assertEquals(expectedResult, result);
        assertTrue(recordTypeBuilder.isEmpty);
    }

    @Test
    public void testBuildWithProperties() {
        String name = "property1";
        JSType type = jsTypeRegistry.getNativeNumberType();
        Node propertyNode = new Node(1); // Dummy node
        recordTypeBuilder.addProperty(name, type, propertyNode);
        JSType expectedResult = new RecordType(jsTypeRegistry, Collections.unmodifiableMap(recordTypeBuilder.properties));
        JSType result = recordTypeBuilder.build();
        assertEquals(expectedResult, result);
        assertTrue(!recordTypeBuilder.isEmpty);
    }
}