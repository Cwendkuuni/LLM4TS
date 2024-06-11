package org.jsoup.nodes;

import org.junit.*;

import static org.junit.Assert.*;

public class CommentTest {

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("This is a comment");
    }

    @Test
    public void testNodeName() {
        String expected = "#comment";
        String actual = comment.nodeName();
        assertEquals(expected, actual);
    }

    @Test
    public void testGetData() {
        String expected = "This is a comment";
        String actual = comment.getData();
        assertEquals(expected, actual);
    }

    @Test
    public void testIsXmlDeclaration() {
        boolean expected = false;
        boolean actual = comment.isXmlDeclaration();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsXmlDeclaration() {
        XmlDeclaration expected = null;
        XmlDeclaration actual = comment.asXmlDeclaration();
        assertEquals(expected, actual);
    }

    @Test
    public void testToString() {
        String expected = "<!--This is a comment-->";
        String actual = comment.toString();
        assertEquals(expected, actual);
    }

    // Additional test cases for error scenarios

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidData() {
        Comment invalidComment = new Comment(null);
    }

    @Test
    public void testIsXmlDeclarationWithInvalidData() {
        Comment invalidComment = new Comment("<!");
        boolean expected = false;
        boolean actual = invalidComment.isXmlDeclaration();
        assertEquals(expected, actual);
    }

}