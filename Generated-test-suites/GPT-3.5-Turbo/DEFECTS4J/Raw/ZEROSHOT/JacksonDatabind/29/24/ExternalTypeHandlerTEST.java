import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;

public class ExternalTypeHandlerTest {
    
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "prop";
        Object bean = new Object();
        
        boolean actual = handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(actual);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "prop";
        Object bean = new Object();
        
        boolean actual = handler.handlePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(actual);
    }
    
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        
        Object actual = handler.complete(p, ctxt, bean);
        
        assertEquals(bean, actual);
    }
    
    @Test
    public void testCompleteWithBuffer() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        
        Object actual = handler.complete(jp, ctxt, buffer, creator);
        
        assertNull(actual);
    }
    
    @Test
    public void testDeserialize() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "test";
        
        Object actual = handler._deserialize(p, ctxt, index, typeId);
        
        assertNull(actual);
    }
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        int index = 0;
        String typeId = "test";
        
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
    
    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        
        builder.addExternal(property, typeDeser);
        
        ExternalTypeHandler handler = builder.build();
        
        assertNotNull(handler);
    }
    
    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        
        boolean actual = extTypedProperty.hasTypePropertyName("prop");
        
        assertFalse(actual);
        
        boolean defaultType = extTypedProperty.hasDefaultType();
        
        assertFalse(defaultType);
        
        String typeId = extTypedProperty.getDefaultTypeId();
        
        assertNull(typeId);
        
        String propertyName = extTypedProperty.getTypePropertyName();
        
        assertNull(propertyName);
        
        SettableBeanProperty actualProperty = extTypedProperty.getProperty();
        
        assertNull(actualProperty);
    }
}