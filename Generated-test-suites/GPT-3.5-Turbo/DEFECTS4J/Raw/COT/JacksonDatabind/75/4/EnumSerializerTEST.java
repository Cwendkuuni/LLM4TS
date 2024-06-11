
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EnumSerializerTest {
   
    @Test
    public void testSerialize_AsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(null, true);
        Enum example = ExampleEnum.ONE;
        
        // Test serialization as index
        assertEquals(0, example.ordinal());
    }
    
    @Test
    public void testSerialize_AsString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(null, false);
        Enum example = ExampleEnum.ONE;
        
        // Test serialization as string
        assertEquals("ONE", example.toString());
    }
    
    @Test
    public void testSerialize_WithOverride() throws Exception {
        EnumSerializer serializer = new EnumSerializer(null, false);
        Enum example = ExampleEnum.ONE;
        
        // Test serialization with overridden value
        assertEquals("One", serializer.serializedValueFor(example));
    }
    
    @Test
    public void testSerialize_WithSchemaAsIndex() throws Exception {
        EnumSerializer serializer = new EnumSerializer(null, true);
        Enum example = ExampleEnum.ONE;
        
        // Test schema generation for index serialization
        assertEquals("integer", serializer.getSchema(null, null).get("type").asText());
    }
    
    @Test
    public void testSerialize_WithSchemaAsString() throws Exception {
        EnumSerializer serializer = new EnumSerializer(null, false);
        Enum example = ExampleEnum.ONE;
        
        // Test schema generation for string serialization
        assertEquals("string", serializer.getSchema(null, null).get("type").asText());
    }
    
    @Test
    public void testSerialize_WithEnumTypes() throws Exception {
        EnumSerializer serializer = new EnumSerializer(null, false);
        Enum example = ExampleEnum.ONE;
        
        // Test enum types in string format visitor
        assertEquals("ONE", serializer.acceptJsonFormatVisitor(visitor, null));
    }
    
    @Test
    public void testSerialize_WithToStringFormatVisitor() throws Exception {
        EnumSerializer serializer = new EnumSerializer(null, false);
        Enum example = ExampleEnum.ONE;
        
        // Test enum types using toString() in string format visitor
        assertEquals("ONE", serializer.acceptJsonFormatVisitor(visitor, null));
    }

    public enum ExampleEnum {
        ONE, TWO, THREE
    }
}
