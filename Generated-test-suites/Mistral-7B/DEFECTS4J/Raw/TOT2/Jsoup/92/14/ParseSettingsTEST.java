import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings settingsHtmlDefault = new ParseSettings(false, false);
        ParseSettings settingsPreserveCase = new ParseSettings(true, true);
        assertEquals(false, settingsHtmlDefault.preserveTagCase());
        assertEquals(true, settingsPreserveCase.preserveTagCase());
    }
}