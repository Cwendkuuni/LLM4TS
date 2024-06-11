import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringReader;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testParseReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<html></html>"), "http://example.com");
        assertNotNull(doc);
    }
    
    @Test
    public void testParseString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com", new ParseSettings(false));
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
    }

    // Other test methods for insert, popStackToClose, parseFragment can be added here

}