import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    // 
    @Test
    public void testPreserveTagCase() {
        ParseSettings settings = new ParseSettings(true, false);
        String tagName = settings.normalizeTag("TAG");
        assertEquals("TAG", tagName);
    }

    // 
}