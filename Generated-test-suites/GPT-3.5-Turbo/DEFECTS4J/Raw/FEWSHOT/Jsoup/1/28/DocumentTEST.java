
package org.jsoup.nodes;

import org.apache.commons.lang.Validate;
import org.jsoup.parser.Tag;

import java.util.List;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

public class DocumentTest {
    
    @Test
    public void testConstructor() {
        Document doc = new Document("");
        Assert.assertEquals(Tag.valueOf("#root").toString(), doc.nodeName());
    }
    
    @Test
    public void testCreateShell() {
        Document doc = Document.createShell("");
        Assert.assertEquals("html", doc.getElementsByTag("html").first().nodeName());
        Assert.assertEquals("head", doc.getElementsByTag("head").first().nodeName());
        Assert.assertEquals("body", doc.getElementsByTag("body").first().nodeName());
    }
    
    @Test
    public void testHead() {
        Document doc = Document.createShell("");
        Assert.assertEquals("head", doc.head().nodeName());
    }
    
    @Test
    public void testBody() {
        Document doc = Document.createShell("");
        Assert.assertEquals("body", doc.body().nodeName());
    }
    
    @Test
    public void testTitle() {
        Document doc = Document.createShell("");
        doc.head().appendElement("title").text("Test Title");
        Assert.assertEquals("Test Title", doc.title());
        
        doc.title("New Title");
        Assert.assertEquals("New Title", doc.title());
    }
    
    @Test
    public void testCreateElement() {
        Document doc = Document.createShell("");
        Element div = doc.createElement("div");
        Assert.assertEquals("div", div.nodeName());
    }
    
    @Test
    public void testNormalise() {
        Document doc = new Document("");
        Assert.assertEquals("html", doc.select("html").first().nodeName());
        
        doc.normalise();
        Assert.assertEquals("html", doc.select("html").first().nodeName());
        Assert.assertEquals("head", doc.head().nodeName());
        Assert.assertEquals("body", doc.body().nodeName());
    }
    
    @Test
    public void testOuterHtml() {
        Document doc = new Document("");
        Assert.assertEquals("", doc.outerHtml());
    }
    
    @Test
    public void testText() {
        Document doc = Document.createShell("");
        doc.body().text("Test Text");
        Assert.assertEquals("Test Text", doc.body().text());
    }
}
