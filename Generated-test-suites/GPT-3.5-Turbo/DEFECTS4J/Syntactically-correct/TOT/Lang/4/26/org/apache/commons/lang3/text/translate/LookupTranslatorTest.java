import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalUseCase() {
        CharSequence[] seq1 = { "apple", "fruit" };
        CharSequence[] seq2 = { "crab", "seafood" };
        LookupTranslator translator = new LookupTranslator(seq1, seq2);

        String input = "I love eating apple and crab.";
        String expectedOutput = "I love eating fruit and seafood.";
        StringBuilder actualOutput = new StringBuilder();

        try {
            translator.translate(input, 0, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred while translating.");
        }

        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testTranslate_inputLengthLessThanShortestTranslation() {
        CharSequence[] seq1 = { "apple", "fruit" };
        LookupTranslator translator = new LookupTranslator(seq1);

        String input = "app";
        String expectedOutput = "app";
        StringBuilder actualOutput = new StringBuilder();

        try {
            translator.translate(input, 0, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred while translating.");
        }

        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testTranslate_inputLengthGreaterThanLongestTranslation() {
        CharSequence[] seq1 = { "apple", "fruit" };
        LookupTranslator translator = new LookupTranslator(seq1);

        String input = "applejam";
        String expectedOutput = "fruitjam";
        StringBuilder actualOutput = new StringBuilder();

        try {
            translator.translate(input, 0, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred while translating.");
        }

        assertEquals(expectedOutput, actualOutput.toString());
    }

    @Test
    public void testTranslate_emptyInput() {
        CharSequence[] seq1 = { "apple", "fruit" };
        LookupTranslator translator = new LookupTranslator(seq1);

        String input = "";
        String expectedOutput = "";
        StringBuilder actualOutput = new StringBuilder();

        try {
            translator.translate(input, 0, actualOutput);
        } catch (Exception e) {
            fail("Exception occurred while translating.");
        }

        assertEquals(expectedOutput, actualOutput.toString());
    }
}