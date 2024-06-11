package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.ContextualDeserializer;
import com.fasterxml.jackson.databind.deser.StandardDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ObjectBuffer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.mockito.Mockito.*;

/**
 * Test class for {@link StringArrayDeserializer}
 */
@JacksonStdImpl
public class StringArrayDeserializerTest {

    private JsonParser mockJsonParser;
    private DeserializationContext mockContext;
    private JsonDeserializer<String> mockElementDeserializer;
    private ObjectBuffer mockObjectBuffer;
    private TypeDeserializer mockTypeDeserializer;

    @Before
    public void setUp() {
        mockJsonParser = Mockito.mock(JsonParser.class);
        mockContext = Mockito.mock(DeserializationContext.class);
        mockElementDeserializer = Mockito.mock(JsonDeserializer.class);
        mockObjectBuffer = Mockito.mock(ObjectBuffer.class);
        mockTypeDeserializer = Mockito.mock(TypeDeserializer.class);
    }

    @Test
    public void testDefaultDeserialization() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");
        when(mockContext.leaseObjectBuffer()).thenReturn(mockObjectBuffer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[]{"test"}, result);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockJsonParser, times(1)).getText();
        verify(mockContext, times(1)).returnObjectBuffer(mockObjectBuffer);
    }

    @Test
    public void testCustomDeserialization() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");
        when(mockContext.leaseObjectBuffer()).thenReturn(mockObjectBuffer);
        when(mockElementDeserializer.deserialize(mockJsonParser, mockContext)).thenReturn("customTest");

        StringArrayDeserializer deserializer = new StringArrayDeserializer(mockElementDeserializer);

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[]{"customTest"}, result);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockJsonParser, times(1)).getText();
        verify(mockContext, times(1)).returnObjectBuffer(mockObjectBuffer);
        verify(mockElementDeserializer, times(1)).deserialize(mockJsonParser, mockContext);
    }

    @Test(expected = IOException.class)
    public void testNonArray() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(false);
        when(mockJsonParser.getText()).thenReturn("test");

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        deserializer.deserialize(mockJsonParser, mockContext);
    }

    @Test
    public void testContextualDeserialization() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");
        when(mockContext.findContextualValueDeserializer(eq(String.class), any())).thenReturn(mockElementDeserializer);

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserialize(mockJsonParser, mockContext);

        assertArrayEquals(new String[]{"test"}, result);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockJsonParser, times(1)).getText();
        verify(mockContext, times(1)).findContextualValueDeserializer(eq(String.class), any());
        verify(mockElementDeserializer, never()).deserialize(mockJsonParser, mockContext);
    }

    @Test
    public void testTypeDeserialization() throws IOException {
        when(mockJsonParser.isExpectedStartArrayToken()).thenReturn(true);
        when(mockJsonParser.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(mockJsonParser.getText()).thenReturn("test");
        when(mockTypeDeserializer.deserializeTypedFromArray(mockJsonParser, mockContext)).thenReturn(new String[]{"test"});

        StringArrayDeserializer deserializer = new StringArrayDeserializer();

        String[] result = deserializer.deserializeWithType(mockJsonParser, mockContext, mockTypeDeserializer);

        assertArrayEquals(new String[]{"test"}, result);
        verify(mockJsonParser, times(2)).nextToken();
        verify(mockJsonParser, times(1)).getText();
        verify(mockContext, never()).returnObjectBuffer(mockObjectBuffer);
        verify(mockTypeDeserializer, times(1)).deserializeTypedFromArray(mockJsonParser, mockContext);
    }
}