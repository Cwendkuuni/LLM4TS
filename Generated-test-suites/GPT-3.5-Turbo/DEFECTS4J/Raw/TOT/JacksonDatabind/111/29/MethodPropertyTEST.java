import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {

    @Mock
    private BeanPropertyDefinition propDef;

    @Mock
    private JavaType type;

    @Mock
    private TypeDeserializer typeDeser;

    @Mock
    private Annotations contextAnnotations;

    @Mock
    private AnnotatedMethod method;

    @Mock
    private JsonDeserializer<?> deser;

    @Mock
    private NullValueProvider nvp;
    
    @Mock
    private JsonParser p;

    @Mock
    private DeserializationContext ctxt;

    @Mock
    private Object instance;

    private Method setter;
    
    private MethodProperty methodProperty;

    @Before
    public void setup() throws NoSuchMethodException, SecurityException {
        setter = MethodProperty.class.getDeclaredMethod("set", Object.class, Object.class);
        methodProperty = new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testConstructor() {
        assertNotNull(methodProperty);
        assertEquals(propDef, methodProperty.getMember());
    }

    @Test
    public void testWithMethods() {
        SettableBeanProperty newMethodProperty = methodProperty.withName(PropertyName.construct("newName"));
        assertNotNull(newMethodProperty);
        assertEquals(PropertyName.construct("newName"), newMethodProperty.getFullName());

        SettableBeanProperty newMethodPropertyWithDeserializer = methodProperty.withValueDeserializer(deser);
        assertNotNull(newMethodPropertyWithDeserializer);
        assertEquals(deser, newMethodPropertyWithDeserializer.getValueDeserializer());

        SettableBeanProperty newMethodPropertyWithNullProvider = methodProperty.withNullProvider(nvp);
        assertNotNull(newMethodPropertyWithNullProvider);
        assertEquals(nvp, newMethodPropertyWithNullProvider.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);

        methodProperty.fixAccess(config);

        verify(method).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<Deprecated> deprecated = Deprecated.class;
        when(method.getAnnotation(deprecated)).thenReturn(deprecated.getAnnotation(deprecated));

        assertEquals(deprecated.getAnnotation(deprecated), methodProperty.getAnnotation(deprecated));
    }

    @Test
    public void testDeserializeAndSet_ValueNull_SkipNulls_True() throws Exception {
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(true);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verifyZeroInteractions(ctxt); // Null value should be skipped, no interaction with context
        verifyZeroInteractions(instance); // Null value should be skipped, no interaction with instance
    }

    @Test
    public void testDeserializeAndSet_ValueNull_SkipNulls_False() throws Exception {
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(false);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(null);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(method, times(1)).invoke(instance, null); // Null value should be set on instance
    }

    @Test
    public void testDeserializeAndSet_ValueNotNull_NoValueTypeDeserializer() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._valueDeserializer.deserialize(p, ctxt)).thenReturn("value");

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(method, times(1)).invoke(instance, "value"); // Non-null value should be set on instance
    }

    @Test
    public void testDeserializeAndSet_ValueNullCoerced_SkipNulls_True() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(p.getValueAsString()).thenReturn("null");
        when(methodProperty._skipNulls).thenReturn(true);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verifyZeroInteractions(ctxt); // Null value should be skipped, no interaction with context
        verifyZeroInteractions(instance); // Null value should be skipped, no interaction with instance
    }

    @Test
    public void testDeserializeAndSet_ValueNullCoerced_SkipNulls_False() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(p.getValueAsString()).thenReturn("null");
        when(methodProperty._skipNulls).thenReturn(false);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(null);

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(method, times(1)).invoke(instance, null); // Null value should be set on instance
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializer() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._valueDeserializer.deserializeWithType(p, ctxt, methodProperty._valueTypeDeserializer)).thenReturn("value");

        methodProperty.deserializeAndSet(p, ctxt, instance);

        verify(method, times(1)).invoke(instance, "value"); // Non-null value should be set on instance
    }

    @Test
    public void testDeserializeSetAndReturn_ValueNull_SkipNulls_True() throws Exception {
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(true);

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result); // Null value should be skipped, result should be null
        verifyZeroInteractions(ctxt); // Null value should be skipped, no interaction with context
        verifyZeroInteractions(instance); // Null value should be skipped, no interaction with instance
    }

    @Test
    public void testDeserializeSetAndReturn_ValueNull_SkipNulls_False() throws Exception {
        JsonToken token = JsonToken.VALUE_NULL;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._skipNulls).thenReturn(false);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(null);

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result); // Null value should be skipped, result should be instance
        verifyZeroInteractions(ctxt); // Null value should be skipped, no interaction with context
        verifyZeroInteractions(instance); // Null value should be skipped, no interaction with instance
    }

    @Test
    public void testDeserializeSetAndReturn_ValueNotNull_NoValueTypeDeserializer() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._valueTypeDeserializer).thenReturn(null);
        when(methodProperty._valueDeserializer.deserialize(p, ctxt)).thenReturn("value");

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result); // Non-null value should be set on instance, result should be instance
        verify(method, times(1)).invoke(instance, "value"); // Non-null value should be set on instance
    }

    @Test
    public void testDeserializeSetAndReturn_ValueNullCoerced_SkipNulls_True() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(p.getValueAsString()).thenReturn("null");
        when(methodProperty._skipNulls).thenReturn(true);

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertNull(result); // Null value should be skipped, result should be null
        verifyZeroInteractions(ctxt); // Null value should be skipped, no interaction with context
        verifyZeroInteractions(instance); // Null value should be skipped, no interaction with instance
    }

    @Test
    public void testDeserializeSetAndReturn_ValueNullCoerced_SkipNulls_False() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(p.getValueAsString()).thenReturn("null");
        when(methodProperty._skipNulls).thenReturn(false);
        when(methodProperty._nullProvider.getNullValue(ctxt)).thenReturn(null);

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result); // Null value should be skipped, result should be instance
        verify(method, times(1)).invoke(instance, null); // Null value should be set on instance
    }

    @Test
    public void testDeserializeSetAndReturn_ValueTypeDeserializer() throws Exception {
        JsonToken token = JsonToken.VALUE_STRING;
        when(p.hasToken(token)).thenReturn(true);
        when(methodProperty._valueDeserializer.deserializeWithType(p, ctxt, methodProperty._valueTypeDeserializer)).thenReturn("value");

        Object result = methodProperty.deserializeSetAndReturn(p, ctxt, instance);

        assertEquals(instance, result); // Non-null value should be set on instance, result should be instance
        verify(method, times(1)).invoke(instance, "value"); // Non-null value should be set on instance
    }

    @Test
    public void testSet_ValueNotNull() throws Exception {
        methodProperty.set(instance, "value");

        verify(method, times(1)).invoke(instance, "value"); // Non-null value should be set on instance
    }

    @Test
    public void testSet_ValueNull() throws Exception {
        methodProperty.set(instance, null);

        verify(method, times(1)).invoke(instance, null); // Null value should be set on instance
    }

    @Test
    public void testSetAndReturn_ValueNotNull() throws Exception {
        Object result = methodProperty.setAndReturn(instance, "value");

        assertEquals(instance, result); // Non-null value should be set on instance, result should be instance
        verify(method, times(1)).invoke(instance, "value"); // Non-null value should be set on instance
    }

    @Test
    public void testSetAndReturn_ValueNull() throws Exception {
        Object result = methodProperty.setAndReturn(instance, null);

        assertEquals(instance, result); // Null value should be set on instance, result should be instance
        verify(method, times(1)).invoke(instance, null); // Null value should be set on instance
    }

    @Test
    public void testReadResolve() throws NoSuchMethodException {
        MethodProperty serializedMethodProperty = new MethodProperty(methodProperty, setter);

        MethodProperty resolvedMethodProperty = (MethodProperty) serializedMethodProperty.readResolve();

        assertNotNull(resolvedMethodProperty);
        assertEquals(serializedMethodProperty._annotated.getAnnotated(), resolvedMethodProperty._setter);
    }
}