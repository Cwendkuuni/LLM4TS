import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonProcessingException;

public class JsonWriteContextTest {
    
    @Mock
    private DupDetector dupDetectorMock;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }
    
    @Test
    public void testCreateRootContext() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertNull(context.getParent());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertNull(context.getDupDetector());
    }
    
    @Test
    public void testCreateRootContextWithDupDetector() {
        JsonWriteContext context = JsonWriteContext.createRootContext(dupDetectorMock);
        Assert.assertNull(context.getParent());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.TYPE_ROOT, context.getType());
        Assert.assertEquals(dupDetectorMock, context.getDupDetector());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildArrayContext();
        Assert.assertEquals(parent, context.getParent());
        Assert.assertEquals(JsonWriteContext.TYPE_ARRAY, context.getType());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext parent = JsonWriteContext.createRootContext();
        JsonWriteContext context = parent.createChildObjectContext();
        Assert.assertEquals(parent, context.getParent());
        Assert.assertEquals(JsonWriteContext.TYPE_OBJECT, context.getType());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildObjectContext();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeFieldName("name"));
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }
    
    @Test
    public void testWriteValueForObject() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildObjectContext();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, context.writeValue());
    }
    
    @Test
    public void testWriteValueForArray() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildArrayContext();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, context.writeValue());
    }
    
    @Test
    public void testWriteValueForRoot() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AS_IS, context.writeValue());
        Assert.assertEquals(JsonWriteContext.STATUS_OK_AFTER_SPACE, context.writeValue());
    }
    
    @Test
    public void testGetCurrentValue() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertNull(context.getCurrentValue());
        Object value = new Object();
        context.setCurrentValue(value);
        Assert.assertEquals(value, context.getCurrentValue());
    }
    
    @Test
    public void testToStringForObject() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildObjectContext();
        context.writeFieldName("name");
        Assert.assertEquals("{\"name\"}", context.toString());
    }
    
    @Test
    public void testToStringForArray() throws JsonProcessingException {
        JsonWriteContext context = JsonWriteContext.createRootContext().createChildArrayContext();
        context.writeValue();
        Assert.assertEquals("[0]", context.toString());
    }
    
    @Test
    public void testToStringForRoot() {
        JsonWriteContext context = JsonWriteContext.createRootContext();
        Assert.assertEquals("/", context.toString());
    }
}