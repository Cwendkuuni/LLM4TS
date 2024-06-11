
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.Writer;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator() {
        CharSequence[] seq1 = { "a", "1" };
        CharSequence[] seq2 = { "b", "2" };
        CharSequence[] seq3 = { "c", "3" };
        CharSequence[][] lookup = { seq1, seq2, seq3 };
        LookupTranslator translator = new LookupTranslator(lookup);

        HashMap<CharSequence, CharSequence> expectedLookupMap = new HashMap<>();
        expectedLookupMap.put("a", "1");
        expectedLookupMap.put("b", "2");
        expectedLookupMap.put("c", "3");

        assertEquals(expectedLookupMap, translator.lookupMap);
        assertEquals(1, translator.shortest);
        assertEquals(1, translator.longest);
    }

    @Test
    public void testTranslate() throws IOException {
        CharSequence[] seq1 = { "a", "1" };
        CharSequence[] seq2 = { "b", "2" };
        CharSequence[] seq3 = { "c", "3" };
        CharSequence[][] lookup = { seq1, seq2, seq3 };
        LookupTranslator translator = new LookupTranslator(lookup);

        CharSequence input = "abcabc";
        Writer out = Mockito.mock(Writer.class);
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);

        translator.translate(input, 0, out);

        Mockito.verify(out, Mockito.times(3)).write(captor.capture());

        assertEquals("1", captor.getAllValues().get(0));
        assertEquals("2", captor.getAllValues().get(1));
        assertEquals("3", captor.getAllValues().get(2));
    }
}
