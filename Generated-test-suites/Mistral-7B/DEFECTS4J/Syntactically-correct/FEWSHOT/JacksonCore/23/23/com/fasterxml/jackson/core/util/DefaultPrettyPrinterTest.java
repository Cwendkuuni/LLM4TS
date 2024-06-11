package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        assertNull(prettyPrinter.getRootSeparator());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinter.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter.getArrayIndenter());
        assertFalse(prettyPrinter.hasSpacesInObjectEntries());
        assertFalse(prettyPrinter.hasSeparators());
    }

    @Test
    public void testConstructorWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(" "));
        assertEquals(new SerializedString(" "), prettyPrinter.getRootSeparator());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinter.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter.getArrayIndenter());
        assertFalse(prettyPrinter.hasSpacesInObjectEntries());
        assertFalse(prettyPrinter.hasSeparators());
    }

    @Test
    public void testConstructorWithRootSeparatorAndSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter(new SerializedString(""), new Separators());
        assertNull(prettyPrinter.getRootSeparator());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinter.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinter.getArrayIndenter());
        assertFalse(prettyPrinter.hasSpacesInObjectEntries());
        assertTrue(prettyPrinter.hasSeparators());
        assertEquals(" ", prettyPrinter.getObjectFieldValueSeparator());
        assertEquals(",", prettyPrinter.getObjectEntrySeparator());
    }

    @Test
    public void testClone() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter clonedPrettyPrinter = new DefaultPrettyPrinter(prettyPrinter);
        assertNotSame(prettyPrinter, clonedPrettyPrinter);
        assertEquals(prettyPrinter.getRootSeparator(), clonedPrettyPrinter.getRootSeparator());
        assertEquals(prettyPrinter.getDefaultRootValueSeparator(), clonedPrettyPrinter.getDefaultRootValueSeparator());
        assertEquals(prettyPrinter.getObjectIndenter(), clonedPrettyPrinter.getObjectIndenter());
        assertEquals(prettyPrinter.getArrayIndenter(), clonedPrettyPrinter.getArrayIndenter());
        assertEquals(prettyPrinter.hasSpacesInObjectEntries(), clonedPrettyPrinter.hasSpacesInObjectEntries());
        assertEquals(prettyPrinter.hasSeparators(), clonedPrettyPrinter.hasSeparators());
        assertEquals(prettyPrinter.getObjectFieldValueSeparator(), clonedPrettyPrinter.getObjectFieldValueSeparator());
        assertEquals(prettyPrinter.getObjectEntrySeparator(), clonedPrettyPrinter.getObjectEntrySeparator());
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithRootSeparator = prettyPrinter.withRootSeparator(new SerializedString("foo"));
        assertNotSame(prettyPrinter, prettyPrinterWithRootSeparator);
        assertEquals(new SerializedString("foo"), prettyPrinterWithRootSeparator.getRootSeparator());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinterWithRootSeparator.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinterWithRootSeparator.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinterWithRootSeparator.getArrayIndenter());
        assertFalse(prettyPrinterWithRootSeparator.hasSpacesInObjectEntries());
        assertFalse(prettyPrinterWithRootSeparator.hasSeparators());
    }

    @Test
    public void testWithRootSeparatorAndSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators();
        DefaultPrettyPrinter prettyPrinterWithRootSeparatorAndSeparators = prettyPrinter.withRootSeparator(new SerializedString(""), separators);
        assertNotSame(prettyPrinter, prettyPrinterWithRootSeparatorAndSeparators);
        assertNull(prettyPrinterWithRootSeparatorAndSeparators.getRootSeparator());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinterWithRootSeparatorAndSeparators.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinterWithRootSeparatorAndSeparators.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinterWithRootSeparatorAndSeparators.getArrayIndenter());
        assertFalse(prettyPrinterWithRootSeparatorAndSeparators.hasSpacesInObjectEntries());
        assertTrue(prettyPrinterWithRootSeparatorAndSeparators.hasSeparators());
        assertEquals(" ", prettyPrinterWithRootSeparatorAndSeparators.getObjectFieldValueSeparator());
        assertEquals(",", prettyPrinterWithRootSeparatorAndSeparators.getObjectEntrySeparator());
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        Separators separators = new Separators();
        DefaultPrettyPrinter prettyPrinterWithSeparators = prettyPrinter.withSeparators(separators);
        assertNotSame(prettyPrinter, prettyPrinterWithSeparators);
        assertNull(prettyPrinterWithSeparators.getRootSeparator());
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinterWithSeparators.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinterWithSeparators.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinterWithSeparators.getArrayIndenter());
        assertFalse(prettyPrinterWithSeparators.hasSpacesInObjectEntries());
        assertTrue(prettyPrinterWithSeparators.hasSeparators());
        assertEquals(" ", prettyPrinterWithSeparators.getObjectFieldValueSeparator());
        assertEquals(",", prettyPrinterWithSeparators.getObjectEntrySeparator());
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultIndenter defaultIndenter = new DefaultIndenter();
        DefaultPrettyPrinter prettyPrinterWithObjectIndenter = prettyPrinter.withObjectIndenter(defaultIndenter);
        assertNotSame(prettyPrinter, prettyPrinterWithObjectIndenter);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinterWithObjectIndenter.getDefaultRootValueSeparator());
        assertEquals(defaultIndenter, prettyPrinterWithObjectIndenter.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinterWithObjectIndenter.getArrayIndenter());
        assertFalse(prettyPrinterWithObjectIndenter.hasSpacesInObjectEntries());
        assertFalse(prettyPrinterWithObjectIndenter.hasSeparators());
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        FixedSpaceIndenter fixedSpaceIndenter = new FixedSpaceIndenter();
        DefaultPrettyPrinter prettyPrinterWithArrayIndenter = prettyPrinter.withArrayIndenter(fixedSpaceIndenter);
        assertNotSame(prettyPrinter, prettyPrinterWithArrayIndenter);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinterWithArrayIndenter.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinterWithArrayIndenter.getObjectIndenter());
        assertEquals(fixedSpaceIndenter, prettyPrinterWithArrayIndenter.getArrayIndenter());
        assertFalse(prettyPrinterWithArrayIndenter.hasSpacesInObjectEntries());
        assertFalse(prettyPrinterWithArrayIndenter.hasSeparators());
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        DefaultPrettyPrinter prettyPrinterWithSpacesInObjectEntries = prettyPrinter.withSpacesInObjectEntries();
        assertNotSame(prettyPrinter, prettyPrinterWithSpacesInObjectEntries);
        assertEquals(DefaultPrettyPrinter.DEFAULT_ROOT_VALUE_SEPARATOR, prettyPrinterWithSpacesInObjectEntries.getDefaultRootValueSeparator());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinterWithSpacesInObjectEntries.getObjectIndenter());
        assertEquals(FixedSpaceIndenter.instance, prettyPrinterWithSpacesInObjectEntries.getArrayIndenter());
        assertTrue(prettyPrinterWithSpacesInObjectEntries.hasSpacesInObjectEntries());
        assertFalse(prettyPrinterWithSpacesInObjectEntries.hasSeparators());
    }
}