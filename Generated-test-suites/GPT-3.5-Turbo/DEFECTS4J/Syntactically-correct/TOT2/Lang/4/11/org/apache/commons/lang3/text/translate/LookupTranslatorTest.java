import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_ShouldTranslateAndReturnPositiveIndex() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringBuilder output = new StringBuilder();
        try {
            int index = lookupTranslator.translate("foo", 0, output);
            assertEquals(3, index);
            assertEquals("bar", output.toString());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testTranslate_ShouldReturnZeroIndexIfNoMatchFound() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringBuilder output = new StringBuilder();
        try {
            int index = lookupTranslator.translate("foobar", 0, output);
            assertEquals(0, index);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testTranslate_ShouldTranslateAndReturnNegativeIndexIfPartialMatch() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        StringBuilder output = new StringBuilder();
        try {
            int index = lookupTranslator.translate("foobar", 0, output);
            assertEquals(-3, index);
            assertEquals("bar", output.toString());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testTranslate_ShouldHandleEdgeCaseOfEmptyInput() {
        LookupTranslator lookupTranslator = new LookupTranslator();
        StringBuilder output = new StringBuilder();
        try {
            int index = lookupTranslator.translate("", 0, output);
            assertEquals(0, index);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test
    public void testTranslate_ShouldHandleEdgeCaseOfEmptyLookupTable() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{});
        StringBuilder output = new StringBuilder();
        try {
            int index = lookupTranslator.translate("foo", 0, output);
            assertEquals(0, index);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown unexpectedly");
        }
    }

    @Test(expected = IOException.class)
    public void testTranslate_ShouldThrowIOExceptionForNullOutput() throws IOException {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        lookupTranslator.translate("foo", 0, null);
    }

    @Test
    public void testConstructor_ShouldInitializeMapWithGivenLookup() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"foo", "bar"});
        assertEquals("bar", lookupTranslator.lookupMap.get("foo"));
    }

    @Test
    public void testConstructor_ShouldSetShortestAndLongestValues() {
        LookupTranslator lookupTranslator = new LookupTranslator(new CharSequence[]{"foo", "bar"}, new CharSequence[]{"hello", "world"});
        assertEquals(3, lookupTranslator.shortest);
        assertEquals(5, lookupTranslator.longest);
    }

}