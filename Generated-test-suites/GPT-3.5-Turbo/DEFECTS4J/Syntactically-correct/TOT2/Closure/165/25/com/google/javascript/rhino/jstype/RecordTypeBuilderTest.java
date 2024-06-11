import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class RecordTypeBuilderTest {
    
    private RecordTypeBuilder recordTypeBuilder;
    
    @Before
    public void setUp() {
        recordTypeBuilder = new RecordTypeBuilder(new JSTypeRegistry());
    }
    
    @Test
    public void testAddProperty() {
        JSType stringType = new JSType("String");
        Node propertyNode = new Node("propertyNode");
        
        RecordTypeBuilder result = recordTypeBuilder.addProperty("name", stringType, propertyNode);
        assertEquals(recordTypeBuilder, result);
    }
    
    @Test
    public void testBuildEmptyRecord() {
        JSType objectType = new JSType("Object");
        
        JSType result = recordTypeBuilder.build();
        assertEquals(objectType, result);
    }
    
    @Test
    public void testBuildNonEmptyRecord() {
        JSType stringType = new JSType("String");
        Node propertyNode = new Node("propertyNode");
        
        recordTypeBuilder.addProperty("name", stringType, propertyNode);
        
        JSType result = recordTypeBuilder.build();
        assertEquals(RecordType.class, result.getClass());
    }
    
    @Test
    public void testAddDuplicateProperty() {
        JSType stringType = new JSType("String");
        Node propertyNode = new Node("propertyNode");
        
        recordTypeBuilder.addProperty("name", stringType, propertyNode);
        RecordTypeBuilder result = recordTypeBuilder.addProperty("name", stringType, propertyNode);
        
        assertNull(result);
    }
}