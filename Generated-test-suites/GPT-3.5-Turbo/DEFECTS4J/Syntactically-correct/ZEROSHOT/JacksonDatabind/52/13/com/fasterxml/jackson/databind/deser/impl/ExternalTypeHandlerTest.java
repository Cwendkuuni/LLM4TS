import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {
    private ExternalTypeHandler externalTypeHandler;

    @Before
    public void setUp() {
        ExternalTypeHandler.ExtTypedProperty property = new ExternalTypeHandler.ExtTypedProperty(null, null);
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(property, null);
        externalTypeHandler = builder.build();
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = new Object();
        boolean result = externalTypeHandler.handleTypePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        String propName = "propName";
        Object bean = new Object();
        boolean result = externalTypeHandler.handlePropertyValue(p, ctxt, propName, bean);
        assertFalse(result);
    }

    @Test
    public void testComplete() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        assertEquals(bean, result);
    }

    @Test
    public void testCompletePropertyBasedCreator() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        ExternalTypeHandler.PropertyValueBuffer buffer = null;
        ExternalTypeHandler.PropertyBasedCreator creator = null;
        Object result = externalTypeHandler.complete(p, ctxt, buffer, creator);
        assertNotNull(result);
    }

    @Test
    public void testDeserialize() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        int index = 1;
        String typeId = "typeId";
        Object result = externalTypeHandler._deserialize(p, ctxt, index, typeId);
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        int index = 1;
        String typeId = "typeId";
        externalTypeHandler._deserializeAndSet(p, ctxt, bean, index, typeId);
    }

    @Test
    public void testBuilder() throws Exception {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        assertNotNull(builder);
    }
}