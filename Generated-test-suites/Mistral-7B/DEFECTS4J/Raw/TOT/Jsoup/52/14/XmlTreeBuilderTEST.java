import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testParseFragment() {
        String xmlFragment = "<root><child element=\"test\"/></root>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();

        Document doc = XmlTreeBuilderTest.parseFragment(xmlFragment, baseUri, errors);
        assertNotNull(doc);
        assertNotNull(doc.childNodes());
        assertEquals(1, doc.childNodes().size());
        Element root = (Element) doc.childNodes().get(0);
        assertEquals("root", root.nodeName());
        Element child = (Element) root.childNodes().get(0);
        assertEquals("child", child.nodeName());
        assertEquals("test", child.attributes().get("element"));
    }

    private static Document parseFragment(String inputFragment, String baseUri, ParseErrorList errors) {
        XmlTreeBuilder parser = new XmlTreeBuilder();
        return parser.parseFragment(inputFragment, baseUri, errors);
    }
}