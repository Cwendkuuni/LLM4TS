import static org.junit.Assert.*;
import org.jsoup.parser.ParseSettings;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
}