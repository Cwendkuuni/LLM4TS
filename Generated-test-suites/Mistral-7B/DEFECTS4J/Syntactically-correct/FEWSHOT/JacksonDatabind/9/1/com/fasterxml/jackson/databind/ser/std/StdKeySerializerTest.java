package com.fasterxml.jackson.databind.ser.std;

import static com.fasterxml.jackson.core.util.TestUtils.convertValueToType;
import static com.fasterxml.jackson.databind.SerializationFeature.FAIL_ON_EMPTY_BEANS;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class StdKeySerializerTest {

    @Test
    public void testSerializeDate() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        Date date = new Date();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(date, gen, provider);

        assertEquals("\"@JsonKey(\"value=\"" + date.toInstant().toString() + "\")\"", gen.getOutputContent().toString());
    }

    @Test
    public void testSerializeString() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        String str = "test";

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(str, gen, provider);

        assertEquals("\"test\"", gen.getOutputContent().toString());
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        StdKeySerializer serializer = new StdKeySerializer();

        JsonNode schema = serializer.getSchema(mock(SerializerProvider.class), JavaType.getRawClass(Object.class));

        assertEquals("string", schema.toString());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        ObjectMapper mapper = new ObjectMapper();

        StdKeySerializer serializer = new StdKeySerializer();

        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = JavaType.getReference(TypeFactory.defaultInstance().constructType(Object.class));

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        visitor.expectStringFormat(typeHint);
    }

    @Test
    public void testSerializeNull() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(null, gen, provider);

        assertNull(gen.getOutputContent());
    }

    @Test
    public void testSerializeArray() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(FAIL_ON_EMPTY_BEANS, false);

        Object[] array = new Object[]{"test1", "test2"};

        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        StdKeySerializer serializer = new StdKeySerializer();

        serializer.serialize(array, gen, provider);

        assertEquals("[\"test1\",\"test2\"]", gen.getOutputContent().toString());
    }
}