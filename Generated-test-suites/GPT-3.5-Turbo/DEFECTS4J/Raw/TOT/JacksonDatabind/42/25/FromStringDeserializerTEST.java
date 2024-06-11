package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Currency;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Pattern;

import static org.mockito.Mockito.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer<TestClass> deserializer;
    private JsonParser mockParser;
    private DeserializationContext mockContext;

    static class TestClass {
    }

    @Before
    public void setUp() {
        deserializer = mock(FromStringDeserializer.class, CALLS_REAL_METHODS);
        mockParser = mock(JsonParser.class);
        mockContext = mock(DeserializationContext.class);
    }

    @Test
    public void deserialize_whenValueTypeIsFile_shouldReturnFileInstance() throws IOException {
        when(deserializer._deserialize(anyString(), eq(mockContext)))
                .thenReturn(new File("test"));

        JsonToken mockToken = mock(JsonToken.class);
        when(mockToken.isScalarValue()).thenReturn(true);
        when(mockToken.name()).thenReturn("VALUE_STRING");

        when(mockParser.getCurrentToken()).thenReturn(mockToken);
        when(mockParser.getValueAsString()).thenReturn("test");

        TestClass result = deserializer.deserialize(mockParser, mockContext);

        verify(mockParser).getValueAsString();
        verify(deserializer, times(1))._deserialize(eq("test"), eq(mockContext));
        verify(mockContext, never()).weirdStringException(anyString(), any(Class.class), anyString());
        verify(mockToken, never()).name();
        verify(mockToken, never()).isScalarValue();
        verify(mockParser, never()).getEmbeddedObject();
        verify(mockContext, never()).wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString());
        verify(mockContext, never()).mappingException(any(Class.class));

        assert result instanceof File;
    }

    @Test
    public void deserialize_whenValueIsEmptyString_shouldReturnNull() throws IOException {
        when(deserializer._deserializeFromEmptyString())
                .thenReturn(null);

        JsonToken mockToken = mock(JsonToken.class);
        when(mockToken.isScalarValue()).thenReturn(false);
        when(mockToken.name()).thenReturn("VALUE_NULL");

        when(mockParser.getCurrentToken()).thenReturn(mockToken);
        when(mockParser.getValueAsString()).thenReturn("");

        TestClass result = deserializer.deserialize(mockParser, mockContext);

        verify(mockParser).getValueAsString();
        verify(deserializer)._deserialize(anyString(), eq(mockContext));
        verify(mockContext, never()).weirdStringException(anyString(), any(Class.class), anyString());
        verify(mockToken, never()).name();
        verify(mockToken, never()).isScalarValue();
        verify(mockParser, never()).getEmbeddedObject();
        verify(mockContext, never()).wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString());
        verify(mockContext, never()).mappingException(any(Class.class));

        assert result == null;
    }

    @Test
    public void deserialize_whenValueIsNull_shouldReturnNull() throws IOException {
        when(deserializer._deserializeFromEmptyString())
                .thenReturn(null);

        JsonToken mockToken = mock(JsonToken.class);
        when(mockToken.isScalarValue()).thenReturn(false);
        when(mockToken.name()).thenReturn("VALUE_NULL");

        when(mockParser.getCurrentToken()).thenReturn(mockToken);
        when(mockParser.getEmbeddedObject()).thenReturn(null);

        TestClass result = deserializer.deserialize(mockParser, mockContext);

        verify(mockParser, never()).getValueAsString();
        verify(deserializer, never())._deserialize(anyString(), eq(mockContext));
        verify(mockContext, never()).weirdStringException(anyString(), any(Class.class), anyString());
        verify(mockToken, never()).name();
        verify(mockToken, never()).isScalarValue();
        verify(mockParser).getEmbeddedObject();
        verify(mockContext, never()).wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString());
        verify(mockContext, never()).mappingException(any(Class.class));

        assert result == null;
    }

    @Test
    public void deserialize_whenValueIsNotAString_shouldThrowMappingException() throws IOException {
        JsonToken mockToken = mock(JsonToken.class);
        when(mockToken.isScalarValue()).thenReturn(false);
        when(mockToken.name()).thenReturn("VALUE_BOOLEAN");

        when(mockParser.getCurrentToken()).thenReturn(mockToken);
        when(mockParser.getValueAsString()).thenReturn("test");

        try {
            deserializer.deserialize(mockParser, mockContext);
        } catch (JsonMappingException e) {
            assert e.getMessage().contains("java.lang.String")
                    && e.getMessage().contains("VALUE_BOOLEAN");
        }

        verify(mockParser, never()).getValueAsString();
        verify(deserializer, never())._deserialize(anyString(), eq(mockContext));
        verify(mockContext, never()).weirdStringException(anyString(), any(Class.class), anyString());
        verify(mockToken, never()).name();
        verify(mockToken, never()).isScalarValue();
        verify(mockParser, never()).getEmbeddedObject();
        verify(mockContext, never()).wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString());
        verify(mockContext).mappingException(eq(TestClass.class));
    }

    @Test
    public void deserialize_whenDeserializationFails_shouldThrowMappingException() throws IOException {
        Exception exception = new Exception("Test Exception");

        when(deserializer._deserialize(anyString(), eq(mockContext)))
                .thenThrow(exception);

        JsonToken mockToken = mock(JsonToken.class);
        when(mockToken.isScalarValue()).thenReturn(true);
        when(mockToken.name()).thenReturn("VALUE_STRING");

        when(mockParser.getCurrentToken()).thenReturn(mockToken);
        when(mockParser.getValueAsString()).thenReturn("test");

        try {
            deserializer.deserialize(mockParser, mockContext);
        } catch (JsonMappingException e) {
            assert e.getMessage().contains("Test Exception");
        }

        verify(mockParser).getValueAsString();
        verify(deserializer)._deserialize(eq("test"), eq(mockContext));
        verify(mockContext, never()).weirdStringException(anyString(), any(Class.class), anyString());
        verify(mockToken).name();
        verify(mockToken).isScalarValue();
        verify(mockParser, never()).getEmbeddedObject();
        verify(mockContext, never()).wrongTokenException(any(JsonParser.class), any(JsonToken.class), anyString());
        verify(mockContext).mappingException(eq(TestClass.class));
    }
}