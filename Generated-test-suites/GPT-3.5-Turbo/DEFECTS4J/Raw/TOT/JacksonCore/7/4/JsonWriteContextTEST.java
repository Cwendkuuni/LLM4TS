import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext context;
    private DupDetector dups;

    @Before
    public void setUp() {
        dups = new DupDetector();
        context = JsonWriteContext.createRootContext(dups);
    }

    @Test
    public void testWriteFieldName_WhenGotNameIsTrue() throws JsonProcessingException {
        context._gotName = true;
        String name = "fieldName";

        int result = context.writeFieldName(name);

        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
    }

    @Test
    public void testWriteFieldName_WhenGotNameIsFalse() throws JsonProcessingException {
        context._gotName = false;
        String name = "fieldName";

        int result = context.writeFieldName(name);

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteFieldName_WhenDuplicateField() {
        context._gotName = false;
        context._currentName = "fieldName";
        dups.add("fieldName");

        try {
            context.writeFieldName("fieldName");
            fail("Expected JsonGenerationException to be thrown");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'fieldName'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue_WhenTypeIsObject() {
        context._type = JsonWriteContext.TYPE_OBJECT;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
        assertTrue(context._gotName);
        assertEquals(0, context._index);
    }

    @Test
    public void testWriteValue_WhenTypeIsArrayAndNoPreviousValues() {
        context._type = JsonWriteContext.TYPE_ARRAY;
        context._index = -1;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(0, context._index);
    }

    @Test
    public void testWriteValue_WhenTypeIsArrayAndPreviousValuesExist() {
        context._type = JsonWriteContext.TYPE_ARRAY;
        context._index = 0;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
        assertEquals(1, context._index);
    }

    @Test
    public void testWriteValue_WhenTypeIsRootAndNoPreviousValues() {
        context._type = JsonWriteContext.TYPE_ROOT;
        context._index = -1;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals(0, context._index);
    }

    @Test
    public void testWriteValue_WhenTypeIsRootAndPreviousValuesExist() {
        context._type = JsonWriteContext.TYPE_ROOT;
        context._index = 0;

        int result = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
        assertEquals(1, context._index);
    }

    @Test
    public void testCreateChildArrayContext() {
        context._child = null;

        JsonWriteContext result = context.createChildArrayContext();

        assertNotNull(result);
        assertEquals(JsonWriteContext.TYPE_ARRAY, result._type);
        assertSame(context, result._parent);
        assertNotNull(result._dups);
        assertNotSame(context._dups, result._dups);
    }

    @Test
    public void testCreateChildObjectContext() {
        context._child = null;

        JsonWriteContext result = context.createChildObjectContext();

        assertNotNull(result);
        assertEquals(JsonWriteContext.TYPE_OBJECT, result._type);
        assertSame(context, result._parent);
        assertNotNull(result._dups);
        assertNotSame(context._dups, result._dups);
    }

}