package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        String xml = "<xml><tag attr=\"value\"><subtag/></tag></xml>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");
        Element root = doc.rootElement();

        assertEquals("xml", root.tagName());
        assertEquals("http://test.com", root.baseUri());

        Element tag = root.elementSibling(0);
        assertEquals("tag", tag.tagName());
        assertNotNull(tag.attribute("attr"));
        assertEquals("value", tag.attribute("attr").getValue());

        Element subtag = tag.child(0);
        assertEquals("subtag", subtag.tagName());
        assertNull(subtag.attributes());
    }

    @Test
    public void testParseWithComments() {
        String xml = "<xml><!-- comment1 --><tag attr=\"value\"><!-- comment2 --><subtag/><!-- comment3 --></tag><!-- comment4 --></xml>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");

        Element root = doc.rootElement();
        assertEquals("xml", root.tagName());
        assertEquals("http://test.com", root.baseUri());

        Element tag = root.elementSibling(0);
        assertEquals("tag", tag.tagName());
        assertNotNull(tag.attribute("attr"));
        assertEquals("value", tag.attribute("attr").getValue());

        Comment comment1 = tag.child(0);
        assertEquals("comment1", comment1.data());

        Element subtag = tag.child(1);
        assertEquals("subtag", subtag.tagName());
        assertNull(subtag.attributes());

        Comment comment2 = subtag.child(0);
        assertEquals("comment2", comment2.data());

        Comment comment3 = subtag.child(1);
        assertEquals("comment3", comment3.data());

        Comment comment4 = doc.child(tag.lastSibling());
        assertEquals("comment4", comment4.data());
    }

    @Test
    public void testParseWithDocumentType() {
        String xml = "<!DOCTYPE xml [ <!ATTLIST xml foo CDATA #REQUIRED > ]><xml><tag attr=\"value\"><subtag/></tag></xml>";
        Document doc = new XmlTreeBuilder().parse(xml, "http://test.com");

        DocumentType doctype = doc.child(0);
        assertEquals("xml", doctype.nodeName());
        assertEquals("http://test.com", doctype.baseUri());
        assertEquals("[CDATA [ <!ATTLIST xml foo CDATA #REQUIRED >]]", doctype.getData());

        Element root = doc.rootElement();
        assertEquals("xml", root.tagName());
        assertEquals("http://test.com", root.baseUri());

        Element tag = root.elementSibling(0);
        assertEquals("tag", tag.tagName());
        assertNotNull(tag.attribute("attr"));
        assertEquals("value", tag.attribute("attr").getValue());

        Element subtag = tag.child(0);
        assertEquals("subtag", subtag.tagName());
        assertNull(subtag.attributes());
    }

    @Test
    public void testParseFragment() {
        String xmlFragment = "<tag attr=\"value\">Content</tag>";
        List<Node> nodes = new XmlTreeBuilder().parseFragment(xmlFragment, "http://test.com", ParseErrorList.noTracking(), ParseSettings.preserveCase).stream().filter(n -> n != null).collect(Collectors.toList());

        Element tag = (Element) nodes.get(0);
        assertEquals("tag", tag.tagName());
        assertNotNull(tag.attribute("attr"));
        assertEquals("value", tag.attribute("attr").getValue());
        assertEquals("Content", tag.text());
    }

    @Test(expected = AssertionError.class)
    public void testValidateFail() {
        String xml = "<xml><!-- foo --><tag/>";
        new XmlTreeBuilder().parse("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + xml, "http://test.com");
        Validate.fail("Unexpected token type: " + Token.Type.COMMENT.getValue());
    }
}