import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {
  
  @Test
  public void testStart() {
    ExternalTypeHandler handler = new ExternalTypeHandler.Builder().build();
    assertNotNull(handler.start());
  }
  
  @Test
  public void testHandleTypePropertyValue() {
    ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
    SettableBeanProperty property = new SettableBeanProperty("prop", null, null, null);
    TypeDeserializer typeDeser = new TypeDeserializer("typeProp", null, null, null);
    builder.addExternal(property, typeDeser);
    ExternalTypeHandler handler = builder.build();
    assertFalse(handler.handleTypePropertyValue(null, null, "prop", null));
    assertFalse(handler.handleTypePropertyValue(null, null, "unknown", null));
  }
  
  @Test
  public void testHandlePropertyValue() {
    ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
    SettableBeanProperty property = new SettableBeanProperty("prop", null, null, null);
    TypeDeserializer typeDeser = new TypeDeserializer("typeProp", null, null, null);
    builder.addExternal(property, typeDeser);
    ExternalTypeHandler handler = builder.build();
    assertFalse(handler.handlePropertyValue(null, null, "prop", null));
    assertFalse(handler.handlePropertyValue(null, null, "unknown", null));
  }
  
  @Test
  public void testComplete() {
    ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
    SettableBeanProperty property = new SettableBeanProperty("prop", null, null, null);
    TypeDeserializer typeDeser = new TypeDeserializer("typeProp", null, null, null);
    builder.addExternal(property, typeDeser);
    ExternalTypeHandler handler = builder.build();
    assertNull(handler.complete(null, null, null));
  }
  
  @Test
  public void testCompleteWithBuffer() {
    ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
    SettableBeanProperty property = new SettableBeanProperty("prop", null, null, null);
    TypeDeserializer typeDeser = new TypeDeserializer("typeProp", null, null, null);
    builder.addExternal(property, typeDeser);
    ExternalTypeHandler handler = builder.build();
    assertNull(handler.complete(null, null, null, null));
  }

}