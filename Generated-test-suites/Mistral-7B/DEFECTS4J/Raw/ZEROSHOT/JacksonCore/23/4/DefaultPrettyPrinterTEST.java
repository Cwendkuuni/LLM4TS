package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.DefaultIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.FixedSpaceIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.NopIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.Separators;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterInstance() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        assertNotNull(pp);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, pp.getRootSeparator());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparator() {
        String rootSeparator = "test";
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(rootSeparator);
        assertEquals(new SerializedString(rootSeparator), pp.getRootSeparator());
    }

    @Test
    public void testDefaultPrettyPrinterWithRootSeparatorSerializedString() {
        SerializedString rootSeparator = new SerializedString("test");
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(rootSeparator);
        assertEquals(rootSeparator, pp.getRootSeparator());
    }

    @Test
    public void testSetRootSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        String rootSeparator = "test";
        DefaultPrettyPrinter newPp = pp.withRootSeparator(rootSeparator);
        assertEquals(new SerializedString(rootSeparator), newPp.getRootSeparator());
        assertEquals(pp, pp.withRootSeparator(pp.getRootSeparator()));
    }

    @Test
    public void testSetArrayIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter arrayIndenter = mock(Indenter.class);
        DefaultPrettyPrinter newPp = pp.withArrayIndenter(arrayIndenter);
        when(arrayIndenter.isInline()).thenReturn(true);
        assertEquals(arrayIndenter, newPp._arrayIndenter);
        assertEquals(pp, pp.withArrayIndenter(pp._arrayIndenter));
    }

    @Test
    public void testSetObjectIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Indenter objectIndenter = mock(Indenter.class);
        DefaultPrettyPrinter newPp = pp.withObjectIndenter(objectIndenter);
        when(objectIndenter.isInline()).thenReturn(true);
        assertEquals(objectIndenter, newPp._objectIndenter);
        assertEquals(pp, pp.withObjectIndenter(pp._objectIndenter));
    }

    @Test
    public void testSetSpacesInObjectEntries() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        boolean spacesInObjectEntries = true;
        DefaultPrettyPrinter newPp = pp.withSpacesInObjectEntries();
        assertEquals(spacesInObjectEntries, newPp._spacesInObjectEntries);
        assertEquals(pp, pp.withoutSpacesInObjectEntries());
    }

    @Test
    public void testSetSeparators() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        Separators separators = new Separators(',', '[', ']', ':', " : ");
        DefaultPrettyPrinter newPp = pp.withSeparators(separators);
        assertEquals(separators, newPp._separators);
        assertEquals(pp, pp.withSeparators(pp._separators));
    }

    @Test
    public void testWriteRootValueSeparator() throws IOException, JsonProcessingException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        String rootSeparator = "test";
        pp.withRootSeparator(rootSeparator);
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeRootValueSeparator(g);
        when(g.writeRaw(rootSeparator)).thenReturn(null);
        pp.writeRootValueSeparator(g);
    }

    @Test
    public void testWriteStartObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeStartObject(g);
        when(g.writeRaw('{')).thenReturn(null);
        pp.writeStartObject(g);

        Indenter indenter = mock(Indenter.class);
        pp.indentObjectsWith(indenter);
        pp.writeStartObject(g);
        when(g.writeRaw('{')).thenReturn(null);
        pp.writeStartObject(g);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testBeforeObjectEntries() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.beforeObjectEntries(g);
        when(g.writeRaw("")).thenReturn(null);
        pp.beforeObjectEntries(g);

        Indenter indenter = mock(Indenter.class);
        pp.indentObjectsWith(indenter);
        pp.beforeObjectEntries(g);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeObjectFieldValueSeparator(g);
        when(g.writeRaw(" : ")).thenReturn(null);
        pp.writeObjectFieldValueSeparator(g);

        Indenter indenter = mock(Indenter.class);
        pp.indentObjectsWith(indenter);
        pp.writeObjectFieldValueSeparator(g);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testWriteObjectEntrySeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeObjectEntrySeparator(g);
        when(g.writeRaw(",")).thenReturn(null);
        pp.writeObjectEntrySeparator(g);

        Indenter indenter = mock(Indenter.class);
        pp.indentObjectsWith(indenter);
        pp.writeObjectEntrySeparator(g);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testWriteEndObject() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        int nrOfEntries = 5;
        pp.writeEndObject(g, nrOfEntries);
        when(g.writeRaw("")).thenReturn(null);
        pp.writeEndObject(g, nrOfEntries);

        Indenter indenter = mock(Indenter.class);
        pp.indentObjectsWith(indenter);
        pp.writeEndObject(g, nrOfEntries);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testWriteStartArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeStartArray(g);
        when(g.writeRaw('[')).thenReturn(null);
        pp.writeStartArray(g);

        Indenter indenter = mock(Indenter.class);
        pp.indentArraysWith(indenter);
        pp.writeStartArray(g);
        when(g.writeRaw('[')).thenReturn(null);
        pp.writeStartArray(g);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testBeforeArrayValues() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.beforeArrayValues(g);
        when(g.writeRaw("")).thenReturn(null);
        pp.beforeArrayValues(g);

        Indenter indenter = mock(Indenter.class);
        pp.indentArraysWith(indenter);
        pp.beforeArrayValues(g);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testWriteArrayValueSeparator() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        pp.writeArrayValueSeparator(g);
        when(g.writeRaw(",")).thenReturn(null);
        pp.writeArrayValueSeparator(g);

        Indenter indenter = mock(Indenter.class);
        pp.indentArraysWith(indenter);
        pp.writeArrayValueSeparator(g);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }

    @Test
    public void testWriteEndArray() throws IOException {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        JsonGenerator g = mock(JsonGenerator.class);
        int nrOfValues = 5;
        pp.writeEndArray(g, nrOfValues);
        when(g.writeRaw("")).thenReturn(null);
        pp.writeEndArray(g, nrOfValues);

        Indenter indenter = mock(Indenter.class);
        pp.indentArraysWith(indenter);
        pp.writeEndArray(g, nrOfValues);
        when(indenter.writeIndentation(g, 1)).thenReturn(null);
    }
}

class Separators implements com.fasterxml.jackson.core.util.Separators {

    private char comma;
    private char arrayValueSeparator;
    private char objectEntrySeparator;
    private String objectFieldValueSeparator;

    public Separators(char comma, char arrayValueSeparator, char objectEntrySeparator, String objectFieldValueSeparator) {
        this.comma = comma;
        this.arrayValueSeparator = arrayValueSeparator;
        this.objectEntrySeparator = objectEntrySeparator;
        this.objectFieldValueSeparator = objectFieldValueSeparator;
    }

    @Override
    public char getComma() {
        return comma;
    }

    @Override
    public char getArrayValueSeparator() {
        return arrayValueSeparator;
    }

    @Override
    public char getObjectEntrySeparator() {
        return objectEntrySeparator;
    }

    @Override
    public String getObjectFieldValueSeparator() {
        return objectFieldValueSeparator;
    }

    public String toString() {
        return "Separators{" +
                "comma=" + comma +
                ", arrayValueSeparator=" + arrayValueSeparator +
                ", objectEntrySeparator=" + objectEntrySeparator +
                ", objectFieldValueSeparator='" + objectFieldValueSeparator + '\'' +
                '}';
    }

    public static Separators DEFAULT_SEPARATORS = new Separators(',', '[', ']', ": ");

}

class FixedSpaceIndenter implements Indenter {

    public static final FixedSpaceIndenter instance = new FixedSpaceIndenter();

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        for (int i = 0; i < level; ++i) {
            g.writeRaw(' ');
        }
    }

    @Override
    public boolean isInline() {
        return true;
    }

}

class NopIndenter implements Indenter, java.io.Serializable {

    public static final NopIndenter instance = new NopIndenter();

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException { }

    @Override
    public boolean isInline() {
        return true;
    }
}