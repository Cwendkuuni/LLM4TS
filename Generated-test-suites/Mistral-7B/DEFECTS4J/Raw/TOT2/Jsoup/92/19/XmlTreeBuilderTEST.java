package org.jsoup.parser.test;

import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.*;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testProcessStartTag() throws Exception {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document doc = xmlBuilder.parse(new StringReader("<testTag attr='value'/>"), null);
        Element testTag = doc.selectElements("testTag").first();
        assertTrue(testTag != null);
        assertTrue(testTag.tagName().equals("testTag"));
        assertTrue(testTag.attr("attr").equals("value"));
    }

    @Test
    public void testEmptyXmlDocument() throws Exception {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document doc = xmlBuilder.parse(new StringReader(""), null);
        assertTrue(doc.childNodes().isEmpty());
    }

    @Test
    public void testXmlDocumentWithComments() throws Exception {
        XmlTreeBuilder xmlBuilder = new XmlTreeBuilder();
        Document doc = xmlBuilder.parse(new StringReader("<test><!-- Comment --></test>"), null);
        Node commentNode = doc.childNodes().get(0);
        assertTrue(commentNode instanceof Comment);
    }

    // Add test cases for other edge cases and exceptions

}
//