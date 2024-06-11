import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.core.JsonParser;
import java.io.IOException;
import java.util.HashMap;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "test";
        Object bean = new Object();
        
        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        String propName = "test";
        Object bean = new Object();
        
        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, propName, bean);
        
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DeserializationContext ctxt = null;
        Object bean = new Object();
        
        Object result = externalTypeHandler.complete(p, ctxt, bean);
        
        assertNotNull(result);
    }

    @Test
    public void test_complete_with_property_buffer_creator() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser jp = null;
        DeserializationContext ctxt = null;
        PropertyValueBuffer buffer = null;
        PropertyBasedCreator creator = null;
        
        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        
        assertNotNull(result);
    }

    @Test
    public void test_deserialize_and_set() throws IOException {
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(null, null, null, null);
        JsonParser p = null;
        DesserializationContext ctxt = null;
        Object bean = new Object();
        int index = 0;
        String typeId = "testId";
        
        externalTypeHandler.handlePropertyValue(p, ctxt, bean);
        
        // validate the result somehow
    }

    @Test
    public void test_build_external_type_handler() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeser = new TypeDeserializer();
        
        builder.addExternal(property, typeDeser);
        
        ExternalTypeHandler externalTypeHandler = builder.build();
        
        assertNotNull(externalTypeHandler);
    }

}