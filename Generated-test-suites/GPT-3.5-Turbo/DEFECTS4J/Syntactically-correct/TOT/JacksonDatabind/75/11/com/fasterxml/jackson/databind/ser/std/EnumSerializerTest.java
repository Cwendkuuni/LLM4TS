import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import java.io.IOException;

import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerializeAsIndexTypical() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        Enum<?> enumValue = EnumType.TYPICAL;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.serialize(enumValue, gen, serializers);
            assertEquals(enumValue.ordinal(), gen.getNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeAsIndexEdge() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        Enum<?> enumValue = EnumType.values()[0];
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.serialize(enumValue, gen, serializers);
            assertEquals(enumValue.ordinal(), gen.getNumber());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeAsIndexError() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        Enum<?> enumValue = null;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.serialize(enumValue, gen, serializers);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("NullPointerException"));
        }
    }

    @Test
    public void testSerializeAsStringTypical() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), false);
        Enum<?> enumValue = EnumType.TYPICAL;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.serialize(enumValue, gen, serializers);
            assertEquals(enumValue.toString(), gen.getString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeAsStringEdge() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), false);
        Enum<?> enumValue = EnumType.values()[0];
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.serialize(enumValue, gen, serializers);
            assertEquals(enumValue.toString(), gen.getString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSerializeAsStringError() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), false);
        Enum<?> enumValue = null;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.serialize(enumValue, gen, serializers);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("NullPointerException"));
        }
    }

    @Test
    public void testCreateContextual() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), false);
        SerializerProvider serializers = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        property.setType(EnumType.class);
        
        try {
            EnumSerializer result = (EnumSerializer) serializer.createContextual(serializers, property);
            assertTrue(result instanceof EnumSerializer);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testCreateContextualError() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), false);
        SerializerProvider serializers = new SerializerProvider();

        try {
            EnumSerializer result = (EnumSerializer) serializer.createContextual(serializers, null);
            assertEquals(serializer, result);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetSchemaAsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = EnumType.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertTrue(schema instanceof IntegerNode);
    }

    @Test
    public void testGetSchemaAsString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), false);
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = EnumType.class;
        
        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertTrue(schema instanceof ObjectNode);
        assertTrue(schema.has("enum"));
    }

    @Test
    public void testAcceptJsonFormatVisitorAsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), true);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(visitor.getIntFormat().getNumberType() == JsonParser.NumberType.INT);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitorAsString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.constructFromName(config, (Class<Enum<?>>) enumClass), false);
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        SerializerProvider serializers = new SerializerProvider();
        
        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(visitor.getStringFormat() != null);
            assertTrue(visitor.getStringFormat().getEnums().size() > 0);
        } catch (JsonMappingException e) {
            e.printStackTrace();
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void test_isShapeWrittenUsingIndexEnumNotSupported() {
        Class<?> enumClass = UnsupportedEnum.class;
        JsonFormat.Value format = new JsonFormat.Value();
        EnumSerializer._isShapeWrittenUsingIndex(enumClass, format, true);
    }

}