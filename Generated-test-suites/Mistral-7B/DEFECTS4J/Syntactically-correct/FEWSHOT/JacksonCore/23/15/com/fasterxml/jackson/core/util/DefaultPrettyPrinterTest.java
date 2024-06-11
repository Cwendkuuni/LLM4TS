package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;

import java.io.IOException;

import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);

        DefaultPrettyPrinter prettyPrinterWithRootSeparator = new DefaultPrettyPrinter(new SerializedString("root separator"));
        assertEquals("root separator", prettyPrinterWithRootSeparator.getRootSeparator().asString());
    }

    @Test
    public void testDefaultPrettyPrinterConfiguration() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();

        // Default values
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter._arrayIndenter);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter._objectIndenter);
        assertTrue(prettyPrinter._spacesInObjectEntries);
        assertEquals(null, prettyPrinter._rootSeparator);
        assertEquals(DEFAULT_SEPARATORS, prettyPrinter._separators);

        // Setters
        DefaultIndenter customObjectIndenter = mock(DefaultIndenter.class);
        FixedSpaceIndenter customArrayIndenter = mock(FixedSpaceIndenter.class);
        Separators customSeparators = mock(Separators.class);
        SerializableString customRootSeparator = new SerializedString("custom root separator");

        prettyPrinter.indentArraysWith(customArrayIndenter);
        assertEquals(customArrayIndenter, prettyPrinter._arrayIndenter);

        prettyPrinter.indentObjectsWith(customObjectIndenter);
        assertEquals(customObjectIndenter, prettyPrinter._objectIndenter);

        prettyPrinter.withRootSeparator(customRootSeparator);
        assertEquals(customRootSeparator, prettyPrinter._rootSeparator);

        prettyPrinter.withSeparators(customSeparators);
        assertEquals(customSeparators, prettyPrinter._separators);

        // Factory methods
        DefaultPrettyPrinter withSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        assertNotSame(prettyPrinter, withSpacesInObjectEntries);
        assertFalse(withSpacesInObjectEntries._spacesInObjectEntries);

        DefaultPrettyPrinter withoutSpacesInObjectEntries = prettyPrinter.withoutSpacesInObjectEntries();
        assertNotSame(prettyPrinter, withoutSpacesInObjectEntries);
        assertFalse(withoutSpacesInObjectEntries._spacesInObjectEntries);
    }

    @Test
    public void testDefaultPrettyPrinterPrettyPrinting() throws IOException {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator jsonGenerator = mock(JsonGenerator.class);

        // Root value
        prettyPrinter.writeRootValueSeparator(jsonGenerator);
        verify(jsonGenerator, times(1)).writeRaw("");

        // Object
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 1);

        verify(jsonGenerator, times(1)).writeRaw("{");
        verify(jsonGenerator, times(1)).writeRaw("}");

        // Array
        prettyPrinter.writeStartArray(jsonGenerator);
        prettyPrinter.beforeArrayValues(jsonGenerator);
        prettyPrinter.writeArrayValueSeparator(jsonGenerator);
        prettyPrinter.writeEndArray(jsonGenerator, 1);

        verify(jsonGenerator, times(1)).writeRaw("[");
        verify(jsonGenerator, times(1)).writeRaw("]");

        // Object with fields
        prettyPrinter.writeStartObject(jsonGenerator);
        prettyPrinter.beforeObjectEntries(jsonGenerator);

        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeStringField("field1", "value1");
        prettyPrinter.writeObjectFieldValueSeparator(jsonGenerator);
        prettyPrinter.writeStringField("field2", "value2");

        prettyPrinter.writeObjectEntrySeparator(jsonGenerator);
        prettyPrinter.writeEndObject(jsonGenerator, 2);

        verify(jsonGenerator, times(1)).writeRaw("{");
        verify(jsonGenerator, times(1)).writeString("field1");
        verify(jsonGenerator, times(1)).writeRaw(":");
        verify(jsonGenerator, times(1)).writeString("value1");
        verify(jsonGenerator, times(1)).writeRaw(",");
        verify(jsonGenerator, times(1)).writeString("field2");
        verify(jsonGenerator, times(1)).writeRaw(":");
        verify(jsonGenerator, times(1)).writeString("value2");
        verify(jsonGenerator, times(1)).writeRaw("}");
    }
}