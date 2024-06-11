package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("input");
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);

        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        List<Node> stack = xmlTreeBuilder.getStack();
        Assert.assertTrue(stack.contains(Document.createShell(baseUri)));
    }

    @Test
    public void testParse() {
        Reader input = new StringReader("input");
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);

        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> parsedFragment = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
        Assert.assertNotNull(parsedFragment);
    }

    // Add more test cases for other methods in the XmlTreeBuilder class

}