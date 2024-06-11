package org.junit.tests;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.Jsoup;

public class CommentTest {
    @Test
    public void testCommentConstruction() {
        Comment comment = new Comment("This is a comment.");
        Assert.assertEquals("This is a comment.", comment.getData());
    }

    // Tests for Comment constructor
    @Test(expected = NullPointerException.class)
    public void testCommentNullData() {
        Comment comment = new Comment(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentEmptyStringData() {
        Comment comment = new Comment("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCommentInvalidBaseUri() {
        Comment comment = new Comment("data", null);
    }

    // Tests for getData()
    @Test(expected = NullPointerException.class)
    public void testCommentNullValue() {
        Comment comment = new Comment("data");
        comment.setValue(null);
        Assert.assertNull(comment.getData());
    }

    // Tests for isXmlDeclaration()
    @Test
    public void testIsXmlDeclarationValidXmlDeclaration() {
        Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>");
        Assert.assertTrue(comment.isXmlDeclaration());
    }

    @Test
    public void testIsXmlDeclarationInvalidString() {
        Comment comment = new Comment("This is a comment.");
        Assert.assertFalse(comment.isXmlDeclaration());
    }

    // Tests for asXmlDeclaration()
    @Test
    public void testAsXmlDeclarationValidXmlDeclaration() {
        String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>";
        Comment comment = new Comment(xmlDeclaration);
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();
        Assert.assertNotNull(xmlDecl);
    }

    @Test
    public void testAsXmlDeclarationInvalidString() {
        Comment comment = new Comment("This is a comment.");
        XmlDeclaration xmlDecl = comment.asXmlDeclaration();
        Assert.assertNull(xmlDecl);
    }
}