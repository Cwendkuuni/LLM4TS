import org.junit.Test;
import org.jsoup.parser.ParseSettings;

public class ParseSettingsTest {

    @Test
    public void testPreserveTagCase() {
        ParseSettings htmlDefault = new ParseSettings(false, false);
        ParseSettings preserveCase = new ParseSettings(true, true);

        assertFalse(htmlDefault.preserveTagCase());
        assertTrue(preserveCase.preserveTagCase());
    }
}