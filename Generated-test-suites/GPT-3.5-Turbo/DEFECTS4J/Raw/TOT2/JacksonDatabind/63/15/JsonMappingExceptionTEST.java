import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {
    
    @Test
    public void testJsonMappingExceptionConstructor() {
        JsonMappingException exception = new JsonMappingException("Test Exception");
        assertNotNull(exception);
        assertEquals("Test Exception", exception.getMessage());
    }
    
    @Test
    public void testAddReferenceWithoutFieldName() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference(this, 0);
        assertEquals(this, reference.getFrom());
        assertEquals(0, reference.getIndex());
    }
    
    @Test
    public void testAddReferenceWithFieldName() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference(this, "fieldName");
        assertEquals(this, reference.getFrom());
        assertEquals("fieldName", reference.getFieldName());
    }
    
    @Test
    public void testGetPathForException() {
        JsonMappingException.Reference reference1 = new JsonMappingException.Reference(this, 1);
        JsonMappingException.Reference reference2 = new JsonMappingException.Reference(this, "fieldName");
        
        JsonMappingException exception = new JsonMappingException("Test Exception");
        exception.prependPath(reference1);
        exception.prependPath(reference2);
        
        assertEquals(2, exception.getPath().size());
        assertEquals(reference2, exception.getPath().get(0));
        assertEquals(reference1, exception.getPath().get(1));
    }
    
    @Test
    public void testGetPathReferenceForException() {
        JsonMappingException.Reference reference = new JsonMappingException.Reference(this, "fieldName");
        
        JsonMappingException exception = new JsonMappingException("Test Exception", reference);
        
        StringBuilder sb = new StringBuilder();
        assertEquals(sb.append("com.fasterxml.jackson.databind.JsonMappingExceptionTest[fieldName]").toString(), exception.getPathReference());
    }
}