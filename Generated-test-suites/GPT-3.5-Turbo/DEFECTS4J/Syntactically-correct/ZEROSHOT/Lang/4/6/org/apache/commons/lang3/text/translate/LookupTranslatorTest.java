package org.apache.commons.lang3.text.translate;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.Test;

import static org.junit.Assert.*;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() throws IOException {
        CharSequence[] lookup1 = new CharSequence[][]{{"a", "b"}, {"c", "d"}};
        LookupTranslator translator = new LookupTranslator(lookup1);
        
        assertEquals("b", translator.translate("a", 0, new StringWriter()).toString());
        assertEquals("d", translator.translate("c", 0, new StringWriter()).toString());
        assertEquals("e", translator.translate("e", 0, new StringWriter()).toString());
        assertEquals("ba", translator.translate("aa", 0, new StringWriter()).toString());
        assertEquals("bd", translator.translate("cd", 0, new StringWriter()).toString());
        assertEquals("ba", translator.translate("aba", 0, new StringWriter()).toString());
        assertEquals("bed", translator.translate("ced", 0, new StringWriter()).toString());
    }
}