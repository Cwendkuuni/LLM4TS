package com.example;

import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultIndenter;
import com.fasterxml.jackson.core.util.SerializedString;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstruction() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDefaultPrettyPrinterConstructionWithNullArguments() {
        new DefaultPrettyPrinter(null);
    }

    @Test
    public void testDefaultPrettyPrinterConstructionWithEmptyStringRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter("");
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testDefaultPrettyPrinterConstructionWithSerializedRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(""));
        assertNotNull(prettyPrinter);
    }

    @Test
    public void testIndentArraysWithNullAndInlineIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        Indenter nullIndenter = null;
        try {
            prettyPrinter.indentArraysWith(nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testIndentObjectsWithNullAndInlineIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        Indenter nullIndenter = null;
        try {
            prettyPrinter.indentObjectsWith(nullIndenter);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithArrayIndenterWithNullAndNonInlineIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        Indenter nullIndenter = null;
        DefaultPrettyPrinter prettyPrinterWithNullIndenter = prettyPrinter.withArrayIndenter(nullIndenter);
        assertNotSame(prettyPrinter, prettyPrinterWithNullIndenter);
    }

    @Test
    public void testWithObjectIndenterWithNullAndNonInlineIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        Indenter nullIndenter = null;
        DefaultPrettyPrinter prettyPrinterWithNullIndenter = prettyPrinter.withObjectIndenter(nullIndenter);
        assertNotSame(prettyPrinter, prettyPrinterWithNullIndenter);
    }

    @Test
    public void testWithSpacesInObjectEntriesWithNullArgument() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        try {
            prettyPrinter.withSpacesInObjectEntries(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithSeparatorsWithNullArguments() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNotNull(prettyPrinter);
        try {
            prettyPrinter.withSeparators(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    // Add test cases for all other methods
}