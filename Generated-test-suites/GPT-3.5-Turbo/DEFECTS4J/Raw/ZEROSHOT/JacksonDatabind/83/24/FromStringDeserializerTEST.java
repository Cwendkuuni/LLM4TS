package com.fasterxml.jackson.databind.deser.std;

import org.junit.*;
import static org.junit.Assert.*;

public class FromStringDeserializerTest {

    private FromStringDeserializer.Std deserializer;
    private JsonParser mockParser;
    private DeserializationContext mockContext;

    @Before
    public void setUp() {
        deserializer = new FromStringDeserializer.Std(Object.class, 0);
        mockParser = Mockito.mock(JsonParser.class);
        mockContext = Mockito.mock(DeserializationContext.class);
    }

    @Test
    public void testDeserialize_withValidInput() throws Exception {
        // Arrange
        Mockito.when(mockParser.getValueAsString()).thenReturn("test");
        
        // Act
        Object result = deserializer.deserialize(mockParser, mockContext);
        
        // Assert
        assertEquals(result, "test");
    }

    @Test(expected = JsonMappingException.class)
    public void testDeserialize_withInvalidInput() throws Exception {
        // Arrange
        Mockito.when(mockParser.getValueAsString()).thenReturn("");
        
        // Act
        deserializer.deserialize(mockParser, mockContext);
        
        // Assert (Exception expected)
    }

    @Test
    public void test_deserializeFromArray() throws Exception {
        // Arrange
        Mockito.when(mockParser.getCurrentToken()).thenReturn(JsonToken.START_ARRAY);
        
        // Act
        Object result = deserializer._deserializeFromArray(mockParser, mockContext);
        
        // Assert
        assertNull(result);
    }

    @Test
    public void test_deserializeEmbedded_withNullValue() throws Exception {
        // Arrange
        Object ob = null;

        // Act
        Object result = deserializer._deserializeEmbedded(ob, mockContext);
        
        // Assert
        assertNull(result);
    }

    @Test(expected = JsonMappingException.class)
    public void test_deserializeEmbedded_withInvalidType() throws Exception {
        // Arrange
        Object ob = new Object();

        // Act
        deserializer._deserializeEmbedded(ob, mockContext);
        
        // Assert (Exception expected)
    }

    @Test
    public void test_deserializeFromEmptyString_withURI() throws Exception {
        // Arrange
        deserializer = new FromStringDeserializer.Std(Object.class, FromStringDeserializer.Std.STD_URI);

        // Act
        Object result = deserializer._deserializeFromEmptyString();
        
        // Assert
        assertEquals(result, URI.create(""));
    }

    @Test
    public void test_deserializeFromEmptyString_withLocale() throws Exception {
        // Arrange
        deserializer = new FromStringDeserializer.Std(Object.class, FromStringDeserializer.Std.STD_LOCALE);

        // Act
        Object result = deserializer._deserializeFromEmptyString();
        
        // Assert
        assertEquals(result, Locale.ROOT);
    }

    @Test
    public void test_deserializeFromEmptyString_withStringBuilder() throws Exception {
        // Arrange
        deserializer = new FromStringDeserializer.Std(Object.class, FromStringDeserializer.Std.STD_STRING_BUILDER);

        // Act
        Object result = deserializer._deserializeFromEmptyString();
        StringBuilder expected = new StringBuilder();
        
        // Assert
        assertEquals(result, expected);
    }
}