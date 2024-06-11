import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        boolean preserveTagCase = parseSettings.preserveTagCase();
        assertEquals(true, preserveTagCase);
    }
}