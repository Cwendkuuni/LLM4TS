import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder parser;

    @Before
    public void setUp() {
        parser = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = parser.defaultSettings();
        Assert.assertNotNull(settings);
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParse() {
        String input = "<root><child1>value1</child1><child2>value2</child2></root>";
        String baseUri = "https://www.example.com";

        Document doc = parser.parse(input, baseUri);

        Assert.assertNotNull(doc);
        Assert.assertEquals("root", doc.child(0).tagName());
        Assert.assertEquals("child1", doc.child(0).child(0).tagName());
        Assert.assertEquals("value1", doc.child(0).child(0).text());
        Assert.assertEquals("child2", doc.child(0).child(1).tagName());
        Assert.assertEquals("value2", doc.child(0).child(1).text());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child1>value1</child1><child2>value2</child2></root>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        parser.initialiseParse(input, baseUri, errors, settings);

        Assert.assertEquals(input, parser.getInput());
        Assert.assertEquals(baseUri, parser.getBaseUri());
        Assert.assertEquals(errors, parser.getErrors());
        Assert.assertEquals(settings, parser.getSettings());
        Assert.assertEquals(1, parser.getStack().size());
        Assert.assertEquals("root", parser.getStack().get(0).tagName());
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, parser.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcess_startTag() {
        Token token = new Token.StartTag("tag", "<tag>", new Attributes());

        boolean result = parser.process(token);

        Assert.assertTrue(result);
        Assert.assertEquals("tag", parser.getCurrentElement().tagName());
    }

    @Test
    public void testProcess_endTag() {
        Token token = new Token.EndTag("tag");

        Element element = new Element("tag");
        parser.getStack().add(element);

        boolean result = parser.process(token);

        Assert.assertTrue(result);
        Assert.assertTrue(parser.getStack().isEmpty());
    }

    @Test
    public void testProcess_comment() {
        Token token = new Token.Comment("comment");

        boolean result = parser.process(token);

        Assert.assertTrue(result);
        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("comment", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcess_character() {
        Token token = new Token.Character("text");

        boolean result = parser.process(token);

        Assert.assertTrue(result);
        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("text", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcess_doctype() {
        Token token = new Token.Doctype("html");

        boolean result = parser.process(token);

        Assert.assertTrue(result);
        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("<!DOCTYPE html>", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("tag");
        parser.getStack().add(element);
        Element node = new Element("child");

        parser.insertNode(node);

        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("child", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_startTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "<tag>", new Attributes());

        Element element = parser.insert(startTag);

        Assert.assertEquals("tag", element.tagName());
        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("tag", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_selfClosingTagUnknown() {
        Token.StartTag startTag = new Token.StartTag("unknown", "<unknown />", new Attributes().put("attr", "value"));

        Element element = parser.insert(startTag);

        Assert.assertEquals("unknown", element.tagName());
        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("unknown", parser.getCurrentElement().childNode(0).outerHtml());
        Assert.assertTrue(element.tag().isKnownTag());
        Assert.assertFalse(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_selfClosingTagKnown() {
        Token.StartTag startTag = new Token.StartTag("known", "<known />", new Attributes().put("attr", "value"));

        Element element = parser.insert(startTag);

        Assert.assertEquals("known", element.tagName());
        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("known", parser.getCurrentElement().childNode(0).outerHtml());
        Assert.assertTrue(element.tag().isKnownTag());
        Assert.assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_comment() {
        Token.Comment commentToken = new Token.Comment("comment");

        parser.insert(commentToken);

        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("<!--comment-->", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_commentBogus() {
        Token.Comment commentToken = new Token.Comment("<!--?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?-->");

        parser.insert(commentToken);

        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\" ?>", parser.getCurrentElement().childNode(0).outerHtml());
        Assert.assertEquals("xml", parser.getCurrentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert_character() {
        Token.Character characterToken = new Token.Character("text");

        parser.insert(characterToken);

        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("text", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_doctype() {
        Token.Doctype doctypeToken = new Token.Doctype("<!DOCTYPE html>");

        parser.insert(doctypeToken);

        Assert.assertEquals(1, parser.getCurrentElement().childNodes().size());
        Assert.assertEquals("<!DOCTYPE html>", parser.getCurrentElement().childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose_notFound() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element = new Element("other");
        parser.getStack().add(element);

        parser.popStackToClose(endTag);

        Assert.assertEquals(1, parser.getStack().size());
    }

    @Test
    public void testPopStackToClose_found() {
        Token.EndTag endTag = new Token.EndTag("tag");

        Element element1 = new Element("tag");
        Element element2 = new Element("other");
        parser.getStack().add(element2);
        parser.getStack().add(element1);

        parser.popStackToClose(endTag);

        Assert.assertEquals(1, parser.getStack().size());
        Assert.assertEquals(element2, parser.getStack().get(0));
    }

    @Test
    public void testParseFragment() {
        String input = "<child1>value1</child1><child2>value2</child2>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = parser.parseFragment(input, baseUri, errors, settings);

        Assert.assertEquals(2, nodes.size());
        Assert.assertEquals("child1", nodes.get(0).nodeName());
        Assert.assertEquals("value1", nodes.get(0).outerHtml());
        Assert.assertEquals("child2", nodes.get(1).nodeName());
        Assert.assertEquals("value2", nodes.get(1).outerHtml());
    }
}