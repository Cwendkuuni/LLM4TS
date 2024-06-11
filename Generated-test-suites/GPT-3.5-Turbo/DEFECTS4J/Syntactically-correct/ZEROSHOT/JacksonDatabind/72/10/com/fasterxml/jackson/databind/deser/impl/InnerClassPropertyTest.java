import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class InnerClassPropertyTest {

    @Mock
    private SettableBeanProperty delegate;
    @Mock
    private Constructor<?> constructor;
    @Mock
    private AnnotatedConstructor annotatedConstructor;
    @Mock
    private JsonDeserializer<?> deserializer;
    @Mock
    private PropertyName newName;
    @Mock
    private Object bean;
    @Mock
    private JsonParser jsonParser;
    @Mock
    private DeserializationContext deserializationContext;
    @Mock
    private Object value;

    private InnerClassProperty innerClassProperty;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        innerClassProperty = new InnerClassProperty(delegate, constructor);
    }

    @Test
    public void testConstructor() {
        assertEquals(delegate, innerClassProperty._delegate);
        assertEquals(constructor, innerClassProperty._creator);
    }

    @Test
    public void testConstructorWithAnnotatedConstructor() {
        InnerClassProperty src = new InnerClassProperty(delegate, constructor);
        src._annotated = annotatedConstructor;

        InnerClassProperty result = new InnerClassProperty(src, annotatedConstructor);

        assertEquals(src, result);
        assertNull(result._annotated);
        assertEquals(annotatedConstructor, result._creator);
    }

    @Test
    public void testConstructorWithDeserializer() {
        InnerClassProperty src = new InnerClassProperty(delegate, constructor);

        InnerClassProperty result = new InnerClassProperty(src, deserializer);

        assertEquals(src, result);
        assertEquals(delegate.withValueDeserializer(deserializer), result._delegate);
        assertEquals(constructor, result._creator);
    }

    @Test
    public void testConstructorWithNewName() {
        InnerClassProperty src = new InnerClassProperty(delegate, constructor);

        InnerClassProperty result = new InnerClassProperty(src, newName);

        assertEquals(src, result);
        assertEquals(delegate.withName(newName), result._delegate);
        assertEquals(constructor, result._creator);
    }

    @Test
    public void testWithName() {
        InnerClassProperty result = innerClassProperty.withName(newName);

        assertEquals(innerClassProperty, result);
        assertEquals(delegate.withName(newName), result._delegate);
        assertEquals(constructor, result._creator);
    }

    @Test
    public void testWithValueDeserializer() {
        InnerClassProperty result = innerClassProperty.withValueDeserializer(deserializer);

        assertEquals(innerClassProperty, result);
        assertEquals(delegate.withValueDeserializer(deserializer), result._delegate);
        assertEquals(constructor, result._creator);
    }

    @Test
    public void testAssignIndex() {
        innerClassProperty.assignIndex(1);

        verify(delegate).assignIndex(1);
    }

    @Test
    public void testGetPropertyIndex() {
        when(delegate.getPropertyIndex()).thenReturn(1);

        int result = innerClassProperty.getPropertyIndex();

        assertEquals(1, result);
        verify(delegate).getPropertyIndex();
    }

    @Test
    public void testGetAnnotation() {
        Class<Annotation> acls = Annotation.class;
        Annotation annotation = mock(Annotation.class);
        when(delegate.getAnnotation(acls)).thenReturn(annotation);

        Annotation result = innerClassProperty.getAnnotation(acls);

        assertEquals(annotation, result);
        verify(delegate).getAnnotation(acls);
    }

    @Test
    public void testGetMember() {
        AnnotatedMember annotatedMember = mock(AnnotatedMember.class);
        when(delegate.getMember()).thenReturn(annotatedMember);

        AnnotatedMember result = innerClassProperty.getMember();

        assertEquals(annotatedMember, result);
        verify(delegate).getMember();
    }

    @Test
    public void testDeserializeAndSetWithNull() throws IOException {
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        Object nullValue = new Object();
        when(deserializationContext.getNullValueDeserializer()).thenReturn(deserializer);
        when(deserializer.getNullValue(deserializationContext)).thenReturn(nullValue);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(deserializer).getNullValue(deserializationContext);
        verify(delegate).set(bean, nullValue);
    }

    @Test
    public void testDeserializeAndSetWithTypeDeserializer() throws IOException {
        JsonToken jsonToken = JsonToken.VALUE_STRING;
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(delegate.getDeserializer()).thenReturn(deserializer);
        when(innerClassProperty._valueTypeDeserializer).thenReturn(deserializer);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(innerClassProperty._valueTypeDeserializer).deserializeWithType(jsonParser, deserializationContext, innerClassProperty);
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        JsonToken jsonToken = JsonToken.VALUE_NUMBER_INT;
        when(jsonParser.getCurrentToken()).thenReturn(jsonToken);
        when(constructor.newInstance(bean)).thenReturn(value);
        when(innerClassProperty._creator.getDeclaringClass()).thenReturn(getClass());
        when(delegate.getDeserializer()).thenReturn(deserializer);

        innerClassProperty.deserializeAndSet(jsonParser, deserializationContext, bean);

        verify(constructor).newInstance(bean);
        verify(deserializer).deserialize(jsonParser, deserializationContext, value);
        verify(delegate).set(bean, value);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object resultValue = new Object();
        when(jsonParser.getCurrentToken()).thenReturn(JsonToken.VALUE_NULL);
        when(deserializationContext.getNullValueDeserializer()).thenReturn(deserializer);
        when(deserializer.getNullValue(deserializationContext)).thenReturn(nullValue);
        when(delegate.setAndReturn(bean, nullValue)).thenReturn(resultValue);

        Object result = innerClassProperty.deserializeSetAndReturn(jsonParser, deserializationContext, bean);

        assertEquals(resultValue, result);
        verify(deserializer).getNullValue(deserializationContext);
        verify(delegate).setAndReturn(bean, nullValue);
    }

    @Test
    public void testSet() throws IOException {
        innerClassProperty.set(bean, value);

        verify(delegate).set(bean, value);
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object resultValue = new Object();
        when(delegate.setAndReturn(bean, value)).thenReturn(resultValue);

        Object result = innerClassProperty.setAndReturn(bean, value);

        assertEquals(resultValue, result);
        verify(delegate).setAndReturn(bean, value);
    }

    @Test
    public void testReadResolve() {
        InnerClassProperty result = innerClassProperty.readResolve();

        assertEquals(innerClassProperty, result);
        assertEquals(new InnerClassProperty(innerClassProperty, innerClassProperty._annotated), result);
    }

    @Test
    public void testWriteReplaceWithAnnotatedConstructor() {
        InnerClassProperty result = innerClassProperty.writeReplace();

        assertEquals(innerClassProperty, result);
        assertEquals(new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, constructor, null, null)), result);
    }

    @Test
    public void testWriteReplaceWithoutAnnotatedConstructor() {
        innerClassProperty._annotated = null;

        InnerClassProperty result = innerClassProperty.writeReplace();

        assertEquals(innerClassProperty, result);
        assertEquals(new InnerClassProperty(innerClassProperty, new AnnotatedConstructor(null, constructor, null, null)), result);
    }
}