import static org.junit.Assert.assertEquals;
import org.jsoup.parser.ParseSettings;

public class XmlTreeBuilderTest {
    // 
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }
    // 
}