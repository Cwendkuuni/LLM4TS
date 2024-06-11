import org.junit.Before;
import org.junit.Test;
import java.io.StringWriter;
import java.util.HashMap;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setUp() {
        CharSequence[][] lookup = {
            {"a", "1"},
            {"b", "2"},
            {"c", "3"},
            {"cd", "4"},
            {"efg", "5"}
        };
        lookupTranslator = new LookupTranslator(lookup);
    }

    @Test
    public void translate_ShouldReturnExpectedOutput_WhenInputContainsSingleCharSequenceInRange() throws Exception {
        StringWriter out = new StringWriter();
        int result = lookupTranslator.translate("a", 0, out);
        assertEquals("1", out.toString());
        assertEquals(1, result);
    }

    @Test
    public void translate_ShouldReturnZero_WhenInputDoesNotContainAnyMatchingCharSequence() throws Exception {
        StringWriter out = new StringWriter();
        int result = lookupTranslator.translate("d", 0, out);
        assertEquals("", out.toString());
        assertEquals(0, result);
    }

    @Test
    public void translate_ShouldReturnExpectedOutput_WhenInputContainsCharSequenceAtEndOfRange() throws Exception {
        StringWriter out = new StringWriter();
        int result = lookupTranslator.translate("defg", 0, out);
        assertEquals("5", out.toString());
        assertEquals(3, result);
    }

    @Test
    public void translate_ShouldReturnExpectedOutput_WhenInputContainsMultipleMatchingCharSequences() throws Exception {
        StringWriter out = new StringWriter();
        int result = lookupTranslator.translate("bcdefg", 0, out);
        assertEquals("235", out.toString());
        assertEquals(4, result);
    }

    @Test
    public void translate_ShouldHandleEmptyInputGracefully() throws Exception {
        StringWriter out = new StringWriter();
        int result = lookupTranslator.translate("", 0, out);
        assertEquals("", out.toString());
        assertEquals(0, result);
    }

    @Test
    public void translate_ShouldHandleNullInputGracefully() throws Exception {
        StringWriter out = new StringWriter();
        int result = lookupTranslator.translate(null, 0, out);
        assertEquals("", out.toString());
        assertEquals(0, result);
    }
}