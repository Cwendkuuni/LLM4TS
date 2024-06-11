import static org.junit.Assert.*;

import java.io.IOException;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler externalTypeHandler;
    private ExtTypedProperty[] properties;
    private HashMap<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;

    @Before
    public void setUp() {
        properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        properties[1] = new ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());

        nameToPropertyIndex = new HashMap<String, Integer>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);

        typeIds = new String[2];
        tokens = new TokenBuffer[2];

        externalTypeHandler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        boolean result = externalTypeHandler.handleTypePropertyValue(jp, ctxt, "property1", new Object());
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        boolean result = externalTypeHandler.handlePropertyValue(jp, ctxt, "property1", new Object());
        assertFalse(result);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        Object result = externalTypeHandler.complete(p, ctxt, new Object());
        assertNull(result);
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        JsonParser jp = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new PropertyBasedCreator();

        Object result = externalTypeHandler.complete(jp, ctxt, buffer, creator);
        assertNull(result);
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();

        Object result = externalTypeHandler._deserialize(p, ctxt, 0, "typeId");
        assertNull(result);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = new JsonParser();
        DeserializationContext ctxt = new DeserializationContext();
        Object bean = new Object();

        externalTypeHandler._deserializeAndSet(p, ctxt, bean, 0, "typeId");
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        builder.addExternal(new SettableBeanProperty(), new TypeDeserializer());

        ExternalTypeHandler handler = builder.build();
        assertNotNull(handler);
    }

    private class SettableBeanProperty {}

    private class TypeDeserializer {
        public String getPropertyName() {
            return "typeProperty";
        }

        public Class<?> getDefaultImpl() {
            return null;
        }

        public Object getTypeIdResolver() {
            return null;
        }
    }

    private class ExtTypedProperty {
        private SettableBeanProperty _property;
        private TypeDeserializer _typeDeserializer;
        private String _typePropertyName;

        public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeser) {
            _property = property;
            _typeDeserializer = typeDeser;
            _typePropertyName = typeDeser.getPropertyName();
        }

        public boolean hasTypePropertyName(String n) {
            return n.equals(_typePropertyName);
        }

        public boolean hasDefaultType() {
            return _typeDeserializer.getDefaultImpl() != null;
        }

        public String getDefaultTypeId() {
            Class<?> defaultType = _typeDeserializer.getDefaultImpl();
            if (defaultType == null) {
                return null;
            }
            return _typeDeserializer.getTypeIdResolver().idFromValueAndType(null, defaultType);
        }

        public String getTypePropertyName() {
            return _typePropertyName;
        }

        public SettableBeanProperty getProperty() {
            return _property;
        }
    }
}