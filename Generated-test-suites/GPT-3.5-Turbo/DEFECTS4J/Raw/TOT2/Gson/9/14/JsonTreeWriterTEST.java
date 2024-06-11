import static org.junit.Assert.*;
import org.junit.Test;

public class JsonTreeWriterTest {

    @Test
    public void testGet() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test get method
        assertNotNull(writer.get());
    }

    @Test
    public void testBeginArray() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test beginArray method
        writer.beginArray();
        assertNotNull(writer.peek());
    }

    @Test
    public void testEndArray() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test endArray method
        writer.beginArray();
        writer.endArray();
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testBeginObject() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test beginObject method
        writer.beginObject();
        assertNotNull(writer.peek());
    }

    @Test
    public void testEndObject() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test endObject method
        writer.beginObject();
        writer.endObject();
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testName() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test name method
        writer.beginObject();
        writer.name("testName");
        assertNotNull(writer.pendingName);
    }

    @Test
    public void testValueString() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test value method for String
        writer.value("testString");
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testNullValue() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test nullValue method
        writer.nullValue();
        assertTrue(writer.peek() instanceof JsonNull);
    }

    @Test
    public void testValueBoolean() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test value method for boolean
        writer.value(true);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testValueDouble() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test value method for double
        writer.value(10.5);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testValueLong() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test value method for long
        writer.value(100);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

    @Test
    public void testValueNumber() {
        // Create JsonTreeWriter instance
        JsonTreeWriter writer = new JsonTreeWriter();

        // Test value method for Number
        writer.value(10);
        assertTrue(writer.peek() instanceof JsonPrimitive);
    }

}