import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {
    
    private ExternalTypeHandler.ExtTypedProperty extTypedProperty;
    private ExternalTypeHandler extTypeHandler;
    private ExternalTypeHandler.ExtTypedProperty[] properties;
    private String[] typeIds;
    private TokenBuffer[] tokens;
    private DeserializationContext ctxt;
    private JsonParser p;
    private Object bean;
    
    @Before
    public void setup() {
        extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(settableBeanProperty, typeDeserializer);
        properties = new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty};
        typeIds = new String[] {"typeId"};
        tokens = new TokenBuffer[] {new TokenBuffer()};
        extTypeHandler = new ExternalTypeHandler(properties, new HashMap<String, Integer>(), typeIds, tokens);
        ctxt = null;
        p = null;
        bean = null;
    }
    
    @Test
    public void testStart() {
        ExternalTypeHandler newHandler = extTypeHandler.start();
        assertNotNull(newHandler);
        assertNotSame(extTypeHandler, newHandler);
    }
    
    @Test
    public void testHandleTypePropertyValue() {
        String propName = "propName";
        boolean result = extTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testHandlePropertyValue() {
        String propName = "propName";
        boolean result = extTypeHandler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }
    
    @Test
    public void testComplete() {
        Object result = extTypeHandler.complete(p, ctxt, bean);
        assertNull(result);
    }
    
    @Test
    public void testCompleteWithPropertyValueBuffer() {
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = extTypeHandler.complete(p, ctxt, buffer, creator);
        assertNull(result);
    }
    
    @Test
    public void testDeserialize() {
        int index = 0;
        String typeId = "typeId";
        Object result = extTypeHandler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }
    
    @Test
    public void testDeserializeAndSet() {
        int index = 0;
        String typeId = "typeId";
        extTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }
}