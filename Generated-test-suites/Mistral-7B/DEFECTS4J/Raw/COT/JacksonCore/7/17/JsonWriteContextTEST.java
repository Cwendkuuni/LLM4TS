import com.fasterxml.jackson.core.*;
import org.junit.Assert;
import org.junit.Test;

public class JsonWriteContextTest {

    @Test
    public void testCreateRootContext() {
        JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext();
        Assert.assertNull(jsonWriteContext.getParent());
        Assert.assertNull(jsonWriteContext.getCurrentName());
        Assert.assertNull(jsonWriteContext.getDupDetector());
    }

    @Test
    public void testCreateRootContextWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext jsonWriteContext = JsonWriteContext.createRootContext(dupDetector);
        Assert.assertEquals(dupDetector, jsonWriteContext.getDupDetector());
        Assert.assertNull(jsonWriteContext.getParent());
        Assert.assertNull(jsonWriteContext.getCurrentName());
    }

    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parentJsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext jsonWriteContext = parentJsonWriteContext.createChildArrayContext();
        Assert.assertEquals(parentJsonWriteContext, jsonWriteContext.getParent());
        Assert.assertNull(jsonWriteContext.getCurrentName());
        Assert.assertNull(jsonWriteContext.getDupDetector());
    }

    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parentJsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext jsonWriteContext = parentJsonWriteContext.createChildObjectContext();
        Assert.assertEquals(parentJsonWriteContext, jsonWriteContext.getParent());
        Assert.assertNull(jsonWriteContext.getCurrentName());
        Assert.assertNull(jsonWriteContext.getDupDetector());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithGotName() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.writeFieldName("name");
        jsonWriteContext.writeFieldName("age");
        jsonWriteContext.writeValue(); // reset _gotName to false
        jsonWriteContext.writeFieldName("name"); // should throw exception
    }

    @Test
    public void testWriteValueForRootContext() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ROOT, null, null);
        jsonWriteContext.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, jsonWriteContext.getStatus());
    }

    @Test
    public void testWriteValueForArrayContext() {
        JsonWriteContext parentJsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_ARRAY, null, null);
        JsonWriteContext jsonWriteContext = parentJsonWriteContext.createChildArrayContext();
        jsonWriteContext.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, jsonWriteContext.getStatus());
    }

    @Test
    public void testWriteValueForObjectContext() {
        JsonWriteContext parentJsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        JsonWriteContext jsonWriteContext = parentJsonWriteContext.createChildObjectContext();
        jsonWriteContext.writeFieldName("name");
        jsonWriteContext.writeValue();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, jsonWriteContext.getStatus());
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteFieldNameWithDupDetector() {
        DupDetector dupDetector = new DupDetector();
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, dupDetector);
        jsonWriteContext.writeFieldName("name");
        jsonWriteContext.writeFieldName("name"); // should throw exception
    }

    @Test
    public void testToString() {
        JsonWriteContext jsonWriteContext = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
        jsonWriteContext.writeFieldName("name");
        jsonWriteContext.writeValue();
        Assert.assertEquals("{\"name\"}", jsonWriteContext.toString());
    }

}