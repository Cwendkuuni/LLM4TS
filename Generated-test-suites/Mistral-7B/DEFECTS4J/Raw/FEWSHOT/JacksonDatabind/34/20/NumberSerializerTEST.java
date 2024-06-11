package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.Version.VERSION_2_12;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.SerializerProviders;
import com.fasterxml.jackson.databind.annotation.JsonSerializable.Loading.From;
import com.fasterxml.jackson.databind.annotation.JsonTypeName;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer.NullValueProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializerBase;
import com.fasterxml.jackson.databind.serde.JsonDeserializer.None;

import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class NumberSerializerTest {

    private SerializerProvider provider;
    private ObjectMapper mapper;

    @Before
    public void setup() {
        provider = new SerializerProvider();
        provider.setNullValueHandler(new NullValueProvider() {
            @Override
            public Object getNullValue(SerializerProvider p, JavaType typeHint) {
                return null;
            }
        });
        mapper = new ObjectMapper(provider);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        StdSerializer<Number> providerSerializer = mock(StdSerializer.class);
        when(provider.findValueSerializer(Integer.class)).thenReturn(providerSerializer);

        serializer.serialize(1, gen, provider);
        assertEquals("1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        StdSerializer<Number> providerSerializer = mock(StdSerializer.class);
        when(provider.findValueSerializer(Long.class)).thenReturn(providerSerializer);

        serializer.serialize(1L, gen, provider);
        assertEquals("1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        StdSerializer<Number> providerSerializer = mock(StdSerializer.class);
        when(provider.findValueSerializer(Float.class)).thenReturn(providerSerializer);

        serializer.serialize(1f, gen, provider);
        assertEquals("1.0", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        StdSerializer<Number> providerSerializer = mock(StdSerializer.class);
        when(provider.findValueSerializer(Double.class)).thenReturn(providerSerializer);

        serializer.serialize(1d, gen, provider);
        assertEquals("1.0", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeByte() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);

        serializer.serialize((byte) 1, gen, provider);
        assertEquals("1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);

        serializer.serialize((short) 1, gen, provider);
        assertEquals("1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);

        serializer.serialize(new BigDecimal("1.23"), gen, provider);
        assertEquals("1.23", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);

        serializer.serialize(BigInteger.valueOf(1), gen, provider);
        assertEquals("1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testSerializeCustomNumber() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonGenerator gen = mock(JsonGenerator.class);
        StdSerializer<Object> providerSerializer = mock(StdSerializer.class);
        when(provider.findValueSerializer(MyNumber.class)).thenReturn(providerSerializer);

        serializer.serialize(new MyNumber(1), gen, provider);
        assertEquals("1", gen.getOutputBuffer().toString());
    }

    @Test
    public void testGetSchema() throws IOException {
        NumberSerializer serializer = NumberSerializer.instance;
        JsonNode schema = serializer.getSchema(provider.getSerializerProvider(), Number.class);
        assertEquals("number", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitorInt() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaBuilder()
                .withSerializer(NumberSerializer.class)
                .withRootValue(Integer.class)
                .build();

        NumberSerializer serializer = NumberSerializer.instance;
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertEquals("integer", visitor.getFormatName());
        assertNull(visitor.getNumberFormat());
    }

    @Test
    public void testAcceptJsonFormatVisitorBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaBuilder()
                .withSerializer(NumberSerializer.class)
                .withRootValue(BigDecimal.class)
                .build();

        NumberSerializer serializer = NumberSerializer.instance;
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
        assertEquals("number", visitor.getFormatName());
        assertNull(visitor.getNumberFormat());
    }

    @Test(expected = JsonMappingException.class)
    public void testAcceptJsonFormatVisitorUnknownType() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonFormatVisitorWrapper visitor = mapper.schemaBuilder()
                .withSerializer(NumberSerializer.class)
                .withRootValue(String.class)
                .build();

        NumberSerializer serializer = NumberSerializer.instance;
        serializer.acceptJsonFormatVisitor(visitor, Number.class);
    }

    @Test
    public void testDeserializeNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("1");

        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(1, deserialized.intValue());
    }

    @Test
    public void testDeserializeBigDecimal() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("1.23");

        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(1.23, deserialized.doubleValue());
    }

    @Test
    public void testDeserializeBigInteger() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("1");

        Number deserialized = mapper.readValue(parser, Number.class);
        assertEquals(1, deserialized.longValue());
    }

    @Test
    public void testDeserializeCustomNumber() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser parser = mapper.getFactory().createParser("1");

        mapper.registerSerializer(MyNumber.class, new MyNumberSerializer());
        MyNumber deserialized = mapper.readValue(parser, MyNumber.class);
        assertEquals(1, deserialized.value);
    }

    @JsonTypeName("MyNumber")
    static class MyNumber {
        int value;

        public MyNumber() {
        }

        public MyNumber(int value) {
            this.value = value;
        }
    }

    static class MyNumberSerializer extends StdSerializer<MyNumber> {
        public MyNumberSerializer() {
            this(null);
        }

        public MyNumberSerializer(Class<MyNumber> t, AnnotatedMethod am, AnnotatedMember amr, AnnotatedClass ac, JavaType valueType) {
            super(t, am, amr, ac, valueType);
        }

        @Override
        public void serialize(MyNumber value, JsonGenerator g, SerializerProvider p) throws IOException {
            g.writeNumber(value.value);
        }

        @Override
        public MyNumber deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new MyNumber(p.getIntValue());
        }
    }
}