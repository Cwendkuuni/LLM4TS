import java.io.IOException;
import java.util.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private ExtTypedProperty[] properties;
    private HashMap<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;

    @Before
    public void setUp() {
        properties = new ExtTypedProperty[2];
        properties[0] = new ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        properties[1] = new ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        nameToPropertyIndex = new HashMap<>();
        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);
        typeIds = new String[2];
        tokens = new TokenBuffer[2];
        handler = new ExternalTypeHandler(properties, nameToPropertyIndex, typeIds, tokens);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler newHandler = handler.start();
        assertNotNull(newHandler);
        assertNotSame(handler, newHandler);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        String propName = "property1";
        Object bean = new Object();
        assertFalse(handler.handleTypePropertyValue(jsonParser, context, propName, bean));
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        String propName = "property1";
        Object bean = new Object();
        assertFalse(handler.handlePropertyValue(jsonParser, context, propName, bean));
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object bean = new Object();
        assertNull(handler.complete(jsonParser, context, bean));
    }

    @Test
    public void testCompleteWithBuffer() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        PropertyValueBuffer buffer = new PropertyValueBuffer(context);
        PropertyBasedCreator creator = new PropertyBasedCreator(context, null, null);
        assertNull(handler.complete(jsonParser, context, buffer, creator));
    }

    @Test
    public void testDeserialize() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        int index = 0;
        String typeId = "type";
        assertNull(handler._deserialize(jsonParser, context, index, typeId));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = new JsonParser();
        DeserializationContext context = new DeserializationContext();
        Object bean = new Object();
        int index = 0;
        String typeId = "type";
        handler._deserializeAndSet(jsonParser, context, bean, index, typeId);
    }

    @Test
    public void testBuilder() {
        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property = new SettableBeanProperty();
        TypeDeserializer typeDeserializer = new TypeDeserializer();
        builder.addExternal(property, typeDeserializer);
        ExternalTypeHandler newHandler = builder.build();
        assertNotNull(newHandler);
    }

    @Test
    public void testExtTypedPropertyLinkTypeProperty() {
        ExtTypedProperty extProperty = new ExtTypedProperty(new SettableBeanProperty(), new TypeDeserializer());
        SettableBeanProperty typeProperty = new SettableBeanProperty();
        extProperty.linkTypeProperty(typeProperty);
        assertNotNull(extProperty.getTypeProperty());
    }
}