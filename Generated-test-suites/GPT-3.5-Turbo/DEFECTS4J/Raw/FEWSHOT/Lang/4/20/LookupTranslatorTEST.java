package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LookupTranslatorTest {
    
    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"hello", "world"},
            {"foo", "bar"}
        });
        
        assertEquals(5, translator.translate("hello", 0, null));
        assertEquals(3, translator.translate("foo", 0, null));
        assertEquals(0, translator.translate("abc", 0, null));
        assertEquals(0, translator.translate("", 0, null));
    }
}