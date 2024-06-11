import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.util.HashMap;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler();
        ExternalTypeHandler result = handler.start();
        assertNotNull(result);
    }

    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler handler = new ExternalTypeHandler();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler handler = new ExternalTypeHandler();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() {
        ExternalTypeHandler handler = new ExternalTypeHandler();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = handler.complete(p, ctxt, bean);
        assertNull(result);
    }

    @Test
    public void testComplete2() {
        ExternalTypeHandler handler = new ExternalTypeHandler();
        JsonParser p = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = handler.complete(p, ctxt, buffer, creator);
        assertNull(result);
    }

    @Test
    public void testBuilderAddExternal() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
    }

    @Test
    public void testExtTypedPropertyLinkTypeProperty() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(null, null);
        SettableBeanProperty p = null;
        extTypedProperty.linkTypeProperty(p);
    }
}