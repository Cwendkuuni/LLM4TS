package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setUp() {
        lookupTranslator = new LookupTranslator(new CharSequence[][]{
                {"a", "1"},
                {"b", "2"},
                {"c", "3"}
        });
    }

    @Test
    public void testTranslate_singleCharacterMatch() throws IOException {
        StringWriter writer = new StringWriter();
        int index = lookupTranslator.translate("a", 0, writer);
        assertEquals(1, index);
        assertEquals("1", writer.toString());
    }

    @Test
    public void testTranslate_singleCharacterNoMatch() throws IOException {
        StringWriter writer = new StringWriter();
        int index = lookupTranslator.translate("d", 0, writer);
        assertEquals(0, index);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_multipleCharacterMatch() throws IOException {
        StringWriter writer = new StringWriter();
        int index = lookupTranslator.translate("abc", 0, writer);
        assertEquals(3, index);
        assertEquals("123", writer.toString());
    }

    @Test
    public void testTranslate_multipleCharacterPartialMatch() throws IOException {
        StringWriter writer = new StringWriter();
        int index = lookupTranslator.translate("abcd", 0, writer);
        assertEquals(3, index);
        assertEquals("123", writer.toString());
    }

    @Test
    public void testTranslate_multipleCharacterNoMatch() throws IOException {
        StringWriter writer = new StringWriter();
        int index = lookupTranslator.translate("def", 0, writer);
        assertEquals(0, index);
        assertEquals("", writer.toString());
    }

}