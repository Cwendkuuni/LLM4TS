import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private JsonParser parser;
    private DeserializationContext context;
    private Object bean;

    @Before
    public void setUp() throws IOException {
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);
        bean = new Object();

        ExtTypedProperty property1 = new ExtTypedProperty(new SettableBeanProperty("prop1", String.class), mock(TypeDeserializer.class));
        ExtTypedProperty property2 = new ExtTypedProperty(new SettableBeanProperty("prop2", Integer.class), mock(TypeDeserializer.class));

        List<ExtTypedProperty> properties = new ArrayList<>();
        properties.add(property1);
        properties.add(property2);

        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<String, Integer>() {{
                    put("prop1", 0);
                    put("prop2", 1);
                }}, null, null);
    }

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        String typeId1 = "StringTypeId";
        String propName1 = "prop1";

        when(parser.getCurrentName()).thenReturn(propName1);
        when(parser.getText()).thenReturn(typeId1);

        handler.handleTypePropertyValue(parser, context, propName1, bean);

        assertEquals(typeId1, handler._typeIds[0]);
        assertEquals(null, handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValueWhenTypePropertyNameMatches() throws IOException {
        String typeId1 = "StringTypeId";
        String propName1 = "prop1";

        when(parser.getCurrentName()).thenReturn(propName1);
        when(parser.getText()).thenReturn(typeId1);

        handler.handlePropertyValue(parser, context, propName1, bean);

        assertEquals(null, handler._typeIds[0]);
        assertNotNull(handler._tokens[0]);
    }

    @Test(expected = IOException.class)
    public void testHandlePropertyValueWhenTypePropertyNameDoesNotMatch() throws IOException {
        String typeId1 = "StringTypeId";
        String propName1 = "prop2";

        when(parser.getCurrentName()).thenReturn(propName1);
        when(parser.getText()).thenReturn(typeId1);

        handler.handlePropertyValue(parser, context, propName1, bean);

        fail("Expected IOException");
    }

    @Test
    public void testHandlePropertyValueWhenTypeIdIsMissing() throws IOException {
        String propName1 = "prop1";

        when(parser.getCurrentName()).thenReturn(propName1);

        handler.handlePropertyValue(parser, context, propName1, bean);

        assertNull(handler._typeIds[0]);
        assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValueWhenValueIsScaledValue() throws IOException {
        String typeId1 = "StringTypeId";
        String propName1 = "prop1";
        String value1 = "value1";

        when(parser.getCurrentName()).thenReturn(propName1);
        when(parser.getText()).thenReturn(typeId1);

        JsonParser valueParser = mock(JsonParser.class);
        Object result = mock(Object.class);

        when(valueParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(valueParser.getValueAsString()).thenReturn(value1);

        when(parser.getParserCreator().createParser(Mockito.any(JsonFactory.class), Mockito.any(IOContext.class))).thenReturn(valueParser);

        handler.handlePropertyValue(parser, context, propName1, bean);

        assertEquals(typeId1, handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        SettableBeanProperty prop = handler._properties[0].getProperty();
        assertEquals(value1, prop.get(bean));
    }

    @Test
    public void testComplete() throws IOException {
        String typeId1 = "StringTypeId";
        String typeId2 = "IntegerTypeId";
        String propName1 = "prop1";
        String propName2 = "prop2";

        when(parser.getText()).thenReturn(typeId1);

        handler.handleTypePropertyValue(parser, context, propName1, bean);

        handler = handler.start();

        when(parser.getText()).thenReturn(typeId2);

        handler.handleTypePropertyValue(parser, context, propName2, bean);

        handler = handler.start();

        Object result = handler.complete(parser, context, bean);

        assertEquals(bean, result);
        assertEquals(typeId1, handler._properties[0].getProperty().getType().getSimpleName());
        assertEquals(typeId2, handler._properties[1].getProperty().getType().getSimpleName());
        assertEquals(typeId1, handler._typeIds[0]);
        assertEquals(typeId2, handler._typeIds[1]);
    }

    @Test
    public void testCompleteWithPropertyValueBuffer() throws IOException {
        Object value1 = "value1";
        Object value2 = 1;
        String typeId1 = "StringTypeId";
        String typeId2 = "IntegerTypeId";
        String propName1 = "prop1";
        String propName2 = "prop2";

        when(parser.getText()).thenReturn(typeId1);

        handler.handleTypePropertyValue(parser, context, propName1, bean);

        handler = handler.start();

        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);
        when(context.findBeanDeserializer(bean.getClass())).thenReturn(mock(BeanDeserializer.class));
        when(context.findCreator(bean.getClass())).thenReturn(mock(PropertyBasedCreator.class));

        when(parser.getText()).thenReturn(typeId2);

        handler.handleTypePropertyValue(parser, context, propName2, bean);

        handler = handler.start();

        SettableBeanProperty prop1 = handler._properties[0].getProperty();
        SettableBeanProperty prop2 = handler._properties[1].getProperty();

        when(prop1.deserialize(parser, context)).thenReturn(value1);
        when(prop2.deserialize(parser, context)).thenReturn(value2);

        when(prop1.getName()).thenReturn(propName1);
        when(prop2.getName()).thenReturn(propName2);

        Object result = handler.complete(parser, context, buffer, mock(PropertyBasedCreator.class));

        assertEquals(bean.getClass(), result.getClass());
        assertEquals(value1, prop1.get(result));
        assertEquals(value2, prop2.get(result));
    }
}