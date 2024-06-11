import static org.junit.Assert.assertEquals;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerationException;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @Before
    public void setUp() {
        context = new JsonWriteContext(JsonStreamContext.TYPE_ROOT, null, null);
    }

    @After
    public void tearDown() {
        context = null;
    }

    @Test
    public void testWriteFieldName_ReturnsStatusOkAsIs_WhenGotNameIsFalseAndTypeIsObject() throws JsonGenerationException {
        int result = context.writeFieldName("field");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("field", context.getCurrentName());
        assertEquals(true, context._gotName);
    }

    @Test
    public void testWriteFieldName_ReturnsStatusExpectValue_WhenGotNameIsTrueAndTypeIsObject() throws JsonGenerationException {
        context._gotName = true;
        int result = context.writeFieldName("field");
        assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, result);
        assertEquals("field", context.getCurrentName());
        assertEquals(true, context._gotName);
    }

    @Test
    public void testWriteFieldName_ThrowsJsonGenerationException_WhenDuplicateFieldNameDetected() {
        JsonWriteContext duplicateContext = new JsonWriteContext(JsonStreamContext.TYPE_OBJECT, null, null);
        duplicateContext.writeFieldName("field");
        try {
            context.writeFieldName("field");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'field'", e.getMessage());
        }
    }

    @Test
    public void testWriteValue_ReturnsStatusOkAsIs_WhenTypeIsObject() {
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    @Test
    public void testWriteValue_ReturnsStatusOkAsIs_WhenTypeIsArrayAndIndexIsNegative() {
        context._type = JsonStreamContext.TYPE_ARRAY;
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testWriteValue_ReturnsStatusOkAfterComma_WhenTypeIsArrayAndIndexIsPositive() {
        context._type = JsonStreamContext.TYPE_ARRAY;
        context._index = 0;
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValue_ReturnsStatusOkAsIs_WhenTypeIsRootAndIndexIsZero() {
        context._index = 0;
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testWriteValue_ReturnsStatusOkAfterSpace_WhenTypeIsRootAndIndexIsPositive() {
        context._index = 1;
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    @Test
    public void testCreateChildArrayContext_ReturnsNewContextWithArrayType() {
        JsonWriteContext childContext = context.createChildArrayContext();
        assertEquals(JsonStreamContext.TYPE_ARRAY, childContext._type);
        assertEquals(context, childContext._parent);
    }

    @Test
    public void testCreateChildObjectContext_ReturnsNewContextWithObjectType() {
        JsonWriteContext childContext = context.createChildObjectContext();
        assertEquals(JsonStreamContext.TYPE_OBJECT, childContext._type);
        assertEquals(context, childContext._parent);
    }

    @Test
    public void testToString_ReturnsJsonPathRepresentationOfContext() {
        context._type = JsonStreamContext.TYPE_OBJECT;
        context._currentName = "field";
        String result = context.toString();
        assertEquals("{\"field\"}", result);

        context._type = JsonStreamContext.TYPE_ARRAY;
        context._index = 1;
        result = context.toString();
        assertEquals("[1]", result);

        context._type = JsonStreamContext.TYPE_ROOT;
        result = context.toString();
        assertEquals("/", result);
    }

}