import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Type;

import org.junit.Test;


public class StdSerializerTest {

    @Test
    public void testSerialize() {
        // Arrange
        StdSerializer<TestClass> serializer = new StdSerializer<>(TestClass.class);

        // Act
        // Create an instance of TestClass
        TestClass testObj = new TestClass("John", 25);
        String json = serialize(testObj, serializer);

        // Assert
        assertNotNull(json);
        assertEquals("{\"name\":\"John\",\"age\":25}", json);
    }

    @Test
    public void testGetSchema() {
        // Arrange
        StdSerializer<TestClass> serializer = new StdSerializer<>(TestClass.class);

        // Act
        // Create an instance of TestClass
        TestClass testObj = new TestClass("John", 25);
        String schema = getSchema(testObj, serializer);

        // Assert
        assertNotNull(schema);
        assertTrue(schema.contains("\"type\":\"string\""));
    }

    @Test
    public void testWrapAndThrow() {
        // Arrange
        StdSerializer<TestClass> serializer = new StdSerializer<>(TestClass.class);

        // Act & Assert
        try {
            // Create an instance of TestClass
            TestClass testObj = new TestClass(null, 25);
            serialize(testObj, serializer);
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Cannot serialize null name"));
        }
    }

    // Helper methods
    private <T> String serialize(T value, StdSerializer<T> serializer) throws IOException {
        String result = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            StringWriter writer = new StringWriter();
            JsonGenerator generator = mapper.getJsonFactory().createJsonGenerator(writer);

            serializer.serialize(value, generator, new DefaultSerializerProvider());
            generator.close();

            result = writer.toString();
        } catch (Exception e) {
            throw new IOException(e);
        }

        return result;
    }

    private <T> String getSchema(T value, StdSerializer<T> serializer) throws IOException {
        String result = null;

        try {
            ObjectMapper mapper = new ObjectMapper();
            StringWriter writer = new StringWriter();
            JsonGenerator generator = mapper.getJsonFactory().createJsonGenerator(writer);

            serializer.getSchema(value, generator, new DefaultSerializerProvider());
            generator.close();

            result = writer.toString();
        } catch (Exception e) {
            throw new IOException(e);
        }

        return result;
    }

    private class TestClass {
        private String name;
        private int age;

        public TestClass(String name, int age) {
            this.name = name;
            this.age = age;
        }

        public String getName() {
            return name;
        }

        public int getAge() {
            return age;
        }
    }

}