import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.AtomicBoolean;
import java.util.UUID;

public class JdkDeserializersTest {

    @Test
    public void testFindForUUID() {
        Object result = JdkDeserializers.find(UUID.class, UUID.class.getName());
        JsonDeserializer<UUID> expected = new UUIDDeserializer();
        Assert.assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testFindForAtomicBoolean() {
        Object result = JdkDeserializers.find(AtomicBoolean.class, AtomicBoolean.class.getName());
        JsonDeserializer<AtomicBoolean> expected = new AtomicBooleanDeserializer();
        Assert.assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testFindForStackTraceElement() {
        Object result = JdkDeserializers.find(StackTraceElement.class, StackTraceElement.class.getName());
        JsonDeserializer<StackTraceElement> expected = new StackTraceElementDeserializer();
        Assert.assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testFindForByteBuffer() {
        Object result = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        JsonDeserializer<ByteBuffer> expected = new ByteBufferDeserializer();
        Assert.assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testFindForUnknownClass() {
        Object result = JdkDeserializers.find(String.class, "java.lang.String".getClass().getName());
        Assert.assertNull(result);
    }

    @Test
    public void testFromStringDeserializer() throws IOException, JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        String json = "{\"className\":\"java.util.UUID\",\"value\":\"123e4567-e89b-12d3-a456-426614174000\"}";
        Object result = mapper.readValue(new ByteArrayInputStream(json.getBytes()), JsonDeserializer.class);
        JsonDeserializer<?> expected = new UUIDDeserializer();
        Assert.assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testFindDeserializerForByteBuffer() {
        Object result = JdkDeserializers.find(ByteBuffer.class, ByteBuffer.class.getName());
        JsonDeserializer<ByteBuffer> expected = new ByteBufferDeserializer();
        Assert.assertEquals(expected.getClass(), result.getClass());
    }

    @Test
    public void testFindDeserializerForUnknownClass() {
        Object result = JdkDeserializers.find(String.class, "java.lang.String".getClass().getName());
        Assert.assertNull(result);
    }
}