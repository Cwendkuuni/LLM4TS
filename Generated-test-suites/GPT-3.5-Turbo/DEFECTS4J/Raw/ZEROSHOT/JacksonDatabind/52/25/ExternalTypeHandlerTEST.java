import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testStart() {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "";
        Object bean = null;
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @SuppressWarnings("resource")
    @Test
    public void testComplete() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        Object result = handler.complete(p, ctxt, bean);
        assertNull(result);
    }

    @SuppressWarnings("resource")
    @Test
    public void testComplete2() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        Object result = handler.complete(p, ctxt, buffer, creator);
        assertNull(result);
    }

    @SuppressWarnings("resource")
    @Test
    public void test_deserialize() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 0;
        String typeId = "";
        Object result = handler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }

    @SuppressWarnings("resource")
    @Test
    public void test_deserializeAndSet() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = null;
        int index = 0;
        String typeId = "";
        handler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testBuilderAddExternal() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = null;
        TypeDeserializer typeDeser = null;
        builder.addExternal(property, typeDeser);
    }

    @Test
    public void testExtTypedProperty() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(null, null);
        extTypedProperty.linkTypeProperty(null);
        assertFalse(extTypedProperty.hasTypePropertyName(null));
        assertFalse(extTypedProperty.hasDefaultType());
        assertNull(extTypedProperty.getDefaultTypeId());
        assertNull(extTypedProperty.getTypePropertyName());
        assertNull(extTypedProperty.getProperty());
        assertNull(extTypedProperty.getTypeProperty());
    }
}