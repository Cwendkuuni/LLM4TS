import org.junit.*;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

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
        JsonParser p = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String propName = "prop";
        Object bean = new Object();
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        String propName = "prop";
        Object bean = new Object();
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object bean = new Object();
        Object result = handler.complete(p, ctxt, bean);
        assertNotNull(result);
        assertSame(bean, result);
    }
    
    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator(null, null, null);
        Object result = handler.complete(p, ctxt, buffer, creator);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserialization() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        int index = 0;
        String typeId = "typeId";
        Object result = handler._deserialize(p, ctxt, index, typeId);
        assertNotNull(result);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = new JsonFactory().createParser("");
        DeserializationContext ctxt = new DeserializationContext(null, null, null);
        Object bean = new Object();
        int index = 0;
        String typeId = "typeId";
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
    
    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty(null, null, null, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, false, null);
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        assertNotNull(handler);
    }
    
    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = new SettableBeanProperty(null, null, null, null);
        TypeDeserializer typeDeser = new TypeDeserializer(null, null, null, null, false, null);
        ExternalTypeHandler.ExtTypedProperty extProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        assertTrue(extProperty.hasTypePropertyName("type"));
        assertFalse(extProperty.hasTypePropertyName("prop"));
        assertFalse(extProperty.hasDefaultType());
        assertNull(extProperty.getDefaultTypeId());
        assertNull(extProperty.getTypeProperty());
        
        SettableBeanProperty typeProperty = new SettableBeanProperty(null, null, null, null);
        extProperty.linkTypeProperty(typeProperty);
        assertEquals(typeProperty, extProperty.getTypeProperty());
    }
}