package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.core.io.SerializedString;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultConstructor() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        assertNull(printer._rootSeparator);
        assertEquals(FixedSpaceIndenter.instance, printer._arrayIndenter);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, printer._objectIndenter);
        assertFalse(printer._spacesInObjectEntries);
        assertNull(printer._separators);
        assertNull(printer._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testStringRootSeparatorConstructor() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter(new SerializedString("root"));
        assertEquals(new SerializedString("root"), printer._rootSeparator);
        assertEquals(FixedSpaceIndenter.instance, printer._arrayIndenter);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, printer._objectIndenter);
        assertFalse(printer._spacesInObjectEntries);
        assertNull(printer._separators);
        assertNull(printer._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testSerializedStringRootSeparatorConstructor() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter(new SerializedString("root"));
        assertEquals(new SerializedString("root"), printer._rootSeparator);
        assertEquals(FixedSpaceIndenter.instance, printer._arrayIndenter);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, printer._objectIndenter);
        assertFalse(printer._spacesInObjectEntries);
        assertNull(printer._separators);
        assertNull(printer._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testCopyConstructor() {
        DefaultPrettyPrinter base = new DefaultPrettyPrinter(new SerializedString("root"));
        DefaultPrettyPrinter copy = new DefaultPrettyPrinter(base);
        assertEquals(base._rootSeparator, copy._rootSeparator);
        assertEquals(base._arrayIndenter, copy._arrayIndenter);
        assertEquals(base._objectIndenter, copy._objectIndenter);
        assertEquals(base._spacesInObjectEntries, copy._spacesInObjectEntries);
        assertEquals(base._separators, copy._separators);
        assertEquals(base._objectFieldValueSeparatorWithSpaces, copy._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testWithRootSeparator() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DefaultPrettyPrinter withRoot = printer.withRootSeparator(new SerializedString("root"));
        assertEquals(new SerializedString("root"), withRoot._rootSeparator);
        assertNotSame(printer, withRoot);
        assertEquals(printer._arrayIndenter, withRoot._arrayIndenter);
        assertEquals(printer._objectIndenter, withRoot._objectIndenter);
        assertEquals(printer._spacesInObjectEntries, withRoot._spacesInObjectEntries);
        assertEquals(printer._separators, withRoot._separators);
        assertEquals(printer._objectFieldValueSeparatorWithSpaces, withRoot._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testWithArrayIndenter() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DefaultPrettyPrinter withArrayIndenter = printer.withArrayIndenter(new FixedSpaceIndenter());
        assertEquals(FixedSpaceIndenter.instance, withArrayIndenter._arrayIndenter);
        assertNotSame(printer, withArrayIndenter);
        assertEquals(printer._rootSeparator, withArrayIndenter._rootSeparator);
        assertEquals(printer._objectIndenter, withArrayIndenter._objectIndenter);
        assertEquals(printer._spacesInObjectEntries, withArrayIndenter._spacesInObjectEntries);
        assertEquals(printer._separators, withArrayIndenter._separators);
        assertEquals(printer._objectFieldValueSeparatorWithSpaces, withArrayIndenter._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testWithObjectIndenter() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DefaultPrettyPrinter withObjectIndenter = printer.withObjectIndenter(new DefaultIndenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("indent");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        assertEquals(printer._rootSeparator, withObjectIndenter._rootSeparator);
        assertEquals(printer._arrayIndenter, withObjectIndenter._arrayIndenter);
        assertInstanceOf(CustomIndenter.class, withObjectIndenter._objectIndenter);
        assertEquals(printer._spacesInObjectEntries, withObjectIndenter._spacesInObjectEntries);
        assertEquals(printer._separators, withObjectIndenter._separators);
        assertEquals(printer._objectFieldValueSeparatorWithSpaces, withObjectIndenter._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testWithSpacesInObjectEntries() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DefaultPrettyPrinter withSpaces = printer.withSpacesInObjectEntries();
        assertTrue(withSpaces._spacesInObjectEntries);
        assertNotSame(printer, withSpaces);
        assertEquals(printer._rootSeparator, withSpaces._rootSeparator);
        assertEquals(printer._arrayIndenter, withSpaces._arrayIndenter);
        assertEquals(printer._objectIndenter, withSpaces._objectIndenter);
        assertEquals(printer._separators, withSpaces._separators);
        assertEquals(printer._objectFieldValueSeparatorWithSpaces, withSpaces._objectFieldValueSeparatorWithSpaces);

        DefaultPrettyPrinter withoutSpaces = printer.withoutSpacesInObjectEntries();
        assertFalse(withoutSpaces._spacesInObjectEntries);
        assertNotSame(printer, withoutSpaces);
        assertEquals(printer._rootSeparator, withoutSpaces._rootSeparator);
        assertEquals(printer._arrayIndenter, withoutSpaces._arrayIndenter);
        assertEquals(printer._objectIndenter, withoutSpaces._objectIndenter);
        assertEquals(printer._separators, withoutSpaces._separators);
        assertEquals(printer._objectFieldValueSeparatorWithSpaces, withoutSpaces._objectFieldValueSeparatorWithSpaces);
    }

    @Test
    public void testWithSeparators() {
        DefaultPrettyPrinter printer = new DefaultPrettyPrinter();
        DefaultPrettyPrinter withSeparators = printer.withSeparators(new Separators() {
            @Override
            public String getObjectEntrySeparator() {
                return "entrySeparator";
            }

            @Override
            public String getObjectFieldValueSeparator() {
                return "fieldValueSeparator";
            }

            @Override
            public String getArrayValueSeparator() {
                return "arrayValueSeparator";
            }
        });
        assertEquals("entrySeparator", withSeparators._separators.getObjectEntrySeparator());
        assertEquals("fieldValueSeparator", withSeparators._separators.getObjectFieldValueSeparator());
        assertEquals("arrayValueSeparator", withSeparators._separators.getArrayValueSeparator());
        assertNotSame(printer, withSeparators);
        assertEquals(printer._rootSeparator, withSeparators._rootSeparator);
        assertEquals(printer._arrayIndenter, withSeparators._arrayIndenter);
        assertEquals(printer._objectIndenter, withSeparators._objectIndenter);
        assertEquals(printer._spacesInObjectEntries, withSeparators._spacesInObjectEntries);
        assertEquals(printer._objectFieldValueSeparatorWithSpaces, withSeparators._objectFieldValueSeparatorWithSpaces);
    }
}

class CustomIndenter implements Indenter {
    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw("customIndentation");
    }

    @Override
    public boolean isInline() {
        return false;
    }
}

class DefaultIndenter implements Indenter {
    static final DefaultIndenter SYSTEM_LINEFEED_INSTANCE = new DefaultIndenter();

    @Override
    public void writeIndentation(JsonGenerator g, int level) throws IOException {
        g.writeRaw(System.lineSeparator());
    }

    @Override
    public boolean isInline() {
        return true;
    }
}

class Separators {
    private final String objectEntrySeparator;
    private final String objectFieldValueSeparator;
    private final String arrayValueSeparator;

    public Separators() {
        this(":", ",", "");
    }

    public Separators(String objectEntrySeparator, String objectFieldValueSeparator, String arrayValueSeparator) {
        this.objectEntrySeparator = objectEntrySeparator;
        this.objectFieldValueSeparator = objectFieldValueSeparator;
        this.arrayValueSeparator = arrayValueSeparator;
    }

    public String getObjectEntrySeparator() {
        return objectEntrySeparator;
    }

    public String getObjectFieldValueSeparator() {
        return objectFieldValueSeparator;
    }

    public String getArrayValueSeparator() {
        return arrayValueSeparator;
    }
}