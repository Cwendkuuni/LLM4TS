package com.fasterxml.jackson.core.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;

public class DefaultPrettyPrinterTest {

    DefaultPrettyPrinter prettyPrinter;

    @Before
    public void setUp() {
        prettyPrinter = new DefaultPrettyPrinter();
    }

    @Test
    public void testDefaultConstruction() {
        assertNull(prettyPrinter.getRootSeparator());
        assertEquals(new SerializedString(" "), prettyPrinter.getArrayIndenter().isInline() ? null : prettyPrinter.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, prettyPrinter.getObjectIndenter());
        assertFalse(prettyPrinter.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, prettyPrinter.getSeparators());
    }

    @Test
    public void testCustomRootSeparatorConstruction() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString("--"));
        assertEquals(new SerializedString("--"), pp.getRootSeparator());
        assertNull(pp.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, pp.getObjectIndenter());
        assertFalse(pp.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, pp.getSeparators());
    }

    @Test
    public void testCustomRootSeparatorAndIndenterConstruction() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString("--"), new FixedSpaceIndenter());
        assertEquals(new SerializedString("--"), pp.getRootSeparator());
        assertEquals(new FixedSpaceIndenter(), pp.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, pp.getObjectIndenter());
        assertFalse(pp.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, pp.getSeparators());
    }

    @Test
    public void testCustomRootSeparatorAndIndenterWithSpacesConstruction() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString("--"), new FixedSpaceIndenter(), true);
        assertEquals(new SerializedString("--"), pp.getRootSeparator());
        assertEquals(new FixedSpaceIndenter(), pp.getArrayIndenter());
        assertFalse(pp.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, pp.getSeparators());
        assertTrue(pp.isSpacesInObjectEntries());
    }

    @Test
    public void testCustomArrayIndenterConstruction() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(null, null, null, new FixedSpaceIndenter());
        assertNull(pp.getRootSeparator());
        assertEquals(new FixedSpaceIndenter(), pp.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, pp.getObjectIndenter());
        assertFalse(pp.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, pp.getSeparators());
    }

    @Test
    public void testCustomObjectIndenterConstruction() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(null, null, new DefaultIndenter(true), null);
        assertNull(pp.getRootSeparator());
        assertNull(pp.getArrayIndenter());
        assertEquals(new DefaultIndenter(true), pp.getObjectIndenter());
        assertFalse(pp.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, pp.getSeparators());
    }

    @Test
    public void testCustomRootSeparatorAndIndenterWithSpacesConstruction() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter(new SerializedString("--"), new FixedSpaceIndenter(), new DefaultIndenter(true), true);
        assertEquals(new SerializedString("--"), pp.getRootSeparator());
        assertEquals(new FixedSpaceIndenter(), pp.getArrayIndenter());
        assertEquals(new DefaultIndenter(true), pp.getObjectIndenter());
        assertFalse(pp.isSpacesInObjectEntries());
        assertTrue(pp.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, pp.getSeparators());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullRootSeparatorConstruction() {
        new DefaultPrettyPrinter(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullArrayIndenterConstruction() {
        new DefaultPrettyPrinter(new SerializedString("--"), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullObjectIndenterConstruction() {
        new DefaultPrettyPrinter(new SerializedString("--"), new FixedSpaceIndenter(), null);
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter ppWithSeparator = pp.withRootSeparator(new SerializedString("--"));
        assertEquals(new SerializedString("--"), ppWithSeparator.getRootSeparator());
        assertNull(ppWithSeparator.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, ppWithSeparator.getObjectIndenter());
        assertFalse(ppWithSeparator.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, ppWithSeparator.getSeparators());
    }

    @Test
    public void testWithCustomArrayIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter ppWithCustomArrayIndenter = pp.withArrayIndenter(new FixedSpaceIndenter());
        assertNull(ppWithCustomArrayIndenter.getRootSeparator());
        assertEquals(new FixedSpaceIndenter(), ppWithCustomArrayIndenter.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, ppWithCustomArrayIndenter.getObjectIndenter());
        assertFalse(ppWithCustomArrayIndenter.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, ppWithCustomArrayIndenter.getSeparators());
    }

    @Test
    public void testWithCustomObjectIndenter() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter ppWithCustomObjectIndenter = pp.withObjectIndenter(new DefaultIndenter(true));
        assertNull(ppWithCustomObjectIndenter.getRootSeparator());
        assertNull(ppWithCustomObjectIndenter.getArrayIndenter());
        assertEquals(new DefaultIndenter(true), ppWithCustomObjectIndenter.getObjectIndenter());
        assertFalse(ppWithCustomObjectIndenter.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, ppWithCustomObjectIndenter.getSeparators());
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter ppWithSpacesInObjectEntries = pp.withSpacesInObjectEntries();
        assertNull(ppWithSpacesInObjectEntries.getRootSeparator());
        assertNull(ppWithSpacesInObjectEntries.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, ppWithSpacesInObjectEntries.getObjectIndenter());
        assertTrue(ppWithSpacesInObjectEntries.isSpacesInObjectEntries());
        assertEquals(DEFAULT_SEPARATORS, ppWithSpacesInObjectEntries.getSeparators());
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter pp = new DefaultPrettyPrinter();
        DefaultPrettyPrinter ppWithSeparators = pp.withSeparators(new Separators(": ", ", "));
        assertNull(ppWithSeparators.getRootSeparator());
        assertNull(ppWithSeparators.getArrayIndenter());
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, ppWithSeparators.getObjectIndenter());
        assertFalse(ppWithSeparators.isSpacesInObjectEntries());
        assertEquals(new Separators(": ", ", "), ppWithSeparators.getSeparators());
    }
}