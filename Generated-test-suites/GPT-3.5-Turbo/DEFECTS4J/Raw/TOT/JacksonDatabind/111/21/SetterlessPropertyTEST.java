import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

public class SetterlessPropertyTest {

    @Test
    public void testConstructorAndGetters() {
        // Test constructor
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        // Verify constructor and getters
        assertEquals(propDef, property.getMember());
        assertEquals(type, property.getType());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(method, property.getGetter());
        assertNull(property.getValueDeserializer());
        assertNull(property.getNullProvider());
        assertFalse(property.hasNullProvider());
    }

    @Test
    public void testWithMethods() {
        // Test withName()
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
        PropertyName newName = new PropertyName("newName");
        SettableBeanProperty newProperty = property.withName(newName);
        assertNotSame(property, newProperty);
        assertEquals(newName, newProperty.getFullName());
        assertNull(newProperty.getValueDeserializer());
        assertNull(newProperty.getNullProvider());

        // Test withValueDeserializer()
        JsonDeserializer<?> deser = Mockito.mock(JsonDeserializer.class);
        SettableBeanProperty deserializedProperty = newProperty.withValueDeserializer(deser);
        assertNotSame(newProperty, deserializedProperty);
        assertEquals(deser, deserializedProperty.getValueDeserializer());
        assertNull(deserializedProperty.getNullProvider());

        // Test withNullProvider()
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);
        SettableBeanProperty nullProviderProperty = deserializedProperty.withNullProvider(nva);
        assertNotSame(deserializedProperty, nullProviderProperty);
        assertEquals(nva, nullProviderProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, method);
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);
        property.fixAccess(config);
        Mockito.verify(method).fixAccess(Mockito.anyBoolean());
    }

    @Test
    public void testGetAnnotation() {
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, method);
        Class<JsonProperty> annotationClass = JsonProperty.class;
        assertEquals(method.getAnnotation(annotationClass), property.getAnnotation(annotationClass));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        // Test case 1: JsonToken.VALUE_NULL
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        Mockito.verify(jsonParser).getCurrentToken();
        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyZeroInteractions(deserializationContext);
        Mockito.verifyZeroInteractions(instance);

        // Test case 2: _valueTypeDeserializer != null
        TypeDeserializer valueTypeDeserializer = Mockito.mock(TypeDeserializer.class);
        property = new SetterlessProperty(null, null, valueTypeDeserializer, null, null);
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        Mockito.verify(jsonParser, Mockito.times(2)).getCurrentToken();
        Mockito.verify(jsonParser).getType();
        Mockito.verify(jsonParser).getText();
        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyZeroInteractions(deserializationContext);

        // Test case 3: toModify == null
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        Mockito.verify(jsonParser, Mockito.times(3)).getCurrentToken();
        Mockito.verify(jsonParser).getType();
        Mockito.verify(jsonParser).getText();
        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verify(deserializationContext).reportBadDefinition(Mockito.any(), Mockito.anyString());

        // Test case 4: happy path
        Object toModify = new Object();
        Mockito.when(jsonParser.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(jsonParser.readValueAs(Mockito.any())).thenReturn(toModify);
        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        Mockito.verify(jsonParser, Mockito.times(4)).getCurrentToken();
        Mockito.verify(jsonParser).readValueAs(Mockito.any());
        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyZeroInteractions(deserializationContext);
        Mockito.verifyZeroInteractions(instance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);

        property.deserializeAndSet(jsonParser, deserializationContext, instance);
        Mockito.verify(jsonParser).getCurrentToken();
        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyZeroInteractions(deserializationContext);
        Mockito.verifyZeroInteractions(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);
        property.set(null, null);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        Object instance = Mockito.mock(Object.class);
        Object value = Mockito.mock(Object.class);

        property.setAndReturn(instance, value);
        Mockito.verifyZeroInteractions(instance);
        Mockito.verifyZeroInteractions(value);
    }

    @Test
    public void testDeserializeAndSet_withException() throws IOException {
        SetterlessProperty property = new SetterlessProperty(null, null, null, null, null);

        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        Object instance = Mockito.mock(Object.class);

        Mockito.doThrow(new IOException()).when(jsonParser).getCurrentToken();
        try {
            property.deserializeAndSet(jsonParser, deserializationContext, instance);
            fail("Should throw IOException");
        } catch (IOException e) {
            // Expected
        }

        Mockito.verify(jsonParser).getCurrentToken();
        Mockito.verifyNoMoreInteractions(jsonParser);
        Mockito.verifyZeroInteractions(deserializationContext);
        Mockito.verifyZeroInteractions(instance);
    }
}