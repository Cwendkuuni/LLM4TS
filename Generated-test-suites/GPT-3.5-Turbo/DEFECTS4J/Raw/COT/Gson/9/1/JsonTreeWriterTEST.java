import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonTreeWriterTest {

    private JsonTreeWriter jsonTreeWriter;

    @Before
    public void setUp() {
        jsonTreeWriter = new JsonTreeWriter();
    }

    @Test
    public void testBeginArray() {
        try {
            jsonTreeWriter.beginArray();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEndArray() {
        try {
            jsonTreeWriter.endArray();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testBeginObject() {
        try {
            jsonTreeWriter.beginObject();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testEndObject() {
        try {
            jsonTreeWriter.endObject();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testName() {
        try {
            jsonTreeWriter.name("test");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueString() {
        try {
            jsonTreeWriter.value("test");
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testNullValue() {
        try {
            jsonTreeWriter.nullValue();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueBoolean() {
        try {
            jsonTreeWriter.value(true);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueDouble() {
        try {
            jsonTreeWriter.value(1.0);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueLong() {
        try {
            jsonTreeWriter.value(1L);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testValueNumber() {
        try {
            jsonTreeWriter.value(1);
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testFlush() {
        try {
            jsonTreeWriter.flush();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        try {
            jsonTreeWriter.close();
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}