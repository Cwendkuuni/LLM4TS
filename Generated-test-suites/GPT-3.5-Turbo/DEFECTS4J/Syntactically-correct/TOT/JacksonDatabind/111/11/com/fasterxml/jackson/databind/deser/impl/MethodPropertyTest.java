
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.PropertyName;

@RunWith(PowerMockRunner.class)
@PrepareForTest({MethodProperty.class})
public class MethodPropertyTest {
    
    private MethodProperty methodProperty;
    private BeanPropertyDefinition propertyDefinition;
    private JavaType type;
    private TypeDeserializer typeDeser;
    private Annotations contextAnnotations;
    private AnnotatedMethod annotatedMethod;
    
    @Before
    public void setup() throws NoSuchMethodException, SecurityException {
        propertyDefinition = mock(BeanPropertyDefinition.class);
        type = mock(JavaType.class);
        typeDeser = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        annotatedMethod = mock(AnnotatedMethod.class);
        
        Method setterMethod = MethodProperty.class.getMethod("set", Object.class, Object.class);
        
        PowerMockito.when(annotatedMethod.getAnnotated()).thenReturn(setterMethod);
        PowerMockito.when(annotatedMethod.getAnnotation(any(Class.class))).thenReturn(null);
        
        methodProperty = new MethodProperty(propertyDefinition, type, typeDeser, contextAnnotations, annotatedMethod);
    }
    
    // Test for constructor
    
    @Test
    public void testConstructor() {
        MethodProperty methodProperty = new MethodProperty(propertyDefinition, type, typeDeser, contextAnnotations, annotatedMethod);
        
        assertEquals(propertyDefinition, methodProperty.getPropertyDefinition());
        assertEquals(type, methodProperty.getType());
        assertEquals(typeDeser, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(annotatedMethod, methodProperty.getMember());
        assertNull(methodProperty.getNullValueProvider());
        assertNull(methodProperty.getValueDeserializer());
        assertNull(methodProperty.getSetter());
        assertEquals(methodProperty.getSetter().getAnnotated(), annotatedMethod.getAnnotated());
        assertFalse(methodProperty.isSkipNulls());
    }
    
    // Test for withName method
    
    @Test
    public void testWithName() {
        MethodProperty newMethodProperty = (MethodProperty) methodProperty.withName(PropertyName.construct("newName"));
        
        assertEquals("newName", newMethodProperty.getName());
        assertNull(newMethodProperty.getName());
    }
    
    // Test for withValueDeserializer method
    
    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        MethodProperty newMethodProperty = (MethodProperty) methodProperty.withValueDeserializer(deserializer);
        
        assertSame(deserializer, newMethodProperty.getValueDeserializer());
        assertNull(newMethodProperty.getValueDeserializer());
        assertSame(methodProperty.getNullValueProvider(), newMethodProperty.getNullValueProvider());
    }
    
    // Test for withNullProvider method
    
    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        MethodProperty newMethodProperty = (MethodProperty) methodProperty.withNullProvider(nullValueProvider);
        
        assertEquals(nullValueProvider, newMethodProperty.getNullValueProvider());
        assertNull(newMethodProperty.getNullValueProvider());
        assertSame(methodProperty.getValueDeserializer(), newMethodProperty.getValueDeserializer());
    }
    
    // Test for fixAccess method
    
    @Test
    public void testFixAccess() throws IOException {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);
        
        methodProperty.fixAccess(config);
        
        verify(annotatedMethod).fixAccess(true);
    }
    
    // Test for getAnnotation method
    
    @Test
    public void testGetAnnotation() {
        assertNull(methodProperty.getAnnotation(TestAnnotation.class));
        verify(annotatedMethod).getAnnotation(TestAnnotation.class);
    }
    
    // Test for deserializeAndSet method
    
    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        Object nullValue = new Object();
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(null);
        when(nullValueProvider.getNullValue(any(DeserializationContext.class))).thenReturn(nullValue);
        
        methodProperty = new MethodProperty(propertyDefinition, type, typeDeser, contextAnnotations, annotatedMethod);
        methodProperty = methodProperty.withValueDeserializer(deserializer);
        methodProperty = methodProperty.withNullProvider(nullValueProvider);
        
        methodProperty.deserializeAndSet(parser, context, instance);
        
        verify(deserializer).deserialize(parser, context);
        verify(annotatedMethod).invoke(instance, nullValue);
    }
    
    // Test for deserializeSetAndReturn method
    
    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext context = mock(DeserializationContext.class);
        Object instance = new Object();
        JsonDeserializer<?> deserializer = mock(JsonDeserializer.class);
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        Object nullValue = new Object();
        when(parser.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        when(deserializer.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn(null);
        when(nullValueProvider.getNullValue(any(DeserializationContext.class))).thenReturn(nullValue);
        
        methodProperty = new MethodProperty(propertyDefinition, type, typeDeser, contextAnnotations, annotatedMethod);
        methodProperty = methodProperty.withValueDeserializer(deserializer);
        methodProperty = methodProperty.withNullProvider(nullValueProvider);
        
        methodProperty.deserializeSetAndReturn(parser, context, instance);
        
        verify(deserializer).deserialize(parser, context);
        verify(annotatedMethod).invoke(instance, nullValue);
    }
    
    // Test for set method
    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        
        methodProperty.set(instance, value);
        
        verify(annotatedMethod).invoke(instance, value);
    }
    
    // Test for setAndReturn method
    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object result = new Object();
        
        when(annotatedMethod.invoke(any(Object.class), any(Object.class))).thenReturn(result);
        
        Object returnValue = methodProperty.setAndReturn(instance, value);
        
        verify(annotatedMethod).invoke(instance, value);
        assertEquals(result, returnValue);
    }
}

@Retention(RetentionPolicy.RUNTIME)
@interface TestAnnotation {}

