import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }
    
    @Test
    public void testAddProperty() {
        String propertyName = "property";
        JSType propertyType = new JSType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        
        // Test adding a property
        RecordTypeBuilder result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        assertNotNull(result);
        
        // Test adding a duplicate property
        result = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        assertNull(result);
    }
    
    @Test
    public void testBuild() {
        // Test building an empty record type
        JSType result = recordTypeBuilder.build();
        JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(expected, result);
        
        // Test building a non-empty record type
        String propertyName = "property";
        JSType propertyType = new JSType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        result = recordTypeBuilder.build();
        Map<String, RecordProperty> properties = result.getProperties();
        assertTrue(properties.containsKey(propertyName));
    }
}