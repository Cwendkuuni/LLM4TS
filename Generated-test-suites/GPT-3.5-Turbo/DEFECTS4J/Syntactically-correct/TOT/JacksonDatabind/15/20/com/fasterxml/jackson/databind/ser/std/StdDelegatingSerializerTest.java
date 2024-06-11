import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        Converter<Object, ?> converter = new Converter<Object, Object>() {
            @Override
            public Object convert(Object value) {
                return value;
            }
        };
        serializer = new StdDelegatingSerializer(converter);

        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new com.fasterxml.jackson.databind.module.SimpleModule() {
            @Override
            public void setupModule(SetupContext context) {
                context.addSerializer(serializer);
            }
        });
    }

    @Test
    public void testSerialize_ObjectNotNull_SerializeDelegateValue() throws IOException {
        Object value = new Object();
        String expected = objectMapper.writeValueAsString(value);

        String actual = objectMapper.writeValueAsString(new TestClass(value));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerialize_ObjectNull_SerializeNull() throws IOException {
        Object value = null;
        String expected = objectMapper.writeValueAsString(value);

        String actual = objectMapper.writeValueAsString(new TestClass(value));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithType_ObjectNotNull_SerializeDelegateValueWithType() throws IOException {
        Object value = new Object();
        String expected = objectMapper.writeValueAsString(value);

        String actual = objectMapper.writeValueAsString(new TypeTestClass(value));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSerializeWithType_ObjectNull_SerializeNullWithType() throws IOException {
        Object value = null;
        String expected = objectMapper.writeValueAsString(value);

        String actual = objectMapper.writeValueAsString(new TypeTestClass(value));

        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testIsEmpty_ObjectNotNull_ReturnFalse() throws IOException {
        Object value = new Object();

        boolean result = serializer.isEmpty(new com.fasterxml.jackson.databind.SerializerProvider(), new TestClass(value));

        Assert.assertFalse(result);
    }

    @Test
    public void testIsEmpty_ObjectNull_ReturnTrue() throws IOException {
        Object value = null;

        boolean result = serializer.isEmpty(new com.fasterxml.jackson.databind.SerializerProvider(), new TestClass(value));

        Assert.assertTrue(result);
    }

    private static class TestClass {
        private Object value;

        public TestClass(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }

    private static class TypeTestClass {
        private Object value;

        public TypeTestClass(Object value) {
            this.value = value;
        }

        public Object getValue() {
            return value;
        }
    }
}