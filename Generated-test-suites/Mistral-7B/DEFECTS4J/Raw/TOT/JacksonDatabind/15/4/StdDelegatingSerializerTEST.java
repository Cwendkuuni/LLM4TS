import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonWriter;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.type.TypeReference;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdDelegatingSerializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSimpleDelegation() throws JsonGenerationException, JsonMappingException, IOException {
        Converter<String, Integer> converter = new SimpleStringToIntegerConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        Object delegateValue = serializer.convertValue("42");
        assertEquals(42, delegateValue);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = objectMapper.getFactory().createJsonWriter(outputStream);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        serializer.serialize(delegateValue, writer, serializerProvider);
        String json = outputStream.toString();

        assertEquals("42", json);
    }

    @Test
    public void testDelegationWithNestedTypes() throws JsonGenerationException, JsonMappingException, IOException {
        Converter<String, MyNestedClass> converter = new MyNestedClassConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        MyNestedClass nestedClass = new MyNestedClass("Test");
        Object delegateValue = serializer.convertValue(nestedClass);
        assertNotNull(delegateValue);
        assertEquals("Test", delegateValue);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = objectMapper.getFactory().createJsonWriter(outputStream);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        serializer.serialize(delegateValue, writer, serializerProvider);
        String json = outputStream.toString();

        assertEquals("{\"value\":\"Test\"}", json);
    }

    @Test(expected = IllegalStateException.class)
    public void testDelegationWithSameType() throws JsonGenerationException, JsonMappingException, IOException {
        Converter<String, String> converter = new IdentityConverter();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        Object delegateValue = serializer.convertValue("Test");
        assertEquals("Test", delegateValue);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonWriter writer = objectMapper.getFactory().createJsonWriter(outputStream);
        SerializerProvider serializerProvider = objectMapper.getSerializerProvider();

        serializer.serialize(delegateValue, writer, serializerProvider);
        String json = outputStream.toString();

        assertEquals("Test", json);

        StdDelegatingSerializer sameTypeSerializer = new StdDelegatingSerializer(serializer);
        Object sameTypeDelegateValue = sameTypeSerializer.convertValue("Test");
        assertEquals("Test", sameTypeDelegateValue);

        ByteArrayOutputStream sameTypeOutputStream = new ByteArrayOutputStream();
        JsonWriter sameTypeWriter = objectMapper.getFactory().createJsonWriter(sameTypeOutputStream);
        SerializerProvider sameTypeSerializerProvider = objectMapper.getSerializerProvider();

        sameTypeSerializer.serialize(sameTypeDelegateValue, sameTypeWriter, sameTypeSerializerProvider);
        String sameTypeJson = sameTypeOutputStream.toString();

        assertEquals("Test", sameTypeJson);

        // This should cause an exception as types are not different
        serializer = sameTypeSerializer;
        delegateValue = serializer.convertValue("Test");
    }

    static class SimpleStringToIntegerConverter implements Converter<String, Integer> {
        @Override
        public Integer convert(String value) {
            return Integer.parseInt(value);
        }
    }

    static class MyNestedClass {
        private String value;

        public MyNestedClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    static class MyNestedClassConverter implements Converter<String, MyNestedClass> {
        @Override
        public MyNestedClass convert(String value) {
            return new MyNestedClass(value);
        }
    }

    static class IdentityConverter implements Converter<String, String> {
        @Override
        public String convert(String value) {
            return value;
        }
    }

    static class JsonTestSerializer extends StdDelegatingSerializer<MyNestedClass> {
        public JsonTestSerializer() {
            this(new MyNestedClassConverter(), null, objectMapper.findValueSerializer(MyNestedClass.class));
        }
    }
}