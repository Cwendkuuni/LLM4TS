import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_COMMENTS;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES;
import static com.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_VALUE_FOR_FIELD_NAME;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private List<ExtTypedProperty> properties;
    private Map<String, Integer> nameToPropertyIndex;
    private String[] typeIds;
    private TokenBuffer[] tokens;

    @Before
    public void setUp() {
        properties = new ArrayList<>();
        nameToPropertyIndex = new HashMap<>();

        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);

        TypeDeserializer typeDeserializer1 = mock(TypeDeserializer.class);
        TypeDeserializer typeDeserializer2 = mock(TypeDeserializer.class);

        properties.add(new ExtTypedProperty(property1, typeDeserializer1));
        properties.add(new ExtTypedProperty(property2, typeDeserializer2));

        nameToPropertyIndex.put("property1", 0);
        nameToPropertyIndex.put("property2", 1);

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(handler);
    }

    @Test
    public void testStart() {
        ExternalTypeHandler startedHandler = handler.start();
        Assert.assertNotSame(handler, startedHandler);
    }

    @Test
    public void testHandleTypePropertyValue_ValidTypeId() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        when(typeDeserializer1.getPropertyName()).thenReturn("typeId1");

        handler.handleTypePropertyValue(parser, context, "property1", bean);

        ensureTypeIdSet(parser, 0, "typeId1");
    }

    @Test
    public void testHandleTypePropertyValue_InvalidTypeId() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handleTypePropertyValue(parser, context, "property1", bean);

        assertTokensNotModified(parser);
    }

    @Test
    public void testHandlePropertyValue_ValidTypeId() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        when(typeDeserializer1.getPropertyName()).thenReturn("typeId1");

        handler.handlePropertyValue(parser, context, "property1", bean);

        ensureTypeIdSet(parser, 0, "typeId1");
    }

    @Test
    public void testHandlePropertyValue_InvalidTypeId() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "property1", bean);

        assertTokensNotModified(parser);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_MissingProperty() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "missingProperty", bean);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValue_MissingTypeId() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.handlePropertyValue(parser, context, "property1", bean);

        String typeId = _tokens[0].getText();
        parser.nextToken();

        throwExceptionIfNotNull(parser, typeId);
    }

    @Test
    public void testComplete() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        Object[] expectedValues = {new Object(), new Object()};

        for (int i = 0; i < _properties.size(); ++i) {
            String typeId = _typeIds[i];
            if (typeId == null) {
                continue;
            }

            when(_typeDeserializer1.deserialize(parser, context)).thenReturn(expectedValues[i]);
        }

        handler.complete(parser, context, bean);

        Object result = bean;
        for (int i = 0; i < _properties.size(); ++i) {
            SettableBeanProperty property = _properties.get(i).getProperty();
            Object expectedValue = expectedValues[i];

            Assert.assertEquals(expectedValue, property.get(result));
        }
    }

    @Test(expected = IOException.class)
    public void testComplete_MissingTypeId() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.complete(parser, context, bean);

        SettableBeanProperty prop = _properties.get(0).getProperty();

        throwExceptionIfNotNull(parser, _typeIds[0]);
    }

    @Test(expected = IOException.class)
    public void testComplete_MissingProperty() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        Object bean = new Object();

        handler.complete(parser, context, bean);

        SettableBeanProperty prop = _properties.get(0).getProperty();

        throwExceptionIfNotNull(parser, prop.getName());
    }

    @Test
    public void testComplete_WithCreator() throws IOException {
        JsonParser parser = createParser();
        DeserializationContext context = mock(DeserializationContext.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);

        when(creator.findCreatorProperty("property1")).thenReturn(mock(SettableBeanProperty.class));
        when(creator.findCreatorProperty("property2")).thenReturn(mock(SettableBeanProperty.class));

        handler.complete(parser, context, buffer, creator);

        Object bean = creator.build(context, buffer);

        Object[] expectedValues = {new Object(), new Object()};

        for (int i = 0; i < _properties.size(); ++i) {
            SettableBeanProperty property = _properties.get(i).getProperty();
            Object expectedValue = expectedValues[i];

            if (creator.findCreatorProperty(property.getName()) != null) {
                Assert.assertEquals(expectedValue, property.get(bean));
            } else {
                Assert.assertEquals(expectedValue, property.get(bean));
            }
        }
    }

    private JsonParser createParser() {
        ObjectCodec codec = new ObjectMapper().getFactory().getEncoder().getDefaultCodec();
        JsonParser parser = codec.getParserForObject("{}");
        parser.setFeatures(ALLOW_UNQUOTED_FIELD_NAMES, ALLOW_UNQUOTED_VALUE_FOR_FIELD_NAME, ALLOW_COMMENTS);
        return parser;
    }

    private void ensureTypeIdSet(JsonParser parser, int index, String typeId) throws IOException {
        Assert.assertEquals(typeId, _typeIds[index]);
        Assert.assertNull(_tokens[index]);
    }

    private void assertTokensNotModified(JsonParser parser) throws IOException {
        for (int i = 0; i < _properties.size(); ++i) {
            String typeId = _typeIds[i];
            TokenBuffer tokens = _tokens[i];

            if (typeId == null && tokens == null) {
                continue;
            }

            JsonToken token = parser.getCurrentToken();
            Assert.assertNotNull(token);
            Assert.assertEquals(token.getTokenTypeId(), tokens.getCurrentToken().getTokenTypeId());
        }
    }

    private void throwExceptionIfNotNull(JsonParser parser, String typeId) throws IOException {
        if (typeId != null) {
            throw new IOException("Type id should not be set: " + typeId);
        }
    }
}