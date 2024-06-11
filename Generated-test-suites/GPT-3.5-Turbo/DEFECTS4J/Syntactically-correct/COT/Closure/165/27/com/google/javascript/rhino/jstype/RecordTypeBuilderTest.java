
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        JSType type = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node();
        
        RecordTypeBuilder result = recordTypeBuilder.addProperty("count", type, propertyNode);
        
        assertNotNull(result);
    }
    
    @Test
    public void testAddPropertyDuplicate() {
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        
        recordTypeBuilder.addProperty("name", type, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty("name", type, propertyNode);
        
        assertNull(result);
    }
    
    @Test
    public void testAddPropertyEmptyName() {
        JSType type = registry.getNativeType(JSTypeNative.BOOLEAN_TYPE);
        Node propertyNode = new Node();
        
        RecordTypeBuilder result = recordTypeBuilder.addProperty("", type, propertyNode);
        
        assertNull(result);
    }
    
    @Test
    public void testBuildEmptyRecord() {
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        JSType result = recordTypeBuilder.build();
        
        assertEquals(objectType, result);
    }
    
    @Test
    public void testBuildNonEmptyRecord() {
        JSType numberType = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node();
        
        recordTypeBuilder.addProperty("count", numberType, propertyNode);
        
        JSType result = recordTypeBuilder.build();
        
        assertTrue(result instanceof RecordType);
        assertTrue(result.getProperties().containsKey("count"));
    }
    
    @Test
    public void testBuildEmptyThenNonEmptyRecord() {
        JSType objectType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        JSType result = recordTypeBuilder.build();
        
        assertEquals(objectType, result);
        
        JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        
        recordTypeBuilder.addProperty("name", stringType, propertyNode);
        
        result = recordTypeBuilder.build();
        
        assertTrue(result instanceof RecordType);
        assertTrue(result.getProperties().containsKey("name"));
    }
}
