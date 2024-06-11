package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {
    private static XmlTreeBuilder xmlTreeBuilder;

    @BeforeClass
    public static void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void defaultSettingsTest() {
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void parseTest() {
        String input = "<xml>sample</xml>";
        String baseUri = "http://www.example.com";
        Document expectedDocument = Jsoup.parse(input, baseUri, Parser.xmlParser());

        Document actualDocument = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void initialiseParseTest() {
        String input = "<xml>sample</xml>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        Document doc = xmlTreeBuilder.getDocument();
        Node node = xmlTreeBuilder.getCurrentNode();

        Assert.assertNotNull(doc);
        Assert.assertNotNull(node);
        Assert.assertEquals(input, doc.toString());
        Assert.assertEquals(baseUri, doc.baseUri());
        Assert.assertEquals(settings, doc.outputSettings().syntax().name());
    }

    @Test
    public void processStartTagTest() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element expectedElement = xmlTreeBuilder.insert(startTag);

        Element actualElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertEquals(expectedElement, actualElement);
    }

    @Test
    public void processEndTagTest() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.process(endTag);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void processCommentTest() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(commentToken);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void processCharacterTest() {
        Token.Character characterToken = new Token.Character("sample");
        xmlTreeBuilder.process(characterToken);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void processDoctypeTest() {
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        xmlTreeBuilder.process(doctype);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void insertNodeTest() {
        Element element = new Element(Tag.valueOf("div"), "http://www.example.com");
        xmlTreeBuilder.insertNode(element);

        List<Node> childNodes = xmlTreeBuilder.getDocument().childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals(element, childNodes.get(0));
    }

    @Test
    public void insertStartTagTest() {
        Token.StartTag startTag = new Token.StartTag("tag", new Attributes());
        Element expectedElement = xmlTreeBuilder.insert(startTag);

        Element actualElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertEquals(expectedElement, actualElement);
    }

    @Test
    public void insertCommentTest() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void insertCharacterTest() {
        Token.Character characterToken = new Token.Character("sample");
        xmlTreeBuilder.insert(characterToken);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void insertDoctypeTest() {
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        xmlTreeBuilder.insert(doctype);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void popStackToCloseTest() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        Assert.assertNull(currentElement);
    }

    @Test
    public void parseFragmentTest() {
        String inputFragment = "<xml>sample</xml>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> expectedNodes = Jsoup.parseFragment(inputFragment, baseUri, Parser.xmlParser());
        List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        Assert.assertEquals(expectedNodes, actualNodes);
    }
}