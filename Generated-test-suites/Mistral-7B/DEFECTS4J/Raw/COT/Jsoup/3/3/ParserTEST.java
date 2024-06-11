import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.TokenQueue;
import org.jsoup.parser.Tokenizer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class ParserTest {

    private Parser parser;
    private Tokenizer tokenizer;
    private TokenQueue tokenQueue;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        tokenizer = mock(Tokenizer.class);
        tokenQueue = new TokenQueue();
        parser = new Parser(tokenizer, null, false);
        doReturn(tokenQueue).when(tokenizer).tokenize(anyString());
    }

    @Test
    public void testParseEmptyHtml() throws IOException {
        String emptyHtml = "";
        Document document = Parser.parse(emptyHtml, "baseUri");
        assertNotNull(document);
        assertEquals("html", document.tagName());
        assertEquals("baseUri", document.baseUri());
        assertTrue(document.body().text().isEmpty());
    }

    @Test
    public void testParseValidHtml() throws IOException {
        String validHtml = "<html><head><title>Valid Test</title></head><body><h1>Hello World</h1></body></html>";
        Document document = Parser.parse(validHtml, "baseUri");
        assertNotNull(document);
        assertEquals("html", document.tagName());
        assertEquals("baseUri", document.baseUri());
        Element title = document.head();
        assertEquals("title", title.tagName());
        assertEquals("Valid Test", title.text());
        Element body = document.body();
        assertEquals("h1", body.tagName());
        assertEquals("Hello World", body.text());
    }

    @Test
    public void testParseHtmlWithComments() throws IOException {
        String htmlWithComments = "<html><!-- This is a comment -->" +
                                 "<head><title>Valid Test</title>" +
                                 " <!-- Another comment -->" +
                                 "</head><body>" +
                                 "<h1>Hello World</h1>" +
                                 "</body></html>";
        Document document = Parser.parse(htmlWithComments, "baseUri");
        assertNotNull(document);
        Element commentElement = document.body().child(0);
        assertTrue(commentElement instanceof Comment);
        assertEquals("This is a comment", commentElement.text());
    }

    @Test
    public void testParseHtmlWithCdata() throws IOException {
        String htmlWithCdata = "<html><html xmlns='http://www.w3.org/1999/xhtml'>" +
                               "<head><title>Valid Test</title>" +
                               "<body>" +
                               "<img src='<![CDATA[http://example.com/image.png]]>' alt='Image'>" +
                               "</body></html>";
        Document document = Parser.parse(htmlWithCdata, "baseUri");
        assertNotNull(document);
        Element imgElement = document.body().child(0);
        assertTrue(imgElement.tagName().equals("img"));
        assertEquals("http://example.com/image.png", imgElement.absUrl("src"));
    }

    @Test
    public void testParseHtmlWithXmlDecl() throws IOException {
        String htmlWithXmlDecl = "<html xmlns='http://www.w3.org/1999/xhtml'>" +
                                 "<head><title>Valid Test</title>" +
                                 "<xml-declaration version='1.0' encoding='UTF-8' standalone='yes'/>" +
                                 "</head><body>" +
                                 "<h1>Hello World</h1>" +
                                 "</body></html>";
        Document document = Parser.parse(htmlWithXmlDecl, "baseUri");
        assertNotNull(document);
        Element xmlDeclarationElement = document.head().child(1);
        assertTrue(xmlDeclarationElement instanceof XmlDeclaration);
        XmlDeclaration xmlDeclaration = (XmlDeclaration) xmlDeclarationElement;
        assertEquals("1.0", xmlDeclaration.version());
        assertEquals("UTF-8", xmlDeclaration.encoding());
        assertTrue(xmlDeclaration.isStandalone());
    }

    @Test
    public void testParseHtmlWithAttributes() throws IOException {
        String htmlWithAttributes = "<html><html xmlns='http://www.w3.org/1999/xhtml'>" +
                                    "<head><title>Valid Test</title>" +
                                    "<body><a href='http://example.com/link' class='external'></a></body></html>";
        Document document = Parser.parse(htmlWithAttributes, "baseUri");
        assertNotNull(document);
        Element linkElement = document.body().child(0);
        assertTrue(linkElement.tagName().equals("a"));
        List<Attribute> attributes = linkElement.attributes();
        assertEquals(2, attributes.size());
        Attribute hrefAttribute = attributes.get(0);
        assertEquals("href", hrefAttribute.key());
        assertEquals("http://example.com/link", hrefAttribute.value());
        Attribute classAttribute = attributes.get(1);
        assertEquals("class", classAttribute.key());
        assertEquals("external", classAttribute.value());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidStartTag() throws IOException {
        String invalidStartTagHtml = "<html><body><invalidTag></html>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(invalidStartTagHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithEmptyTag() throws IOException {
        String emptyTagHtml = "<html><body><img/></html>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(emptyTagHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithMissingEndTag() throws IOException {
        String missingEndTagHtml = "<html><body><p>Hello World<script";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(missingEndTagHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidEndTag() throws IOException {
        String invalidEndTagHtml = "<html><body><p>Hello World</p></div>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(invalidEndTagHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidTagNesting() throws IOException {
        String invalidTagNestingHtml = "<html><body><div><div>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(invalidTagNestingHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidTagName() throws IOException {
        String invalidTagNameHtml = "<html><body><invalidTag>Hello World</invalidTag>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(invalidTagNameHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidAttributeName() throws IOException {
        String invalidAttributeNameHtml = "<html><body><a href='http://example.com/link' invalidAttribute='value'></a>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(invalidAttributeNameHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidAttributeValue() throws IOException {
        String invalidAttributeValueHtml = "<html><body><a href='http://example.com/link' class='<script>alert(\"Hello World\")</script>'></a>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(invalidAttributeValueHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseHtmlWithInvalidXmlDecl() throws IOException {
        String invalidXmlDeclHtml = "<html xmlns:foo='http://example.com/foo'>" +
                                    "<head><title>Valid Test</title>" +
                                    "<xml-declaration version='1.2' encoding='UTF-8' standalone='yes'/>" +
                                    "</head><body>" +
                                    "<h1>Hello World</h1>" +
                                    "</body></html>";
        Parser parser = new Parser(tokenizer, null, false);
        parser.parse(invalidXmlDeclHtml, "baseUri");
        fail("Expected IllegalArgumentException");
    }
}