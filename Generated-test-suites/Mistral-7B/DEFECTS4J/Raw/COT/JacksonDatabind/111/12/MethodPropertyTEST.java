import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;

public class MethodPropertyTest {

    private Object testInstance;
    private JsonParser mockJsonParser;
    private DeserializationContext mockContext;
    private Object value;

    @Before
    public void setup() throws Exception {
        mockJsonParser = Mockito.mock(JsonParser.class);
        mockContext = Mockito.mock(DeserializationContext.class);
        testInstance = new Object();
        value = new Object();
    }

    @Test
    public void testMethodPropertyConstructor() {
        BeanPropertyDefinition propDef = Mockito.mock(BeanPropertyDefinition.class);
        JavaType type = Mockito.mock(JavaType.class);
        TypeDeserializer typeDeser = Mockito.mock(TypeDeserializer.class);
        Annotations contextAnnotations = Mockito.mock(Annotations.class);
        AnnotatedMethod method = Mockito.mock(AnnotatedMethod.class);

        new MethodProperty(propDef, type, typeDeser, contextAnnotations, method);

        verify(method).getAnnotated();
        verifyNoMoreInteractions(propDef, type, typeDeser, contextAnnotations);
    }

    @Test
    public void testMethodPropertyWithName() {
        MethodProperty methodProperty = new MethodProperty(Mockito.mock(MethodProperty.class), new PropertyName("name"));

        assertEquals("name", methodProperty.getName());

        verifyNoMoreInteractions(Mockito.mock(JavaType.class), Mockito.mock(JsonDeserializer.class), Mockito.mock(NullValueProvider.class));
    }

    @Test
    public void testMethodPropertyWithValueDeserializer() {
        MethodProperty methodProperty = new MethodProperty(Mockito.mock(MethodProperty.class), Mockito.mock(JsonDeserializer.class), Mockito.mock(NullValueProvider.class));

        assertEquals(Mockito.mock(JsonDeserializer.class), methodProperty.getValueDeserializer());

        verifyNoMoreInteractions(Mockito.mock(JavaType.class), Mockito.mock(PropertyName.class));
    }

    @Test
    public void testMethodPropertyWithNullProvider() {
        MethodProperty methodProperty = new MethodProperty(Mockito.mock(MethodProperty.class), Mockito.mock(JsonDeserializer.class), Mockito.mock(NullValueProvider.class));

        assertEquals(Mockito.mock(NullValueProvider.class), methodProperty.getNullProvider());

        verifyNoMoreInteractions(Mockito.mock(JavaType.class), Mockito.mock(PropertyName.class), Mockito.mock(JsonDeserializer.class));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("testValue");
        when(mockContext.findBeanProperty(_annotated)).thenReturn(this);

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), _annotated);

        methodProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);

        verify(testInstance).getClass();
        verify(_setter).invoke(testInstance, eq("testValue"));
        verifyNoMoreInteractions(mockJsonParser, mockContext);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullValueWithSkipNulls() throws IOException {
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.findBeanProperty(_annotated)).thenReturn(this);

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), _annotated);
        methodProperty._skipNulls = true;

        methodProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);

        verifyNoMoreInteractions(mockJsonParser, mockContext, testInstance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeAndSetNullValueWithoutSkipNulls() throws IOException {
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.findBeanProperty(_annotated)).thenReturn(this);

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), _annotated);

        methodProperty.deserializeAndSet(mockJsonParser, mockContext, testInstance);

        fail("Expected IOException");
    }

    @Test
    public void testDeserializeSetAndReturnNullValueWithSkipNulls() throws IOException {
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.findBeanProperty(_annotated)).thenReturn(this);

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), _annotated);
        methodProperty._skipNulls = true;

        Object result = methodProperty.deserializeSetAndReturn(mockJsonParser, mockContext, testInstance);

        assertNull(result);
        verifyNoMoreInteractions(mockJsonParser, mockContext, testInstance);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("testValue");
        when(mockContext.findBeanProperty(_annotated)).thenReturn(this);

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), _annotated);

        Object result = methodProperty.deserializeSetAndReturn(mockJsonParser, mockContext, testInstance);

        assertSame(testInstance, result);
        verify(_setter).invoke(testInstance, eq("testValue"));
        verifyNoMoreInteractions(mockJsonParser, mockContext, testInstance);
    }

    @Test(expected = IOException.class)
    public void testDeserializeSetAndReturnNullValueWithoutSkipNulls() throws IOException {
        when(mockJsonParser.getValueAsToken()).thenReturn(JsonToken.VALUE_NULL);
        when(mockContext.findBeanProperty(_annotated)).thenReturn(this);

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(BeanPropertyDefinition.class), Mockito.mock(JavaType.class), Mockito.mock(TypeDeserializer.class), Mockito.mock(Annotations.class), _annotated);

        Object result = methodProperty.deserializeSetAndReturn(mockJsonParser, mockContext, testInstance);

        fail("Expected IOException");
    }

    @Test
    public void testSet() throws Exception {
        Object testInstance = new Object();

        Method method = Mockito.mock(Method.class);
        when(method.invoke(testInstance, any())).thenAnswer(i -> i.getArguments()[0]);
        when(method.getName()).thenReturn("setTestMethod");

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(MethodProperty.class), method);

        methodProperty.set(testInstance, value);

        assertEquals(value, testInstance.getClass().getDeclaredMethod("getTestMethod").invoke(testInstance));
        verify(testInstance).getClass();
        verify(_setter).invoke(testInstance, value);
        verifyNoMoreInteractions(mockJsonParser, mockContext);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        Object testInstance = new Object();

        Method method = Mockito.mock(Method.class);
        when(method.invoke(testInstance, any())).thenAnswer(i -> i.getArguments()[0]);
        when(method.getName()).thenReturn("setTestMethod");

        MethodProperty methodProperty = new MethodProperty(Mockito.mock(MethodProperty.class), method);

        Object result = methodProperty.setAndReturn(testInstance, value);

        assertSame(value, result);
        assertEquals(value, testInstance.getClass().getDeclaredMethod("getTestMethod").invoke(testInstance));
        verify(testInstance).getClass();
        verify(_setter).invoke(testInstance, value);
        verifyNoMoreInteractions(mockJsonParser, mockContext);
    }

    @Test
    public void testReadResolve() throws Exception {
        MethodProperty methodProperty = new MethodProperty(Mockito.mock(MethodProperty.class), _annotated.getAnnotated());

        assertEquals(methodProperty, methodProperty.readResolve());

        verifyNoMoreInteractions(Mockito.mock(AnnotatedMethod.class));
    }
}