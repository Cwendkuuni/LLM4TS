import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class JdkDeserializersTest {

    private static ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testFindForUUID() {
        Class<UUID> rawType = UUID.class;
        String clsName = UUID.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(UUID.class, deserializer.getClass());

        // typical use case
        String json = "\"123e4567-123e-4567-aef0-123456789012\"";
        UUID expected = new UUID("123e4567-123e-4567-aef0-123456789012");
        try {
            UUID actual = objectMapper.readValue(json, UUID.class);
            Assert.assertEquals(expected, actual);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFindForAtomicBoolean() {
        Class<AtomicBoolean> rawType = AtomicBoolean.class;
        String clsName = AtomicBoolean.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(AtomicBooleanDeserializer.class, deserializer.getClass());

        // typical use case
        String json = "true";
        AtomicBoolean expected = new AtomicBoolean(true);
        try {
            AtomicBoolean actual = objectMapper.readValue(json, AtomicBoolean.class);
            Assert.assertEquals(expected, actual);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        // edge case
        json = "false";
        expected = new AtomicBoolean(false);
        try {
            AtomicBoolean actual = objectMapper.readValue(json, AtomicBoolean.class);
            Assert.assertEquals(expected, actual);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        // error scenario
        json = "[1]";
        try {
            objectMapper.readValue(json, AtomicBoolean.class);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testFindForStackTraceElement() {
        Class<StackTraceElement> rawType = StackTraceElement.class;
        String clsName = StackTraceElement.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(StackTraceElementDeserializer.class, deserializer.getClass());

        // typical use case
        String json = "{\"className\":\"JdkDeserializersTest\", \"methodName\":\"testFindForStackTraceElement\", \"lineNumber\":123, \"fileName\":\"JdkDeserializersTest.java\", \"nativeMethod\":false}";
        StackTraceElement expected = new StackTraceElement("JdkDeserializersTest", "testFindForStackTraceElement", "JdkDeserializersTest.java", 123);
        try {
            StackTraceElement actual = objectMapper.readValue(json, StackTraceElement.class);
            Assert.assertEquals(expected, actual);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }

    @Test
    public void testFindForByteBuffer() {
        Class<ByteBuffer> rawType = ByteBuffer.class;
        String clsName = ByteBuffer.class.getName();
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        Assert.assertNotNull(deserializer);
        Assert.assertEquals(ByteBufferDeserializer.class, deserializer.getClass());

        // typical use case
        String json = "{\"limit\":10, \"position\":0, \"mark\":-1}";
        ByteBuffer expected = ByteBuffer.allocate(10);
        try {
            ByteBuffer actual = objectMapper.readValue(json, new TypeReference<ByteBuffer>() {});
            Assert.assertEquals(expected, actual);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        // edge case
        json = "{\"limit\":-1, \"position\":0, \"mark\":-1}";
        expected = ByteBuffer.allocate(0);
        try {
            ByteBuffer actual = objectMapper.readValue(json, new TypeReference<ByteBuffer>() {});
            Assert.assertEquals(expected, actual);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }

        // error scenario
        json = "{\"limit\":10, \"position\":11, \"mark\":-1}";
        try {
            objectMapper.readValue(json, ByteBuffer.class);
            Assert.fail("Expected IOException");
        } catch (IOException e) {
            // Expected
        }
    }
}