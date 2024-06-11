package org.jsoup.safety;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;

import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerInstance() {
        Whitelist whitelist = new Whitelist(); // Assuming Whitelist is a valid class
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertNotNull(cleaner);
    }

    @Test
    public void testCleanDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createEmpty();
        Element body = new Element(Tag.BODY, "", new Attributes());
        body.appendText("Test <script>alert('Hello')</script>");
        dirtyDocument.setBody(body);

        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        Element cleanBody = cleanDocument.body();
        Assert.assertNull(cleanBody.childNode(0)); // The script tag should have been removed
    }

    @Test
    public void testIsValidDocument() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createEmpty();
        Element body = new Element(Tag.BODY, "", new Attributes());
        body.appendText("Test <script>alert('Hello')</script>");
        dirtyDocument.setBody(body);

        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);
        Assert.assertFalse(isValid);

        Document cleanDocument = cleaner.clean(dirtyDocument);
        isValid = cleaner.isValid(cleanDocument);
        Assert.assertTrue(isValid);
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Document dirtyDocument = Document.createEmpty();
        Element body = new Element(Tag.BODY, "", new Attributes());
        body.appendText("Test <p id='test' class='test' title='Test Title'>Hello World</p>");
        dirtyDocument.setBody(body);

        Cleaner cleaner = new Cleaner(whitelist);
        Element cleanBody = new Element(Tag.BODY, "", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(body, cleanBody);

        Element cleanChild = cleanBody.childNodes().get(0);
        Assert.assertInstanceOf(Element.class, cleanChild);
        Assert.assertEquals("p", cleanChild.tagName());
        Assert.assertEquals("test", cleanChild.id());
        Assert.assertEquals("test", cleanChild.classes().get(0));
        Assert.assertNull(cleanChild.attributes().get("title")); // Title should have been discarded
        Assert.assertEquals(1, numDiscarded);
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Assert.assertTrue(cleaner.whitelist.isSafeTag("p"));
        Assert.assertFalse(cleaner.whitelist.isSafeTag("script"));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element sourceElement = new Element(Tag.P, "", new Attributes().put("class", "test").put("title", "Test Title"));

        Assert.assertTrue(cleaner.whitelist.isSafeAttribute("p", sourceElement, new Attribute("class", "test")));
        Assert.assertFalse(cleaner.whitelist.isSafeAttribute("p", sourceElement, new Attribute("title", "Test Title")));
    }

    @Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        Element pElement = new Element(Tag.P, "", new Attributes());

        List<Attribute> enforcedAttributes = cleaner.whitelist.getEnforcedAttributes("p");
        Assert.assertNotNull(enforcedAttributes);
        Assert.assertEquals(1, enforcedAttributes.size());
        Assert.assertEquals("class", enforcedAttributes.get(0).getName());
    }
}