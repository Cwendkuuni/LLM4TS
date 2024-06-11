import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {
    
    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
        assertNotSame(handler, newHandler);
    }
    
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = handler.complete(p, ctxt, bean);
        assertNull(result);
    }
    
    @Test
    public void testCompleteWithBufferAndCreator() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = handler.complete(p, ctxt, buffer, creator);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "";
        Object result = handler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = "";
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
    
    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        assertNotNull(builder);
    }
    
    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        ExternalTypeHandler.ExtTypedProperty extProp = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        assertNotNull(extProp);
        assertFalse(extProp.hasTypePropertyName(""));
        assertFalse(extProp.hasDefaultType());
        assertNull(extProp.getDefaultTypeId());
        assertNull(extProp.getTypePropertyName());
        assertNull(extProp.getProperty());
        assertNull(extProp.getTypeProperty());
    }
}