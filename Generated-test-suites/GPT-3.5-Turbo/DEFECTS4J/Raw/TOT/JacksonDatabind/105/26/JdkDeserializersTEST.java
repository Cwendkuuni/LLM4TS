import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class JdkDeserializersTest {

    @Test
    public void testFindShouldReturnExpectedDeserializerForValidClassNames() {
        // Test for valid class names
        Class<?> rawType = UUID.class;
        String className = "java.util.UUID";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof FromStringDeserializer);

        rawType = AtomicBoolean.class;
        className = "java.util.concurrent.atomic.AtomicBoolean";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof AtomicBooleanDeserializer);

        rawType = StackTraceElement.class;
        className = "java.lang.StackTraceElement";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof StackTraceElementDeserializer);

        rawType = ByteBuffer.class;
        className = "java.nio.ByteBuffer";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNotNull(deserializer);
        assertTrue(deserializer instanceof ByteBufferDeserializer);

    }

    @Test
    public void testFindShouldReturnNullForInvalidClassNames() {
        // Test for invalid class names
        Class<?> rawType = UUID.class;
        String className = "java.util.Random";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        rawType = AtomicBoolean.class;
        className = "java.lang.Boolean";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        rawType = StackTraceElement.class;
        className = "java.lang.String";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        rawType = ByteBuffer.class;
        className = "java.util.List";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);
    }

    @Test
    public void testFindShouldReturnNullForPrimitiveTypes() {
        // Test for primitive types
        Class<?> rawType = int.class;
        String className = "int";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        rawType = double.class;
        className = "double";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        rawType = boolean.class;
        className = "boolean";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);
    }

    @Test
    public void testFindShouldReturnNullForUnsupportedClasses() {
        // Test for unsupported classes
        Class<?> rawType = Date.class;
        String className = "java.util.Date";
        JsonDeserializer<?> deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        rawType = List.class;
        className = "java.util.List";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);

        rawType = String.class;
        className = "java.lang.String";
        deserializer = JdkDeserializers.find(rawType, className);
        assertNull(deserializer);
    }
}