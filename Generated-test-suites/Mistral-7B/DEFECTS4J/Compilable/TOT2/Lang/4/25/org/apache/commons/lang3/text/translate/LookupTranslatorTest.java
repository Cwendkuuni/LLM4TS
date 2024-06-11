package org.apache.commons.lang3.text.translate;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[][] lookup = {{"hello", "world"}, {"abcd", "efgh"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNotNull(translator);
    }
}