import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Test;
import static org.junit.Assert.*;

public class JsonWriteContextTest {
  
  @Test
  public void testWriteFieldName_emptyObjectContext_returnsStatusExpectValue() throws JsonProcessingException {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createRootContext();
    
    // Act
    int status = context.writeFieldName("fieldName");
    
    // Assert
    assertEquals(JsonWriteContext.STATUS_EXPECT_VALUE, status);
  }
  
  @Test
  public void testWriteFieldName_nonEmptyObjectContext_returnsStatusOKAfterComma() throws JsonProcessingException {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createChildObjectContext();
    
    // Act
    int status = context.writeFieldName("fieldName");
    
    // Assert
    assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
  }
  
  @Test
  public void testWriteValue_objectContext_returnsStatusOKAfterColon() throws JsonProcessingException {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createChildObjectContext();
    
    // Act
    int status = context.writeValue();
    
    // Assert
    assertEquals(JsonWriteContext.STATUS_OK_AFTER_COLON, status);
  }
  
  @Test
  public void testWriteValue_emptyObjectContext_returnsStatusOKAsIs() throws JsonProcessingException {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createRootContext();
    
    // Act
    int status = context.writeValue();
    
    // Assert
    assertEquals(JsonWriteContext.STATUS_OK_AS_IS, status);
  }
  
  @Test
  public void testWriteValue_nonRootObjectContext_returnsStatusOKAfterComma() throws JsonProcessingException {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createChildArrayContext();
    
    // Act
    int status = context.writeValue();
    
    // Assert
    assertEquals(JsonWriteContext.STATUS_OK_AFTER_COMMA, status);
  }
  
  @Test
  public void testToString_rootContext_returnsProperJsonPathRepresentation() {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createRootContext();
    
    // Act
    String jsonPath = context.toString();
    
    // Assert
    assertEquals("/", jsonPath);
  }
  
  @Test
  public void testToString_objectContextWithName_returnsProperJsonPathRepresentation() throws JsonProcessingException {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createRootContext();
    context.createChildObjectContext();
    context.writeFieldName("fieldName");
    
    // Act
    String jsonPath = context.toString();
    
    // Assert
    assertEquals("/{\"fieldName\"}", jsonPath);
  }
  
  @Test
  public void testToString_arrayContext_returnsProperJsonPathRepresentation() throws JsonProcessingException {
    // Arrange
    JsonWriteContext context = JsonWriteContext.createChildArrayContext();
    
    // Act
    String jsonPath = context.toString();
    
    // Assert
    assertEquals("/[0]", jsonPath);
  }
  
  @Test
  public void testCreateRootContext_withDupDetector_createsRootContextWithDupDetector() {
    // Arrange
    DupDetector dd = new DupDetector(null);
    
    // Act
    JsonWriteContext context = JsonWriteContext.createRootContext(dd);
    
    // Assert
    assertEquals(dd, context.getDupDetector());
  }
  
  @Test
  public void testWithDupDetector_setsDupDetector() {
    // Arrange
    DupDetector dd = new DupDetector(null);
    JsonWriteContext context = JsonWriteContext.createRootContext();
    
    // Act
    context.withDupDetector(dd);
    
    // Assert
    assertEquals(dd, context.getDupDetector());
  }
  
  @Test
  public void testGetCurrentValue_returnsCurrentValue() {
    // Arrange
    JsonWriteContext context = new JsonWriteContext(JsonWriteContext.TYPE_OBJECT, null, null);
    Object value = new Object();
    context.setCurrentValue(value);
    
    // Act
    Object currentValue = context.getCurrentValue();
    
    // Assert
    assertEquals(value, currentValue);
  }
  
  @Test
  public void testReset_objectContext_resetsContext() {
    // Arrange
    JsonWriteContext parentContext = JsonWriteContext.createRootContext();
    JsonWriteContext context = parentContext.createChildObjectContext();
    context.writeFieldName("fieldName");
    context.writeValue();
    
    // Act
    JsonWriteContext resetContext = context.reset(JsonWriteContext.TYPE_OBJECT);
    
    // Assert
    assertEquals(parentContext, resetContext.getParent());
    assertEquals(JsonWriteContext.TYPE_OBJECT, resetContext.getType());
    assertEquals(null, resetContext.getCurrentName());
    assertEquals(false, resetContext.getGotName());
    assertEquals(null, resetContext.getCurrentValue());
  }
}