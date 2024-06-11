import static org.junit.Assert.*;
import org.junit.Test;

public class RecordTypeBuilderTest {
    
    @Test
    public void testAddProperty_typical() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType numberType = new JSType();
        Node numberNode = new Node();
        
        RecordTypeBuilder result = builder.addProperty("age", numberType, numberNode);
        
        assertNotNull(result);
        assertEquals(1, builder.properties.size());
        assertTrue(builder.properties.containsKey("age"));
        assertEquals(numberType, builder.properties.get("age").getType());
        assertEquals(numberNode, builder.properties.get("age").getPropertyNode());
    }
    
    @Test
    public void testAddProperty_duplicate() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType numberType1 = new JSType();
        JSType numberType2 = new JSType();
        Node numberNode1 = new Node();
        Node numberNode2 = new Node();
        
        builder.addProperty("age", numberType1, numberNode1);
        RecordTypeBuilder result = builder.addProperty("age", numberType2, numberNode2);
        
        assertNull(result);
        assertEquals(1, builder.properties.size());
        assertTrue(builder.properties.containsKey("age"));
        assertEquals(numberType1, builder.properties.get("age").getType());
        assertEquals(numberNode1, builder.properties.get("age").getPropertyNode());
    }
    
    @Test
    public void testBuild_emptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        
        JSType result = builder.build();
        
        assertEquals(expectedType, result);
    }
    
    @Test
    public void testBuild_nonEmptyRecord() {
        JSTypeRegistry registry = new JSTypeRegistry();
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        
        JSType stringType = new JSType();
        Node stringNode = new Node();
        
        builder.addProperty("name", stringType, stringNode);
        
        JSType expectedType = new RecordType(registry, builder.properties);
        
        JSType result = builder.build();
        
        assertEquals(expectedType, result);
    }

}