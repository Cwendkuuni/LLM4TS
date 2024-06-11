package com.example.junit.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Type;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonDeserializer.ContextualDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;

@RunWith(BaseTest.NiceMockitoRunner.class)
public class NumberSerializersTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testAddAll() throws JsonMappingException, JsonProcessingException {
        Map<String, JsonSerializer<?>> allDeserializers = Mockito.mock(Map.class);
        NumberSerializers.addAll(allDeserializers);
        assertEquals(8, allDeserializers.size());
    }

    @Test
    public void testShortSerializer() throws JsonMappingException, JsonProcessingException {
        Short number = Short.MIN_VALUE;
        String json = mapper.writer().writeValueAsString(number);
        Short deserializedNumber = mapper.readerForType(Short.class).readValue(json);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testIntegerSerializer() throws JsonMappingException, JsonProcessingException {
        Integer number = Integer.MIN_VALUE;
        String json = mapper.writer().writeValueAsString(number);
        Integer deserializedNumber = mapper.readerForType(Integer.class).readValue(json);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testIntLikeSerializer() throws JsonMappingException, JsonProcessingException {
        Long number = Long.MIN_VALUE;
        String json = mapper.writer().writeValueAsString(number);
        Number deserializedNumber = mapper.readerForType(Number.class).readValue(json);
        assertEquals(number.intValue(), deserializedNumber.intValue());
    }

    @Test
    public void testLongSerializer() throws JsonMappingException, JsonProcessingException {
        Long number = Long.MIN_VALUE;
        String json = mapper.writer().writeValueAsString(number);
        Long deserializedNumber = mapper.readerForType(Long.class).readValue(json);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testFloatSerializer() throws JsonMappingException, JsonProcessingException {
        Float number = Float.MIN_VALUE;
        String json = mapper.writer().writeValueAsString(number);
        Float deserializedNumber = mapper.readerForType(Float.class).readValue(json);
        assertEquals(number, deserializedNumber);
    }

    @Test
    public void testDoubleSerializer() throws JsonMappingException, JsonProcessingException {
        Double number = Double.MIN_VALUE;
        String json = mapper.writer().writeValueAsString(number);
        Double deserializedNumber = mapper.readerForType(Double.class).readValue(json);
        assertEquals(number, deserializedNumber);
    }

    @Test(expected = JsonProcessingException.class)
    public void testUnknownTypeSerialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{ \"value\": 123456789012345678901234567890 }";
        mapper.readValue(json, Number.class);
    }

    @Test
    public void testContextualDeserialization() throws JsonMappingException, JsonProcessingException {
        JsonDeserialize jsonDeserialize = Mockito.mock(JsonDeserialize.class);
        JsonFormat jsonFormat = Mockito.mock(JsonFormat.class);
        AnnotatedMember annotatedMember = Mockito.mock(AnnotatedMember.class);

        when(jsonDeserialize.getAnnotation()).thenReturn(jsonFormat);
        when(jsonFormat.getShape()).thenReturn(JsonFormat.Value.Shape.STRING);

        ContextualDeserializer serializer = Mockito.spy(ToStringSerializer.instance);
        when(serializer.createContextual(Mockito.any(SerializerProvider.class), Mockito.any(BeanProperty.class)))
                .thenReturn(jsonDeserialize);

        String json = "\"Hello World\"";
        Object deserializedObject = mapper.readerForType(String.class).readValue(json);
        assertEquals(deserializedObject, serializer.deserialize(json, String.class, mapper, null));

        verify(serializer).createContextual(Mockito.any(SerializerProvider.class), Mockito.any(BeanProperty.class));
    }

    @Test
    public void testNullValueSerialization() throws JsonMappingException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = Mockito.spy(MapSerializer.NullSerializers.Base.instance);

        String json = mapper.writer().withSerializerForNull(serializer).writeValueAsString(null);
        assertNull(mapper.readerForType(Object.class).readValue(json));

        verify(serializer).serializeNull(Mockito.any(JsonGenerator.class), Mockito.any(SerializerProvider.class));
    }
}