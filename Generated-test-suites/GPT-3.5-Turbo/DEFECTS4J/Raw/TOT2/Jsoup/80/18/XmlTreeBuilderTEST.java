package org.jsoup.parser;

import static org.junit.Assert.*;

import org.junit.Test;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<xml></xml>"), "http://example.com");
        assertNotNull(doc);
        assertEquals("xml", doc.child(0).nodeName());
    }

    @Test
    public void testParseWithString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<xml></xml>", "http://example.com");
        assertNotNull(doc);
        assertEquals("xml", doc.child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader("<xml></xml>"), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(Document.OutputSettings.Syntax.xml, builder.document().outputSettings().syntax());
    }

    // Add more test cases for other methods in XmlTreeBuilder class
    
}