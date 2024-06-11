package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;

public class CleanerTest {

    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = createDirtyDocument();
        Document cleanedDocument = cleaner.clean(dirtyDocument);
        
        assertFalse(cleanedDocument == dirtyDocument);
        assertNotNull(cleanedDocument);
        assertNotNull(cleanedDocument.body());
    }
    
    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Document dirtyDocument = createDirtyDocument();
        boolean isValid = cleaner.isValid(dirtyDocument);
        
        assertFalse(isValid);
    }

    @Test
    public void testCleaningVisitorHead() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element root = new Element("body");
        Element destination = new Element("div");
        destination.appendChild(new Element("span"));
        
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.head(new Element("a"), 0);
        
        assertEquals(1, destination.children().size());
    }
    
    @Test
    public void testCleaningVisitorTail() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element root = new Element("body");
        Element destination = new Element("div");
        destination.appendChild(new Element("span"));
        
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);
        cleaningVisitor.tail(new Element("a"), 0);
        
        assertTrue(destination.parent() == null);
    }
    
    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element source = new Element("body");
        source.appendChild(new Element("div"));
        Element dest = new Element("div");
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);
        
        assertEquals(1, dest.children().size());
        assertEquals(0, numDiscarded);
    }
    
    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = Whitelist.basic();
        Cleaner cleaner = new Cleaner(whitelist);
        
        Element sourceEl = new Element("a");
        Attributes attrs = new Attributes();
        sourceEl.attributes().put("href", "example.com");
        sourceEl.attributes().put("target", "_blank");
        sourceEl.attributes().put("class", "link");
        sourceEl.attributes().put("data-attr", "value");
        sourceEl.attributes().put("style", "color:red;");
        sourceEl.attributes().put("onclick", "alert('hello');");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        assertEquals(sourceEl.tagName(), elementMeta.el.tagName());
        assertEquals(4, elementMeta.numAttribsDiscarded);
    }
    
    private Document createDirtyDocument() {
        Document document = Document.createShell("");
        Element body = document.body();
        body.appendElement("div");
        body.appendElement("a").attr("href", "example.com");
        body.appendElement("script").text("alert('xss');");
        body.appendElement("img").attr("src", "xss.jpg");
        body.appendElement("style").text(".red { color:red; }");
        
        return document;
    }    
}