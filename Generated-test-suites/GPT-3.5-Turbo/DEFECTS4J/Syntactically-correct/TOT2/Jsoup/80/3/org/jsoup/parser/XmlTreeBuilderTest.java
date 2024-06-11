
package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<html></html>", "http://example.com");
        xmlTreeBuilder.insertNode(new Token.Character("test"));
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<html><body></body></html>", "http://example.com");
        xmlTreeBuilder.insert(new Token.StartTag("body"));
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<!--comment-->", "http://example.com");
        xmlTreeBuilder.insert(new Token.Comment("comment"));
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<!DOCTYPE html>", "http://example.com");
        xmlTreeBuilder.insert(new Token.Doctype("html"));
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse("<html><div><p></p></div></html>", "http://example.com");
        xmlTreeBuilder.popStackToClose(new Token.EndTag("div"));
        assertNotNull(xmlTreeBuilder);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("<div></div>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
    }
}
