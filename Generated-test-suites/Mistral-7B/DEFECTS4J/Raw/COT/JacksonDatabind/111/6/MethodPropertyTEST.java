import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.util.Annotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {

    @InjectMocks
    private MethodProperty methodProperty;

    private static final JavaType JAVA_TYPE = Mockito.mock(JavaType.class);
    private static final TypeDeserializer TYPE_DESERIALIZER = Mockito.mock(TypeDeserializer.class);
    private static final NullValueProvider NULL_VALUE_PROVIDER = Mockito.mock(NullValueProvider.class);

    @Test
    public void testConstructorWithValues() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        AnnotatedMethod annotatedMethod = Mockito.mock(AnnotatedMethod.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);

        when(annotatedMethod.getAnnotated()).thenReturn(contextAnnotations);

        methodProperty = new MethodProperty(propDef, JAVA_TYPE, TYPE_DESERIALIZER, contextAnnotations, annotatedMethod);

        assertEquals(propDef, methodProperty.getPropertyDefinition());
        assertEquals(JAVA_TYPE, methodProperty.getType());
        assertEquals(TYPE_DESERIALIZER, methodProperty.getTypeDeserializer());
        assertEquals(contextAnnotations, methodProperty.getContextAnnotations());
        assertEquals(annotatedMethod, methodProperty.getAnnotatedMethod());
    }

    @Test
    public void testWithName() {
        PropertyName newName = Mockito.mock(PropertyName.class);

        MethodProperty methodProperty = new MethodProperty(methodProperty, newName);

        assertEquals(newName, methodProperty.getName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> valueDeserializer = Mockito.mock(JsonDeserializer.class);

        methodProperty = new MethodProperty(methodProperty, valueDeserializer, NULL_VALUE_PROVIDER);

        assertEquals(valueDeserializer, methodProperty.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = Mockito.mock(NullValueProvider.class);

        methodProperty = new MethodProperty(methodProperty, _valueDeserializer, nva);

        assertEquals(nva, methodProperty.getNullProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = Mockito.mock(DeserializationConfig.class);

        methodProperty.fixAccess(config);

        verify(annotatedMethod).fixAccess(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS));
    }

    @Test
    public void testGetAnnotation() {
        @JsonProperty("testAnnotation")
        Annotation testAnnotation = Mockito.mock(Annotation.class);

        when(annotatedMethod.getAnnotation(testAnnotation.getClass())).thenReturn(testAnnotation);

        assertEquals(testAnnotation, methodProperty.getAnnotation(testAnnotation.getClass()));
        assertNull(methodProperty.getAnnotation(Null.class));
    }

    @Test
    public void testGetMember() {
        assertEquals(annotatedMethod, methodProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.readValueAs(String.class)).thenReturn("testValue");

        String expectedValue = "testValue";
        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(instance).getClass();
        verify(instance).getClass().getMethod("setTest", String.class).invoke(instance, expectedValue);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithNullValue() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(instance).getClass();
        verify(_nullValueProvider).getNullValue(deserializationContext);
        verify(instance).getClass().getMethod("setTest", Object.class).invoke(instance, Mockito.eq(null));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithValueDeserializer() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenThrow(new IOException());

        methodProperty = new MethodProperty(methodProperty, jsonDeserializer, NULL_VALUE_PROVIDER);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.readValueAs(String.class)).thenReturn("testValue");

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
        verifyThrows(IOException.class, () -> jsonDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetWithValueDeserializerWithType() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        when(jsonDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer)).thenThrow(new IOException());

        methodProperty = new MethodProperty(methodProperty, jsonDeserializer, typeDeserializer, NULL_VALUE_PROVIDER);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.readValueAs(String.class)).thenReturn("testValue");

        methodProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        verify(jsonDeserializer).deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        verifyThrows(IOException.class, () -> jsonDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithNullValue() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);

        methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(instance).getClass();
        verify(_nullValueProvider).getNullValue(deserializationContext);
        verify(instance).getClass().getMethod("setTest", Object.class).invoke(instance, Mockito.eq(null));
        verify(instance).equals(instance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithValueDeserializer() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenThrow(new IOException());

        methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
        verifyThrows(IOException.class, () -> jsonDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnWithValueDeserializerWithType() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        when(jsonDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer)).thenThrow(new IOException());

        methodProperty = new MethodProperty(methodProperty, jsonDeserializer, typeDeserializer, NULL_VALUE_PROVIDER);

        when(jsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jsonParser.readValueAs(String.class)).thenReturn("testValue");

        methodProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        verify(jsonDeserializer).deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        verifyThrows(IOException.class, () -> jsonDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();

        methodProperty.set(instance, "testValue");

        verify(instance).getClass().getMethod("setTest", String.class).invoke(instance, "testValue");
    }

    @Test
    public void testSetWithNullValue() throws IOException {
        Object instance = new Object();

        methodProperty.set(instance, null);

        verify(_nullValueProvider).getNullValue(null);
        verify(instance).getClass().getMethod("setTest", Object.class).invoke(instance, null);
    }

    @Test(expected = IOException.class)
    public void testSetWithValueDeserializer() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);

        JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        when(jsonDeserializer.deserialize(jsonParser, deserializationContext)).thenThrow(new IOException());

        methodProperty.set(instance, jsonParser);

        verify(jsonDeserializer).deserialize(jsonParser, deserializationContext);
        verifyThrows(IOException.class, () -> jsonDeserializer.deserialize(jsonParser, deserializationContext));
    }

    @Test(expected = IOException.class)
    public void testSetWithValueDeserializerWithType() throws IOException {
        Object instance = new Object();
        JsonParser jsonParser = Mockito.mock(JsonParser.class);
        DeserializationContext deserializationContext = Mockito.mock(DeserializationContext.class);
        TypeDeserializer typeDeserializer = Mockito.mock(TypeDeserializer.class);

        JsonDeserializer<?> jsonDeserializer = Mockito.mock(JsonDeserializer.class);
        when(jsonDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer)).thenThrow(new IOException());

        methodProperty = new MethodProperty(methodProperty, jsonDeserializer, typeDeserializer, NULL_VALUE_PROVIDER);

        methodProperty.set(instance, jsonParser);

        verify(jsonDeserializer).deserializeWithType(jsonParser, deserializationContext, typeDeserializer);
        verifyThrows(IOException.class, () -> jsonDeserializer.deserializeWithType(jsonParser, deserializationContext, typeDeserializer));
    }

    @Test
    public void testReadResolve() throws ObjectStreamException {
        Object instance = new Object();

        methodProperty = new MethodProperty(methodProperty, annotatedMethod);

        Object readResolve = methodProperty.readResolve();

        assertEquals(methodProperty, readResolve);
    }
}