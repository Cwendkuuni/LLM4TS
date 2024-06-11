package org.jsoup.helper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;
import org.w3c.dom.Document;
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


public class W3CDomTest {

    private static class TestNodeVisitor implements NodeVisitor {
        private static final String xmlnsKey = "xmlns";
        private static final String xmlnsPrefix = "xmlns:";
        private final StringBuilder serializedDoc;
        private final HashMap<String, String> namespaces = new HashMap<String, String>(); // prefix => urn
        private Element dest;

        public TestNodeVisitor(StringBuilder serializedDoc) {
            this.serializedDoc = serializedDoc;
        }

        public void head(org.jsoup.nodes.Node source, int depth) {
            if (source instanceof org.jsoup.nodes.Element) {
                org.jsoup.nodes.Element sourceEl = (org.jsoup.nodes.Element) source;

                String prefix = updateNamespaces(sourceEl);
                String namespace = namespaces.get(prefix);

                serializedDoc.append("<").append(sourceEl.tagName()).append(" ");
                copyAttributes(sourceEl);
                serializedDoc.append(">");

                dest = sourceEl; // descend
            } else if (source instanceof org.jsoup.nodes.TextNode) {
                org.jsoup.nodes.TextNode sourceText = (org.jsoup.nodes.TextNode) source;
                serializedDoc.append(sourceText.getWholeText());
            } else if (source instanceof org.jsoup.nodes.Comment) {
                org.jsoup.nodes.Comment sourceComment = (org.jsoup.nodes.Comment) source;
                serializedDoc.append("<!--").append(sourceComment.getData()).append("-->");
            } else if (source instanceof org.jsoup.nodes.DataNode) {
                org.jsoup.nodes.DataNode sourceData = (org.jsoup.nodes.DataNode) source;
                serializedDoc.append(sourceData.getWholeData());
            } else {
                // unhandled
            }
        }

        public void tail(org.jsoup.nodes.Node source, int depth) {
            if (source instanceof org.jsoup.nodes.Element) {
                org.jsoup.nodes.Element sourceEl = (org.jsoup.nodes.Element) source;

                serializedDoc.append("</").append(sourceEl.tagName()).append(">");
                dest = (Element) dest.getParent(); // undescend. cromulent.
            }
        }

        private void copyAttributes(org.jsoup.nodes.Element el) {
            Attributes attributes = el.attributes();
            for (Attribute attribute : attributes) {
                String key = attribute.getKey().replaceAll("[^-a-zA-Z0-9_:.]", "");
                serializedDoc.append(key).append("=\"").append(attribute.getValue()).append("\" ");
            }
        }

        /**
         * Finds any namespaces defined in this element. Returns any tag prefix.
         */
        private String updateNamespaces(org.jsoup.nodes.Element el) {
            // scan the element for namespace declarations
            // like: xmlns="blah" or xmlns:prefix="blah"
            Attributes attributes = el.attributes();
            for (Attribute attr : attributes) {
                String key = attr.getKey();
                String prefix;
                if (key.equals(xmlnsKey)) {
                    prefix = "";
                } else if (key.startsWith(xmlnsPrefix)) {
                    prefix = key.substring(xmlnsPrefix.length());
                } else {
                    continue;
                }
                namespaces.put(prefix, attr.getValue());
            }

            // get the element prefix if any
            int pos = el.tagName().indexOf(":");
            return pos > 0 ? el.tagName().substring(0, pos) : "";
        }

    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
        String serializedW3cDoc = new W3CDom().asString(w3cDoc);

        StringBuilder serializedExpectedDoc = new StringBuilder();
        TestNodeVisitor visitor = new TestNodeVisitor(serializedExpectedDoc);
        NodeTraversor traversor = new NodeTraversor(visitor);
        traversor.traverse(jsoupDoc);

        assertEquals(serializedExpectedDoc.toString(), serializedW3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = null; // Assign a test W3CDom object
        new W3CDom().convert(jsoupDoc, w3cDoc);

        StringBuilder serializedExpectedDoc = new StringBuilder();
        TestNodeVisitor visitor = new TestNodeVisitor(serializedExpectedDoc);
        NodeTraversor traversor = new NodeTraversor(visitor);
        traversor.traverse(jsoupDoc);

        String serializedW3cDoc = new W3CDom().asString(w3cDoc);
        assertEquals(serializedExpectedDoc.toString(), serializedW3cDoc);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = null; // Assign a test W3CDom object
        String serializedW3cDoc = new W3CDom().asString(w3cDoc);
        // Compare the serializedW3cDoc with expected value using assert statement
    }
}