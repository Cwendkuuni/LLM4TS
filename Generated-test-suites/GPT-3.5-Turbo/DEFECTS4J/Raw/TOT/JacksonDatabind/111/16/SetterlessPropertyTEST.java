import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;
    private BeanPropertyDefinition propDef;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod method;
    private DeserializationContext ctxt;
    private JsonParser jsonParser;
    private DeserializationConfig config;
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        propDef = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        method = mock(AnnotatedMethod.class);
        ctxt = mock(DeserializationContext.class);
        jsonParser = mock(JsonParser.class);
        config = mock(DeserializationConfig.class);
        objectMapper = mock(ObjectMapper.class);

        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        try {
            Method getterMethod = SetterlessProperty.class.getDeclaredMethod("deserializeAndSet", JsonParser.class, DeserializationContext.class, Object.class);
            when(ctxt.isEnabled(any())).thenReturn(true);
            when(jsonParser.getCurrentToken()).thenReturn(null);
            when(jsonParser.getText()).thenReturn("test");
            when(method.getAnnotated()).thenReturn(getterMethod);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        try {
            when(jsonParser.getCurrentToken()).thenReturn(null);
            when(jsonParser.getText()).thenReturn("test");
            when(method.invoke(any(), any())).thenReturn(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDeserializeAndSet_WhenTokenIsNull_ShouldNotSetAnything() throws IOException {
        setterlessProperty.deserializeAndSet(jsonParser, ctxt, new Object());
        verify(jsonParser, times(1)).getCurrentToken();
        verify(jsonParser, never()).getText();
        verifyZeroInteractions(ctxt);
        verifyZeroInteractions(propDef);
        verifyZeroInteractions(type);
        verifyZeroInteractions(typeDeser);
        verifyZeroInteractions(contextAnnotations);
        verifyZeroInteractions(method);
    }

    @Test
    public void testDeserializeAndSet_WhenTypeDeserializerNotNull_ShouldReportBadDefinition() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_FALSE);
        setterlessProperty.deserializeAndSet(jsonParser, ctxt, new Object());
        verify(jsonParser, times(1)).getCurrentToken();
        verify(jsonParser, never()).getText();
        verify(ctxt, times(1)).reportBadDefinition(any(), any());
        verifyZeroInteractions(config);
        verifyZeroInteractions(propDef);
        verifyZeroInteractions(type);
        verifyZeroInteractions(contextAnnotations);
        verifyZeroInteractions(method);
    }

    @Test
    public void testDeserializeAndSet_WhenToModifyIsNull_ShouldReportBadDefinition() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);
        when(method.invoke(any(), any())).thenReturn(null);
        setterlessProperty.deserializeAndSet(jsonParser, ctxt, new Object());
        verify(jsonParser, times(1)).getCurrentToken();
        verify(jsonParser, never()).getText();
        verify(ctxt, times(1)).reportBadDefinition(any(), any());
        verifyZeroInteractions(config);
        verifyZeroInteractions(propDef);
        verifyZeroInteractions(type);
        verifyZeroInteractions(typeDeser);
        verifyZeroInteractions(contextAnnotations);
        verifyZeroInteractions(method);
    }
    
    @Test
    public void testDeserializeAndSet_WhenToModifyIsNotNull_ShouldInvokeDeserializeMethod() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_TRUE);
        when(method.invoke(any(), any())).thenReturn(objectMapper.readValue("\"test\"", Object.class));
        setterlessProperty.deserializeAndSet(jsonParser, ctxt, new Object());
        verify(jsonParser, times(1)).getCurrentToken();
        verify(jsonParser, never()).getText();
        verify(method, times(1)).invoke(any(), any());
        verifyZeroInteractions(config);
        verifyZeroInteractions(propDef);
        verifyZeroInteractions(type);
        verifyZeroInteractions(typeDeser);
        verifyZeroInteractions(contextAnnotations);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet_ShouldThrowUnsupportedOperationException() throws IOException {
        setterlessProperty.set(new Object(), new Object());
    }

    @Test
    public void testSetAndReturn_ShouldInvokeSetMethod() throws IOException {
        Object instance = new Object();
        setterlessProperty.setAndReturn(instance, new Object());
        verify(method, times(1)).invoke(any(), any());
        assertEquals(instance, setterlessProperty);
    }

    @Test
    public void testGetAnnotation_ShouldInvokeGetAnnotationMethod() {
        Class annotationClass = Annotation.class;
        setterlessProperty.getAnnotation(annotationClass);
        verify(method, times(1)).getAnnotation(annotationClass);
    }

    @Test
    public void testGetMember_ShouldReturnAnnotatedMember() {
        assertEquals(method, setterlessProperty.getMember());
    }

    @Test
    public void testWithName_ShouldReturnSetterlessPropertyWithNewName() {
        PropertyName newName = mock(PropertyName.class);
        assertEquals(setterlessProperty, setterlessProperty.withName(newName));
    }

    @Test
    public void testWithValueDeserializer_WhenValueDeserializerTheSame_ShouldReturnSetterlessProperty() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        when(setterlessProperty.getValueDeserializer()).thenReturn(deser);
        assertEquals(setterlessProperty, setterlessProperty.withValueDeserializer(deser));
    }

    @Test
    public void testWithValueDeserializer_ShouldReturnSetterlessPropertyWithNewValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        assertEquals(setterlessProperty, setterlessProperty.withValueDeserializer(deser));
    }

    @Test
    public void testWithNullProvider_ShouldReturnSetterlessPropertyWithNewNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        assertEquals(setterlessProperty, setterlessProperty.withNullProvider(nva));
    }

    @Test
    public void testFixAccess_WhenOverridePublicAccessModifiers_Enabled_ShouldInvokeFixAccessMethod() {
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        setterlessProperty.fixAccess(config);
        verify(method, times(1)).fixAccess(true);
    }

    @Test
    public void testFixAccess_WhenOverridePublicAccessModifiers_Disabled_ShouldInvokeFixAccessMethod() {
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(false);
        setterlessProperty.fixAccess(config);
        verify(method, times(1)).fixAccess(false);
    }
}