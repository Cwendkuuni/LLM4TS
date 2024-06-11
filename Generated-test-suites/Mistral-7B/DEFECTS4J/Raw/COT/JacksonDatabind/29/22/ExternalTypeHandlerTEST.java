import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProp";
        String typeId = "TestType";

        handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._properties.length - 1]);
        assertEquals(null, handler._tokens[handler._properties.length - 1]);
    }

    @Test
    public void testHandleTypePropertyValue_WithoutBeanOrTokens() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = null;
        String propName = "testProp";
        String typeId = "TestType";

        handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertEquals(typeId, handler._typeIds[handler._properties.length - 1]);
        assertEquals(null, handler._tokens[handler._properties.length - 1]);
    }

    @Test
    public void testHandleTypePropertyValue_WithoutTypeId() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProp";

        handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertEquals(null, handler._typeIds[handler._properties.length - 1]);
        assertEquals(null, handler._tokens[handler._properties.length - 1]);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProp";

        handler.handlePropertyValue(jp, ctxt, propName, bean);
        assertEquals(null, handler._typeIds[handler._properties.length - 1]);
        assertEquals(null, handler._tokens[handler._properties.length - 1]);
    }

    @Test
    public void testHandlePropertyValue_WithoutBeanOrTokens() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = null;
        String propName = "testProp";

        handler.handlePropertyValue(jp, ctxt, propName, bean);
        assertEquals(null, handler._typeIds[handler._properties.length - 1]);
        assertEquals(null, handler._tokens[handler._properties.length - 1]);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_WithoutValidTypeId() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProp";
        String typeId = "TestType";
        handler._typeIds[0] = "InvalidType";

        handler.handlePropertyValue(jp, ctxt, propName, bean);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_WithoutValidProperty() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "InvalidProp";

        handler.handlePropertyValue(jp, ctxt, propName, bean);
    }

    @Test
    public void testComplete() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();

        handler.complete(jp, ctxt, bean);

        for (int i = 0; i < handler._properties.length; i++) {
            assertEquals(null, handler._typeIds[i]);
            assertEquals(null, handler._tokens[i]);
        }
    }

    @Test(expected = IOException.class)
    public void testComplete_WithoutAllPropertiesSet() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        handler._typeIds[0] = null;

        handler.complete(jp, ctxt, bean);
    }

    @Test(expected = IOException.class)
    public void testComplete_WithoutAllTokensSet() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        handler._tokens[0] = null;

        handler.complete(jp, ctxt, bean);
    }

    @Test
    public void testComplete_WithNaturalType() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProp";

        handler.handlePropertyValue(jp, ctxt, propName, bean);
        handler.complete(jp, ctxt, bean);

        for (int i = 0; i < handler._properties.length; i++) {
            assertEquals(null, handler._typeIds[i]);
            assertEquals(null, handler._tokens[i]);
        }
    }

    @Test
    public void testComplete_WithDefaultType() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        String propName = "testProp";
        String defaultTypeId = "DefaultType";

        handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        handler.complete(jp, ctxt, bean);

        assertTrue(handler._typeIds[0].equals(defaultTypeId));
        assertEquals(null, handler._tokens[0]);
    }

    @Test
    public void testComplete_WithPropertyBasedCreator() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = createMockCreator();

        handler.handlePropertyValue(jp, ctxt, "prop1", bean);
        handler.handlePropertyValue(jp, ctxt, "prop2", bean);

        handler.complete(jp, ctxt, buffer, creator);

        for (int i = 0; i < handler._properties.length; i++) {
            assertEquals(null, handler._typeIds[i]);
            assertEquals(null, handler._tokens[i]);
        }

        assertEquals("prop1Value", buffer.getParameter(handler._properties[0].getProperty()));
        assertEquals("prop2Value", buffer.getParameter(handler._properties[1].getProperty()));
    }

    @Test
    public void testDeserialize() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        int index = 0;
        String typeId = "TestType";

        Object result = handler._deserialize(jp, ctxt, index, typeId);

        assertTrue(result instanceof String);
        assertEquals(typeId, result);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ExternalTypeHandler handler = new ExternalTypeHandler(createProperties(), createNameToPropertyIndex(), new String[]{}, new TokenBuffer[0]);
        JsonParser jp = createMockParser();
        DeserializationContext ctxt = createMockContext();
        Object bean = new Object();
        int index = 0;
        String typeId = "TestType";

        handler._deserializeAndSet(jp, ctxt, bean, index, typeId);

        assertEquals(typeId, handler._typeIds[index]);
        assertEquals(null, handler._tokens[index]);
    }

    private JsonParser createMockParser() {
        return createMock(JsonParser.class);
    }

    private DeserializationContext createMockContext() {
        return createMock(DeserializationContext.class);
    }

    private PropertyBasedCreator createMockCreator() {
        return createMock(PropertyBasedCreator.class);
    }

    private Object[] createProperties() {
        SettableBeanProperty prop1 = createMockProperty();
        SettableBeanProperty prop2 = createMockProperty();
        TypeDeserializer typeDeser1 = createMockTypeDeserializer();
        TypeDeserializer typeDeser2 = createMockTypeDeserializer();
        return new Object[] { new ExtTypedProperty(prop1, typeDeser1), new ExtTypedProperty(prop2, typeDeser2) };
    }

    private SettableBeanProperty createMockProperty() {
        return createMock(SettableBeanProperty.class);
    }

    private TypeDeserializer createMockTypeDeserializer() {
        return createMock(TypeDeserializer.class);
    }

    private static <T> T createMock(Class<T> clazz) {
        return mock(clazz);
    }

}