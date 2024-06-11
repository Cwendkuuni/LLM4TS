import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings expected = ParseSettings.preserveCase;
        ParseSettings actual = xmlTreeBuilder.defaultSettings();
        assertEquals(expected, actual);
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<html></html>"), "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseWithString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    // Add more test cases for other methods as needed

}