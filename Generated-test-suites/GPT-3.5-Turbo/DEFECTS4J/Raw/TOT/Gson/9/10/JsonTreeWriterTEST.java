import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    @Test
    public void testBeginArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        assertEquals("[", writer.get().toString());
    }

    @Test
    public void testEndArray() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginArray();
        writer.endArray();
        assertEquals("[]", writer.get().toString());
    }

    @Test
    public void testBeginObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        assertEquals("{", writer.get().toString());
    }

    @Test
    public void testEndObject() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.endObject();
        assertEquals("{}", writer.get().toString());
    }
    
    @Test
    public void testName() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.name("name");
        assertEquals("{\"name\":", writer.get().toString());
    }
    
    @Test
    public void testValueString() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("Hello");
        assertEquals("\"Hello\"", writer.get().toString());
    }
    
    @Test
    public void testValueNull() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.nullValue();
        assertEquals("null", writer.get().toString());
    }
    
    @Test
    public void testValueBoolean() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(true);
        assertEquals("true", writer.get().toString());
    }
    
    @Test
    public void testValueDouble() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(2.5);
        assertEquals("2.5", writer.get().toString());
    }
    
    @Test
    public void testValueLong() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(10L);
        assertEquals("10", writer.get().toString());
    }

    @Test
    public void testValueNumber() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(3.14);
        assertEquals("3.14", writer.get().toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testIllegalState() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value("Hello");
        writer.beginArray();
    }

    @Test(expected = IOException.class)
    public void testIncompleteDocument() throws IOException {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.beginObject();
        writer.close();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInfinityValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(Double.POSITIVE_INFINITY);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNaNValue() {
        JsonTreeWriter writer = new JsonTreeWriter();
        writer.value(Double.NaN);
    }

}