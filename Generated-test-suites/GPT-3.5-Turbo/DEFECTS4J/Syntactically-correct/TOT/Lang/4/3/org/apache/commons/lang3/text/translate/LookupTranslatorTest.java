import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_WhenInputIsNull_ShouldReturnZero() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder builder = new StringBuilder();
        int result = translator.translate(null, 0, builder);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WhenIndexIsNegative_ShouldReturnZero() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder builder = new StringBuilder();
        int result = translator.translate("Hello", -1, builder);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WhenIndexIsGreaterThanInputLength_ShouldReturnZero() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder builder = new StringBuilder();
        int result = translator.translate("Hello", 10, builder);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WhenLookupTableIsEmpty_ShouldReturnZero() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder builder = new StringBuilder();
        int result = translator.translate("Hello", 0, builder);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WhenSubSequenceFoundInLookupTable_ShouldReturnLengthOfSubSequence() {
        CharSequence[] lookup = {{"abc", "ABC"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder builder = new StringBuilder();
        int result = translator.translate("Hello abc", 6, builder);
        assertEquals(3, result);
    }

    @Test
    public void testTranslate_WhenSubSequenceNotFoundInLookupTable_ShouldReturnZero() {
        CharSequence[] lookup = {{"abc", "ABC"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder builder = new StringBuilder();
        int result = translator.translate("Hello def", 6, builder);
        assertEquals(0, result);
    }

    @Test
    public void testTranslate_WhenSubSequenceIsShortest_ShouldReturnLengthOfSubSequence() {
        CharSequence[] lookup = {{"ab", "AB"}, {"abc", "ABC"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder builder = new StringBuilder();
        int result = translator.translate("Hello abc", 6, builder);
        assertEquals(2, result);
    }

    @Test
    public void testTranslate_WhenSubSequenceIsLongest_ShouldReturnLengthOfSubSequence() {
        CharSequence[] lookup = {{"ab", "AB"}, {"abc", "ABC"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringBuilder builder = new StringBuilder();
        int result = translator.translate("Hello abc", 6, builder);
        assertEquals(3, result);
    }
}