import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;

public class ExternalTypeHandlerTest {
  
  private ExternalTypeHandler externalTypeHandler;
  
  @Before
  public void setUp() {
    externalTypeHandler = new ExternalTypeHandler.Builder().build();
  }
  
  @Test
  public void testStart() {
    ExternalTypeHandler newHandler = externalTypeHandler.start();
    assertNotNull(newHandler);
  }
  
  @Test
  public void testHandleTypePropertyValue() throws JsonParseException {
    // Test case for when property value is an external type id
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    String propName = "prop";
    Object bean = new Object();
    
    // Setting up the necessary properties for the test case
    ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(
        new SettableBeanProperty(), new TypeDeserializer());
    externalTypeHandler.addProperty(property, 0);
    externalTypeHandler.addTokenBufferForIndex(new TokenBuffer(), 0);
    externalTypeHandler.addTypeForIndex("type", 0);
    
    boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
    assertTrue(result);
  }
  
  @Test
  public void testHandlePropertyValue() throws JsonParseException {
    // Test case for when property value is not an external type id
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    String propName = "prop";
    Object bean = new Object();
    
    // Setting up the necessary properties for the test case
    ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(
        new SettableBeanProperty(), new TypeDeserializer());
    externalTypeHandler.addProperty(property, 0);
    externalTypeHandler.addTokenBufferForIndex(new TokenBuffer(), 0);
    externalTypeHandler.addTypeForIndex("type", 0);
    
    boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
    assertTrue(result);
  }
  
  @Test
  public void testComplete() throws JsonParseException {
    JsonParser jp = new JsonParser();
    DeserializationContext ctxt = new DeserializationContext();
    Object bean = new Object();
    
    // Setting up the necessary properties for the test case
    ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(
        new SettableBeanProperty(), new TypeDeserializer());
    externalTypeHandler.addProperty(property, 0);
    externalTypeHandler.addTokenBufferForIndex(new TokenBuffer(), 0);
    externalTypeHandler.addTypeForIndex("type", 0);
    
    Object result = externalTypeHandler.complete(jp, ctxt, bean);
    assertNotNull(result);
    assertEquals(bean, result);
  }

}