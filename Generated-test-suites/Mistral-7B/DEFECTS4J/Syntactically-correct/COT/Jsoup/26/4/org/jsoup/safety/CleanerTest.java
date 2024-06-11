package org.jsoup.safety;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.jsoup.Jsoup.connect;
import static org.jsoup.Jsoup.parse;
import static org.jsoup.helper.W3C.createWhitelist;

/**
 * Test class for Cleaner class
 */
public class CleanerTest {
    private Document dirtyDocument;
    private Whitelist whitelist;
    private Cleaner cleaner;

    @Before
    public void setUp() {
        whitelist = createWhitelist();
        cleaner = new Cleaner(whitelist);
        dirtyDocument = parse("<html><body><div id='test' data-test='test'><h1>Hello<script>alert('XSS');</script></h1></div></body></html>");
    }

    /**
     * Test method for constructor
     */
    @Test
    public void testCleanerConstructor() {
        Cleaner cleaner = new Cleaner(whitelist);
        Validate.notNull(cleaner);
    }

    /**
     * Test method for clean method
     */
    @Test
    public void testCleanMethod() {
        Document cleanDocument = cleaner.clean(dirtyDocument);
        Element root = cleanDocument.body();
        Validate.notNull(root);
        Validate.isInstanceOf(Element.class, root);
        Validate.that(root.tagName(), is("html"));
        Validate.that(root.child(0).tagName(), is("head"));
        Validate.that(root.child(1).tagName(), is("body"));
        Validate.that(root.child(1).child(0).tagName(), is("div"));
        Validate.that(root.child(1).child(0).attr("id"), is("test"));
        Validate.that(root.child(1).child(0).attr("data-test"), is("test"));
        Validate.that(root.child(1).child(0).child(0).tagName(), is("h1"));
        Validate.that(root.child(1).child(0).child(0).text(), is("Hello"));
        Validate.that(root.child(1).child(0).child(1), nullValue());
    }

    /**
     * Test method for isValid method
     */
    @Test
    public void testIsValidMethod() {
        boolean isValid = cleaner.isValid(dirtyDocument);
        Validate.that(isValid, is(false));
    }

    /**
     * Test method for copySafeNodes method with safe tag
     */
    @Test
    public void testCopySafeNodesMethodWithSafeTag() {
        Element safeElement = new Element(Tag.H1, "http://example.com/", new Attributes());
        safeElement.text("Safe text");

        Element dest = new Element(Tag.BODY, "http://example.com/", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(safeElement, dest);

        Validate.that(dest.child(0), is(instanceOf(Element.class)));
        Validate.that(dest.child(0).tagName(), is("h1"));
        Validate.that(dest.child(0).text(), is("Safe text"));
        Validate.that(numDiscarded, is(0));
    }

    /**
     * Test method for copySafeNodes method with unsafe tag
     */
    @Test
    public void testCopySafeNodesMethodWithUnsafeTag() {
        Element unsafeElement = new Element(Tag.SCRIPT, "http://example.com/", new Attributes());
        unsafeElement.text("Unsafe script");

        Element dest = new Element(Tag.BODY, "http://example.com/", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(unsafeElement, dest);

        Validate.that(dest.child(0), nullValue());
        Validate.that(numDiscarded, is(1));
    }

    /**
     * Test method for copySafeNodes method with safe tag and
     * unsafe attributes
     */
    @Test
    public void testCopySafeNodesMethodWithSafeTagAndUnsafeAttributes() {
        Element safeElementWithUnsafeAttributes = new Element(Tag.DIV, "http://example.com/", new Attributes())
                .attr("id", "test")
                .attr("data-test", "test")
                .attr("style", "color: red; background-color: blue;");

        Element dest = new Element(Tag.BODY, "http://example.com/", new Attributes());
        int numDiscarded = cleaner.copySafeNodes(safeElementWithUnsafeAttributes, dest);

        Element copiedElement = dest.child(0);
        Validate.that(copiedElement.tagName(), is("div"));
        Validate.that(copiedElement.attr("id"), is("test"));
        Validate.that(copiedElement.attr("data-test"), is("test"));
        Validate.that(copiedElement.attr("style"), is(""));
        Validate.that(numDiscarded, is(2));
    }

    /**
     * Test method for isSafeTag method
     */
    @Test
    public void testIsSafeTagMethod() {
        Validate.that(cleaner.whitelist.isSafeTag("h1"), is(true));
        Validate.that(cleaner.whitelist.isSafeTag("script"), is(false));
    }

    /**
     * Test method for isSafeAttribute method
     */
    @Test
    public void testIsSafeAttributeMethod() {
        Validate.that(cleaner.whitelist.isSafeAttribute("div", null, new Attribute("id", "test")), is(true));
        Validate.that(cleaner.whitelist.isSafeAttribute("div", null, new Attribute("onclick", "alert('XSS');")), is(false));
    }

    /**
     * Test method for getEnforcedAttributes method
     */
    @Test
    public void testGetEnforcedAttributesMethod() {
        List<Attribute> enforcedAttributes = cleaner.whitelist.getEnforcedAttributes("div");
        Validate.that(enforcedAttributes.size(), is(1));
        Validate.that(enforcedAttributes.get(0).getKey(), is("id"));
        Validate.that(enforcedAttributes.get(0).getValue(), is(""));
    }
}