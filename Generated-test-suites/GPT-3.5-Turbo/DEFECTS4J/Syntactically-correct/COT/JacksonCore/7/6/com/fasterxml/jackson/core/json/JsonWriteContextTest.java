
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;

public class JsonWriteContextTest {

    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        jsonWriteContext = JsonWriteContext.createRootContext();
    }

    @Test
    public void testWriteFieldName() {
        try {
            int status = jsonWriteContext.writeFieldName("name");
            Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);

            status = jsonWriteContext.writeFieldName("age");
            Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);

        } catch (JsonProcessingException e) {
            Assert.fail("Unexpected exception occurred!");
        }
    }

    @Test
    public void testWriteValue() {
        try {
            int status = jsonWriteContext.writeValue();
            Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);

            jsonWriteContext.createChildArrayContext();
            status = jsonWriteContext.writeValue();
            Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);

            // Test array context
            jsonWriteContext.createChildObjectContext();
            status = jsonWriteContext.writeValue();
            Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);

        } catch (JsonProcessingException e) {
            Assert.fail("Unexpected exception occurred!");
        }
    }

    @Test
    public void testCreateChildArrayContext() {
        Assert.assertNull(jsonWriteContext.getParent());

        jsonWriteContext.createChildArrayContext();

        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, jsonWriteContext._child._type);
        Assert.assertEquals(jsonWriteContext, jsonWriteContext._child.getParent());
    }

    @Test
    public void testCreateChildObjectContext() {
        Assert.assertNull(jsonWriteContext.getParent());

        jsonWriteContext.createChildObjectContext();

        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, jsonWriteContext._child._type);
        Assert.assertEquals(jsonWriteContext, jsonWriteContext._child.getParent());
    }

    @Test
    public void testGetCurrentValue() {
        Object object = new Object();
        jsonWriteContext.setCurrentValue(object);

        Assert.assertEquals(object, jsonWriteContext.getCurrentValue());
    }
}
