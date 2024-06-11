import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EnumSerializerTest {
    private EnumSerializer enumSerializer;
    
    @Before
    public void setUp() {
        EnumValues enumValues = new EnumValues();
        enumSerializer = new EnumSerializer(enumValues);
    }
    
    @Test
    public void testSerialize_NullEnum_ThrowsIOException() {
        try {
            enumSerializer.serialize(null, new JsonGenerator(), new SerializerProvider());
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals("Invalid enum value: null", e.getMessage());
        }
    }
    
    @Test
    public void testSerialize_EnumWithSerializeAsIndex_SerializesAsIndex() {
        EnumWithSerializeAsIndex enumValue = EnumWithSerializeAsIndex.FIRST;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            enumSerializer.serialize(enumValue, gen, serializers);
            assertEquals("0", gen.getOutput());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
    
    @Test
    public void testSerialize_EnumWithoutSerializeAsIndex_SerializesUsingValue() {
        EnumWithoutSerializeAsIndex enumValue = EnumWithoutSerializeAsIndex.SECOND;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            enumSerializer.serialize(enumValue, gen, serializers);
            assertEquals("SECOND", gen.getOutput());
        } catch (IOException e) {
            fail("Unexpected IOException thrown");
        }
    }
    
    @Test
    public void testGetSchema_EnumWithSerializeAsIndex_ReturnsIntegerSchema() {
        EnumWithSerializeAsIndex enumValue = EnumWithSerializeAsIndex.FIRST;
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = enumValue.getClass();
        
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        assertEquals("integer", schema.getType());
        assertTrue(schema.isNumber());
        assertTrue(schema.isIntegralNumber());
    }
    
    @Test
    public void testGetSchema_EnumWithoutSerializeAsIndex_ReturnsStringSchema() {
        EnumWithoutSerializeAsIndex enumValue = EnumWithoutSerializeAsIndex.SECOND;
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = enumValue.getClass();
        
        JsonNode schema = enumSerializer.getSchema(provider, typeHint);
        assertEquals("string", schema.getType());
        assertTrue(schema.isTextual());
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_EnumWithSerializeAsIndex_VisitsIntFormat() {
        EnumWithSerializeAsIndex enumValue = EnumWithSerializeAsIndex.FIRST;
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType(enumValue.getClass());
        
        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertEquals(JsonParser.NumberType.INT, visitor.getVisitedIntFormat());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException thrown");
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitor_EnumWithoutSerializeAsIndex_VisitsStringFormat() {
        EnumWithoutSerializeAsIndex enumValue = EnumWithoutSerializeAsIndex.SECOND;
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType(enumValue.getClass());
        
        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertNotNull(visitor.getVisitedStringFormat());
            assertEquals(1, visitor.getVisitedStringFormat().enumTypes().size());
        } catch (JsonMappingException e) {
            fail("Unexpected JsonMappingException thrown");
        }
    }
}