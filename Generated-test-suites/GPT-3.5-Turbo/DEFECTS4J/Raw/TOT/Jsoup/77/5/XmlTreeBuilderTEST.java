import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private String baseUri;
    private ParseErrorList errors;
    private ParseSettings settings;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        baseUri = "https://example.com";
        errors = ParseErrorList.noTracking();
        settings = ParseSettings.preserveCase;
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<root><child></child></root>");
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <root>\n   <child></child>\n  </root>\n </body>\n</html>",
                document.html());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root><child></child></root>";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertNotNull(document);
        Assert.assertEquals("<html>\n <head></head>\n <body>\n  <root>\n   <child></child>\n  </root>\n </body>\n</html>",
                document.html());
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root><child></child></root>");
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assert.assertEquals(xmlTreeBuilder.stack.get(0).nodeName(), "#document");
    }

    @Test
    public void testProcessForStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes().put("class", "test");
        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(element);
        Assert.assertEquals("div", element.tagName());
        Assert.assertEquals(1, element.attributes().size());
        Assert.assertEquals("test", element.attr("class"));
    }

    @Test
    public void testProcessForEndTag() {
        xmlTreeBuilder.stack.add(new Element("div"));

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.process(endTag);

        Assert.assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessForComment() {
        Token.Comment commentToken = new Token.Comment("Test Comment");
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof Comment);
        Assert.assertEquals("Test Comment", ((Comment) childNodes.get(0)).getData());
    }

    @Test
    public void testProcessForCharacter() {
        Token.Character character = new Token.Character("Test Character");
        xmlTreeBuilder.insert(character);

        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("Test Character", childNodes.get(0).outerHtml());
    }

    @Test
    public void testProcessForDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element("div");
        xmlTreeBuilder.stack.add(parentElement);

        Element childElement = new Element("p");
        xmlTreeBuilder.insertNode(childElement);

        Assert.assertEquals(1, parentElement.childNodes().size());
        Assert.assertEquals("p", parentElement.child(0).tagName());
    }

    @Test
    public void testInsertForSelfClosingStartTag() {
        Token.StartTag startTag = new Token.StartTag("img");
        startTag.setSelfClosing(true);
        startTag.attributes().put("src", "image.jpg");

        Element element = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(element);
        Assert.assertEquals("img", element.tagName());
        Assert.assertEquals(1, element.attributes().size());
        Assert.assertEquals("image.jpg", element.attr("src"));
        Assert.assertTrue(element.tag().isKnownTag());
        Assert.assertTrue(element.tag().isSelfClosing());

        // test unknown tag with self-closing
        startTag = new Token.StartTag("custom");
        startTag.setSelfClosing(true);

        element = xmlTreeBuilder.insert(startTag);

        Assert.assertNotNull(element);
        Assert.assertEquals("custom", element.tagName());
        Assert.assertEquals(0, element.attributes().size());
        Assert.assertFalse(element.tag().isKnownTag());
        Assert.assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertForCommentTokenBogus() {
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        commentToken.setBogus(true);
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof Comment);
        Assert.assertEquals("<!DOCTYPE html>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsertForCharacterTokenCData() {
        Token.Character character = new Token.Character("<![CDATA[ Test data ]]> ");
        character.setCData(true);
        xmlTreeBuilder.insert(character);

        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertTrue(childNodes.get(0) instanceof CDataNode);
        Assert.assertEquals("<![CDATA[ Test data ]]>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testInsertForDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype("html");
        doctype.setPublicIdentifier("PUBLIC");
        doctype.setSystemIdentifier("SYSTEM");
        doctype.setPubSysKey("PUBSYSKEY");

        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.doc.childNodes();
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"PUBLIC\" \"SYSTEM\" PUBSYSKEY>", childNodes.get(0).outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Element div1 = new Element("div", baseUri);
        xmlTreeBuilder.stack.add(div1);
        Element div2 = new Element("div", baseUri);
        xmlTreeBuilder.stack.add(div2);
        Element div3 = new Element("div", baseUri);
        xmlTreeBuilder.stack.add(div3);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);

        Assert.assertEquals(2, xmlTreeBuilder.stack.size());
        Assert.assertEquals(div1, xmlTreeBuilder.stack.get(0));
        Assert.assertEquals(div2, xmlTreeBuilder.stack.get(1));
    }

    @Test
    public void testParseFragment() throws IOException {
        String inputFragment = "<root><child></child><child></child></root>";
        List<Node> childNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        Assert.assertNotNull(childNodes);
        Assert.assertEquals(1, childNodes.size());
        Assert.assertEquals("root", childNodes.get(0).nodeName());
        Assert.assertEquals(2, childNodes.get(0).childNodes().size());
        Assert.assertEquals("child", childNodes.get(0).child(0).nodeName());
        Assert.assertEquals("child", childNodes.get(0).child(1).nodeName());
    }
}