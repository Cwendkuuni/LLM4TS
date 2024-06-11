import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {

    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.writeFieldName("testField"));
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, jsonWriteContext.writeFieldName("testField"));
    }

    @Test
    public void testWriteValue() {
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.writeValue());

        JsonWriteContext arrayContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, jsonWriteContext, null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, arrayContext.writeValue());

        JsonWriteContext rootContext = JsonWriteContext.createRootContext(null);
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, rootContext.writeValue());
    }

    @Test(expected = JsonGenerationException.class)
    public void testCheckDup() throws JsonProcessingException {
        jsonWriteContext.writeFieldName("testField");
        jsonWriteContext.writeFieldName("testField");
    }

    @Test
    public void testToString() {
        assertEquals("{/}", jsonWriteContext.toString());

        jsonWriteContext.writeFieldName("testField");
        assertEquals("{\"testField\"}", jsonWriteContext.toString());
    }

    @Test
    public void testGetCurrentValue() {
        assertNull(jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testSetCurrentValue() {
        Object value = new Object();
        jsonWriteContext.setCurrentValue(value);
        assertEquals(value, jsonWriteContext.getCurrentValue());
    }

    @Test
    public void testParentContext() {
        assertNull(jsonWriteContext.getParent());
    }

    @Test
    public void testCurrentName() {
        assertNull(jsonWriteContext.getCurrentName());
        jsonWriteContext.writeFieldName("testField");
        assertEquals("testField", jsonWriteContext.getCurrentName());
    }

    @Test
    public void testDupDetector() {
        assertNull(jsonWriteContext.getDupDetector());
    }

    @Test
    public void testResetContext() {
        jsonWriteContext.writeFieldName("testField");
        jsonWriteContext.reset(JsonWriteContext.TYPE_OBJECT);
        assertNull(jsonWriteContext.getCurrentName());
        assertNull(jsonWriteContext.getDupDetector());
    }
}