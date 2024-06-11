package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.fasterxml.jackson.databind.util.TypeUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class MethodPropertyTest {

    private static final MethodProperty.Builder TEST_BUILDER = new MethodProperty.Builder(
            new BeanPropertyDefinition(null, "test", JsonType.VALUE_STRING_TYPE),
            JsonType.VALUE_STRING_TYPE, null,
            Annotations.findAnnotation(JsonProperty.class, null),
            mock(AnnotatedMethod.class));

    private static final MethodProperty TEST_PROPERTY = TEST_BUILDER.build();

    private Object testInstance;
    private Object testValue;
    private JsonParser testParser;
    private DeserializationContext testContext;
    private JsonDeserializer<Object> testDeserializer;
    private NullValueProvider testNullProvider;
    private TypeDeserializer testTypeDeserializer;
    private Method testSetter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        testInstance = mock(Object.class);
        testValue = "testValue";
        testParser = mock(JsonParser.class);
        testContext = mock(DeserializationContext.class);
        testDeserializer = mock(JsonDeserializer.class);
        testNullProvider = mock(NullValueProvider.class);
        testTypeDeserializer = mock(TypeDeserializer.class);
        testSetter = mock(Method.class);

        when(testSetter.getName()).thenReturn("setTest");
        when(testSetter.getParameterTypes()[0]).thenReturn(JsonType.VALUE_STRING_TYPE);

        when(testParser.getCurrentName()).thenReturn("test");
        when(testParser.getCurrentValueAsString()).thenReturn(testValue);
        when(testParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        when(testContext.findDeserializerForPropertyName(eq("test"))).thenReturn(testDeserializer);
        when(testContext.findNullValueProvider()).thenReturn(testNullProvider);
        when(testContext.findTypeDeserializer(any(DeserializationConfig.class), eq(JsonType.VALUE_STRING_TYPE)))
                .thenReturn(testTypeDeserializer);
        when(testContext.isEnabled(any(MapperFeature.class))).thenReturn(true);
        when(testContext.findBeanUtil()).thenReturn(BeanUtil.instance);
        when(testContext.findTypeUtil()).thenReturn(TypeUtil.instance);
    }

    @Test
    public void testConstructor() {
        assertSame(TEST_BUILDER._setter, TEST_PROPERTY._setter);
        assertSame(TEST_BUILDER._annotated, TEST_PROPERTY._annotated);
    }

    @Test
    public void testAnnotations() {
        JsonProperty annotation = Annotations.findAnnotation(JsonProperty.class, TEST_PROPERTY._annotated);
        assertSame(annotation, TEST_PROPERTY.getAnnotation(JsonProperty.class));
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        when(testDeserializer.deserialize(testParser, testContext)).thenReturn(testValue);
        when(testTypeDeserializer.deserialize(testParser, testContext, any(TypeDeserializer.class)))
                .thenReturn(testValue);

        TEST_PROPERTY.deserializeAndSet(testParser, testContext, testInstance);

        verify(testInstance).setTest(testValue);
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        objectInPlace(testParser);
        when(testDeserializer.deserialize(testParser, testContext)).thenReturn(testValue);
        when(testTypeDeserializer.deserialize(testParser, testContext, any(TypeDeserializer.class)))
                .thenReturn(testValue);

        Object result = TEST_PROPERTY.deserializeSetAndReturn(testParser, testContext, testInstance);

        assertSame(testInstance, result);
    }

    @Test
    public void testSet() throws IOException {
        when(testDeserializer.deserialize(testParser, testContext)).thenReturn(testValue);
        when(testTypeDeserializer.deserialize(testParser, testContext, any(TypeDeserializer.class)))
                .thenReturn(testValue);

        TEST_PROPERTY.set(testInstance, testValue);

        verify(testInstance).setTest(testValue);
    }

    @Test
    public void testSetNull() throws IOException {
        when(testContext.findNullValueProvider()).thenReturn(new NullValueProviderDefault());

        TEST_PROPERTY.set(testInstance, null);

        verify(testInstance, never()).setTest(null);
    }

    @Test
    public void testSetWithTypeDeserializer() throws IOException {
        when(testDeserializer.deserializeWithType(testParser, testContext, eq(JsonType.VALUE_STRING_TYPE)))
                .thenReturn("testValueTypeDeserializer");

        TEST_PROPERTY.set(testInstance, testValue);

        verify(testInstance).setTest("testValueTypeDeserializer");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetNullParser() throws IOException {
        TEST_PROPERTY.deserializeAndSet(null, testContext, testInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeSetAndReturnNullParser() throws IOException {
        TEST_PROPERTY.deserializeSetAndReturn(null, testContext, testInstance);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetNullParser() throws IOException {
        TEST_PROPERTY.set(null, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullContext() throws IOException {
        TEST_PROPERTY.set(testInstance, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullInstance() throws IOException {
        TEST_PROPERTY.set(null, testValue);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullValue() throws IOException {
        TEST_PROPERTY.set(testInstance, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetWithNullSetter() throws IOException {
        TEST_PROPERTY = new MethodProperty(TEST_PROPERTY, null);
        TEST_PROPERTY.set(testInstance, testValue);
    }

    @Test(expected = IllegalAccessException.class)
    public void testSetWithIllegalAccess() throws IOException {
        doThrow(new IllegalAccessException("test")).when(testSetter);

        TEST_PROPERTY.set(testInstance, testValue);
    }

    @Test(expected = InvocationTargetException.class)
    public void testSetWithException() throws IOException {
        doThrow(new RuntimeException("test")).when(testSetter).invoke(testInstance, testValue);

        TEST_PROPERTY.set(testInstance, testValue);
    }

    @Test
    public void testReadResolve() throws IOException {
        MethodProperty result = TEST_PROPERTY.readResolve();
        assertSame(TEST_PROPERTY, result);
    }
}

class JsonParserMock extends JsonParser {
    @Override
    public void close() throws IOException {
        // Do nothing
    }

    @Override
    public JsonToken nextToken() throws IOException {
        JsonToken token = super.nextToken();
        objectInPlace(this);
        return token;
    }

    @Override
    public String getText() throws IOException, JsonProcessingException {
        String text = super.getText();
        objectInPlace(this);
        return text;
    }
}

class ObjectInPlace {
    static void objectInPlace(Object obj) {
        if (obj instanceof JsonParser) {
            JsonParser jsonParser = (JsonParser) obj;
            jsonParser.setCodec(null);
            jsonParser.setContext(null);
            jsonParser.setParserConfig(null);
            jsonParser.setRootValueContainer(null);
            jsonParser.setStreamCodecs(null);
            jsonParser.setEventReader(null);
            jsonParser.setCodecProvider(null);
            jsonParser.setFieldHandler(null);
            jsonParser.setCurrentLocation(null);
            jsonParser.setBaseHaltException(null);
            jsonParser.setCurrentName(null);
            jsonParser.setCurrentValue(null);
            jsonParser.setConfigOverrides(null);
            jsonParser.setParserState(null);
            jsonParser.setParserFeatures(null);
        }
    }
}

class MethodPropertyTest$$JsonDeserializerMock extends JsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return null;
    }

    @Override
    public Object deserializeWithType(JsonParser p, DeserializationContext ctxt,
            TypeDeserializer vtd) throws IOException {
        return null;
    }
}

class MethodPropertyTest$$NullValueProviderMock extends NullValueProvider {
    @Override
    public Object getNullValue(DeserializationContext ctxt) {
        return null;
    }
}

class MethodProperty$$Builder {
    private final BeanPropertyDefinition _beanPropertyDefinition;
    private final Type _valueType;
    private final TypeDeserializer _valueTypeDeserializer;
    private final Annotations _annotations;
    private final AnnotatedMethod _annotatedMethod;

    public MethodProperty$$Builder(BeanPropertyDefinition beanPropertyDefinition,
            Type valueType, TypeDeserializer valueTypeDeserializer,
            Annotations annotations, AnnotatedMethod annotatedMethod) {
        this._beanPropertyDefinition = beanPropertyDefinition;
        this._valueType = valueType;
        this._valueTypeDeserializer = valueTypeDeserializer;
        this._annotations = annotations;
        this._annotatedMethod = annotatedMethod;
    }

    public MethodProperty build() {
        return new MethodProperty(this);
    }
}