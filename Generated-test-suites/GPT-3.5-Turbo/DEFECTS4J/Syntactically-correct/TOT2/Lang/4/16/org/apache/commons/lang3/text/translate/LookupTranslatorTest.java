import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class LookupTranslatorTest {

    @Test
    public void testConstructor() {
        CharSequence[] testLookup = {{"a", "1"}, {"b", "2"}};
        LookupTranslator lookupTranslator = new LookupTranslator(testLookup);

        assertEquals(2, lookupTranslator.translate("a", 0, null));
        assertEquals(0, lookupTranslator.translate("c", 0, null));
    }

    @Test
    public void testTranslate() {
        CharSequence[] testLookup = {{"a", "1"}, {"b", "2"}};
        LookupTranslator lookupTranslator = new LookupTranslator(testLookup);

        assertEquals(1, lookupTranslator.translate("abc", 0, null));
        assertEquals(0, lookupTranslator.translate("def", 0, null));
    }

    @Test
    public void testTranslateWithLongestValue() {
        CharSequence[] testLookup = {{"ab", "12"}, {"c", "3"}};
        LookupTranslator lookupTranslator = new LookupTranslator(testLookup);

        assertEquals(2, lookupTranslator.translate("abc", 0, null));
    }

    @Test
    public void testTranslateWithShortestValue() {
        CharSequence[] testLookup = {{"a", "1"}, {"bc", "23"}};
        LookupTranslator lookupTranslator = new LookupTranslator(testLookup);

        assertEquals(1, lookupTranslator.translate("a", 0, null));
    }

    @Test
    public void testTranslateWithEmptyLookup() {
        LookupTranslator lookupTranslator = new LookupTranslator();

        assertEquals(0, lookupTranslator.translate("abc", 0, null));
    }

    @Test
    public void testTranslateWithNullLookup() {
        LookupTranslator lookupTranslator = new LookupTranslator(null);

        assertEquals(0, lookupTranslator.translate("abc", 0, null));
    }

    @Test
    public void testTranslateWithIndexOutOfRange() {
        CharSequence[] testLookup = {{"a", "1"}, {"b", "2"}};
        LookupTranslator lookupTranslator = new LookupTranslator(testLookup);

        try {
            lookupTranslator.translate("abc", 4, null);
            fail("Exception should have been thrown");
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
}