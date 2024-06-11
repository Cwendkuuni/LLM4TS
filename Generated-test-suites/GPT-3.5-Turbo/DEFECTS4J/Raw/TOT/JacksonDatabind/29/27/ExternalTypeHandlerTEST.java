import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "type";
        Object bean = new Object();
        
        boolean result = handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "type";
        Object bean = new Object();
        
        boolean result = handler.handlePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }
    
    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        
        Object result = handler.complete(p, ctxt, bean);
        
        assertNotNull(result);
    }
    
    @Test
    public void testComplete_withPropertyValueBufferAndPropertyBasedCreator() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator();
        
        Object result = handler.complete(jp, ctxt, buffer, creator);
        
        assertNotNull(result);
    }
    
    @Test(expected = IOException.class)
    public void test_complete_throwsIOException_whenTypeIdIsNull() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        
        handler.complete(p, ctxt, bean);
    }
    
    @Test(expected = IOException.class)
    public void test_complete_throwsIOException_whenPropertyIsNull() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        builder.addExternal(property, typeDeser);
        ExternalTypeHandler handler = builder.build();
        
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        
        handler.complete(p, ctxt, bean);
    }
}