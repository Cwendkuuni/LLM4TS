package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.parser.Parser;
import org.junit.Test;

import java.io.IOException;

/**
 * JUnit test cases for Comment class
 */
public class CommentTest {

    Comment testComment = new Comment("Test Comment");

    @Test
    public void testNodeName() {
        assertEquals("#comment", testComment.nodeName());
    }

    @Test
    public void testGetData() {
        assertEquals("Test Comment", testComment.getData());
    }

    @Test
    public void testIsXmlDeclaration() {
        Comment xmlComment = new Comment("<!--!DOCTYPE html-->");
        assertEquals(true, xmlComment.isXmlDeclaration());
    }

    @Test
    public void testAsXmlDeclaration() {
        Comment xmlComment = new Comment("<!--!DOCTYPE html-->");
        XmlDeclaration xmlDeclaration = xmlComment.asXmlDeclaration();
        assertEquals("#doctype", xmlDeclaration.nodeName());
    }

    // Edge cases and exception handling scenarios to be tested

    @Test
    public void testEmptyData() {
        Comment emptyComment = new Comment("");
        assertEquals("", emptyComment.getData());
    }

    @Test
    public void testInvalidXmlDeclaration() {
        Comment invalidXmlComment = new Comment("<!--abc-->");
        XmlDeclaration xmlDeclaration = invalidXmlComment.asXmlDeclaration();
        assertEquals(null, xmlDeclaration);
    }

    @Test(expected = IOException.class)
    public void testOuterHtmlHeadIOException() throws IOException {
        Appendable accum = mock(Appendable.class);
        when(accum.append("<!--")).thenThrow(new IOException());
        testComment.outerHtmlHead(accum, 0, new OutputSettings());
    }

    // Additional test cases can be added as needed

}