import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

@RunWith(MockitoJUnitRunner.class)
public class SetterlessPropertyTest {

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
    private DeserializationContext deserializationContext;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private JsonDeserializer<Object> jsonDeserializer;
    @Mock
    private NullValueProvider nullValueProvider;

    private SetterlessProperty setterlessProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(method.getAnnotated()).thenReturn(mock(Method.class));
        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        setterlessProperty = setterlessProperty.withValueDeserializer(jsonDeserializer);
        setterlessProperty = setterlessProperty.withNullProvider(nullValueProvider);
    }

    @Test
    public void testGetName() {
        assertEquals("setName", setterlessProperty.getName());
    }

    @Test
    public void testGetAnnotated() {
        assertEquals(method, setterlessProperty.getMember());
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> annotationClass = Annotation.class;
        assertNull(setterlessProperty.getAnnotation(annotationClass));
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        setterlessProperty.fixAccess(config);
        verify(method).fixAccess(true);
    }

    @Test
    public void testWithNullProvider() {
        SetterlessProperty modifiedProperty = setterlessProperty.withNullProvider(nullValueProvider);
        assertNotNull(modifiedProperty);
        assertEquals(nullValueProvider, modifiedProperty.getNullValueProvider());
    }

    @Test
    public void testDeserializeAndSet_ValueTokenIsNull() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, new Object());

        // Verify that nothing was set since value is null
        verify(jsonDeserializer, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void testDeserializeAndSet_ValueTypeDeserializerNotNull() throws IOException {
        // Since _valueTypeDeserializer is not null, it will call reportBadDefinition
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, new Object());
        verify(deserializationContext).reportBadDefinition(eq(type), anyString());
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializerWithNullToModify() throws Exception {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(method.invoke(any(), (Object[]) null)).thenReturn(null);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, new Object());

        // Even though value is not null, it returns null and reports bad definition
        verify(deserializationContext).reportBadDefinition(eq(type), anyString());
        verify(jsonDeserializer, never()).deserialize(any(JsonParser.class), any(DeserializationContext.class), any(Object.class));
    }

    @Test
    public void testDeserializeAndSet_ValueDeserializer() throws Exception {
        Object toModify = new Object();

        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NUMBER_INT);
        when(method.invoke(any(), (Object[]) null)).thenReturn(toModify);

        setterlessProperty.deserializeAndSet(jsonParser, deserializationContext, new Object());

        verify(jsonDeserializer).deserialize(eq(jsonParser), eq(deserializationContext), eq(toModify));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        setterlessProperty.set(new Object(), new Object());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();

        setterlessProperty.setAndReturn(instance, new Object());

        // Unsupported operation should be called
        verify(setterlessProperty).set(instance, new Object());
    }

    @Test
    public void testWithPropertyName() {
        String newName = "newName";
        SetterlessProperty modifiedProperty = setterlessProperty.withName(PropertyName.construct(newName));
        assertNotNull(modifiedProperty);
        assertEquals(newName, modifiedProperty.getName());
    }

    @Test
    public void testWithValueDeserializer_SameDeserializer() {
        SetterlessProperty modifiedProperty = setterlessProperty.withValueDeserializer(jsonDeserializer);
        assertNotNull(modifiedProperty);
        assertEquals(setterlessProperty, modifiedProperty);
    }

    @Test
    public void testWithValueDeserializer_DifferentDeserializer() {
        SetterlessProperty modifiedProperty = setterlessProperty.withValueDeserializer(mock(JsonDeserializer.class));
        assertNotNull(modifiedProperty);
        assertNotEquals(setterlessProperty, modifiedProperty);
    }

    @Test
    public void testWithMismatchedNullProvider() {
        SetterlessProperty modifiedProperty = setterlessProperty.withNullProvider(mock(NullValueProvider.class));
        assertNotNull(modifiedProperty);
        assertEquals(nullValueProvider, modifiedProperty.getNullValueProvider());
    }
}