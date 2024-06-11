package org.jsoup.parser;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Test class for the Parser class.
 */
@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

    private Parser parser;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        parser = new Parser("", "", false);
    }

    

    // Test case for the public constructor
    @Test
    public void testParserConstructor() {
        assertNotNull(parser);
    }

    // Test case for the parse method
    @Test
    public void testParse() {
        String html = "<html><head><title>Test Title</title></head>" +
                "<body><h1>Test Heading</h1><p>Test Paragraph</p></body></html>";
        Document doc = Parser.parse(html, "");
        assertEquals("Test Title", doc.title().text());
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    // Test case for the parseBodyFragment method
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<body><h1>Test Heading</h1><p>Test Paragraph</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        assertEquals("Test Heading", doc.select("h1").text());
        assertEquals("Test Paragraph", doc.select("p").text());
    }

    // Test case for parsing empty HTML
    @Test
    public void testParseEmptyHtml() {
        Document doc = Parser.parse("", "");
        assertEquals(0, doc.childNodes().size());
    }

    // Test case for parsing invalid HTML
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidHtml() {
        Parser.parse("", ""); // empty string is not a valid HTML
    }

    // Test case for parsing a fragment with a base tag
    @Test
    public void testParseBodyFragmentWithBaseTag() {
        String bodyHtml = "<body><base href='/test'><h1>Test Heading</h1><p>Test Paragraph</p></body>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");
        assertEquals("/test", doc.baseUri());
    }

    // Test case for handling comments
    @Test
    public void testParseComment() {
        String html = "<html><!-- Test Comment --></html>";
        Document doc = Parser.parse(html, "");
        Element comment = doc.select("meta").first();
        assertEquals("Test Comment", comment.text());
    }

    // Test case for handling CDATA sections
    @Test
    public void testParseCdata() {
        String html = "<html><![CDATA[Test CDATA Section]]></html>";
        Document doc = Parser.parse(html, "");
        Element cdata = doc.select("meta").first();
        assertEquals("Test CDATA Section", cdata.text());
    }

    // Test case for handling XML declarations
    @Test
    public void testParseXmlDeclaration() {
        String html = "<html xmlns='http://www.w3.org/1999/xhtml' xml:lang='en'><head><title>Test Title</title></head><body><h1>Test Heading</h1></body></html>";
        Document doc = Parser.parse(html, "");
        XmlDeclaration xmlDeclaration = doc.select("meta[xml:lang='en'][xmlns]").first();
        assertEquals("http://www.w3.org/1999/xhtml", xmlDeclaration.xmlns());
        assertEquals("en", xmlDeclaration.xmlLang());
    }

    // Test case for handling empty tags
    @Test
    public void testParseEmptyTag() {
        String html = "<html><body><img src='image.png' /></body></html>";
        Document doc = Parser.parse(html, "");
        Element img = doc.select("img").first();
        assertTrue(img.isEmpty());
    }

    // Test case for handling self-closing tags
    @Test
    public void testParseSelfClosingTag() {
        String html = "<html><body><input type='text' /></body></html>";
        Document doc = Parser.parse(html, "");
        Element input = doc.select("input").first();
        assertTrue(input.isEmpty());
    }

    // Test case for handling improperly nested tags
    @Test(expected = IllegalArgumentException.class)
    public void testParseImproperlyNestedTags() {
        String html = "<html><body><div><div></div></div></body></html>";
    }

    // Test case for handling invalid tags
    @Test(expected = IllegalArgumentException.class)
    public void testParseInvalidTag() {
        String html = "<html><body><div><invalid></invalid></div></body></html>";
    }

    // Test case for handling attributes with empty values
    @Test
    public void testParseAttributeWithEmptyValue() {
        String html = "<html><body><div class=''></div></body></html>";
        Document doc = Parser.parse(html, "");
        Element div = doc.select("div").first();
        List<Attribute> attributes = div.attributes();
        assertEquals(1, attributes.size());
        assertEquals("class", attributes.get(0).key());
        assertEquals("", attributes.get(0).value());
    }

    // Test case for handling attributes with special characters
    @Test
    public void testParseAttributeWithSpecialCharacter() {
        String html = "<html><body><div id='test & quote\' class='class &amp; name'></div></body></html>";
        Document doc = Parser.parse(html, "");
        Element div = doc.select("div").first();
        List<Attribute> attributes = div.attributes();
        assertEquals(3, attributes.size());
        assertEquals("id", attributes.get(0).key());
        assertEquals("test & quote'", attributes.get(0).value());
        assertEquals("class", attributes.get(1).key());
        assertEquals("class &amp; name", attributes.get(1).value());
    }

    // Test case for handling attributes with multiple values
    @Test
    public void testParseAttributeWithMultipleValues() {
        String html = "<html><body><div class='class1 class2'></div></body></html>";
        Document doc = Parser.parse(html, "");
        Element div = doc.select("div").first();
        List<Attribute> attributes = div.attributes();
        assertEquals(1, attributes.size());
        assertEquals("class", attributes.get(0).key());
        List<String> values = Arrays.asList(attributes.get(0).value().split(" "));
        assertTrue(values.contains("class1"));
        assertTrue(values.contains("class2"));
    }

    // Test case for handling text nodes with special characters
    @Test
    public void testParseTextNodeWithSpecialCharacter() {
        String html = "<html><body><p>Test &amp; Quote</p></body></html>";
        Document doc = Parser.parse(html, "");
        TextNode textNode = (TextNode) doc.select("p").first().childNodes().get(0);
        assertEquals("Test & Quote", textNode.text());
    }

    
}