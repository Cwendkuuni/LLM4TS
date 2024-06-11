import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class XmlTreeBuilderTest {

    @Test
    public void testParseSimpleXML() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child1></child1><child2><grandChild/></child2></root>";
        Document doc = xmlTreeBuilder.parse(input, null);

        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        Element child1 = root.child(0);
        assertEquals("child1", child1.tagName());
        assertEquals(0, child1.childElementCount());

        Element child2 = root.child(1);
        assertEquals("child2", child2.tagName());
        Element grandChild = child2.child(0);
        assertEquals("grandChild", grandChild.tagName());
        assertEquals(0, grandChild.childElementCount());
    }

    @Test
    public void testParseSelfClosingXML() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><img src=\"image.jpg\" /> <child/></root>";
        Document doc = xmlTreeBuilder.parse(input, null);

        Element root = doc.rootElement();
        Element img = root.child(0);
        assertEquals("img", img.tagName());
        assertEquals("image.jpg", img.attr("src"));
        assertEquals(0, img.childElementCount());

        Element child = root.child(1);
        assertEquals("child", child.tagName());
        assertEquals(0, child.childElementCount());

        assertFalse(img.isTagStart());
        assertFalse(img.isTagEnd());
    }

    @Test
    public void testParseEmptyXML() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root />";
        Document doc = xmlTreeBuilder.parse(input, null);

        Element root = doc.rootElement();
        assertEquals("root", root.tagName());
        assertEquals(0, root.childElementCount());
    }

    @Test
    public void testParseXMLWithComments() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><!-- comment1 --><child><!-- comment2 --><grandChild /></child></root>";
        Document doc = xmlTreeBuilder.parse(input, null);

        Element root = doc.rootElement();
        Element child = root.child(0);
        Element grandChild = child.child(0);

        Comment comment1 = (Comment) root.child(0);
        assertEquals("comment1", comment1.text());

        Comment comment2 = (Comment) grandChild.previousSibling();
        assertEquals("comment2", comment2.text());
    }

    @Test
    public void testParseXMLWithErrors() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child><child1/><child2><child3 < /child2></child>";
        ParseErrorList errorList = new ParseErrorList();

        xmlTreeBuilder.parse(input, null, errorList, ParseSettings.preserveCase);

        assertThrows(IllegalArgumentException.class, () -> xmlTreeBuilder.parse(input, null));
        assertEquals(3, errorList.size());

        ParseError error1 = errorList.get(0);
        assertEquals("Unexpected end tag : CHILD", error1.getMessage());

        ParseError error2 = errorList.get(1);
        assertEquals("Unexpected start tag : CHILD3", error2.getMessage());

        ParseError error3 = errorList.get(2);
        assertEquals("Unexpected end tag : CHILD3", error3.getMessage());
    }

    @Test
    public void testParseXMLFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child><grandChild /></child></root>";
        Document doc = xmlTreeBuilder.parseFragment(input, null, new ParseErrorList(), ParseSettings.preserveCase);

        Element root = doc.rootElement();
        Element child = root.child(0);
        Element grandChild = child.child(0);

        assertEquals(1, doc.childNodeCount());
        assertEquals("root", root.tagName());
        assertEquals(1, root.childElementCount());
        assertEquals("child", child.tagName());
        assertEquals(1, child.childElementCount());
        assertEquals("grandChild", grandChild.tagName());
        assertEquals(0, grandChild.childElementCount());
    }

    @Test
    public void testParseXMLWithUnknownTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><unknownTag /></root>";
        Document doc = xmlTreeBuilder.parse(input, null);

        Element root = doc.rootElement();
        Element unknownTag = root.child(0);

        assertEquals(1, doc.childNodeCount());
        assertEquals("root", root.tagName());
        assertEquals(1, root.childElementCount());
        assertEquals("unknownTag", unknownTag.tagName());
        assertNull(unknownTag.previousSibling());
        assertTrue(unknownTag.isSelfClosing());
    }
}