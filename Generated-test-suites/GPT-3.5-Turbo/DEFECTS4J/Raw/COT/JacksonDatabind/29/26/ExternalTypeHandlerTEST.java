
import org.junit.Test;
import org.junit.Assert;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        Assert.assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        Assert.assertFalse(result);
    }

    @Test
    public void testComplete() throws Exception {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        Assert.assertNull(result);
    }

    @Test
    public void testCompleteWithCreator() throws Exception {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(property, typeDeser);
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler.Builder().build();
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        Assert.assertNull(result);
    }
}
