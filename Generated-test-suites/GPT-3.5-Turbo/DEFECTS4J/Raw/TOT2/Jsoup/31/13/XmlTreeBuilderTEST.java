import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Parser.ParseErrorList;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        String input = "<html></html>";
        String baseUri = "http://example.com";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Document doc = xmlTreeBuilder.getDocument();

        assertTrue(doc != null);
        assertTrue(doc.childNodes().size() == 1);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token.StartTag("div", null);
        xmlTreeBuilder.process(startTagToken);
        Document doc = xmlTreeBuilder.getDocument();

        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.getDocument();
        xmlTreeBuilder.insertNode(new Element("div"));
        
        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", null);
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("div", element.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Document doc = xmlTreeBuilder.getDocument();

        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a text node");
        xmlTreeBuilder.insert(characterToken);
        Document doc = xmlTreeBuilder.getDocument();

        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", null, null);
        xmlTreeBuilder.insert(doctypeToken);
        Document doc = xmlTreeBuilder.getDocument();

        assertEquals(1, doc.childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.insert(new Token.StartTag("div", null));
        xmlTreeBuilder.popStackToClose(endTag);
        Document doc = xmlTreeBuilder.getDocument();

        assertEquals(0, doc.childNodes().size());
    }

}