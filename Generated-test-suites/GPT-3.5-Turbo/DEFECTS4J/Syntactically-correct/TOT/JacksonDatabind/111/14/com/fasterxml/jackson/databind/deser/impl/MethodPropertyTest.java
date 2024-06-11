import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.*;
import com.fasterxml.jackson.databind.deser.std.NullValueProvider;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@RunWith(MockitoJUnitRunner.class)
public class MethodPropertyTest {

    @Mock
    JsonParser jsonParserMock;

    @Mock
    DeserializationContext deserializationContextMock;

    @Mock
    Object instanceMock;

    @Mock
    JsonProcessingException jsonProcessingExceptionMock;

    @Mock
    NullValueProvider<Object> nullValueProviderMock;

    @Mock
    JavaType javaTypeMock;

    @Mock
    SettableBeanProperty settableBeanPropertyMock;

    @SuppressWarnings("rawtypes")
    @Mock
    JsonDeserializer jsonDeserializerMock;

    @Mock
    PropertyName propertyNameMock;

    @Mock
    AnnotatedMethod annotatedMethodMock;

    @Mock
    Annotations annotationsMock;

    @SuppressWarnings("rawtypes")
    @Mock
    TypeDeserializer typeDeserializerMock;

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeAndSet_WithNullValue_DoesNothingWhenSkipNullsIsTrue() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = true;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        methodProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(0)).invoke(Mockito.any(), Mockito.any());
        Mockito.verifyZeroInteractions(nullValueProviderMock);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeAndSet_WithNullValue_ReturnsNullValueWhenSkipNullsIsFalse() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = false;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        Mockito.when(nullValueProviderMock.getNullValue(Mockito.any())).thenReturn(null);

        methodProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(1)).invoke(Mockito.any(), Mockito.isNull());
        Mockito.verify(nullValueProviderMock, Mockito.times(1)).getNullValue(Mockito.any());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeAndSet_WithNonNullValue_DeserializesAndSetsValue() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = false;
        methodProperty._valueDeserializer = jsonDeserializerMock;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn("test_value");

        methodProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(1)).invoke(Mockito.any(), Matchers.eq("test_value"));
        Mockito.verifyZeroInteractions(nullValueProviderMock);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeAndSet_WithNonNullValue_DeserializesAndSetsNullValueWhenDeserializationReturnsNull() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = false;
        methodProperty._valueDeserializer = jsonDeserializerMock;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn(null);
        Mockito.when(nullValueProviderMock.getNullValue(Mockito.any())).thenReturn(null);

        methodProperty.deserializeAndSet(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(1)).invoke(Mockito.any(), Mockito.isNull());
        Mockito.verify(nullValueProviderMock, Mockito.times(1)).getNullValue(Mockito.any());
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeSetAndReturn_WithNullValue_ReturnsInstanceWhenSkipNullsIsTrue() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = true;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);

        Object result = methodProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(0)).invoke(Mockito.any(), Mockito.any());
        Mockito.verifyZeroInteractions(nullValueProviderMock);

        Mockito.assertEquals(result, instanceMock);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeSetAndReturn_WithNullValue_ReturnsInstanceWhenSkipNullsIsFalse() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = false;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(true);
        Mockito.when(nullValueProviderMock.getNullValue(Mockito.any())).thenReturn(null);

        Object result = methodProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(1)).invoke(Mockito.any(), Mockito.isNull());
        Mockito.verify(nullValueProviderMock, Mockito.times(1)).getNullValue(Mockito.any());

        Mockito.assertEquals(result, instanceMock);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeSetAndReturn_WithNonNullValue_DeserializesSetsAndReturnsInstance() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = false;
        methodProperty._valueDeserializer = jsonDeserializerMock;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn("test_value");

        Object result = methodProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(1)).invoke(Mockito.any(), Matchers.eq("test_value"));
        Mockito.verifyZeroInteractions(nullValueProviderMock);

        Mockito.assertEquals(result, instanceMock);
    }

    @SuppressWarnings("rawtypes")
    @Test
    public void deserializeSetAndReturn_WithNonNullValue_DeserializesSetsNullValueAndReturnsInstanceWhenDeserializationReturnsNull() throws IOException {
        MethodProperty methodProperty = new MethodProperty(settableBeanPropertyMock, javaTypeMock, null, annotationsMock, annotatedMethodMock);
        methodProperty._nullProvider = nullValueProviderMock;
        methodProperty._skipNulls = false;
        methodProperty._valueDeserializer = jsonDeserializerMock;

        Mockito.when(jsonParserMock.hasToken(JsonToken.VALUE_NULL)).thenReturn(false);
        Mockito.when(jsonDeserializerMock.deserialize(jsonParserMock, deserializationContextMock)).thenReturn(null);
        Mockito.when(nullValueProviderMock.getNullValue(Mockito.any())).thenReturn(null);

        Object result = methodProperty.deserializeSetAndReturn(jsonParserMock, deserializationContextMock, instanceMock);

        Mockito.verify(jsonParserMock, Mockito.never()).nextValue();
        Mockito.verify(jsonParserMock, Mockito.never()).getCurrentName();
        Mockito.verify(jsonParserMock, Mockito.never()).isExpectedStartObjectToken();
        Mockito.verify(jsonParserMock, Mockito.never()).skipChildren();
        Mockito.verify(jsonParserMock, Mockito.never()).readString();

        Mockito.verifyMethod(_setter, Mockito.times(1)).invoke(Mockito.any(), Mockito.isNull());
        Mockito.verify(nullValueProviderMock, Mockito.times(1)).getNullValue(Mockito.any());

        Mockito.assertEquals(result, instanceMock);
    }

    // TODO: Add more comprehensive test cases for the other methods in MethodProperty class

}