
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext context;

    @Before
    public void setUp() {
        context = new JsonWriteContext(0, null, null);
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = context.createChildArrayContext();
        assertNotNull(childArrayContext);
        assertEquals(childArrayContext.getParent(), context);
        assertEquals(childArrayContext.getType(), JsonWriteContext.TYPE_ARRAY);
        assertFalse(childArrayContext.gotName());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = context.createChildObjectContext();
        assertNotNull(childObjectContext);
        assertEquals(childObjectContext.getParent(), context);
        assertEquals(childObjectContext.getType(), JsonWriteContext.TYPE_OBJECT);
        assertFalse(childObjectContext.gotName());
    }

    @Test
    public void testWriteValue() {
        context.setType(JsonWriteContext.TYPE_OBJECT);
        context.setGotName(true);
        context.setIndex(0);

        int status = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertFalse(context.gotName());
        assertEquals(1, context.getIndex());
    }

    @Test
    public void testWriteValueArray() {
        context.setType(JsonWriteContext.TYPE_ARRAY);
        context.setIndex(0);

        int status = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
        assertEquals(1, context.getIndex());
    }

    @Test
    public void testWriteValueRoot() {
        context.setType(JsonWriteContext.TYPE_ROOT);
        context.setIndex(0);

        int status = context.writeValue();

        assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, status);
        assertEquals(1, context.getIndex());
    }

    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String fieldName = "testField";

        int status = context.writeFieldName(fieldName);

        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertTrue(context.gotName());
        assertEquals(fieldName, context.getCurrentName());
    }

    @Test(expected = JsonGenerationException.class)
    public void testWriteFieldNameDuplicate() throws JsonProcessingException {
        String fieldName = "testField";
        DupDetector dupDetector = new DupDetector(null);

        context.setDupDetector(dupDetector);
        dupDetector.reset();
        dupDetector._firstName = fieldName;

        context.writeFieldName(fieldName);
    }

    @Test
    public void testResetObjectContext() {
        context.setType(JsonWriteContext.TYPE_OBJECT);
        context.setGotName(true);
        context.setIndex(0);
        context.setCurrentName("testField");
        context.setCurrentValue("testValue");

        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_OBJECT);

        assertEquals(JsonWriteContext.TYPE_OBJECT, resetContext.getType());
        assertFalse(resetContext.gotName());
        assertEquals(-1, resetContext.getIndex());
        assertNull(resetContext.getCurrentName());
        assertNull(resetContext.getCurrentValue());
        assertNull(resetContext.getDupDetector());
    }

    @Test
    public void testResetArrayContext() {
        context.setType(JsonWriteContext.TYPE_ARRAY);
        context.setIndex(0);
        context.setCurrentName("testField");
        context.setCurrentValue("testValue");

        JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_ARRAY);

        assertEquals(JsonWriteContext.TYPE_ARRAY, resetContext.getType());
        assertEquals(-1, resetContext.getIndex());
        assertNull(resetContext.getCurrentName());
        assertNull(resetContext.getCurrentValue());
        assertNull(resetContext.getDupDetector());
    }

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertNotNull(rootContext);
        assertNull(rootContext.getParent());
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertFalse(rootContext.gotName());
        assertNull(rootContext.getDupDetector());
    }
}
