import org.junit.Test;
import org.jsoup.helper.CompareUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(parseSettings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse("<xml></xml>", "");
        assertEquals("<xml></xml>", document.outerHtml());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse("<xml></xml>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element element = xmlTreeBuilder.processStartTag(startTag);
        assertNotNull(element);
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.processEndTag(endTag);
        assertNull(xmlTreeBuilder.getCurrentElement());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        Comment comment = xmlTreeBuilder.processComment(commentToken);
        assertNotNull(comment);
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("character");
        TextNode textNode = xmlTreeBuilder.processCharacter(characterToken);
        assertNotNull(textNode);
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype", "", "", "");
        DocumentType documentType = xmlTreeBuilder.processDoctype(doctype);
        assertNotNull(documentType);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<xml></xml>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}