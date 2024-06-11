import org.jsoup.internal.TestUtil;
import org.jsoup.parser.*;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);

        assertEquals("html", doc.nodeName());
    }

    // Add more test cases here for each public method in the XmlTreeBuilder class

}