import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_withValidNumericEntity_shouldTranslateToChar() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&amp;&#120;&#x79;", 0, writer);
        assertEquals(15, result);
        assertEquals("&xy", writer.toString());
    }

    @Test
    public void testTranslate_withNoSemiColonRequired_shouldNotTranslate() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#120", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withNoSemiColonRequiredAndErrorIfNoSemiColonOption_shouldThrowException() {
        StringWriter writer = new StringWriter();
        try {
            unescaper.translate("&#120", 0, writer);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
            assertEquals("", writer.toString());
        }
    }

    @Test
    public void testTranslate_withInvalidNumericEntity_shouldNotTranslate() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#xyz;", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withHighValueNumericEntity_shouldTranslateToCharPair() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#65536;", 0, writer);
        assertEquals(10, result);
        assertEquals("\uD800\uDC00", writer.toString());
    }

    @Test
    public void testTranslate_withEmptyInput_shouldNotTranslate() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInputStartingWithNonNumericEntity_shouldNotTranslate() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("a&120;", 1, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_withInputEndingWithoutSemiColon_shouldNotTranslate() throws IOException {
        StringWriter writer = new StringWriter();
        int result = unescaper.translate("&#120", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testIsSet_withSetOption_shouldReturnTrue() {
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
    }

    @Test
    public void testIsSet_withUnsetOption_shouldReturnFalse() {
        unescaper = new NumericEntityUnescaper();
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
    }

    @After
    public void tearDown() {
        unescaper = null;
    }
}