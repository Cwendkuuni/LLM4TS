package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    private DefaultPrettyPrinter _defaultPrettyPrinter;
    private JsonGenerator _jsonGeneratorMock;
    private Indenter _objectIndenterMock;
    private Indenter _arrayIndenterMock;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        _jsonGeneratorMock = mock(JsonGenerator.class);
        _objectIndenterMock = mock(Indenter.class);
        _arrayIndenterMock = mock(Indenter.class);

        _defaultPrettyPrinter = new DefaultPrettyPrinter();
        _defaultPrettyPrinter.indentObjectsWith(_objectIndenterMock);
        _defaultPrettyPrinter.indentArraysWith(_arrayIndenterMock);
    }

    @Test
    public void testDefaultConstructor() {
        _defaultPrettyPrinter = new DefaultPrettyPrinter();
        _defaultPrettyPrinter.writeRootValueSeparator(_jsonGeneratorMock);
        verify(_jsonGeneratorMock, times(1)).writeRaw(' ');
    }

    @Test
    public void testConstructorWithRootSeparator() {
        _defaultPrettyPrinter = new DefaultPrettyPrinter(new SerializedString("customRootSeparator"));
        _defaultPrettyPrinter.writeRootValueSeparator(_jsonGeneratorMock);
        verify(_jsonGeneratorMock, times(1)).writeRaw("customRootSeparator");
    }

    @Test
    public void testWriteStartObject() {
        _defaultPrettyPrinter.writeStartObject(_jsonGeneratorMock);
        verify(_jsonGeneratorMock, times(1)).writeRaw('{');
    }

    @Test
    public void testWriteEndObject() throws IOException {
        _defaultPrettyPrinter.writeStartObject(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectEntrySeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValue(_jsonGeneratorMock, "fieldName", "fieldValue");
        _defaultPrettyPrinter.writeObjectEntrySeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValue(_jsonGeneratorMock, "secondFieldName", "secondFieldValue");
        _defaultPrettyPrinter.writeEndObject(_jsonGeneratorMock, 2);

        verify(_jsonGeneratorMock, times(1)).writeRaw('{');
        verify(_jsonGeneratorMock, times(2)).writeRaw(' ');
        verify(_jsonGeneratorMock, times(1)).writeRaw('}');
    }

    @Test
    public void testWriteStartArray() {
        _defaultPrettyPrinter.writeStartArray(_jsonGeneratorMock);
        verify(_jsonGeneratorMock, times(1)).writeRaw('[');
    }

    @Test
    public void testWriteEndArray() throws IOException {
        _defaultPrettyPrinter.writeStartArray(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeArrayValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeArrayValue(_jsonGeneratorMock, "value1");
        _defaultPrettyPrinter.writeArrayValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeArrayValue(_jsonGeneratorMock, "value2");
        _defaultPrettyPrinter.writeEndArray(_jsonGeneratorMock, 2);

        verify(_jsonGeneratorMock, times(1)).writeRaw('[');
        verify(_jsonGeneratorMock, times(2)).writeRaw(' ');
        verify(_jsonGeneratorMock, times(1)).writeRaw(']');
    }

    @Test(expected = JsonProcessingException.class)
    public void testWriteObjectFieldValueSeparatorWithEmptyString() throws IOException {
        _defaultPrettyPrinter.writeObjectFieldValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValue(_jsonGeneratorMock, "fieldName", "fieldValue");
    }

    @Test
    public void testWriteObjectFieldValueSeparatorWithSpaces() throws IOException {
        _defaultPrettyPrinter = new DefaultPrettyPrinter();
        _defaultPrettyPrinter.withSpacesInObjectEntries();
        _defaultPrettyPrinter.writeObjectFieldValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValue(_jsonGeneratorMock, "fieldName", "fieldValue");
        verify(_jsonGeneratorMock, times(1)).writeRaw("  ");
    }

    @Test
    public void testWriteObjectEntrySeparatorWithEmptyString() throws IOException {
        _defaultPrettyPrinter.writeObjectEntrySeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValue(_jsonGeneratorMock, "fieldName", "fieldValue");
    }

    @Test
    public void testWriteObjectEntrySeparatorWithSpaces() throws IOException {
        _defaultPrettyPrinter = new DefaultPrettyPrinter();
        _defaultPrettyPrinter.withSpacesInObjectEntries();
        _defaultPrettyPrinter.writeObjectEntrySeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValue(_jsonGeneratorMock, "fieldName", "fieldValue");
        verify(_jsonGeneratorMock, times(1)).writeRaw("\n  ");
    }

    @Test
    public void testWriteArrayValueSeparatorWithEmptyString() throws IOException {
        _defaultPrettyPrinter.writeArrayValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeArrayValue(_jsonGeneratorMock, "value1");
    }

    @Test
    public void testWriteArrayValueSeparatorWithSpaces() throws IOException {
        _defaultPrettyPrinter = new DefaultPrettyPrinter();
        _defaultPrettyPrinter.withSpacesInObjectEntries();
        _defaultPrettyPrinter.writeArrayValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeArrayValue(_jsonGeneratorMock, "value1");
        verify(_jsonGeneratorMock, times(1)).writeRaw("\n  ");
    }

    @Test
    public void testWriteIndentationWithObjectIndenter() throws IOException {
        when(_objectIndenterMock.isInline()).thenReturn(false);
        when(_objectIndenterMock.writeIndentation(_jsonGeneratorMock, anyInt()))
                .thenReturn(null);

        _defaultPrettyPrinter.writeObjectEntrySeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeObjectFieldValue(_jsonGeneratorMock, "fieldName", "fieldValue");
        verify(_objectIndenterMock, times(1)).writeIndentation(_jsonGeneratorMock, 1);
    }

    @Test
    public void testWriteIndentationWithArrayIndenter() throws IOException {
        when(_arrayIndenterMock.isInline()).thenReturn(false);
        when(_arrayIndenterMock.writeIndentation(_jsonGeneratorMock, anyInt()))
                .thenReturn(null);

        _defaultPrettyPrinter.writeArrayValueSeparator(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeArrayValue(_jsonGeneratorMock, "value1");
        verify(_arrayIndenterMock, times(1)).writeIndentation(_jsonGeneratorMock, 1);
    }

    @Test
    public void testWriteNullObject() throws IOException {
        _defaultPrettyPrinter.writeStartObject(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeNull(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeEndObject(_jsonGeneratorMock, 0);

        verify(_jsonGeneratorMock, times(1)).writeRaw('{');
        verify(_jsonGeneratorMock, times(1)).writeNull();
        verify(_jsonGeneratorMock, times(1)).writeRaw('}');
    }

    @Test
    public void testWriteEmptyArray() throws IOException {
        _defaultPrettyPrinter.writeStartArray(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeEndArray(_jsonGeneratorMock, 0);

        verify(_jsonGeneratorMock, times(1)).writeRaw('[');
        verify(_jsonGeneratorMock, times(1)).writeRaw(']');
    }

    @Test
    public void testWriteEmptyObject() throws IOException {
        _defaultPrettyPrinter.writeStartObject(_jsonGeneratorMock);
        _defaultPrettyPrinter.writeEndObject(_jsonGeneratorMock, 0);

        verify(_jsonGeneratorMock, times(1)).writeRaw('{');
        verify(_jsonGeneratorMock, times(1)).writeRaw('}');
    }
}