
import org.junit.*;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    
    NumericEntityUnescaper unescaper;

    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testIsSet() {
        assertTrue(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
    }

    @Test
    public void testTranslate_NormalInput() {
        StringBuilder builder = new StringBuilder();
        unescaper.translate("&#65;", 0, new StringWriter(builder));
        assertEquals("A", builder.toString());
    }

    @Test
    public void testTranslate_HexInput() {
        StringBuilder builder = new StringBuilder();
        unescaper.translate("&#x41;", 0, new StringWriter(builder));
        assertEquals("A", builder.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_SemiColonRequiredError() throws IOException {
        unescaper.translate("&#65", 0, new StringWriter());
    }

    @Test
    public void testTranslate_SemiColonOptional() {
        StringBuilder builder = new StringBuilder();
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        unescaper.translate("&#65", 0, new StringWriter(builder));
        assertEquals("A", builder.toString());
    }

    @Test
    public void testTranslate_ErrorIfNoSemiColon() {
        StringBuilder builder = new StringBuilder();
        unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        try {
            unescaper.translate("&#65", 0, new StringWriter(builder));
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Exception expected
            assertTrue(e.getMessage().contains("Semi-colon required"));
        }
    }

    @Test
    public void testTranslate_LargeEntityValue() {
        StringBuilder builder = new StringBuilder();
        unescaper.translate("&#65536;", 0, new StringWriter(builder));
        assertEquals("\uD800\uDC00", builder.toString());
    }

    @Test
    public void testTranslate_NoEntityValue() {
        StringBuilder builder = new StringBuilder();
        assertEquals(0, unescaper.translate("&;", 0, new StringWriter(builder)));
        assertEquals("", builder.toString());
    }

}

