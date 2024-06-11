import static org.junit.Assert.*;
import java.io.IOException;
import java.util.HashMap;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyValueBuffer;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
public class ExternalTypeHandlerTest {
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        boolean result = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = null;
        Object bean = null;
        boolean result = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertNull(result);
    }

    @Test
    public void testCompleteWithPropertyValueBufferAndPropertyBasedCreator() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);
        assertNull(result);
    }

    @Test
    public void testExtTypedProperty() {
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        assertTrue(extTypedProperty.hasTypePropertyName(""));
        assertFalse(extTypedProperty.hasDefaultType());
        assertNull(extTypedProperty.getDefaultTypeId());
        assertNull(extTypedProperty.getTypePropertyName());
        assertNull(extTypedProperty.getProperty());
    }
}
