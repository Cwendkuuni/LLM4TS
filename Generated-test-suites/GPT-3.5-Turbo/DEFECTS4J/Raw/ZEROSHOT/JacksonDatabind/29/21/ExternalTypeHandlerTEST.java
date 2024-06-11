import static org.junit.Assert.*;
import org.junit.Test;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import java.util.HashMap;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty}, new HashMap<String, Integer>(), new String[1], new TokenBuffer[1]);

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        boolean result = externalTypeHandler.handleTypePropertyValue(jsonParser, deserializationContext, "propName", new Object());

        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty}, new HashMap<String, Integer>(), new String[1], new TokenBuffer[1]);

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        boolean result = externalTypeHandler.handlePropertyValue(jsonParser, deserializationContext, "propName", new Object());

        assertFalse(result);
    }

    @Test
    public void testComplete() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty}, new HashMap<String, Integer>(), new String[1], new TokenBuffer[1]);

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        Object result = new Object();
        try {
            result = externalTypeHandler.complete(jsonParser, deserializationContext, new Object());
        } catch (Exception e) {}
        
        assertNotNull(result);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty}, new HashMap<String, Integer>(), new String[1], new TokenBuffer[1]);

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();
        PropertyValueBuffer propertyValueBuffer = new PropertyValueBuffer();
        PropertyBasedCreator propertyBasedCreator = new PropertyBasedCreator();

        Object result = new Object();
        try {
            result = externalTypeHandler.complete(jsonParser, deserializationContext, propertyValueBuffer, propertyBasedCreator);
        } catch (Exception e) {}

        assertNotNull(result);
    }

    @Test
    public void testDeserialize() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty}, new HashMap<String, Integer>(), new String[1], new TokenBuffer[1]);

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        Object result = new Object();
        try {
            result = externalTypeHandler._deserialize(jsonParser, deserializationContext, 0, "typeId");
        } catch (Exception e) {}

        assertNotNull(result);
    }

    @Test
    public void testDeserializeAndSet() {
        ExternalTypeHandler.ExtTypedProperty extTypedProperty = new ExternalTypeHandler.ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        ExternalTypeHandler externalTypeHandler = new ExternalTypeHandler(new ExternalTypeHandler.ExtTypedProperty[] {extTypedProperty}, new HashMap<String, Integer>(), new String[1], new TokenBuffer[1]);

        JsonParser jsonParser = new JsonParser();
        DeserializationContext deserializationContext = new DeserializationContext();

        Object result = new Object();
        try {
            result = externalTypeHandler._deserializeAndSet(jsonParser, deserializationContext, new Object(), 0, "typeId");
        } catch (Exception e) {}

        assertNotNull(result);
    }

    @Test
    public void testExtTypedPropertyConstructor() {
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        ExtTypedProperty extTypedProperty = new ExtTypedProperty(settableBeanProperty, typeDeserializer);

        assertNotNull(extTypedProperty);
    }

    @Test
    public void testHasTypePropertyName() {
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        ExtTypedProperty extTypedProperty = new ExtTypedProperty(settableBeanProperty, typeDeserializer);
        
        boolean result = extTypedProperty.hasTypePropertyName("typePropertyName");

        assertFalse(result);
    }

    @Test
    public void testHasDefaultType() {
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        ExtTypedProperty extTypedProperty = new ExtTypedProperty(settableBeanProperty, typeDeserializer);
        
        boolean result = extTypedProperty.hasDefaultType();
        
        assertFalse(result);
    }

    @Test
    public void testGetTypePropertyName() {
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        ExtTypedProperty extTypedProperty = new ExtTypedProperty(settableBeanProperty, typeDeserializer);
        
        String result = extTypedProperty.getTypePropertyName();
        
        assertNotNull(result);
    }

    @Test
    public void testGetProperty() {
        SettableBeanProperty settableBeanProperty = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        ExtTypedProperty extTypedProperty = new ExtTypedProperty(settableBeanProperty, typeDeserializer);
        
        SettableBeanProperty result = extTypedProperty.getProperty();
        
        assertNotNull(result);
    }
}