
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Node;
import org.w3c.dom.Comment;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        W3CDom w3cDom = new W3CDom();

        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assert w3cDoc != null;
        Element root = w3cDoc.getDocumentElement();
        assert root != null;
        assert root.getTagName().equals("html");
        assert root.getChildNodes().getLength() == 1;
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Test</h1></body></html>");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        builder.convert(jsoupDoc, w3cDoc);

        assert w3cDoc != null;
        Element root = w3cDoc.getDocumentElement();
        assert root != null;
        assert root.getTagName().equals("html");
        assert root.getChildNodes().getLength() == 1;
    }

    @Test
    public void testConvertWithTextNodes() {
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Test</h1><p>Paragraph</p></body></html>");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        builder.convert(jsoupDoc, w3cDoc);

        assert w3cDoc != null;
        Element root = w3cDoc.getDocumentElement();
        assert root != null;
        assert root.getTagName().equals("html");
        assert root.getChildNodes().getLength() == 3;

        Node child = root.getChildNodes().item(1);
        assert child.getNodeType() == Node.ELEMENT_NODE;
        assert child.getNodeName().equals("p");
        assert child.getTextContent().equals("Paragraph");
    }

    @Test
    public void testConvertWithCommentNodes() {
        Document jsoupDoc = Jsoup.parse("<html><body><!-- Test Comment --><h1>Test</h1></body></html>");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        builder.convert(jsoupDoc, w3cDoc);

        assert w3cDoc != null;
        Element root = w3cDoc.getDocumentElement();
        assert root != null;
        assert root.getTagName().equals("html");
        assert root.getChildNodes().getLength() == 3;

        Node child = root.getChildNodes().item(0);
        assert child.getNodeType() == Node.COMMENT_NODE;
        assert child.getNodeValue().equals(" Test Comment ");
    }

    @Test
    public void testConvertWithDataNodes() {
        Document jsoupDoc = Jsoup.parse("<html><body><script>console.log('Test');</script><h1>Test</h1></body></html>");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();

        builder.convert(jsoupDoc, w3cDoc);

        assert w3cDoc != null;
        Element root = w3cDoc.getDocumentElement();
        assert root != null;
        assert root.getTagName().equals("html");
        assert root.getChildNodes().getLength() == 3;

        Node child = root.getChildNodes().item(0);
        assert child.getNodeType() == Node.TEXT_NODE;
        assert child.getNodeValue().equals("console.log('Test');");
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("test");
        jsoupElement.attr("key1", "value1");
        jsoupElement.attr("key2", "value2");

        HashMap<String, String> namespaces = new HashMap<>();
        Element w3cElement = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument().createElement("test");

        builder.copyAttributes(jsoupElement, w3cElement);

        assert w3cElement.getAttributes().getLength() == 2;
        assert w3cElement.getAttribute("key1").equals("value1");
        assert w3cElement.getAttribute("key2").equals("value2");
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(null);
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("test");

        jsoupElement.attr("xmlns", "urn:default");
        jsoupElement.attr("xmlns:prefix", "urn:prefix");

        String prefix = builder.updateNamespaces(jsoupElement);

        assert prefix.equals("prefix");
        assert builder.namespacesStack.peek().size() == 2;
        assert builder.namespacesStack.peek().get("").equals("urn:default");
        assert builder.namespacesStack.peek().get("prefix").equals("urn:prefix");

        jsoupElement.attr("xmlns:prefix2", "urn:prefix2");
        prefix = builder.updateNamespaces(jsoupElement);

        assert prefix.equals("prefix2");
        assert builder.namespacesStack.peek().size() == 3;
        assert builder.namespacesStack.peek().get("").equals("urn:default");
        assert builder.namespacesStack.peek().get("prefix").equals("urn:prefix");
        assert builder.namespacesStack.peek().get("prefix2").equals("urn:prefix2");

        jsoupElement.attr("xmlns:prefix", "urn:prefix-updated");
        prefix = builder.updateNamespaces(jsoupElement);

        assert prefix.equals("prefix");
        assert builder.namespacesStack.peek().size() == 3;
        assert builder.namespacesStack.peek().get("").equals("urn:default");
        assert builder.namespacesStack.peek().get("prefix").equals("urn:prefix-updated");
        assert builder.namespacesStack.peek().get("prefix2").equals("urn:prefix2");
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();

        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = w3cDoc.createElement("test");
        w3cDoc.appendChild(root);

        Text text = w3cDoc.createTextNode("Test text");
        root.appendChild(text);

        String result = w3cDom.asString(w3cDoc);

        assert result.equals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><test>Test text</test>");
    }
}
