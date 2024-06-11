package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Doctype;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.AttributeToken;
import org.jsoup.parser.Token.CommentToken;
import org.jsoup.parser.Token.CharacterToken;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.DoctypeToken;

public class XmlTreeBuilderTest {

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

        // Test with empty input
        Token[] tokens = new Token[0];
        xmlTreeBuilder.initialiseParse("", null, null);
        xmlTreeBuilder.parse(tokens);
        assertEquals(0, xmlTreeBuilder.doc.childNodes().size());

        // Test with StartTag
        String input = "<test>";
        tokens = parseInputToTokens(input);
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.parse(tokens);
        Element root = xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals("test", root.nodeName());

        // Test with EndTag
        input = "<test></test>";
        tokens = parseInputToTokens(input);
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.parse(tokens);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertSame(root, xmlTreeBuilder.doc.childNodes().get(0));

        // Test with Comment
        input = "<!-- Comment -->";
        tokens = parseInputToTokens(input);
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.parse(tokens);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals("Comment", xmlTreeBuilder.doc.childNodes().get(0).nodeName());

        // Test with Character
        input = "<test>abc</test>";
        tokens = parseInputToTokens(input);
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.parse(tokens);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals("abc", xmlTreeBuilder.doc.childNodes().get(0).text());

        // Test with Doctype
        input = "<!DOCTYPE test [ <!ENTITY testEntity SYMBOL \"test\"> ]>";
        tokens = parseInputToTokens(input);
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.parse(tokens);
        Doctype doctype = (Doctype) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals("test", doctype.name());

        // Test with EOF
        input = "";
        tokens = new Token[0];
        xmlTreeBuilder.initialiseParse(input, null, null);
        xmlTreeBuilder.parse(tokens);
        assertEquals(0, xmlTreeBuilder.doc.childNodes().size());

        // Test with unexpected token type
        input = "<test> <test>";
        tokens = parseInputToTokens(input);
        xmlTreeBuilder.initialiseParse(input, null, null);
        try {
            xmlTreeBuilder.parse(tokens);
            Validate.fail("Unexpected test success.");
        } catch (AssertionError e) {
            // expected
        }
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("test");
        xmlTreeBuilder.insertNode(root);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertSame(root, xmlTreeBuilder.doc.childNodes().get(0));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Comment comment = new Comment("Comment");
        xmlTreeBuilder.insert(new CommentToken("-- Comment"));
        xmlTreeBuilder.insert(comment);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals("Comment", xmlTreeBuilder.doc.childNodes().get(0).nodeName());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("abc");
        xmlTreeBuilder.insert(new CharacterToken('a'));
        xmlTreeBuilder.insert(new CharacterToken('b'));
        xmlTreeBuilder.insert(new CharacterToken('c'));
        xmlTreeBuilder.insert(textNode);
        assertEquals("abc", textNode.text());
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals("abc", xmlTreeBuilder.doc.childNodes().get(0).text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Doctype doctype = new Doctype("test", null, null, null);
        xmlTreeBuilder.insert(new DoctypeToken("<!DOCTYPE test [ <!ENTITY testEntity SYMBOL \"test\"> ]>"));
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals("test", doctype.name());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        xmlTreeBuilder.insert(new StartTag("parent"));
        xmlTreeBuilder.insert(parent);
        xmlTreeBuilder.insert(new StartTag("child1"));
        xmlTreeBuilder.insert(child1);
        xmlTreeBuilder.insert(new EndTag("child1"));
        xmlTreeBuilder.insert(new StartTag("child2"));
        xmlTreeBuilder.insert(child2);
        xmlTreeBuilder.insert(new EndTag("parent"));

        xmlTreeBuilder.popStackToClose(new EndTag("child2"));
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertSame(parent, xmlTreeBuilder.stack.get(1));

        xmlTreeBuilder.popStackToClose(new EndTag("child1"));
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertSame(parent, xmlTreeBuilder.stack.get(0));

        xmlTreeBuilder.popStackToClose(new EndTag("parent"));
        assertEquals(0, xmlTreeBuilder.stack.size());

        xmlTreeBuilder.popStackToClose(new EndTag("test"));
        assertEquals(0, xmlTreeBuilder.stack.size()); // should not find "parent" in the stack
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<test><child><grandchild/></child></test>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, null, null);
        Document doc = xmlTreeBuilder.doc;
        assertEquals(1, doc.childNodes().size());
        assertEquals("test", doc.childNodes().get(0).nodeName());

        Element test = (Element) doc.childNodes().get(0);
        assertEquals(1, test.childNodes().size());
        assertEquals("child", test.childNodes().get(0).nodeName());

        Element child = (Element) test.childNodes().get(0);
        assertEquals(1, child.childNodes().size());
        assertEquals("grandchild", child.childNodes().get(0).nodeName());

        assertEquals(3, nodes.size());
        assertEquals(test, nodes.get(0));
        assertEquals(child, nodes.get(1));
        assertEquals(child.childNodes().get(0), nodes.get(2));
    }

    @Test
    public void testParseFragmentWithXmlDeclaration() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<test><child><grandchild/></child></test>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, null, null);
        Document doc = xmlTreeBuilder.doc;
        assertEquals(1, doc.childNodes().size());
        DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
        assertEquals("xml", doctypeNode.name());
        assertEquals(3, nodes.size());
        assertEquals(doc, nodes.get(0));
        assertEquals(doctypeNode, nodes.get(1));
        assertEquals(test, nodes.get(2));
    }

    @Test
    public void testParseFragmentWithEmptyInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment("", null, null);
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseFragmentWithInvalidInput() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<test><!-- Comment --><child></test>";
        try {
            List<Node> nodes = xmlTreeBuilder.parseFragment(input, null, null);
            Validate.fail("Unexpected test success.");
        } catch (AssertionError e) {
            // expected
        }
        assertEquals(0, xmlTreeBuilder.doc.childNodes().size());
    }

    private Token[] parseInputToTokens(String input) {
        return new ParseHandler() {
            @Override
            public void handleToken(Token token) {
                // do nothing
            }

            @Override
            public void handleStartTag(StartTag tag, List<Attribute> attrs) {
                if (tag.name().equals("test")) {
                    tag.setIsSelfClosing(true);
                }
                handleToken(tag);
            }

            @Override
            public void handleEndTag(EndTag tag) {
                handleToken(tag);
            }

            @Override
            public void handleCharacter(CharSequence chars, int length) {
                if (chars.toString().equals("")) {
                    handleToken(new CommentToken(""));
                } else {
                    handleToken(new CharacterToken(chars.charAt(0)));
                    for (int i = 1; i < length; i++) {
                        handleToken(new CharacterToken(chars.charAt(i)));
                    }
                }
            }

            @Override
            public void handleDoctype(Doctype doctype) {
                handleToken(new DoctypeToken(doctype.toString()));
            }

            @Override
            public void handleError(ParseError error) {
                // do nothing
            }
        }.handleInput(input);
    }
}