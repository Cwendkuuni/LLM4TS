import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testGet() {
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testBeginArray() {
        jsonTreeWriter.beginArray();
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testEndArray() {
        jsonTreeWriter.beginArray();
        jsonTreeWriter.endArray();
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testBeginObject() {
        jsonTreeWriter.beginObject();
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testEndObject() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.endObject();
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testName() {
        jsonTreeWriter.beginObject();
        jsonTreeWriter.name("key");
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testValueString() {
        jsonTreeWriter.value("value");
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testNullValue() {
        jsonTreeWriter.nullValue();
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testValueBoolean() {
        jsonTreeWriter.value(true);
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testValueDouble() {
        jsonTreeWriter.value(10.5);
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testValueLong() {
        jsonTreeWriter.value(100L);
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testValueNumber() {
        jsonTreeWriter.value(123.45);
        assertNotNull(jsonTreeWriter.get());
    }

    @Test
    public void testFlush() {
        assertDoesNotThrow(() -> jsonTreeWriter.flush());
    }

    @Test
    public void testClose() {
        assertDoesNotThrow(() -> jsonTreeWriter.close());
    }
}