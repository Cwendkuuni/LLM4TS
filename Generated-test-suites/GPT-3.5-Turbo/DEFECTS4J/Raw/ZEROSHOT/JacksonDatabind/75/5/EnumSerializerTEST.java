import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), Boolean.TRUE);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        try {
            enumSerializer.serialize(Enum.values()[0], jsonGenerator, serializerProvider);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSerializeAsString() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), Boolean.FALSE);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        try {
            enumSerializer.serialize(Enum.values()[0], jsonGenerator, serializerProvider);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testSerializeUsingToString() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), null);
        JsonGenerator jsonGenerator = null;
        SerializerProvider serializerProvider = null;
        serializerProvider.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        try {
            enumSerializer.serialize(Enum.values()[0], jsonGenerator, serializerProvider);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSchemaWithSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), Boolean.TRUE);
        SerializerProvider serializerProvider = null;
        try {
            enumSerializer.getSchema(serializerProvider, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testGetSchemaWithoutSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), Boolean.FALSE);
        SerializerProvider serializerProvider = null;
        try {
            enumSerializer.getSchema(serializerProvider, null);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), Boolean.TRUE);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testAcceptJsonFormatVisitorWithoutSerializeAsIndex() {
        EnumSerializer enumSerializer = new EnumSerializer(EnumValues.constructFromName(null, Enum.class), Boolean.FALSE);
        JsonFormatVisitorWrapper visitor = null;
        JavaType typeHint = null;
        try {
            enumSerializer.acceptJsonFormatVisitor(visitor, typeHint);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}