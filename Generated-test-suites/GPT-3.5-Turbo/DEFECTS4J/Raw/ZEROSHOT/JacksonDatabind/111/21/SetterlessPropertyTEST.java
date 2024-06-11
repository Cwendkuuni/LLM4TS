import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);
        Method annotated = mock(Method.class);
        when(method.getAnnotated()).thenReturn(annotated);

        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;

        Annotation result = setterlessProperty.getAnnotation(acls);

        verify(setterlessProperty.getMember()).getAnnotation(acls);
        assertEquals(null, result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = setterlessProperty.getMember();

        assertEquals(setterlessProperty.getMember(), result);
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.getCurrentToken()).thenReturn(token);

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).getCurrentToken();
        verifyZeroInteractions(ctxt);
        verifyZeroInteractions(instance);
    }

    @Test
    public void testDeserializeAndSet_WithTypeDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.getCurrentToken()).thenReturn(token);
        when(setterlessProperty.getValueTypeDeserializer()).thenReturn(mock(TypeDeserializer.class));

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).getCurrentToken();
        verify(ctxt).reportBadDefinition(setterlessProperty.getType(), "Problem deserializing 'setterless' property (\"" + setterlessProperty.getName() + "\"): no way to handle typed deser with setterless yet");
        verify(setterlessProperty, never()).getGetter();
        verifyZeroInteractions(instance);
    }

    @Test
    public void testDeserializeAndSet_GetterInvokeException() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.getCurrentToken()).thenReturn(token);
        when(setterlessProperty.getValueTypeDeserializer()).thenReturn(null);
        when(setterlessProperty.getGetter()).thenThrow(new Exception());

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).getCurrentToken();
        verify(setterlessProperty).getGetter();
        verifyZeroInteractions(ctxt);
        verifyZeroInteractions(instance);
    }

    @Test
    public void testDeserializeAndSet_NullToModify() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.getCurrentToken()).thenReturn(token);
        when(setterlessProperty.getValueTypeDeserializer()).thenReturn(null);
        when(setterlessProperty.getGetter()).thenReturn(mock(Method.class));
        when(setterlessProperty.getGetter().invoke(instance, (Object[]) null)).thenReturn(null);

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).getCurrentToken();
        verify(setterlessProperty).getGetter();
        verify(ctxt).reportBadDefinition(setterlessProperty.getType(), "Problem deserializing 'setterless' property '" + setterlessProperty.getName() + "': get method returned null");
        verifyZeroInteractions(instance);
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializer() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonToken token = JsonToken.VALUE_STRING;
        Object toModify = new Object();
        when(p.getCurrentToken()).thenReturn(token);
        when(setterlessProperty.getValueTypeDeserializer()).thenReturn(null);
        when(setterlessProperty.getGetter()).thenReturn(mock(Method.class));
        when(setterlessProperty.getGetter().invoke(instance, (Object[]) null)).thenReturn(toModify);
        JsonDeserializer<?> valueDeserializer = mock(JsonDeserializer.class);
        when(setterlessProperty.getValueDeserializer()).thenReturn(valueDeserializer);

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        verify(p).getCurrentToken();
        verify(setterlessProperty).getGetter();
        verify(setterlessProperty).getValueDeserializer();
        verify(valueDeserializer).deserialize(p, ctxt, toModify);
        verifyZeroInteractions(ctxt);
        verifyZeroInteractions(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        Object result = setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);

        verify(setterlessProperty).deserializeAndSet(p, ctxt, instance);
        assertEquals(instance, result);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        setterlessProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = setterlessProperty.setAndReturn(instance, value);

        verify(setterlessProperty).set(instance, value);
        assertEquals(instance, result);
    }
}