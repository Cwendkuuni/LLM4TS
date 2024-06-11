import static org.junit.Assert.*;
import org.junit.Test;

public class EnumSerializerTest {

    @Test
    public void testSerialize_AsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.EXAMPLE, true);
        EnumExample example = EnumExample.FIRST;

        try {
            StringWriter writer = new StringWriter();
            JsonGenerator generator = new JsonFactory().createGenerator(writer);
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            serializer.serialize((Enum<?>) example, generator, provider);
            generator.close();

            assertEquals("0", writer.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSerialize_AsString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.EXAMPLE, false);
        EnumExample example = EnumExample.SECOND;

        try {
            StringWriter writer = new StringWriter();
            JsonGenerator generator = new JsonFactory().createGenerator(writer);
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            serializer.serialize((Enum<?>) example, generator, provider);
            generator.close();

            assertEquals("\"SECOND\"", writer.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testSerialize_UsingToString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.EXAMPLE, null);
        EnumExample example = EnumExample.THIRD;

        try {
            StringWriter writer = new StringWriter();
            JsonGenerator generator = new JsonFactory().createGenerator(writer);
            SerializerProvider provider = new DefaultSerializerProvider.Impl();
            serializer.serialize((Enum<?>) example, generator, provider);
            generator.close();

            assertEquals("\"THIRD\"", writer.toString());
        } catch (IOException e) {
            fail();
        }
    }

    @Test
    public void testGetSchema_AsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.EXAMPLE, true);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        try {
            ObjectNode node = serializer.getSchema(provider, EnumExample.class);
            JsonNode enumNode = node.get("enum");
            JsonNode typeNode = node.get("type");

            assertNotNull(enumNode);
            assertNull(typeNode);
            assertEquals("integer", typeNode.asText());
            assertEquals(3, enumNode.size());
            assertTrue(enumNode.has("FIRST"));
            assertTrue(enumNode.has("SECOND"));
            assertTrue(enumNode.has("THIRD"));
        } catch (JsonMappingException e) {
            fail();
        }
    }

    @Test
    public void testGetSchema_AsString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.EXAMPLE, false);
        SerializerProvider provider = new DefaultSerializerProvider.Impl();

        try {
            ObjectNode node = serializer.getSchema(provider, EnumExample.class);
            JsonNode enumNode = node.get("enum");
            JsonNode typeNode = node.get("type");

            assertNotNull(enumNode);
            assertNull(typeNode);
            assertEquals("string", typeNode.asText());
            assertEquals(3, enumNode.size());
            assertTrue(enumNode.has("FIRST"));
            assertTrue(enumNode.has("SECOND"));
            assertTrue(enumNode.has("THIRD"));
        } catch (JsonMappingException e) {
            fail();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsIndex() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.EXAMPLE, true);
        JsonFormatVisitorWrapper visitor = new FakeJsonFormatVisitorWrapper(serializer);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(EnumExample.class);

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(visitor.intFormatCalled);
        } catch (JsonMappingException e) {
            fail();
        }
    }

    @Test
    public void testAcceptJsonFormatVisitor_AsString() {
        EnumSerializer serializer = new EnumSerializer(EnumValues.EXAMPLE, false);
        JsonFormatVisitorWrapper visitor = new FakeJsonFormatVisitorWrapper(serializer);
        JavaType typeHint = TypeFactory.defaultInstance().constructType(EnumExample.class);

        try {
            serializer.acceptJsonFormatVisitor(visitor, typeHint);
            assertTrue(visitor.stringFormatCalled);
        } catch (JsonMappingException e) {
            fail();
        }
    }

    public class FakeJsonFormatVisitorWrapper extends JsonFormatVisitorWrapper.Base {
        public boolean intFormatCalled = false;
        public boolean stringFormatCalled = false;

        public FakeJsonFormatVisitorWrapper(JsonSerializer<?> serializer) {
            super(serializer);
        }

        @Override
        public JsonIntFormatVisitor expectIntFormat(JavaType type) throws JsonMappingException {
            intFormatCalled = true;
            return super.expectIntFormat(type);
        }

        @Override
        public JsonStringFormatVisitor expectStringFormat(JavaType type) throws JsonMappingException {
            stringFormatCalled = true;
            return super.expectStringFormat(type);
        }
    }
}