import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.DeserializationProblem;
import com.fasterxml.jackson.databind.deser.std.FromStringDeserializer;
import com.fasterxml.jackson.databind.deser.std.UUIDDeserializer;
import com.fasterxml.jackson.databind.deser.std.StackTraceElementDeserializer;
import com.fasterxml.jackson.databind.deser.std.AtomicBooleanDeserializer;
import com.fasterxml.jackson.databind.deser.std.ByteBufferDeserializer;
import org.junit.Test;
import org.junit.Assert;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class JdkDeserializersTest {

    @Test
    public void testFind() throws IOException {
        String clsName = UUID.class.getName();
        Class<?> rawType = UUID.class;
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof UUIDDeserializer);

        clsName = StackTraceElement.class.getName();
        rawType = StackTraceElement.class;
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof StackTraceElementDeserializer);

        clsName = AtomicBoolean.class.getName();
        rawType = AtomicBoolean.class;
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        clsName = ByteBuffer.class.getName();
        rawType = ByteBuffer.class;
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNotNull(deserializer);
        Assert.assertTrue(deserializer instanceof ByteBufferDeserializer);

        clsName = Object.class.getName();
        rawType = Object.class;
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);

        clsName = String.class.getName();
        rawType = Object.class;
        deserializer = JdkDeserializers.find(rawType, clsName);
        assertNull(deserializer);
    }

    @Test(expected = DeserializationProblem.ClassIdMismatch.class)
    public void testFindClassMismatch() throws IOException {
        String clsName = Integer.class.getName();
        Class<?> rawType = String.class;
        JdkDeserializers.find(rawType, clsName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNullClassName() throws IOException {
        Class<?> rawType = UUID.class;
        String clsName = null;
        JdkDeserializers.find(rawType, clsName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFindNullType() throws IOException {
        String clsName = UUID.class.getName();
        Class<?> rawType = null;
        JdkDeserializers.find(rawType, clsName);
    }

    @Test
    public void testFindMultipleMatches() throws IOException {
        String clsName = List.class.getName();
        Class<?> rawType = Object[].class;
        JsonDeserializer<?> deserializer1 = JdkDeserializers.find(rawType, clsName);
        JsonDeserializer<?> deserializer2 = FromStringDeserializer.findDeserializer(rawType);
        assertNotNull(deserializer1);
        assertNotNull(deserializer2);
        Assert.assertNotSame(deserializer1, deserializer2);
    }

    @Test
    public void testClassNamesHashSet() {
        HashSet<String> classNames = new HashSet<>(JdkDeserializers._classNames);
        Assert.assertEquals(classNames.size(), 11);
        Arrays.asList(
                UUID.class, AtomicBoolean.class, StackTraceElement.class, ByteBuffer.class,
                FromStringDeserializer.class, Integer.class, Long.class, Float.class,
                Double.class, Character.class, Short.class
        ).forEach(clazz -> Assert.assertTrue(classNames.contains(clazz.getName())));
    }
}