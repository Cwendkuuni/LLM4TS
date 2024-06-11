package org.apache.commons.lang3.text.translate;
import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {
    
    @Test
    public void testTranslate() throws Exception {
        LookupTranslator lookupTranslator = new LookupTranslator();
        CharSequence[] lookup1 = { "a", "A" };
        CharSequence[] lookup2 = { "b", "B" };
        lookupTranslator = new LookupTranslator(lookup1, lookup2);
        
        // Test translating single characters
        assertEquals("A", lookupTranslator.translate("a", 0, null));
        assertEquals("B", lookupTranslator.translate("b", 0, null));
        
        // Test translating longer strings
        assertEquals("AA", lookupTranslator.translate("aa", 0, null));
        assertEquals("BB", lookupTranslator.translate("bb", 0, null));
    }

}