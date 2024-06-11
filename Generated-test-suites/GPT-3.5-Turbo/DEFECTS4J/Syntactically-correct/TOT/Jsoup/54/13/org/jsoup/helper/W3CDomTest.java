import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Test typical use case
        Document jsoupDoc = Parser.parseBodyFragment("<div><p>Hello world!</p></div>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Verify output
        Assert.assertNotNull(w3cDoc);
        NodeList elements = w3cDoc.getElementsByTagName("p");
        Assert.assertEquals(1, elements.getLength());
        Node element = elements.item(0);
        Assert.assertEquals("Hello world!", element.getTextContent());

        // Test edge case with empty input
        jsoupDoc = Parser.parseBodyFragment("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Verify output
        Assert.assertNotNull(w3cDoc);
        elements = w3cDoc.getElementsByTagName("p");
        Assert.assertEquals(0, elements.getLength());

        // Test error scenario with null input
        try {
            w3cDom.fromJsoup(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception correctly thrown
        }
    }

    @Test
    public void testConvert() {
        // Creating a JSoup document for testing
        Document jsoupDoc = Parser.parseBodyFragment("<div><p>Hello world!</p></div>");
        W3CDom w3cDom = new W3CDom();

        // Test typical use case
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document out = w3cDom.convert(jsoupDoc, w3cDoc);

        // Verify output
        Assert.assertEquals(w3cDoc, out);

        // Test edge case with empty input
        jsoupDoc = Parser.parseBodyFragment("");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        out = w3cDom.convert(jsoupDoc, w3cDoc);

        // Verify output
        Assert.assertEquals(w3cDoc, out);

        // Test error scenario with null inputs
        try {
            w3cDom.convert(jsoupDoc, null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception correctly thrown
        }
    }

    @Test
    public void testAsString() {
        // Creating a W3C document for testing
        Document jsoupDoc = Parser.parseBodyFragment("<div><p>Hello world!</p></div>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Test typical use case
        String xmlString = w3cDom.asString(w3cDoc);
        String expectedXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<div xmlns=\"\"><p>Hello world!</p></div>";
        Assert.assertEquals(expectedXmlString, xmlString);

        // Test edge case with empty input
        Document emptyJsoupDoc = Parser.parseBodyFragment("");
        org.w3c.dom.Document emptyW3cDoc = w3cDom.fromJsoup(emptyJsoupDoc);
        String emptyXmlString = w3cDom.asString(emptyW3cDoc);
        String expectedEmptyXmlString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>" +
                "<div xmlns=\"\"></div>";
        Assert.assertEquals(expectedEmptyXmlString, emptyXmlString);

        // Test error scenario with null input
        try {
            w3cDom.asString(null);
            Assert.fail("Expected NullPointerException to be thrown");
        } catch (NullPointerException e) {
            // Exception correctly thrown
        }
    }
}