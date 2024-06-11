package org.jsoup.safety;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CleanerTest {
    @Test
    public void testClean() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document dirtyDocument = new Document("");
        Element dirtyElement = new Element("div");
        dirtyElement.append("dirty text");
        dirtyDocument.body().appendChild(dirtyElement);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element cleanElement = cleanDocument.body().child(0);

        Assert.assertEquals("div", cleanElement.tagName());
        Assert.assertTrue(cleanElement.childNodes().get(0) instanceof TextNode);
        Assert.assertEquals("dirty text", cleanElement.text());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Document validDocument = new Document("");
        Element validElement = new Element("p");
        validElement.append("valid text");
        validDocument.body().appendChild(validElement);

        Document invalidDocument = new Document("");
        Element invalidElement = new Element("script");
        invalidElement.append("alert('xss attack');");
        invalidDocument.body().appendChild(invalidElement);

        boolean isValidValid = cleaner.isValid(validDocument);
        boolean isValidInvalid = cleaner.isValid(invalidDocument);

        Assert.assertTrue(isValidValid);
        Assert.assertFalse(isValidInvalid);
    }

    @Test
    public void testCleaningVisitor() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Element root = new Element("div");
        Element destination = new Element("div");
        Cleaner.CleaningVisitor cleaningVisitor = cleaner.new CleaningVisitor(root, destination);

        Element safeElement1 = new Element("p");
        safeElement1.append("safe text 1");
        Element safeElement2 = new Element("span");
        safeElement2.append("safe text 2");
        Element unsafeElement = new Element("script");
        safeElement1.append("alert('xss attack');");
        
        cleaningVisitor.head(safeElement1, 0);
        cleaningVisitor.head(safeElement2, 1);
        cleaningVisitor.head(unsafeElement, 2);
        cleaningVisitor.tail(safeElement2, 1);
        cleaningVisitor.tail(safeElement1, 0);

        List<Element> destinationChildren = destination.children();
        Assert.assertEquals(2, destinationChildren.size());
        Assert.assertEquals("p", destinationChildren.get(0).tagName());
        Assert.assertEquals("safe text 1", destinationChildren.get(0).text());
        Assert.assertEquals("span", destinationChildren.get(1).tagName());
        Assert.assertEquals("safe text 2", destinationChildren.get(1).text());
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = Whitelist.simpleText();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = new Element("div");
        Element dest = new Element("div");
        
        Element safeElement = new Element("p");
        safeElement.append("safe text");
        Element unsafeElement = new Element("script");
        safeElement.append("alert('xss attack');");

        source.appendChild(safeElement);
        source.appendChild(unsafeElement);
        
        int numDiscarded = cleaner.copySafeNodes(source, dest);

        List<Element> destChildren = dest.children();
        Assert.assertEquals(1, destChildren.size());
        Assert.assertEquals("p", destChildren.get(0).tagName());
        Assert.assertEquals("safe text", destChildren.get(0).text());
        Assert.assertEquals(1, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceEl = new Element("div");
        sourceEl.attr("class", "safeClass");
        sourceEl.attr("onclick", "alert('xss attack');");
        
        Cleaner.ElementMeta elementMeta = cleaner.createSafeElement(sourceEl);
        
        Element dest = elementMeta.el;
        Assert.assertEquals("div", dest.tagName());
        Assert.assertEquals(1, dest.attributes().size());
        Assert.assertEquals("safeClass", dest.attr("class"));
        Assert.assertEquals(1, elementMeta.numAttribsDiscarded);
    }

    @Test
    public void testCleanerConstructor() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Assert.assertEquals(whitelist, cleaner.whitelist);
    }
}