package org.jsoup.safety;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);
        assertEquals(whitelist, cleaner.whitelist);
    }

    @Test(expected = NullPointerException.class)
    public void testCleanerNullWhitelist() {
        Cleaner cleaner = new Cleaner(null);
    }

    @Test
    public void testClean() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1>Hello<script>alert('XSS')</script></h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertEquals("<html><body><h1></h1></body></html>", cleanDocument.html());
    }

    @Test
    public void testIsValid() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1>Hello<script>alert('XSS')</script></h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        assertTrue(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testIsValidWithInvalidHtml() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><script>alert('XSS')</script></body></html>";
        Document dirtyDocument = Jsoup.parse(html);

        assertFalse(cleaner.isValid(dirtyDocument));
    }

    @Test
    public void testCopySafeNodes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String html = "<html><body><h1 id='h1' class='h1' style='color:red;'><script>alert('XSS')</script>Foo<img src='bar.jpg' alt='Baz' title='Quux'></h1></body></html>";
        Document dirtyDocument = Jsoup.parse(html);
        Element body = dirtyDocument.body();

        Element cleanElement = new Element(Tag.valueOf("div"), body.baseUri(), new Attributes());
        int numDiscarded = cleaner.copySafeNodes(body, cleanElement);

        assertEquals("<div></div>", cleanElement.html());
        assertEquals(7, numDiscarded);

        Element h1 = body.getElementById("h1");
        Element cleanH1 = new Element(Tag.valueOf("h1"), h1.baseUri(), new Attributes());
        numDiscarded = cleaner.copySafeNodes(h1, cleanH1);

        assertEquals("<h1></h1>", cleanH1.html());
        assertEquals(4, numDiscarded);

        Element img = body.querySelector("img");
        Element cleanImg = new Element(Tag.valueOf("img"), img.baseUri(), new Attributes());
        numDiscarded = cleaner.copySafeNodes(img, cleanImg);

        assertEquals("<img src='bar.jpg' alt='Baz' title='Quux'/>", cleanImg.html());
        assertEquals(0, numDiscarded);
    }

    @Test
    public void testCreateSafeElement() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element sourceElement = new Element(Tag.valueOf("div"), null, new Attributes().put("id", "source"));
        sourceElement.text("Source text");

        Element destElement = new Element(Tag.valueOf("div"), sourceElement.baseUri(), new Attributes());
        int numDiscarded = cleaner.copySafeNodes(sourceElement, destElement);

        assertEquals("<div>Source text</div>", destElement.html());
        assertEquals(0, numDiscarded);

        Element meta = cleaner.createSafeElement(sourceElement);
        assertEquals("<div></div>", meta.el.html());
        assertEquals(0, meta.numAttribsDiscarded);

        sourceElement.attributes().put("class", "foo");
        meta = cleaner.createSafeElement(sourceElement);
        assertEquals("<div class='foo'></div>", meta.el.html());
        assertEquals(1, meta.numAttribsDiscarded);

        sourceElement.attributes().put("style", "color:red;");
        meta = cleaner.createSafeElement(sourceElement);
        assertEquals("<div></div>", meta.el.html());
        assertEquals(2, meta.numAttribsDiscarded);

        sourceElement.attributes().put("data-xss", "evil");
        meta = cleaner.createSafeElement(sourceElement);
        assertEquals("<div></div>", meta.el.html());
        assertEquals(3, meta.numAttribsDiscarded);
    }

    @Test
    public void testIsSafeTag() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        assertEquals(true, cleaner.whitelist.isSafeTag("div"));
        assertEquals(false, cleaner.whitelist.isSafeTag("script"));
    }

    @Test
    public void testIsSafeAttribute() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        assertEquals(true, cleaner.whitelist.isSafeAttribute("div", null, new Attribute("class", "foo")));
        assertEquals(false, cleaner.whitelist.isSafeAttribute("div", null, new Attribute("onclick", "alert('XSS')")));
    }

    @Test
    public void testGetEnforcedAttributes() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        List<Attribute> attributes = Arrays.asList(
                new Attribute("class", "foo"),
                new Attribute("style", "color:red;")
        );

        assertEquals(attributes, cleaner.whitelist.getEnforcedAttributes("div"));
    }

}