package com.fasterxml.jackson.core.util;

import static org.junit.Assert.*;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.io.SerializedString;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {

    @Test
    public void testDefaultPrettyPrinterConstructor() {
        DefaultPrettyPrinter p1 = new DefaultPrettyPrinter();
        DefaultPrettyPrinter p2 = new DefaultPrettyPrinter(null);
        DefaultPrettyPrinter p3 = new DefaultPrettyPrinter(new SerializedString(""));

        // Test that default root separator is set
        JsonGenerator g = new JsonFactory().createGenerator(new ByteArrayOutputStream(), p1);
        assertNull(p1.getRootSeparator());
        p1.writeRootValueSeparator(g);
        assertEquals(" ", g.getOutputBuffer().toString().trim());

        // Test that root separator can be set
        DefaultPrettyPrinter p4 = new DefaultPrettyPrinter(new SerializedString("----"));
        g.reset();
        p4.writeRootValueSeparator(g);
        assertEquals("----", g.getOutputBuffer().toString().trim());

        // Test that default indenters are set
        p1 = new DefaultPrettyPrinter();
        p2 = new DefaultPrettyPrinter();
        Indenter aIndenter = p1.getArrayIndenter();
        Indenter oIndenter = p1.getObjectIndenter();
        assertEquals(FixedSpaceIndenter.instance, aIndenter);
        assertEquals(DefaultIndenter.SYSTEM_LINEFEED_INSTANCE, oIndenter);

        // Test that indenters can be set
        p1.indentArraysWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("ARR_INDENT");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        p2.indentObjectsWith(new Indenter() {
            @Override
            public void writeIndentation(JsonGenerator g, int level) throws IOException {
                g.writeRaw("OBJ_INDENT");
            }

            @Override
            public boolean isInline() {
                return false;
            }
        });
        g.reset();
        p1.writeStartArray(g);
        p2.writeStartObject(g);
        assertEquals("ARR_INDENTOBJ_INDENT", g.getOutputBuffer().toString().trim());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidIndenter() {
        DefaultPrettyPrinter p = new DefaultPrettyPrinter();
        p.indentObjectsWith(new Object() {});
    }

    @Test
    public void testWriteObjectFieldValueSeparator() {
        DefaultPrettyPrinter p = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new ByteArrayOutputStream(), p);
        p.writeObjectFieldValueSeparator(g);
        assertEquals(" ", g.getOutputBuffer().toString().trim());

        p = new DefaultPrettyPrinter();
        p.withSpacesInObjectEntries();
        p.writeObjectFieldValueSeparator(g);
        assertEquals("  ", g.getOutputBuffer().toString().trim());
    }

    @Test
    public void testWriteObjectEntrySeparator() {
        DefaultPrettyPrinter p = new DefaultPrettyPrinter();
        JsonGenerator g = new JsonFactory().createGenerator(new ByteArrayOutputStream(), p);
        p.writeObjectEntrySeparator(g);
        assertEquals("\n", g.getOutputBuffer().toString().trim());

        p = new DefaultPrettyPrinter();
        p.withSpacesInObjectEntries();
        p.writeObjectEntrySeparator(g);
        assertEquals("\n  ", g.getOutputBuffer().toString().trim());
    }

    // Add more tests for other methods as needed
}