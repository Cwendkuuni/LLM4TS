import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;

import java.io.IOException;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ValueInstantiatorTest {

    private ObjectMapper objectMapper;
    private ValueInstantiator valueInstantiator;
    private DeserializationContext context;
    private JsonParser parser;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
        valueInstantiator = new BaseValueInstantiator(String.class);
        context = mock(DeserializationContext.class);
        parser = mock(JsonParser.class);
    }

    @Test
    public void testCanInstantiate() throws IOException {
        assertEquals(false, valueInstantiator.canInstantiate());
    }

    @Test
    public void testGetValueTypeDesc() {
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());
    }

    @Test
    public void testGetValueClass() {
        assertEquals(String.class, valueInstantiator.getValueClass());
    }

    @Test
    public void testCreateUsingDefault() throws IOException {
        when(context.handleMissingInstantiator(String.class, parser, "test"))
                .thenReturn(new String("default"));

        String result = (String) valueInstantiator.createUsingDefault(context);

        assertEquals("default", result);
    }

    @Test(expected = IllegalStateException.class)
    public void testCreateFromObjectWithNullArguments() throws IOException {
        when(context.getParser()).thenReturn(parser);

        SettableBeanProperty[] properties = new SettableBeanProperty[0];
        valueInstantiator.createFromObjectWith(context, properties, PropertyValueBuffer.emptyBuffer());
    }

    @Test
    public void testCreateFromObjectWithSingleArgument() throws IOException {
        when(context.getParser()).thenReturn(parser);

        SettableBeanProperty property = Mockito.mock(SettableBeanProperty.class);
        when(property.getName()).thenReturn(new PropertyName("test"));
        when(property.getMember()).thenReturn(Mockito.mock(Object.class));
        when(property.getValueBuffer()).thenReturn(PropertyValueBuffer.emptyBuffer());

        SettableBeanProperty[] properties = new SettableBeanProperty[]{property};
        when(context.findBeanProperty(PropertyName.construct(new String[]{"test"}))).thenReturn(property);

        String result = (String) valueInstantiator.createFromObjectWith(context, properties, PropertyValueBuffer.emptyBuffer());

        assertEquals("test", result);
    }

    @Test
    public void testCreateFromObjectWithMultipleArguments() throws IOException {
        when(context.getParser()).thenReturn(parser);

        SettableBeanProperty property1 = Mockito.mock(SettableBeanProperty.class);
        when(property1.getName()).thenReturn(new PropertyName("test1"));
        when(property1.getMember()).thenReturn(Mockito.mock(Object.class));
        when(property1.getValueBuffer()).thenReturn(PropertyValueBuffer.emptyBuffer());

        SettableBeanProperty property2 = Mockito.mock(SettableBeanProperty.class);
        when(property2.getName()).thenReturn(new PropertyName("test2"));
        when(property2.getMember()).thenReturn(Mockito.mock(Object.class));
        when(property2.getValueBuffer()).thenReturn(PropertyValueBuffer.emptyBuffer());

        SettableBeanProperty[] properties = new SettableBeanProperty[]{property1, property2};
        when(context.findBeanProperties(PropertyName.construct(new String[]{"test1", "test2"})))
                .thenReturn(new SettableBeanProperty[]{property1, property2});

        String result = (String) valueInstantiator.createFromObjectWith(context, properties, PropertyValueBuffer.emptyBuffer());

        assertEquals("test1:test1, test2:test2", result);
    }

    @Test
    public void testCreateUsingDelegate() throws IOException {
        when(context.handleMissingInstantiator(String.class, parser, "test"))
                .thenReturn(new String("delegate"));

        String result = (String) valueInstantiator.createUsingDelegate(context, "delegate");

        assertEquals("delegate", result);
    }

    @Test
    public void testCreateFromString() throws IOException {
        when(context.handleMissingInstantiator(String.class, parser, "test"))
                .thenReturn(new String("default"));
        when(context.getParser().getText()).thenReturn("test");

        String result = (String) valueInstantiator.createFromString(context, parser);

        assertEquals("default", result);
    }

    @Test
    public void testCreateFromInt() throws IOException {
        when(context.handleMissingInstantiator(String.class, parser, "test"))
                .thenReturn(new String("default"));
        when(context.getParser().getValueAsInt()).thenReturn(1);

        String result = (String) valueInstantiator.createFromInt(context, parser);

        assertEquals("default", result);
    }

    @Test
    public void testCreateFromLong() throws IOException {
        when(context.handleMissingInstantiator(String.class, parser, "test"))
                .thenReturn(new String("default"));
        when(context.getParser().getValueAsLong()).thenReturn(1L);

        String result = (String) valueInstantiator.createFromLong(context, parser);

        assertEquals("default", result);
    }

    @Test
    public void testCreateFromDouble() throws IOException {
        when(context.handleMissingInstantiator(String.class, parser, "test"))
                .thenReturn(new String("default"));
        when(context.getParser().getValueAsDouble()).thenReturn(1.0);

        String result = (String) valueInstantiator.createFromDouble(context, parser);

        assertEquals("default", result);
    }

    @Test
    public void testCreateFromBoolean() throws IOException {
        when(context.handleMissingInstantiator(String.class, parser, "test"))
                .thenReturn(new String("default"));
        when(context.getParser().getValueAsBoolean()).thenReturn(true);

        String result = (String) valueInstantiator.createFromBoolean(context, parser);

        assertEquals("default", result);
    }

    private static class BaseValueInstantiator extends ValueInstantiator.Base {

        public BaseValueInstantiator(Class<?> valueType) {
            super(valueType);
        }

        @Override
        public boolean canCreateFromString() {
            return true;
        }

        @Override
        public Object createFromString(DeserializationContext ctxt, String value) throws IOException {
            return value;
        }
    }
}