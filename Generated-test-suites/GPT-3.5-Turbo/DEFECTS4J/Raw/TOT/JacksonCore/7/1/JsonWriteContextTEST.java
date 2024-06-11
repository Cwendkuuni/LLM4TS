import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {

    private JsonWriteContext jsonWriteContext;

    @Before
    public void setUp() {
        jsonWriteContext = JsonWriteContext.createRootContext();
    }
    
    @Test
    public void testCreateRootContext() {
        assertNotNull(jsonWriteContext);
        assertEquals(JsonWriteContext.TYPE_ROOT, jsonWriteContext.getType());
        assertNull(jsonWriteContext.getParent());
        assertNull(jsonWriteContext.getCurrentName());
    }
    
    @Test
    public void testCreateChildArrayContext() {
        JsonWriteContext childArrayContext = jsonWriteContext.createChildArrayContext();
        
        assertNotNull(childArrayContext);
        assertEquals(JsonWriteContext.TYPE_ARRAY, childArrayContext.getType());
        assertEquals(jsonWriteContext, childArrayContext.getParent());
        assertNull(childArrayContext.getCurrentName());
    }
    
    @Test
    public void testCreateChildObjectContext() {
        JsonWriteContext childObjectContext = jsonWriteContext.createChildObjectContext();
        
        assertNotNull(childObjectContext);
        assertEquals(JsonWriteContext.TYPE_OBJECT, childObjectContext.getType());
        assertEquals(jsonWriteContext, childObjectContext.getParent());
        assertNull(childObjectContext.getCurrentName());
    }
    
    @Test
    public void testWriteFieldName() throws JsonProcessingException {
        String fieldName = "testField";
        int status = jsonWriteContext.writeFieldName(fieldName);
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
        assertEquals(fieldName, jsonWriteContext.getCurrentName());
    }
    
    @Test
    public void testWriteFieldNameWithDuplicate() {
        String fieldName = "duplicateField";
        jsonWriteContext = jsonWriteContext.withDupDetector(new DupDetector());
        
        try {
            jsonWriteContext.writeFieldName(fieldName);
            jsonWriteContext.writeFieldName(fieldName);
            fail("JsonGenerationException should have been thrown.");
        } catch (JsonGenerationException e) {
            assertEquals("Duplicate field '" + fieldName + "'", e.getMessage());
        }
    }
    
    @Test
    public void testWriteValueForObjectContext() {
        jsonWriteContext = jsonWriteContext.createChildObjectContext();
        
        int status = jsonWriteContext.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
    }
    
    @Test
    public void testWriteValueForArrayContext() {
        jsonWriteContext = jsonWriteContext.createChildArrayContext();
        
        int status = jsonWriteContext.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
    }
    
    @Test
    public void testWriteValueForRootContext() {
        int status = jsonWriteContext.writeValue();
        
        assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
    }
    
    @Test
    public void testToStringForObjectContext() {
        jsonWriteContext = jsonWriteContext.createChildObjectContext();
        
        String expectedString = "{\"?\"}";
        String actualString = jsonWriteContext.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testToStringForArrayContext() {
        jsonWriteContext = jsonWriteContext.createChildArrayContext();
        
        String expectedString = "[0]";
        String actualString = jsonWriteContext.toString();
        
        assertEquals(expectedString, actualString);
    }
    
    @Test
    public void testToStringForRootContext() {
        String expectedString = "/";
        String actualString = jsonWriteContext.toString();
        
        assertEquals(expectedString, actualString);
    }
}