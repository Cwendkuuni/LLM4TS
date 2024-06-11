package org.jsoup.nodes;

import org.jsoup.Jsoup;

import org.jsoup.parser.Parser;

import org.junit.Assert;

import org.junit.Before;

import org.junit.Test;

import java.io.IOException;


public class CommentTest {

    private Comment comment;

    @Before
    public void setUp() {
        comment = new Comment("Test comment");
    }

    @Test
    public void testNodeName() {
        String expectedNodeName = "#comment";
        String actualNodeName = comment.nodeName();

        Assert.assertEquals(expectedNodeName, actualNodeName);
    }

    @Test
    public void testGetData() {
        String expectedData = "Test comment";
        String actualData = comment.getData();

        Assert.assertEquals(expectedData, actualData);
    }

    @Test
    public void testIsXmlDeclaration() {
        boolean expectedIsXmlDeclaration = false;
        boolean actualIsXmlDeclaration = comment.isXmlDeclaration();

        Assert.assertEquals(expectedIsXmlDeclaration, actualIsXmlDeclaration);
    }

    @Test
    public void testAsXmlDeclaration() {
        XmlDeclaration expectedXmlDeclaration = null;
        XmlDeclaration actualXmlDeclaration = comment.asXmlDeclaration();

        Assert.assertEquals(expectedXmlDeclaration, actualXmlDeclaration);
    }

    @Test
    public void testOuterHtmlHead() throws IOException {
        StringBuilder builder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        outputSettings.prettyPrint(true);
        int depth = 0;

        comment.outerHtmlHead(builder, depth, outputSettings);

        String expectedOuterHtmlHead = "<!--Test comment-->";
        String actualOuterHtmlHead = builder.toString();

        Assert.assertEquals(expectedOuterHtmlHead, actualOuterHtmlHead);
    }

    @Test
    public void testOuterHtmlTail() throws IOException {
        StringBuilder builder = new StringBuilder();
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        int depth = 0;

        comment.outerHtmlTail(builder, depth, outputSettings);

        String expectedOuterHtmlTail = "";
        String actualOuterHtmlTail = builder.toString();

        Assert.assertEquals(expectedOuterHtmlTail, actualOuterHtmlTail);
    }

    @Test
    public void testToString() {
        String expectedToString = "<!--Test comment-->";
        String actualToString = comment.toString();

        Assert.assertEquals(expectedToString, actualToString);
    }
}