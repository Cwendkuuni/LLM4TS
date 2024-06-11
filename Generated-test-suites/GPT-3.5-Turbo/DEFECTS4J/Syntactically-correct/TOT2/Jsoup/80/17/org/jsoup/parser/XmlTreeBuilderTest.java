import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Token;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<html></html>");
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseStringString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<html></html>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
    }

    // Other method tests for XmlTreeBuilder class can go here

}