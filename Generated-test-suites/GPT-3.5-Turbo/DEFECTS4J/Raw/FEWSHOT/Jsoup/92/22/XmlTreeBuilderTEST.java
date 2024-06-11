import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder builder;

    @Before
    public void setUp() {
        builder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        Assert.assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
    }

    @Test
    public void testInitialiseParse() {
        // Test case when input is null
        builder.initialiseParse(null, "", new Parser(builder));
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertEquals(builder.doc, builder.stack.get(0));

        // Test case when input is not null
        builder.initialiseParse(new StringReader(""), "", new Parser(builder));
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertEquals(builder.doc, builder.stack.get(0));
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testParse() {
        // Test case when input is null
        Document document1 = builder.parse((Reader) null, "");
        Assert.assertNotNull(document1);
        Assert.assertTrue(document1.childNodes().isEmpty());

        // Test case when input is not null
        Document document2 = builder.parse(new StringReader(""), "");
        Assert.assertNotNull(document2);
        Assert.assertTrue(document2.childNodes().isEmpty());
    }

    @Test
    public void testProcess() {
        // Test case with different token types
        Assert.assertTrue(builder.process(new Token.StartTag("tag")));
        Assert.assertTrue(builder.process(new Token.EndTag("tag")));
        Assert.assertTrue(builder.process(new Token.Comment("comment")));
        Assert.assertTrue(builder.process(new Token.Character("text")));
        Assert.assertTrue(builder.process(new Token.Doctype("doctype")));

        // Test case with unexpected token type
        try {
            builder.process(new Token.TokenType() {
                @Override
                public String toString() {
                    return "custom";
                }
            });
            Assert.fail("Expected Validate.fail() to be called");
        } catch (Throwable t) {
            Assert.assertEquals("Unexpected token type: custom", t.getMessage());
        }
    }

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("tag"), "", builder.settings);
        builder.stack.add(element);

        builder.insertNode(new Node(""));
        Assert.assertEquals(1, element.childNodes().size());
        Assert.assertEquals("", element.childNodes().get(0).nodeName());
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr", "value");

        Element element = builder.insert(startTag);
        Assert.assertEquals("tag", element.nodeName());
        Assert.assertEquals("value", element.attributes().get("attr"));
        Assert.assertEquals(builder.baseUri, element.baseUri());
        Assert.assertEquals(builder.settings, element.parser().settings());
        Assert.assertEquals(ParseSettings.normalizeTag("tag"), element.tag());
        Assert.assertEquals(Tag.valueOf("tag"), element.tag());
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertEquals(element, builder.stack.get(0));

        // Test case when start tag is self-closing
        startTag.selfClosing = true;
        builder.insert(startTag);
        Assert.assertTrue(element.tag().isKnownTag());
    }

    @Test
    public void testInsert_Comment() {
        Comment comment = new Comment("comment");
        builder.insert(comment);
        Assert.assertEquals(1, builder.currentElement().childNodes().size());
        Assert.assertEquals(comment, builder.currentElement().childNodes().get(0));

        // Test case when comment is a bogus xml declaration
        Token.Comment bogusComment = new Token.Comment("<!DOCTYPE html>");
        bogusComment.bogus = true;
        builder.insert(bogusComment);
        Assert.assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testInsert_Character() {
        // Test case when character is a CData
        Token.Character cdata = new Token.Character("cdata");
        cdata.setCDataFlag(true);
        builder.insert(cdata);
        Assert.assertTrue(builder.currentElement().childNodes().get(0) instanceof CDataNode);

        // Test case when character is a TextNode
        builder.insert(new Token.Character("text"));
        Assert.assertTrue(builder.currentElement().childNodes().get(1) instanceof TextNode);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");
        doctype.publicIdentifier("public");
        doctype.systemIdentifier("system");

        builder.insert(doctype);
        Assert.assertEquals(1, builder.currentElement().childNodes().size());
        Assert.assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Element element = new Element(Tag.valueOf("tag"), "", builder.settings);
        builder.stack.add(element);

        // Test case when element is not found in the stack
        builder.popStackToClose(endTag);
        Assert.assertEquals(1, builder.stack.size());

        // Test case when element is found in the stack
        builder.stack.add(new Element(Tag.valueOf("tag"), "", builder.settings));
        builder.popStackToClose(endTag);
        Assert.assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>text</tag>";
        List<Node> nodes = builder.parseFragment(inputFragment, "", new Parser(builder));
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
    }

    @Test
    public void testParseFragmentWithContext() {
        String inputFragment = "<tag>text</tag>";
        Element context = new Element(Tag.valueOf("context"), "", builder.settings);
        List<Node> nodes = builder.parseFragment(inputFragment, context, "", new Parser(builder));
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
    }

}