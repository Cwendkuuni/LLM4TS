package org.jsoup.parser;
    
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = new Document("");
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        
        assertTrue(parseSettings.preserveCase());
        assertFalse(parseSettings.html());
        assertFalse(parseSettings.useMathML());
        assertFalse(parseSettings.useSVG());
    }
    
    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("");
        String baseUri = "";

        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testParseStringString() {
        String input = "";
        String baseUri = "";

        Document parsedDoc = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals(doc, parsedDoc);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("");
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token token = new Token.StartTag("tag");
        
        Element element = xmlTreeBuilder.insert(token.asStartTag());
        
        assertEquals(Tag.valueOf("tag", xmlTreeBuilder.settings), element.tag());
        assertEquals("", element.baseUri());
        assertEquals(xmlTreeBuilder.settings.normalizeAttributes(new Attributes()), element.attributes());
    }

    @Test
    public void testProcessEndTag() {
        Token token = new Token.EndTag("tag");
        
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag", xmlTreeBuilder.settings), "", new Attributes()));

        xmlTreeBuilder.popStackToClose(token.asEndTag());
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(new Comment("comment"), xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("data");
        
        xmlTreeBuilder.insert(characterToken);
        
        assertEquals(new TextNode("data"), xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("name", "public", "system");

        DocumentType doctypeNode = new DocumentType(xmlTreeBuilder.settings.normalizeTag("name"), "public", "system");
        doctypeNode.setPubSysKey(doctypeToken.getPubSysKey());
        
        xmlTreeBuilder.insert(doctypeToken);
        
        assertEquals(doctypeNode, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag", xmlTreeBuilder.settings), "", new Attributes()));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "";
        String baseUri = "";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        assertEquals(doc.childNodes(), nodes);
    }

}