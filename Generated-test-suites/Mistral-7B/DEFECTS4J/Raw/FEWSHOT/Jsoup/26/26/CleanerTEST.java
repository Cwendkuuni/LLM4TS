package org.jsoup.safety;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CleanerTest {

    @Test
    public void testCleanerConstruction() {
        Whitelist whitelist = new Whitelist(); // a valid whitelist is required, but not tested here
        Cleaner cleaner = new Cleaner(whitelist);
        assertNotNull(cleaner);
    }

    // test clean method with valid input document
    @Test
    public void testCleanMethodValidInput() {
        Whitelist whitelist = new Whitelist();

        Document dirtyDocument = Document.parse("<html><body><p>Hello World!</p></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertTrue(cleanDocument.body().text().equals("Hello World!"));
        assertTrue(cleanDocument.body().children().size() == 1);
    }

    // test clean method with invalid input document
    @Test
    public void testCleanMethodInvalidInput() {
        Whitelist whitelist = new Whitelist();

        Document dirtyDocument = Document.parse("<html><body><script>alert('XSS!')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        Document cleanDocument = cleaner.clean(dirtyDocument);

        assertNull(cleanDocument.body().child(0).toString());
    }

    // test isValid method with valid input document
    @Test
    public void testIsValidMethodValidInput() {
        Whitelist whitelist = new Whitelist();

        Document dirtyDocument = Document.parse("<html><body><p>Hello World!</p></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertTrue(isValid);
    }

    // test isValid method with invalid input document
    @Test
    public void testIsValidMethodInvalidInput() {
        Whitelist whitelist = new Whitelist();

        Document dirtyDocument = Document.parse("<html><body><script>alert('XSS!')</script></body></html>");
        Cleaner cleaner = new Cleaner(whitelist);
        boolean isValid = cleaner.isValid(dirtyDocument);

        assertFalse(isValid);
    }

    // test isSafeTag method with valid tag
    @Test
    public void testIsSafeTagValidTag() {
        Whitelist whitelist = new Whitelist();

        String tag = "p";
        boolean isSafe = Cleaner.class.getDeclaredMethod("isSafeTag", String.class, Element.class, Attribute.class).invoke(cleaner, tag, null, null);

        assertTrue(isSafe);
    }

    // test isSafeTag method with invalid tag
    @Test
    public void testIsSafeTagInvalidTag() {
        Whitelist whitelist = new Whitelist();

        String tag = "script";
        boolean isSafe = Cleaner.class.getDeclaredMethod("isSafeTag", String.class, Element.class, Attribute.class).invoke(cleaner, tag, null, null);

        assertFalse(isSafe);
    }

    // test isSafeAttribute method with safe attribute
    @Test
    public void testIsSafeAttributeSafeAttribute() {
        Whitelist whitelist = new Whitelist();

        String tag = "p";
        String attribute = "class";
        boolean isSafe = Cleaner.class.getDeclaredMethod("isSafeAttribute", String.class, String.class, String.class, Element.class, Attribute.class).invoke(cleaner, tag, attribute, null, null, null);

        assertTrue(isSafe);
    }

    // test isSafeAttribute method with unsafe attribute
    @Test
    public void testIsSafeAttributeUnsafeAttribute() {
        Whitelist whitelist = new Whitelist();

        String tag = "p";
        String attribute = "onclick";
        boolean isSafe = Cleaner.class.getDeclaredMethod("isSafeAttribute", String.class, String.class, String.class, Element.class, Attribute.class).invoke(cleaner, tag, attribute, null, null, null);

        assertFalse(isSafe);
    }

    // test copySafeNodes method with empty source and destination
    @Test
    public void testCopySafeNodesEmptyInput() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        Element source = Element.parse("<html></html>").body();
        Element dest = Element.parse("<html></html>").body();

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded == 0);
    }

    // test copySafeNodes method with safe input
    @Test
    public void testCopySafeNodesSafeInput() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String tag = "p";
        String text = "Hello World!";

        Element source = new Element(Tag.valueOf(tag), null, new Attributes());
        source.text(text);
        Element dest = new Element(Tag.valueOf(tag), null, new Attributes());

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded == 0);
        assertTrue(dest.text().equals(text));
    }

    // test copySafeNodes method with unsafe input
    @Test
    public void testCopySafeNodesUnsafeInput() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String tag = "script";
        String text = "alert('XSS!')";

        Element source = new Element(Tag.valueOf(tag), null, new Attributes());
        source.text(text);
        Element dest = new Element(Tag.valueOf("p"), null, new Attributes());

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded == 1);
        assertNull(dest.child(0).toString());
    }

    // test copySafeNodes method with nested safe input
    @Test
    public void testCopySafeNodesNestedSafeInput() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String tag = "p";
        String text = "Hello <p>World!</p>";

        Element source = new Element(Tag.valueOf(tag), null, new Attributes());
        source.text(text);
        Element dest = new Element(Tag.valueOf(tag), null, new Attributes());

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded == 0);
        assertTrue(dest.child(0).tagName().equals("p"));
        assertTrue(dest.child(0).text().equals("World!"));
    }

    // test copySafeNodes method with mixed safe and unsafe input
    @Test
    public void testCopySafeNodesMixedInput() {
        Whitelist whitelist = new Whitelist();
        Cleaner cleaner = new Cleaner(whitelist);

        String tag = "div";
        String text = "Hello <script>World!</script>";

        Element source = new Element(Tag.valueOf(tag), null, new Attributes());
        source.text(text);
        Element dest = new Element(Tag.valueOf(tag), null, new Attributes());

        int numDiscarded = cleaner.copySafeNodes(source, dest);

        assertTrue(numDiscarded == 2);
        assertNull(dest.child(0).toString());
    }
}