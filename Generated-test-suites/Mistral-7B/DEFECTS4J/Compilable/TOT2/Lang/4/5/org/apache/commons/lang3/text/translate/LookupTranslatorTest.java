package org.apache.commons.lang3.text.translate;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import static org.junit.Assert.assertNotNull;

public class LookupTranslatorTest {
    @Test
    public void testLookupTranslatorConstruction() {
        CharSequence[][] lookup = new CharSequence[][]{{"key1", "value1"}, {"key2", "value2"}};
        LookupTranslator lookupTranslator = new LookupTranslator(lookup);
        assertNotNull(lookupTranslator);
    }
}