import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SetterlessPropertyTest {

    private SetterlessProperty setterlessProperty;

    @Before
    public void setUp() {
        BeanPropertyDefinition propDef = mock(BeanPropertyDefinition.class);
        JavaType type = mock(JavaType.class);
        TypeDeserializer typeDeser = mock(TypeDeserializer.class);
        Annotations contextAnnotations = mock(Annotations.class);
        AnnotatedMethod method = mock(AnnotatedMethod.class);

        setterlessProperty = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);
    }

    @Test
    public void testWithName() {
        PropertyName newName = mock(PropertyName.class);
        SettableBeanProperty result = setterlessProperty.withName(newName);

        assertEquals(newName, result.getFullName());
    }

    @Test
    public void testWithValueDeserializer() {
        JsonDeserializer<?> deser = mock(JsonDeserializer.class);
        SettableBeanProperty result = setterlessProperty.withValueDeserializer(deser);

        assertEquals(deser, result.getValueDeserializer());
    }

    @Test
    public void testWithNullProvider() {
        NullValueProvider nva = mock(NullValueProvider.class);
        SettableBeanProperty result = setterlessProperty.withNullProvider(nva);

        assertEquals(nva, result.getNullValueProvider());
    }

    @Test
    public void testFixAccess() {
        DeserializationConfig config = mock(DeserializationConfig.class);
        when(config.isEnabled(MapperFeature.OVERRIDE_PUBLIC_ACCESS_MODIFIERS)).thenReturn(true);

        setterlessProperty.fixAccess(config);
        verify(setterlessProperty.getMember()).fixAccess(true);
    }

    @Test
    public void testGetAnnotation() {
        Class<A> acls = mock(Class.class);
        Annotation result = mock(Annotation.class);

        when(setterlessProperty.getMember().getAnnotation(acls)).thenReturn(result);

        assertEquals(result, setterlessProperty.getAnnotation(acls));
    }

    @Test
    public void testGetMember() {
        AnnotatedMember result = mock(AnnotatedMember.class);
        when(setterlessProperty.getMember()).thenReturn(result);

        assertEquals(result, setterlessProperty.getMember());
    }

    @Test
    public void testDeserializeAndSet_NullValue() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(p.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        // Assert that nothing is set since the value is null
        assertNull(instance);
    }

    @Test
    public void testDeserializeAndSet_WithGetter() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);
        Object toModify = mock(Object.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);

        when(p.getCurrentToken()).thenReturn(JsonToken.START_OBJECT);
        when(setterlessProperty.getGetter().invoke(instance, (Object[])null)).thenReturn(toModify);
        when(setterlessProperty.getValueDeserializer()).thenReturn(valueDeserializer);

        setterlessProperty.deserializeAndSet(p, ctxt, instance);

        verify(valueDeserializer).deserialize(p, ctxt, toModify);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        JsonParser p = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        setterlessProperty.deserializeSetAndReturn(p, ctxt, instance);

        // Assert no exception thrown
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

        assertEquals(instance, result);
    }
}