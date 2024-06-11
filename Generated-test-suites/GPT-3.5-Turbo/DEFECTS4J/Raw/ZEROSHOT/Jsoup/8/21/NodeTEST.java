import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class NodeTest {

    @Test
    public void testNodeName() {
        Element element = Jsoup.parse("<div></div>").selectFirst("div");
        Assert.assertEquals("div", element.nodeName());
    }

    @Test
    public void testAttr() {
        Element element = Jsoup.parse("<div id='myDiv'></div>").selectFirst("div");
        Assert.assertEquals("myDiv", element.attr("id"));
    }

    @Test
    public void testAttributes() {
        Element element = Jsoup.parse("<div id='myDiv' class='myClass'></div>").selectFirst("div");
        Assert.assertEquals("myDiv", element.attributes().get("id"));
        Assert.assertEquals("myClass", element.attributes().get("class"));
    }

    @Test
    public void testHasAttr() {
        Element element = Jsoup.parse("<div id='myDiv'></div>").selectFirst("div");
        Assert.assertTrue(element.hasAttr("id"));
        Assert.assertFalse(element.hasAttr("class"));
    }

    @Test
    public void testRemoveAttr() {
        Element element = Jsoup.parse("<div id='myDiv'></div>").selectFirst("div");
        element.removeAttr("id");
        Assert.assertFalse(element.hasAttr("id"));
    }

    @Test
    public void testBaseUri() {
        Document document = Jsoup.parse("<html><head><base href='http://www.example.com/'></head><body></body></html>");
        Element element = document.selectFirst("base");
        Assert.assertEquals("http://www.example.com/", element.baseUri());
    }

    @Test
    public void testChildNode() {
        Element parent = Jsoup.parse("<div><p>Child Node</p></div>").selectFirst("div");
        Element child = parent.childNode(0);
        Assert.assertEquals("p", child.nodeName());
        Assert.assertEquals("Child Node", child.text());
    }

    @Test
    public void testChildNodes() {
        Element parent = Jsoup.parse("<div><p>Child Node 1</p><p>Child Node 2</p></div>").selectFirst("div");
        List<Element> childNodes = parent.childNodes();
        Assert.assertEquals(2, childNodes.size());
        Assert.assertEquals("p", childNodes.get(0).nodeName());
        Assert.assertEquals("p", childNodes.get(1).nodeName());
    }

    @Test
    public void testParent() {
        Element parent = Jsoup.parse("<div></div>").selectFirst("div");
        Element child = Jsoup.parse("<p></p>").selectFirst("p");
        parent.appendChild(child);
        Assert.assertEquals(parent, child.parent());
    }

    @Test
    public void testOwnerDocument() {
        Document document = Jsoup.parse("<html></html>");
        Element element = document.selectFirst("html");
        Assert.assertEquals(document, element.ownerDocument());
    }

    @Test
    public void testRemove() {
        Element parent = Jsoup.parse("<div><p></p></div>").selectFirst("div");
        Element child = parent.selectFirst("p");
        child.remove();
        Assert.assertNull(child.parent());
    }

    @Test
    public void testReplaceWith() {
        Element parent = Jsoup.parse("<div><p></p></div>").selectFirst("div");
        Element child1 = parent.selectFirst("p");
        Element child2 = Jsoup.parse("<span></span>").selectFirst("span");
        child1.replaceWith(child2);
        Assert.assertNull(child1.parent());
        Assert.assertEquals(parent, child2.parent());
    }

    @Test
    public void testSiblingNodes() {
        Element parent = Jsoup.parse("<div><p></p><span></span></div>").selectFirst("div");
        Element child = parent.selectFirst("p");
        List<Element> siblings = child.siblingNodes();
        Assert.assertEquals(2, siblings.size());
        Assert.assertEquals("span", siblings.get(0).nodeName());
        Assert.assertEquals("div", siblings.get(1).nodeName());
    }

    @Test
    public void testNextSibling() {
        Element parent = Jsoup.parse("<div><p></p><span></span></div>").selectFirst("div");
        Element child1 = parent.selectFirst("p");
        Element child2 = parent.selectFirst("span");
        Element nextSibling = child1.nextSibling();
        Assert.assertEquals(child2, nextSibling);
    }

    @Test
    public void testPreviousSibling() {
        Element parent = Jsoup.parse("<div><p></p><span></span></div>").selectFirst("div");
        Element child1 = parent.selectFirst("p");
        Element child2 = parent.selectFirst("span");
        Element previousSibling = child2.previousSibling();
        Assert.assertEquals(child1, previousSibling);
    }

    @Test
    public void testOuterHtml() {
        Element element = Jsoup.parse("<div><p>Child Node</p></div>").selectFirst("div");
        String expected = "<div>\n" +
                          " <p>\n" +
                          "  Child Node\n" +
                          " </p>\n" +
                          "</div>";
        Assert.assertEquals(expected, element.outerHtml());
    }

    @Test
    public void testToString() {
        Element element = Jsoup.parse("<div></div>").selectFirst("div");
        Assert.assertEquals(element.outerHtml(), element.toString());
    }
}