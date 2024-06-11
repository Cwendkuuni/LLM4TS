import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonSerializer.ValueType;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JsonValueSerializerTest {

    private ObjectMapper mapper;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
    }

    @Test
    public void testBasicUsage() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new TestValueMethod(), null, null, true);
        String json = mapper.writeValueAsString(new TestValueClass("Test"));
        assertEquals("\"Test\"", json);
    }

    @Test
    public void testNullValue() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new TestValueMethod(), null, null, true);
        String json = mapper.writeValueAsString(new TestValueClass(null));
        assertEquals("null", json);
    }

    @Test
    public void testExceptionThrown() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new ThrowingValueMethod(), null, null, true);
        try {
            String json = mapper.writeValueAsString(new TestValueClass(new TestException()));
            fail("Expected JsonMappingException to be thrown");
        } catch (JsonMappingException e) {
            assertNotNull(e.getCause());
        }
    }

    @Test
    public void testSerializingPrimitiveTypes() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new TestIntMethod(), null, null, true);
        int value = 42;
        String json = mapper.writeValueAsString(new TestIntClass(value));
        assertEquals("42", json);

        serializer = new JsonValueSerializer(new TestBooleanMethod(), null, null, true);
        boolean valueB = true;
        json = mapper.writeValueAsString(new TestBooleanClass(valueB));
        assertEquals("true", json);

        serializer = new JsonValueSerializer(new TestDoubleMethod(), null, null, true);
        double valueD = 3.14;
        json = mapper.writeValueAsString(new TestDoubleClass(valueD));
        assertEquals("3.14", json);
    }

    @Test
    public void testSerializingStringTypes() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new TestStringMethod(), null, null, true);
        String json = mapper.writeValueAsString(new TestStringClass("Hello"));
        assertEquals("\"Hello\"", json);
    }

    @Test
    public void testTypeInformation() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new TestStringMethod(), null, null, false);
        String json = mapper.writeValueAsString(new TestStringClass("Hello"));
        assertEquals("\"Hello\"", json);

        JsonSerializer<Object> valueSerializer = mapper.findValueSerializer(String.class, null);
        TypeSerializer typeSerializer = mapper.getTypeSerializer();

        serializer = new JsonValueSerializer(new TestStringMethod(), null, valueSerializer, false);
        json = mapper.writeValueAsString(new TestStringClass("Hello"));
        assertEquals("\"Hello\"", json);
        assertEquals(ValueType.STRING, typeSerializer.readValueAsValueType(json));
    }

    @Test
    public void testEnumValues() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new TestEnumValueMethod(), null, null, true);

        String json = mapper.writeValueAsString(new TestEnumClass(TestEnum.VALUE1));
        assertEquals("\"VALUE1\"", json);

        json = mapper.writeValueAsString(new TestEnumClass(TestEnum.VALUE2));
        assertEquals("\"VALUE2\"", json);
    }

    @Test
    public void testJsonMappingExceptionWithPath() throws IOException, JsonMappingException {
        JsonValueSerializer serializer = new JsonValueSerializer(new ThrowingValueMethodWithPath(), null, null, true);
        try {
            String json = mapper.writeValueAsString(new TestValueClass(new TestExceptionWithPath()));
            fail("Expected JsonMappingException with path to be thrown");
        } catch (JsonMappingException e) {
            assertEquals("TestValueClass.access$100(Ljava/lang/Object;)Ljava/lang/Object;", e.getPath());
        }
    }

    private static class TestValueClass {
        private final String value;

        public TestValueClass(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class TestValueMethod {
        @JsonValue
        public static String getValue(TestValueClass obj) {
            return obj.getValue();
        }
    }

    private static class TestIntClass {
        private final int value;

        public TestIntClass(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private static class TestIntMethod {
        @JsonValue
        public static int getValue(TestIntClass obj) {
            return obj.getValue();
        }
    }

    private static class TestBooleanClass {
        private final boolean value;

        public TestBooleanClass(boolean value) { this.value = value; }

        public boolean getValue() { return value; }
    }

    private static class TestBooleanMethod {
        @JsonValue
        public static boolean getValue(TestBooleanClass obj) {
            return obj.getValue();
        }
    }

    private static class TestDoubleClass {
        private final double value;

        public TestDoubleClass(double value) { this.value = value; }

        public double getValue() { return value; }
    }

    private static class TestDoubleMethod {
        @JsonValue
        public static double getValue(TestDoubleClass obj) {
            return obj.getValue();
        }
    }

    private static class TestStringClass {
        private final String value;

        public TestStringClass(String value) { this.value = value; }

        public String getValue() { return value; }
    }

    private static class TestStringMethod {
        @JsonValue
        public static String getValue(TestStringClass obj) {
            return obj.getValue();
        }
    }

    private static class TestEnumValueMethod {
        @JsonValue
        public static String getValue(TestEnumClass obj) {
            return obj.value.name();
        }
    }

    private static class TestEnumClass {
        public static enum TestEnum { VALUE1, VALUE2 }

        private final TestEnum value;

        public TestEnumClass(TestEnum value) { this.value = value; }
    }

    private static class TestException {
    }

    private static class ThrowingValueMethod implements JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jg, SerializerProvider sp) throws IOException {
            throw new IOException();
        }
    }

    private static class ThrowingValueMethodWithPath implements JsonSerializer<Object> {
        @Override
        public void serialize(Object o, JsonGenerator jg, SerializerProvider sp) throws IOException {
            throw new JsonMappingException.ValueInstantiationException("TestValueClass.access$100(Ljava/lang/Object;)Ljava/lang/Object;");
        }
    }
}