import static org.junit.Assert.*;
import org.junit.Test;

public class JsonMappingExceptionTest {
  
  @Test
  public void testReferenceCreation() {
    JsonMappingException.Reference reference = new JsonMappingException.Reference("Test", "fieldName");
    
    assertEquals("Test", reference.getFrom());
    assertEquals("fieldName", reference.getFieldName());
  }
  
  @Test
  public void testReferenceDescription() {
    JsonMappingException.Reference reference = new JsonMappingException.Reference("Test", "fieldName");
    
    assertEquals("Test['fieldName']", reference.getDescription());
  }
  
  @Test
  public void testJsonMappingExceptionConstructor() {
    JsonMappingException exception = new JsonMappingException("Test message");
    
    assertEquals("Test message", exception.getMessage());
  }
  
  @Test
  public void testJsonMappingExceptionWithPath() {
    JsonMappingException originalException = new JsonMappingException("Original exception");
    JsonMappingException.Reference reference = new JsonMappingException.Reference("Test", "fieldName");
    JsonMappingException exceptionWithPath = JsonMappingException.wrapWithPath(originalException, reference);
    
    assertTrue(exceptionWithPath.getPath().contains(reference));
  }
  
  @Test
  public void testJsonMappingExceptionPrependPath() {
    JsonMappingException exception = new JsonMappingException("Test message");
    JsonMappingException.Reference reference = new JsonMappingException.Reference("Test", "fieldName");
    exception.prependPath(reference);
    
    assertTrue(exception.getPath().contains(reference));
  }
}