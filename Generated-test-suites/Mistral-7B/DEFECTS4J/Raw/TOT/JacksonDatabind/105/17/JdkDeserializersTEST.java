import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Test;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JdkDeserializersTest {

    @Test
    public void testFindForUUID() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(UUID.class, UUID.class.getName());
        Object result = null;
        try {
            result = mapper.readValue("\"01234567-89AB-CDEF-1234-567890123456\"", new TypeReference<UUID>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        assertEquals(new UUID("01234567-89AB-CDEF-1234-567890123456"), result);
        assertNull(JdkDeserializers.find(Integer.class, UUID.class.getName()));
    }

    @Test
    public void testFindForAtomicBoolean() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        Object result = null;
        try {
            result = mapper.readValue("true", AtomicBoolean.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        AtomicBoolean expected = new AtomicBoolean(true);
        assertEquals(expected, result);

        deserializer = JdkDeserializers.find(Integer.class, AtomicBoolean.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindForStackTraceElement() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        Object result = null;
        try {
            result = mapper.readValue("[class com.example.Main test(com.example.MyClass.java:35)]", StackTraceElement[].class);
            StackTraceElement expected = new StackTraceElement("com.example.Main", "test", "com.example.MyClass.java", 35);
            assertEquals(expected, ((StackTraceElement[]) result)[0]);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        deserializer = JdkDeserializers.find(Integer.class, StackTraceElement.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindForByteBuffer() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        Object result = null;
        try {
            ByteBuffer byteBuffer = ByteBuffer.allocate(10);
            byteBuffer.put("Hello".getBytes());
            result = mapper.readValue(mapper.writeValueAsString(byteBuffer), ByteBuffer.class);
            assertEquals(byteBuffer, (ByteBuffer) result);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        deserializer = JdkDeserializers.find(Integer.class, ByteBuffer.class.getName());
        assertNull(deserializer);
    }

    @Test
    public void testFindUnknownClass() {
        ObjectMapper mapper = new ObjectMapper();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(String.class, "UnknownClass.class.getName()");
        assertNull(deserializer);
    }
}