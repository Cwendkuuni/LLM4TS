import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext jsonContext;
    private DupDetectorMock dupDetectorMock;

    @Before
    public void setUp() {
        jsonContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        dupDetectorMock = new DupDetectorMock();
        jsonContext.withDupDetector(dupDetectorMock);
    }

    @Test
    public void testWriteFieldName_WhenNameIsNull_ReturnsExpectValueStatus() throws JsonProcessingException {
        int status = jsonContext.writeFieldName(null);
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);
    }

    @Test
    public void testWriteFieldName_WhenDuplicatedFieldName_ThrowsJsonGenerationException() {
        dupDetectorMock.setIsDup(true);
        try {
            jsonContext.writeFieldName("fieldName");
            fail("Expected JsonGenerationException was not thrown");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }

    @Test
    public void testWriteFieldName_WhenNoNameYet_ReturnsOkAsIsStatus() throws JsonProcessingException {
        int status = jsonContext.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testWriteFieldName_WhenNameAlreadyExists_ReturnsOkAfterCommaStatus() throws JsonProcessingException {
        dupDetectorMock.setIsDup(false);
        jsonContext.writeFieldName("fieldName1");
        jsonContext.writeFieldName("fieldName2");
        int status = jsonContext.writeFieldName("fieldName3");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteFieldName_WhenRootContext_ReturnsOkAfterSpaceStatus() throws JsonProcessingException {
        jsonContext.reset(JsonWriteContext.TYPE_ROOT);
        int status = jsonContext.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
    }

    @Test
    public void testWriteValue_WhenContextIsObject_ReturnsOkAfterColonStatus() {
        int status = jsonContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }

    @Test
    public void testWriteValue_WhenContextIsArray_ReturnsOkAfterCommaStatus() {
        jsonContext.reset(JsonWriteContext.TYPE_ARRAY);
        int status = jsonContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }

    @Test
    public void testWriteValue_WhenContextIsRoot_ReturnsOkAsIsStatus() {
        jsonContext.reset(JsonWriteContext.TYPE_ROOT);
        int status = jsonContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }

    @Test
    public void testCreateChildArrayContext_WhenChildContextDoesNotExist_ReturnsNewChildArrayContext() {
        JsonWriteContext childContext = jsonContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(jsonContext, childContext.getParent());
    }

    @Test
    public void testCreateChildArrayContext_WhenChildContextAlreadyExists_ReturnsChildContextReset() {
        jsonContext.createChildArrayContext();
        JsonWriteContext childContext = jsonContext.createChildArrayContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childContext.getType());
        assertEquals(jsonContext, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext_WhenChildContextDoesNotExist_ReturnsNewChildObjectContext() {
        JsonWriteContext childContext = jsonContext.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(jsonContext, childContext.getParent());
    }

    @Test
    public void testCreateChildObjectContext_WhenChildContextAlreadyExists_ReturnsChildContextReset() {
        jsonContext.createChildObjectContext();
        JsonWriteContext childContext = jsonContext.createChildObjectContext();
        assertNotNull(childContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childContext.getType());
        assertEquals(jsonContext, childContext.getParent());
    }

    @Test
    public void testGetCurrentValue_ReturnsCurrentValue() {
        Object value = new Object();
        jsonContext.setCurrentValue(value);
        assertEquals(value, jsonContext.getCurrentValue());
    }

    @Test
    public void testToString_WhenContextIsObject_ReturnsJsonPathRepresentationForObject() {
        jsonContext.reset(JsonWriteContext.TYPE_OBJECT);
        jsonContext.writeFieldName("fieldName");
        assertEquals("{\"fieldName\"}", jsonContext.toString());
    }

    @Test
    public void testToString_WhenContextIsArray_ReturnsJsonPathRepresentationForArray() {
        jsonContext.reset(JsonWriteContext.TYPE_ARRAY);
        jsonContext.writeValue();
        assertEquals("[0]", jsonContext.toString());
    }

    @Test
    public void testToString_WhenContextIsRoot_ReturnsJsonPathRepresentationForRoot() {
        jsonContext.reset(JsonWriteContext.TYPE_ROOT);
        assertEquals("/", jsonContext.toString());
    }

    private class DupDetectorMock extends DupDetector {
        private boolean isDup;

        public void setIsDup(boolean isDup) {
            this.isDup = isDup;
        }

        @Override
        public boolean isDup(String name) throws JsonProcessingException {
            return isDup;
        }

        @Override
        public void reset() {
        }

        @Override
        public DupDetector child() {
            return null;
        }
    }
}