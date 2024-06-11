
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;

public class CreatorPropertyTest {

    private CreatorProperty creatorProperty;
    private PropertyName propertyName;
    private JavaType javaType;
    private PropertyMetadata propertyMetadata;
    private Annotations annotations;
    private AnnotatedParameter annotatedParameter;
    
    @Before
    public void setup() {
        propertyName = mock(PropertyName.class);
        javaType = mock(JavaType.class);
        propertyMetadata = mock(PropertyMetadata.class);
        annotations = mock(Annotations.class);
        annotatedParameter = mock(AnnotatedParameter.class);

        creatorProperty = new CreatorProperty(propertyName, javaType, null, null, annotations, annotatedParameter, 0, null, propertyMetadata);
    }
    
    @Test
    public void testConstructor() {
        assertNotNull(creatorProperty);
        assertEquals(propertyName, creatorProperty.getName());
        assertEquals(javaType, creatorProperty.getType());
        assertNull(creatorProperty.getTypeDeserializer());
        assertEquals(annotations, creatorProperty.getContextAnnotations());
        assertEquals(annotatedParameter, creatorProperty.getMember());
        assertEquals(propertyMetadata, creatorProperty.getMetadata());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        SettableBeanProperty result = creatorProperty.withNullProvider(nullValueProvider);

        assertNotNull(result);
        assertEquals(creatorProperty.getName(), result.getName());
        assertEquals(creatorProperty.getType(), result.getType());
        assertEquals(creatorProperty.getTypeDeserializer(), result.getTypeDeserializer());
        assertEquals(creatorProperty.getContextAnnotations(), result.getContextAnnotations());
        assertEquals(creatorProperty.getMember(), result.getMember());
        assertEquals(creatorProperty.getMetadata(), result.getMetadata());
        assertEquals(nullValueProvider, result.getNullValueProvider());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();

        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);

        creatorProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(fallbackSetter, times(1)).set(instance, creatorProperty.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser jsonParser = mock(JsonParser.class);
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object instance = new Object();
        Object expectedResult = new Object();

        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);

        when(fallbackSetter.setAndReturn(instance, creatorProperty.deserialize(jsonParser, deserializationContext))).thenReturn(expectedResult);
        
        Object result = creatorProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        assertEquals(expectedResult, result);
        verify(fallbackSetter, times(1)).setAndReturn(instance, creatorProperty.deserialize(jsonParser, deserializationContext));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);

        creatorProperty.set(instance, value);

        verify(fallbackSetter, times(1)).set(instance, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();
        Object expectedResult = new Object();

        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        creatorProperty.setFallbackSetter(fallbackSetter);

        when(fallbackSetter.setAndReturn(instance, value)).thenReturn(expectedResult);

        Object result = creatorProperty.setAndReturn(instance, value);

        assertEquals(expectedResult, result);
        verify(fallbackSetter, times(1)).setAndReturn(instance, value);
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testFindInjectableValue() throws JsonMappingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object beanInstance = new Object();

        InvalidDefinitionException expectedException = new InvalidDefinitionException(null, "Test Exception", null);
        when(deserializationContext.findInjectableValue(null, creatorProperty, beanInstance)).thenThrow(expectedException);

        try {
            creatorProperty.findInjectableValue(deserializationContext, beanInstance);
        } catch (JsonMappingException e) {
            assertEquals(expectedException, e);
        }

        verify(deserializationContext, times(1)).reportBadDefinition(ClassUtil.classOf(beanInstance), "Property 'null' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured");
        verify(deserializationContext, times(1)).findInjectableValue(null, creatorProperty, beanInstance);
    }
    
    @Test
    public void testInject() throws IOException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object beanInstance = new Object();
        Object injectableValue = new Object();

        creatorProperty.setFallbackSetter(mock(SettableBeanProperty.class));

        when(creatorProperty.findInjectableValue(deserializationContext, beanInstance)).thenReturn(injectableValue);

        creatorProperty.inject(deserializationContext, beanInstance);

        verify(creatorProperty, times(1)).findInjectableValue(deserializationContext, beanInstance);
        verify(creatorProperty.getFallbackSetter(), times(1)).set(beanInstance, injectableValue);
    }

    @Test
    public void testFindInjectableValueWhenInjectableValueIdIsNull() throws JsonMappingException {
        DeserializationContext deserializationContext = mock(DeserializationContext.class);
        Object beanInstance = new Object();
        
        creatorProperty = new CreatorProperty(propertyName, javaType, null, null, annotations, annotatedParameter, 0, null, propertyMetadata);

        creatorProperty.findInjectableValue(deserializationContext, beanInstance);

        verify(deserializationContext, times(1)).reportBadDefinition(ClassUtil.classOf(beanInstance), "Property 'null' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured");
        verifyNoMoreInteractions(deserializationContext);
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;

        assertNull(creatorProperty.getAnnotation(acls));
        verify(annotatedParameter, times(1)).getAnnotation(Annotation.class);
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }
    
    @Test
    public void testSetIgnorable() {
        assertFalse(creatorProperty.isIgnorable());

        creatorProperty.markAsIgnorable();

        assertTrue(creatorProperty.isIgnorable());
    }
    
    @Test
    public void testToString() {
        assertEquals("[creator property, name 'propertyName'; inject id 'null']", creatorProperty.toString());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testVerifySetterWhenFallbackSetterIsNull() throws IOException {
        creatorProperty.set(null, null);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testReportMissingSetterWhenFallbackSetterIsNull() throws IOException {
        creatorProperty._reportMissingSetter(null, null);
    }
}
