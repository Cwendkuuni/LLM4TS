package com.fasterxml.jackson.core.util;

import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Test;
import org.junit.Before;
import org.junit.Assert;

import static org.mockito.Mockito.*;

public class DefaultPrettyPrinterTest {
    // Create a spy instance of JsonGenerator for testing
    JsonGenerator mockJsonGenerator;

    @Before
    public void setUp() {
        mockJsonGenerator = spy(JsonGenerator.class);
    }

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("");
    }

    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString("test"));
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("test");
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withRootSeparator(new SerializedString("test"));
        prettyPrinter.writeRootValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw("test");
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter arrayIndenter = mock(Indenter.class);
        prettyPrinter.indentArraysWith(arrayIndenter);
        prettyPrinter.writeStartArray(mockJsonGenerator);
        verify(arrayIndenter).writeIndentation(mockJsonGenerator, 0);
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Indenter objectIndenter = mock(Indenter.class);
        prettyPrinter.indentObjectsWith(objectIndenter);
        prettyPrinter.writeStartObject(mockJsonGenerator);
        prettyPrinter.beforeObjectEntries(mockJsonGenerator);
        verify(objectIndenter).writeIndentation(mockJsonGenerator, 0);
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter = prettyPrinter.withSpacesInObjectEntries();
        Assert.assertTrue(prettyPrinter._spacesInObjectEntries);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithNullObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter.indentObjectsWith(null);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter._spacesInObjectEntries = false;
        prettyPrinter.writeObjectFieldValueSeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(_separators.getObjectFieldValueSeparator());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        prettyPrinter._objectIndenter = new FixedSpaceIndenter();
        prettyPrinter.writeObjectEntrySeparator(mockJsonGenerator);
        verify(mockJsonGenerator).writeRaw(" " + _separators.getObjectEntrySeparator() + " ");
    }
}