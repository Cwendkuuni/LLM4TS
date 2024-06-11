
package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        Document doc = xmlTreeBuilder.parse("<div></div>", "");

        assertEquals("div", doc.child(0).nodeName());
    }

    @Test
    public void testParseWithReader() {
        Document doc = xmlTreeBuilder.parse(new StringReader("<div></div>"), "");

        assertEquals("div", doc.child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(new StringReader("<div></div>"), "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals(doc, xmlTreeBuilder.getCurrentElement());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());

        xmlTreeBuilder.process(startTag);

        assertEquals("div", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.process(endTag);

        assertEquals(Stack.empty(), xmlTreeBuilder.getStack());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.process(commentToken);

        assertEquals("This is a comment", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");

        xmlTreeBuilder.process(characterToken);

        assertEquals("This is a character", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");

        xmlTreeBuilder.process(doctype);

        assertEquals("html", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element("div");

        xmlTreeBuilder.insertNode(node);

        assertEquals(node, xmlTreeBuilder.getCurrentElement().child(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("div", element.nodeName());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals("This is a comment", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");

        xmlTreeBuilder.insert(characterToken);

        assertEquals("This is a character", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");

        xmlTreeBuilder.insert(doctype);

        assertEquals("html", xmlTreeBuilder.getCurrentElement().nodeName());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element element = new Element("div");
        xmlTreeBuilder.getStack().push(element);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(Stack.empty(), xmlTreeBuilder.getStack());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment("<div></div>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);

        assertEquals("div", nodes.get(0).nodeName());
    }
}
