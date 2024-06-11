package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder builder;

    public XmlTreeBuilderTest() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilderTest.MockReader reader = new XmlTreeBuilderTest.MockReader();
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        
        builder.initialiseParse(reader, baseUri, parser);
        
        assertEquals(builder.stack.size(), 1);
        assertEquals(builder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testParse() {
        XmlTreeBuilderTest.MockReader reader = new XmlTreeBuilderTest.MockReader();
        String baseUri = "http://example.com";
        
        Document document = builder.parse(reader, baseUri);
        
        assertEquals(document.baseUri(), baseUri);
    }

    @Test
    public void testParseString() {
        String input = "<html><body>Test</body></html>";
        String baseUri = "http://example.com";
        
        Document document = builder.parse(input, baseUri);
        
        assertEquals(document.baseUri(), baseUri);
    }

    @Test
    public void testProcess() {
        Token.StartTag startTag = new Token.StartTag("div");
        Token.EndTag endTag = new Token.EndTag("div");
        Token.Comment commentToken = new Token.Comment("Test comment");
        Token.Character characterToken = new Token.Character("Test character");
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        Token.EOF eofToken = new Token.EOF();

        builder.process(startTag);
        builder.process(endTag);
        builder.process(commentToken);
        builder.process(characterToken);
        builder.process(doctypeToken);
        builder.process(eofToken);

        // assertion statements here
    }

    @Test
    public void testInsertNode() {
        Node node = new Node("");

        builder.insertNode(node);

        // assertion statements here
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");

        Element element = builder.insert(startTag);

        // assertion statements here
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Test comment");

        builder.insert(commentToken);

        // assertion statements here
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Test character");

        builder.insert(characterToken);

        // assertion statements here
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");

        builder.insert(doctypeToken);

        // assertion statements here
    }

    @Test
    public void testPopStackToClose() {
        Element element1 = new Element("div");
        Element element2 = new Element("span");
        Element element3 = new Element("p");

        builder.stack.add(element1);
        builder.stack.add(element2);
        builder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("span");

        builder.popStackToClose(endTag);

        // assertion statements here
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<div><span>Test</span></div>";
        String baseUri = "http://example.com";
        Parser parser = new Parser();

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);

        // assertion statements here
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<span>Test</span>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        Parser parser = new Parser();

        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);

        // assertion statements here
    }

    private class MockReader extends Reader {
        public int read(char[] cbuf, int off, int len) {
            return -1;
        }

        public void close() {
            // do nothing
        }
    }
}