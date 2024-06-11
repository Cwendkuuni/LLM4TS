import org.junit.Before;
import org.junit.Test;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @Before
    public void setup() {
        context = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        int result = context.writeFieldName("name");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
        assertEquals("name", context.getCurrentName());
    }

    @Test
    public void testWriteFieldNameWhenNameAlreadyExists() throws JsonProcessingException {
        context.writeFieldName("name");
        try {
            context.writeFieldName("name");
            fail("Expected JsonGenerationException");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field 'name'", e.getMessage());
        }
    }

    @Test
    public void testWriteValueForObjectType() {
        context = context.createChildObjectContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, result);
    }

    @Test
    public void testWriteValueForArrayType() {
        context = context.createChildArrayContext();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValueForRootType() {
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, result);
    }

    @Test
    public void testWriteValueForFirstValueInRootType() {
        context.writeValue();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, result);
    }

    @Test
    public void testWriteValueForFirstValueInArrayType() {
        context = context.createChildArrayContext();
        context.writeValue();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testWriteValueForFirstValueInObjectType() {
        context = context.createChildObjectContext();
        context.writeFieldName("name");
        context.writeValue();
        int result = context.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, result);
    }

    @Test
    public void testCreateChildArrayContext() {
        context = context.createChildArrayContext();
        assertNotNull(context);
        assertEquals(context.getType(), JsonWriteContext.TYPE_ARRAY);
    }

    @Test
    public void testCreateChildObjectContext() {
        context = context.createChildObjectContext();
        assertNotNull(context);
        assertEquals(context.getType(), JsonWriteContext.TYPE_OBJECT);
    }
}