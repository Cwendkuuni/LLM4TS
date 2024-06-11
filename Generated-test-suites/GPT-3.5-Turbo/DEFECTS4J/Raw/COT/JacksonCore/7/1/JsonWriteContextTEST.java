import com.fasterxml.jackson.core.JsonGenerationException;
import org.junit.Test;

import static org.junit.Assert.*;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        assertEquals(JsonWriteContext.TYPE_ROOT, rootContext.getType());
        assertNull(rootContext.getParent());
        assertNull(rootContext.getCurrentName());
        assertNull(rootContext.getCurrentValue());
        assertNull(rootContext.getDupDetector());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext childArrayContext = rootContext.createChildArrayContext();
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(rootContext, childArrayContext.getParent());
        assertNull(childArrayContext.getCurrentName());
        assertNull(childArrayContext.getCurrentValue());
        assertNull(childArrayContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext childObjectContext = rootContext.createChildObjectContext();
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(rootContext, childObjectContext.getParent());
        assertNull(childObjectContext.getCurrentName());
        assertNull(childObjectContext.getCurrentValue());
        assertNull(childObjectContext.getDupDetector());
    }

    @Test
    public void testWriteFieldName() throws JsonGenerationException {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        int status = rootContext.writeFieldName("fieldName");
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals("fieldName", rootContext.getCurrentName());
    }

    @Test
    public void testWriteValueForObjectContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext objectContext = rootContext.createChildObjectContext();
        int status = objectContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
        assertEquals(0, objectContext.getCurrentIndex());
    }

    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        JsonWriteContext arrayContext = rootContext.createChildArrayContext();
        int status = arrayContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(0, arrayContext.getCurrentIndex());
    }

    @Test
    public void testWriteValueForRootContext() {
        JsonWriteContext rootContext = JsonWriteContext.createRootContext();
        int status = rootContext.writeValue();
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(0, rootContext.getCurrentIndex());
    }
}