import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

public class InnerClassPropertyTest {

    @Mock
    private SettableBeanProperty delegate;

    private Constructor<?> constructor;

    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() throws Exception {
        delegate = mock(SettableBeanProperty.class);
        constructor = mock(Constructor.class);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
    }

    @Test
    public void testConstructor() {
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testWithDeser() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deserializer);
        assertEquals(delegate, result._delegate);
        assertEquals(constructor, result._creator);
        assertEquals(deserializer, result._valueDeserializer);
    }

    @Test
    public void testWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        InnerClassProperty result = innerClassProperty.withName(newName);
        assertEquals(delegate, result._delegate);
        assertEquals(constructor, result._creator);
        assertEquals(newName, result._name);
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object bean = new Object();

        Object value = new Object();
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(innerClassProperty._valueDeserializer.getNullValue(ctxt)).thenReturn(value);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        verify(delegate).set(bean, value);

        value = new Object();
        when(jp.getCurrentToken()).thenReturn(JsonToken.VALUE_STRING);
        when(innerClassProperty._valueDeserializer.deserialize(jp, ctxt)).thenReturn(value);
        innerClassProperty.deserializeAndSet(jp, ctxt, bean);
        verify(delegate).set(bean, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();
        when(innerClassProperty.deserialize(jp, ctxt)).thenReturn(value);
        assertEquals(value, innerClassProperty.deserializeSetAndReturn(jp, ctxt, instance));
    }

    @Test
    public void testSet() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        innerClassProperty.set(instance, value);
        verify(delegate).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object instance = new Object();
        Object value = new Object();
        when(delegate.setAndReturn(instance, value)).thenReturn(value);
        assertEquals(value, innerClassProperty.setAndReturn(instance, value));
    }
}