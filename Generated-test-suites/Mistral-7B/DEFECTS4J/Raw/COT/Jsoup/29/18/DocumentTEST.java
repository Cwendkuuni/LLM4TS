import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;

import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

    @Test
    public void testDocumentConstructor() {
        Document document = new Document("baseUri");
        Assert.assertNotNull(document);
        Assert.assertEquals("#root", document.tagName());
        Assert.assertNull(document.head());
        Assert.assertNull(document.body());
    }

    @Test
    public void testCreateShell() {
        Document document = Document.createShell("baseUri");
        Assert.assertNotNull(document);
        Assert.assertEquals("#document", document.tagName());
        Element html = document.head();
        Assert.assertNotNull(html);
        Assert.assertEquals("html", html.tagName());
        Element head = html.elementSibling();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
        Element body = head.nextSibling();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testHeadMethod() {
        Document document = new Document("baseUri");
        Element head = document.head();
        Assert.assertNotNull(head);
        Assert.assertEquals("head", head.tagName());
    }

    @Test
    public void testBodyMethod() {
        Document document = new Document("baseUri");
        Element body = document.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testTitleMethod() {
        Document document = new Document("baseUri");
        document.title("Title");
        Assert.assertEquals("Title", document.title());
    }

    @Test
    public void testTitleMethodEmpty() {
        Document document = new Document("baseUri");
        Assert.assertEquals("", document.title());
    }

    @Test
    public void testTitleMethodWithSpace() {
        Document document = new Document("baseUri");
        document.title(" Title ");
        Assert.assertEquals(" Title ", document.title());
    }

    @Test
    public void testCreateElementMethod() {
        Document document = new Document("baseUri");
        Element element = document.createElement("tagName");
        Assert.assertNotNull(element);
        Assert.assertEquals("tagName", element.tagName());
        Assert.assertEquals("baseUri", element.baseUri());
    }

    @Test
    public void testNormaliseMethod() {
        Document document = new Document("baseUri");
        document.body().text("Text inside body");
        document.head().text("Text inside head");
        document.appendElement("div").text("Text inside div");
        document.normalise();
        Assert.assertEquals("Text inside body", document.body().text());
        Assert.assertEquals("", document.head().text());
        List<Element> elements = document.getElementsByTag("div");
        Assert.assertEquals(1, elements.size());
        Assert.assertEquals("div", elements.get(0).tagName());
    }

    @Test(expected = NullPointerException.class)
    public void testFindFirstElementByTagNameMethodNullNode() {
        Document document = new Document("baseUri");
        findFirstElementByTagName(document, null, "tagName");
    }

    @Test
    public void testFindFirstElementByTagNameMethodNullTag() {
        Document document = new Document("baseUri");
        Node node = document.head();
        findFirstElementByTagName(document, node, null);
    }

    @Test
    public void testFindFirstElementByTagNameMethodExistingTag() {
        Document document = new Document("baseUri");
        Element head = document.appendElement("head");
        Element tag = document.appendElement("tag", head);
        Element found = findFirstElementByTagName(document, head, "tag");
        Assert.assertEquals(tag, found);
    }

    @Test
    public void testFindFirstElementByTagNameMethodNonExistingTag() {
        Document document = new Document("baseUri");
        Element head = document.appendElement("head");
        findFirstElementByTagName(document, head, "unknownTag");
        Assert.assertNull(findFirstElementByTagName(document, head, "unknownTag"));
    }

    @Test
    public void testOuterHtmlMethod() {
        Document document = new Document("baseUri");
        document.body().text("Text inside body");
        Assert.assertEquals("Text inside body", document.outerHtml());
    }

    @Test
    public void testTextMethod() {
        Document document = new Document("baseUri");
        document.body().text("Text inside body");
        document.text("New text inside body");
        Assert.assertEquals("New text inside body", document.body().text());
    }

    @Test
    public void testNodeNameMethod() {
        Document document = new Document("baseUri");
        Assert.assertEquals("#document", document.nodeName());
    }

    @Test
    public void testCloneMethod() {
        Document document = new Document("baseUri");
        document.title("Title");
        Document clone = document.clone();
        Assert.assertEquals("Title", document.title());
        Assert.assertEquals("", clone.title());
    }

    private Element findFirstElementByTagName(Document document, Node node, String tag) {
        if (node.nodeName().equals(tag)) {
            return (Element) node;
        } else {
            for (Node child : node.childNodes()) {
                Element found = findFirstElementByTagName(document, child, tag);
                if (found != null) {
                    return found;
                }
            }
        }
        return null;
    }
}