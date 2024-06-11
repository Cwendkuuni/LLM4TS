package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document.OutputSettings.Syntax;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Element doc;
    private Element el;
    private TextNode textNode;
    private Comment commentNode;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = mock(Element.class);
        el = mock(Element.class);
        textNode = mock(TextNode.class);
        commentNode = mock(Comment.class);

        xmlTreeBuilder.push("", "");
        xmlTreeBuilder.stack.add(doc);
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("", "", null);
        assertEquals(Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTagToken = mock(Token.StartTag.class);
        xmlTreeBuilder.process(startTagToken);
        verify(xmlTreeBuilder).insert(startTagToken.asStartTag());
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTagToken = mock(Token.EndTag.class);
        xmlTreeBuilder.process(endTagToken);
        verify(xmlTreeBuilder).popStackToClose(endTagToken.asEndTag());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        xmlTreeBuilder.process(commentToken);
        verify(xmlTreeBuilder).insert(commentToken.asComment());
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = mock(Token.Character.class);
        xmlTreeBuilder.process(characterToken);
        verify(xmlTreeBuilder).insert(characterToken.asCharacter());
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctypeToken = mock(Token.Doctype.class);
        xmlTreeBuilder.process(doctypeToken);
        verify(xmlTreeBuilder).insert(doctypeToken.asDoctype());
    }

    @Test(expected = RuntimeException.class)
    public void testProcess_UnexpectedToken() {
        Token token = mock(Token.class);
        xmlTreeBuilder.process(token);
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);
        xmlTreeBuilder.insertNode(node);
        verify(xmlTreeBuilder.currentElement()).appendChild(node);
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);

        xmlTreeBuilder.insert(startTag);
        verify(Tag).valueOf(startTag.name());
        verify(xmlTreeBuilder).insertNode(el);
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(tag).setSelfClosing();
        verify(xmlTreeBuilder.stack).add(el);
    }

    @Test
    public void testInsert_SelfClosingStartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);

        xmlTreeBuilder.insert(startTag);
        verify(Tag).valueOf(startTag.name());
        verify(xmlTreeBuilder).insertNode(el);
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(tag, never()).setSelfClosing();
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        Comment comment = mock(Comment.class);

        xmlTreeBuilder.insert(commentToken);
        verify(xmlTreeBuilder).insertNode(comment);
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = mock(Token.Character.class);
        TextNode node = mock(TextNode.class);

        xmlTreeBuilder.insert(characterToken);
        verify(xmlTreeBuilder).insertNode(node);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctypeToken = mock(Token.Doctype.class);
        DocumentType doctype = mock(DocumentType.class);

        xmlTreeBuilder.insert(doctypeToken);
        verify(xmlTreeBuilder).insertNode(doctype);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);

        xmlTreeBuilder.popStackToClose(endTag);
        verify(xmlTreeBuilder.stack, never()).remove(anyInt());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "";
        ParseErrorList errors = null;
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", errors);
        assertEquals(0, nodes.size());
    }
}