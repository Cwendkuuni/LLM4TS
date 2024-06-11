package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testCreateDocumentWithBaseUri() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testCreateShellWithBaseUri() {
        String baseUri = "http://example.com";
        Document document = Document.createShell(baseUri);
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testHeadAccessors() {
        Document document = new Document("about:blank");
        Element head = document.head();
        Assert.assertNotNull(head);
        document.title("Test Title");
        String title = document.title();
        Assert.assertEquals("Test Title", title);
    }

    @Test
    public void testBodyAccessors() {
        Document document = new Document("about:blank");
        Element body = document.body();
        Assert.assertNull(body);
        document.title("Test Title");
        body = document.body();
        Assert.assertNotNull(body);
    }

    @Test
    public void testCreateElementWithBaseUri() {
        String baseUri = "http://example.com";
        Document document = new Document(baseUri);
        Element newElement = document.createElement("a");
        Assert.assertEquals(baseUri, newElement.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        Document document = new Document("about:blank");
        Element htmlEl = document.findFirstElementByTagName("html", document);
        document.normalise();
        Assert.assertNotNull(htmlEl.childNodes().get(0));
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testFindFirstElementByTagName() {
        Document document = new Document("about:blank");
        Element htmlEl = document.findFirstElementByTagName("html", document);
        Assert.assertNotNull(htmlEl);
        Element head = document.findFirstElementByTagName("head", htmlEl);
        Assert.assertNotNull(head);
        Element body = document.findFirstElementByTagName("body", htmlEl);
        Assert.assertNotNull(body);
        Assert.assertNull(document.findFirstElementByTagName("button", document));
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameWithNullNode() {
        Document document = new Document("about:blank");
        findFirstElementByTagName(null, document);
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("about:blank");
        document.title("Test Title");
        String html = document.outerHtml();
        Assert.assertTrue(html.contains("<html><head><title>Test Title</title></head><body></body></html>"));
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("about:blank");
        document.text("Test Text");
        Element body = document.body();
        TextNode textNode = (TextNode) body.childNodes().get(0);
        Assert.assertEquals("Test Text", textNode.text());
    }

    @Test
    public void testCloneMethod() {
        Document document1 = new Document("about:blank");
        OutputSettings outputSettings1 = document1.outputSettings();
        Document document2 = document1.clone();
        OutputSettings outputSettings2 = document2.outputSettings();
        Assert.assertEquals(outputSettings1, outputSettings2);
    }

    private void findFirstElementByTagName(Node node, Document document) {
        if (node != null) {
            if (node.nodeName().equals("html")) {
                Element htmlEl = (Element) node;
                Element head = document.findFirstElementByTagName("head", htmlEl);
                Element body = document.findFirstElementByTagName("body", htmlEl);
                Assert.assertNotNull(head);
                Assert.assertNotNull(body);
            } else {
                for (Node child : node.childNodes()) {
                    findFirstElementByTagName(child, document);
                }
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateDocumentWithNullBaseUri() {
        String baseUri = null;
        Document document = new Document(baseUri);
    }

    @Test
    public void testNormaliseTextNodesWithEmptyDocument() {
        Document document = new Document("about:blank");
        List<Node> nodesToMove = new ArrayList<>();
        for (Node node : document.childNodes()) {
            if (node instanceof TextNode) {
                nodesToMove.add(node);
            }
        }
        for (int i = nodesToMove.size() - 1; i >= 0; i--) {
            Node node = nodesToMove.get(i);
            document.removeChild(node);
            document.body().prependChild(new TextNode(" ", ""));
            document.body().prependChild(node);
        }
        Assert.assertEquals(0, document.body().childNodes().size());
    }

    @Test
    public void testNormaliseStructureWithMultipleHeads() {
        Document document = new Document("about:blank");
        Element firstHead = document.appendElement("head");
        Element secondHead = document.appendElement("head");
        List<Node> nodesToMove = new ArrayList<>();
        for (Node node : secondHead.childNodes()) {
            nodesToMove.add(node);
        }
        for (int i = nodesToMove.size() - 1; i >= 0; i--) {
            Node node = nodesToMove.get(i);
            document.removeChild(node);
            firstHead.appendChild(node);
        }
        Assert.assertEquals(nodesToMove.size(), firstHead.childNodes().size());
    }

    @Test
    public void testNormaliseStructureWithMultipleBodies() {
        Document document = new Document("about:blank");
        Element firstBody = document.appendElement("body");
        Element secondBody = document.appendElement("body");
        List<Node> nodesToMove = new ArrayList<>();
        for (Node node : secondBody.childNodes()) {
            nodesToMove.add(node);
        }
        for (int i = nodesToMove.size() - 1; i >= 0; i--) {
            Node node = nodesToMove.get(i);
            document.removeChild(node);
            firstBody.appendChild(node);
        }
        Assert.assertEquals(nodesToMove.size(), firstBody.childNodes().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNormaliseStructureWithNullElement() {
        Document document = new Document("about:blank");
        document.normaliseStructure(null, document);
    }

    @Test
    public void testNormaliseStructureWithNullHtmlElement() {
        Document document = new Document("about:blank");
        Element htmlEl = document.findFirstElementByTagName("html", document);
        document.normaliseStructure("head", null);
        Assert.assertNotNull(htmlEl.parentNode());
    }
}