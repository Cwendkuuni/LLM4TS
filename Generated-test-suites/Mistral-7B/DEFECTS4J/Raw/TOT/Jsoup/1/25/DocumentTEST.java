package org.jsoup.nodes;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

public class DocumentTest {

    @Test
    public void testCreateDocument() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertNotNull(doc);
        Assert.assertEquals("http://example.com", doc.baseUri());
        Element html = doc.head();
        Assert.assertNotNull(html);
        Element head = html.getChild(0);
        Assert.assertEquals("html", head.tagName());
        Element body = doc.body();
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.tagName());
    }

    @Test
    public void testSetTitle() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Test Title");
        Assert.assertEquals("Test Title", doc.title());
        Element titleEl = doc.getElementsByTag("title").first();
        Assert.assertNotNull(titleEl);
        Assert.assertEquals("Test Title", titleEl.text().trim());
    }

    @Test
    public void testGetTitleEmptyDocument() {
        Document doc = new Document("http://example.com");
        Assert.assertEquals("", doc.title());
    }

    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("http://example.com");
        Element p = doc.createElement("p");
        Assert.assertEquals("p", p.tagName());
        Assert.assertEquals("http://example.com", p.baseUri());
    }

    @Test
    public void testNormaliseDocument() {
        String html = "<html><head><title>Test</title></head><body><p>Test</p></body></html>";
        Document doc = Jsoup.parse(html);
        doc.normalise();
        Element body = doc.body();
        List<Node> bodyChildren = body.childNodes();
        int textNodeIndex = 0;
        for (int i = 0; i < bodyChildren.size(); i++) {
            if (bodyChildren.get(i) instanceof TextNode) {
                if (textNodeIndex == 0) {
                    Assert.assertEquals("", bodyChildren.get(i).text().trim());
                } else {
                    Assert.assertEquals("Test", bodyChildren.get(i).text().trim());
                }
                textNodeIndex++;
            } else {
                Assert.assertEquals("p", bodyChildren.get(i).tagName());
            }
        }
    }

    @Test(expected = NullPointerException.class)
    public void testTitleNullArgument() {
        Document doc = Document.createShell("http://example.com");
        doc.title(null);
    }

    @Test
    public void testGetEmptyTitle() {
        Document doc = Document.createShell("http://example.com");
        Assert.assertEquals("", doc.title());
        Element titleEl = doc.getElementsByTag("title").first();
        Assert.assertNull(titleEl);
    }

    @Test
    public void testGetTitleWithText() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Testing Title");
        Assert.assertEquals("Testing Title", doc.title());
        Element titleEl = doc.getElementsByTag("title").first();
        Assert.assertNotNull(titleEl);
        Assert.assertEquals("Testing Title", titleEl.text().trim());
    }

    @Test
    public void testSetTitleTwice() {
        Document doc = Document.createShell("http://example.com");
        doc.title("Testing Title 1");
        doc.title("Testing Title 2");
        Assert.assertEquals("Testing Title 2", doc.title());
        Element titleEl = doc.getElementsByTag("title").first();
        Assert.assertNotNull(titleEl);
        Assert.assertEquals("Testing Title 2", titleEl.text().trim());
    }

    @Test
    public void testCreateShellWithNullBaseUri() {
        Assert.assertThrows(NullPointerException.class, () -> Document.createShell(null));
    }
}