import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.deser.DeserializationContext;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class MethodPropertyTest {

    private MethodProperty methodProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;

    @Before
    public void setup() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        method = mock(AnnotatedMethod.class);
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty newProperty = methodProperty.withName(newName);

        assertEquals(newName, newProperty.getFullName());
        assertEquals(type, newProperty.getType());
        assertEquals(typeDeser, newProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, newProperty.getContextAnnotations());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SettableBeanProperty newProperty = methodProperty.withValueDeserializer(deser);

        assertEquals(deser, newProperty.getValueDeserializer());
        assertEquals(contextAnnotations, newProperty.getContextAnnotations());

    }
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty newProperty = methodProperty.withNullProvider(nva);

        assertEquals(nva, newProperty.getNullValueProvider());
        assertEquals(contextAnnotations, newProperty.getContextAnnotations());

    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        MapperFeature feature = MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS;
        when(config.isEnabled(feature)).thenReturn(true);
        methodProperty.fixAccess(config);

        verify(method).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = A.class;
        A annotation = mock(A.class);
        when(method.getAnnotation(acls)).thenReturn(annotation);

        A result = methodProperty.getAnnotation(acls);

        assertEquals(annotation, result);
    }

    @Test
    public void testNullGetAnnotation() {
        Class<A> acls = A.class;
        when(method.getAnnotation(acls)).thenReturn(null);

        A result = methodProperty.getAnnotation(acls);

        assertNull(result);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember member = methodProperty.getMember();

        assertEquals(method, member);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenReturn(null);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(method, times(1)).invoke(instance, value);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithException() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenThrow(new Exception());

        methodProperty.deserializeAndSet(p, ctxt, instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenReturn(new Object());

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNotNull(result);
        verify(method, times(1)).invoke(instance, value);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithException() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        Object value = new Object();

        when(p.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenThrow(new Exception());

        methodProperty.deserializeSetAndReturn(p, ctxt, instance);
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenReturn(null);

        methodProperty.set(instance, value);

        verify(method, times(1)).invoke(instance, value);
    }

    @Test(expected = IOException.class)
    public void testSetWithException() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenThrow(new Exception());

        methodProperty.set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenReturn(new Object());

        Object result = methodProperty.setAndReturn(instance, value);

        assertNotNull(result);
        verify(method, times(1)).invoke(instance, value);
    }

    @Test(expected = IOException.class)
    public void testSetAndReturnWithException() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        when(method.getAnnotated()).thenReturn(mock(Method.class));
        when(method.invoke(instance, value)).thenThrow(new Exception());

        methodProperty.setAndReturn(instance, value);
    }
}