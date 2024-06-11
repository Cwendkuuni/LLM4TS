import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private DeserializationContext ctxt;
    private JsonParser jp;
    private TokenBuffer tokens;
    
    @Before
    public void setUp() throws IOException {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        
        SettableBeanProperty property = null; // Create your own settable bean property
        TypeDeserializer typeDeser = null; // Create your own type deserializer
        builder.addExternal(property, typeDeser);
        
        ExtTypedProperty[] properties = builder.build()._properties;
        HashMap<String, Integer> nameToPropertyIndex = builder.build()._nameToPropertyIndex;
        String[] typeIds = new String[properties.length];
        TokenBuffer[] tokens = new TokenBuffer[properties.length];
        
        handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
        
        ctxt = null; // Create your own deserialization context
        jp = null; // Create your own json parser
        tokens = new TokenBuffer(new ObjectMapper(), false);
    }
    
    @Test
    public void testStart() {
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
    }
    
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String propName = "propName"; // Create your own property name
        Object bean = null; // Create your own object
        
        boolean result = handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertTrue(result);
    }
    
    @Test
    public void testHandlePropertyValue() throws IOException {
        String propName = "propName"; // Create your own property name
        Object bean = null; // Create your own object
        
        boolean result = handler.handlePropertyValue(jp, ctxt, propName, bean);
        assertTrue(result);
    }
    
    @Test
    public void testComplete() throws IOException {
        Object bean = null; // Create your own object
        
        Object result = handler.complete(jp, ctxt, bean);
        assertNotNull(result);
    }
    
    @Test
    public void testCompleteWithCreator() throws IOException {
        PropertyValueBuffer buffer = null; // Create your own property value buffer
        PropertyBasedCreator creator = null; // Create your own property based creator
        
        Object result = handler.complete(jp, ctxt, buffer, creator);
        assertNotNull(result);
    }
}