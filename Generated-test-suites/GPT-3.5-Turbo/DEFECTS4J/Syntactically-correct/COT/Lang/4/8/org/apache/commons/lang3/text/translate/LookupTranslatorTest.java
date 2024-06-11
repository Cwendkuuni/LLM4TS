import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;
    private StringWriter writer;

    @Before
    public void setUp() {
        CharSequence[][] lookup = {
                {"a", "A"},
                {"b", "B"},
                {"c", "C"}
        };
        lookupTranslator = new LookupTranslator(lookup);
        writer = new StringWriter();
    }

    @After
    public void tearDown() throws IOException {
        writer.close();
    }

    @Test
    public void testTranslate_withMatchingInput_shouldTranslateAndReturnLength() throws IOException {
        int length = lookupTranslator.translate("a", 0, writer);
        Assert.assertEquals(1, length);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withNonMatchingInput_shouldNotTranslateAndReturnZeroLength() throws IOException {
        int length = lookupTranslator.translate("d", 0, writer);
        Assert.assertEquals(0, length);
        Assert.assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withPartialMatchedInput_shouldTranslateAndReturnLength() throws IOException {
        int length = lookupTranslator.translate("ab", 0, writer);
        Assert.assertEquals(1, length);
        Assert.assertEquals("A", writer.toString());
    }

    @Test
    public void testTranslate_withMultipleMatches_shouldTranslateLongestMatchAndReturnLength() throws IOException {
        int length = lookupTranslator.translate("abb", 0, writer);
        Assert.assertEquals(2, length);
        Assert.assertEquals("AB", writer.toString());
    }

    @Test
    public void testTranslate_withInputExceedingMaxLength_shouldTranslateAndReturnLength() throws IOException {
        int length = lookupTranslator.translate("abcd", 0, writer);
        Assert.assertEquals(3, length);
        Assert.assertEquals("ABC", writer.toString());
    }
}