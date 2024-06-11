package org.jsoup.helper;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {

    private W3CDom w3CDom;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
    }

    @Test
    public void fromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void convert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("");
        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("");
        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("");

        w3CDom.convert(jsoupDoc, w3cDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void asString() {
        Document w3cDoc = null; //Provide a W3C Document object

        String w3cString = w3CDom.asString(w3cDoc);

        assertNotNull(w3cString);
        assertTrue(w3cString instanceof String);
    }
}