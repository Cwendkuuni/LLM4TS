import org.junit.Test;
import org.jsoup.parser.ParseSettings;
import static org.junit.Assert.*;

public class ParseSettingsTest {

    @Test
    public void testHtmlDefault() {
        // Test the htmlDefault static variable
        ParseSettings htmlDefault = ParseSettings.htmlDefault;
        assertTrue(htmlDefault.preserveTagCase());
        assertFalse(htmlDefault.preserveAttributeCase());
    }

    @Test
    public void testPreserveCase() {
        // Test the preserveCase static variable
        ParseSettings preserveCase = ParseSettings.preserveCase;
        assertTrue(preserveCase.preserveTagCase());
        assertTrue(preserveCase.preserveAttributeCase());
    }

    // Add test cases for other methods here...
}