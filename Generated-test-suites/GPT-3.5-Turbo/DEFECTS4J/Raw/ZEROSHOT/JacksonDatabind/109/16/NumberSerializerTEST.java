import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextualWithNullFormat() throws JsonMappingException {
        JsonSerializer<?> serializer = numberSerializer.createContextual(null, null);
        assertEquals(numberSerializer, serializer);
    }

    @Test
    public void testCreateContextualWithNonStringFormat() throws JsonMappingException {
        JsonSerializer<?> serializer = numberSerializer.createContextual(null, createBeanProperty(JsonFormat.Shape.NUMBER));
        assertEquals(numberSerializer, serializer);
    }

    @Test
    public void testCreateContextualWithNonDefaultStringFormat() throws JsonMappingException {
        JsonSerializer<?> serializer = numberSerializer.createContextual(null, createBeanProperty(JsonFormat.Shape.STRING));
        assertEquals(ToStringSerializer.instance, serializer);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize(new BigDecimal("10.5"), generator, null);
        assertEquals("10.5", generator.toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize(new BigInteger("10"), generator, null);
        assertEquals("10", generator.toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize(10L, generator, null);
        assertEquals("10", generator.toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize(10.5, generator, null);
        assertEquals("10.5", generator.toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize(10.5f, generator, null);
        assertEquals("10.5", generator.toString());
    }

    @Test
    public void testSerializeInt() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize(10, generator, null);
        assertEquals("10", generator.toString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize((byte) 10, generator, null);
        assertEquals("10", generator.toString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize((short) 10, generator, null);
        assertEquals("10", generator.toString());
    }

    @Test
    public void testSerializeCustomType() throws IOException {
        JsonGenerator generator = createMockGenerator();
        numberSerializer.serialize(new CustomNumber(10), generator, null);
        assertEquals("10", generator.toString());
    }

    @Test
    public void testGetSchemaWithIntTypeHint() {
        SerializerProvider provider = null;
        JsonNode node = numberSerializer.getSchema(provider, int.class);
        assertEquals("integer", node.get("type").asText());
    }

    @Test
    public void testGetSchemaWithBigDecimalTypeHint() {
        SerializerProvider provider = null;
        JsonNode node = numberSerializer.getSchema(provider, BigDecimal.class);
        assertEquals("number", node.get("type").asText());
    }

    @Test
    public void testGetSchemaWithBigIntegerTypeHint() {
        SerializerProvider provider = null;
        JsonNode node = numberSerializer.getSchema(provider, BigInteger.class);
        assertEquals("integer", node.get("type").asText());
    }

    @Test
    public void testAcceptJsonFormatVisitorWithIntTypeHint() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = createMockVisitor();
        JavaType typeHint = createMockType(int.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithBigDecimalTypeHint() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = createMockVisitor();
        JavaType typeHint = createMockType(BigDecimal.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testAcceptJsonFormatVisitorWithCustomType() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = createMockVisitor();
        JavaType typeHint = createMockType(CustomNumber.class);
        numberSerializer.acceptJsonFormatVisitor(visitor, typeHint);
    }

    /**
     * Helper method to create a mock bean property with the given shape.
     */
    private BeanProperty createBeanProperty(JsonFormat.Shape shape) {
        return new BeanProperty() {
            @Override
            public String getName() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public <A extends java.lang.annotation.Annotation> A getAnnotation(Class<A> acls) {
                return null;
            }

            @Override
            public boolean hasAnnotation(Class<?> acls) {
                return false;
            }

            @Override
            public boolean isRequired() {
                return false;
            }

            @Override
            public JsonFormat.Value findFormatOverrides(SerializerProvider ctxt) {
                return new JsonFormat.Value(shape);
            }

            @Override
            public Optional<AnnotatedMember> getMember() {
                return Optional.empty();
            }
        };
    }

    /**
     * Helper method to create a mock JsonGenerator.
     */
    private JsonGenerator createMockGenerator() {
        return new JsonGenerator() {

            private StringBuilder sb = new StringBuilder();

            @Override
            public void writeStartArray() throws IOException {
                // do nothing
            }

            @Override
            public void writeEndArray() throws IOException {
                // do nothing
            }

            @Override
            public void writeStartObject() throws IOException {
                // do nothing
            }

            @Override
            public void writeEndObject() throws IOException {
                // do nothing
            }

            @Override
            public void writeFieldName(String name) throws IOException {
                // do nothing
            }

            @Override
            public void writeString(String text) throws IOException {
                // do nothing
            }

            @Override
            public void writeNumber(int v) throws IOException {
                sb.append(v);
            }

            @Override
            public void writeNumber(long v) throws IOException {
                sb.append(v);
            }

            @Override
            public void writeNumber(BigInteger v) throws IOException {
                sb.append(v);
            }

            @Override
            public void writeNumber(double v) throws IOException {
                sb.append(v);
            }

            @Override
            public void writeNumber(float v) throws IOException {
                sb.append(v);
            }

            @Override
            public void writeNumber(BigDecimal v) throws IOException {
                sb.append(v);
            }

            @Override
            public void writeNumber(String encodedValue) throws IOException {
                sb.append(encodedValue);
            }

            @Override
            public void writeBoolean(boolean state) throws IOException {
                // do nothing
            }

            @Override
            public void writeNull() throws IOException {
                // do nothing
            }

            @Override
            public void close() throws IOException {
                // do nothing
            }

            @Override
            public boolean isClosed() {
                return false;
            }
            
            @Override
            public void flush() throws IOException {
                // do nothing
            }

            @Override
            public Object getOutputTarget() {
                return null;
            }

            @Override
            public int getOutputBuffered() {
                return 0;
            }

            @Override
            public Object getCurrentValue() {
                return null;
            }
        };
    }

    /**
     * Helper method to create a mock JsonFormatVisitorWrapper.
     */
    private JsonFormatVisitorWrapper createMockVisitor() {
        return new JsonFormatVisitorWrapper() {

            @Override
            public void setProvider(SerializerProvider provider) {
                // do nothing
            }

            @Override
            public SerializerProvider getProvider() {
                return null;
            }
        };
    }

    /**
     * Helper method to create a mock JavaType with the given raw class.
     */
    private JavaType createMockType(Class<?> rawClass) {
        return new JavaType(null, rawClass, null, null, null, false, null, null);
    }

    /**
     * Helper class to represent a custom number type.
     */
    private class CustomNumber extends Number {

        private static final long serialVersionUID = 1L;

        private int value;

        public CustomNumber(int value) {
            this.value = value;
        }

        @Override
        public int intValue() {
            return value;
        }

        @Override
        public long longValue() {
            return value;
        }

        @Override
        public float floatValue() {
            return value;
        }

        @Override
        public double doubleValue() {
            return value;
        }
    }
}