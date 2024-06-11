import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Mock
    private Document doc;

    @Mock
    private Token token;

    @Mock
    private TreeBuilder treeBuilder;

    @Mock
    private DocumentType docType;

    @Mock
    private Token.StartTag startTag;

    @Before
    public void setup() {
        xmlTreeBuilder = Mockito.spy(new XmlTreeBuilder());
        xmlTreeBuilder.doc = doc;
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml>Test</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        verify(xmlTreeBuilder).initialiseParse(input, baseUri, errors);

        Stack stack = xmlTreeBuilder.getStack();
        assertEquals(doc, stack.get(0));
    }

    @Test
    public void testProcess_StartTag() {
        doReturn("div").when(startTag).name();
        doReturn(Token.TokenType.StartTag).when(token).type();
        doReturn(startTag).when(token).asStartTag();

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(startTag);
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        doReturn("div").when(endTag).name();
        doReturn(Token.TokenType.EndTag).when(token).type();
        doReturn(endTag).when(token).asEndTag();

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).popStackToClose(endTag);
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        doReturn("a comment").when(commentToken).getData();
        doReturn(Token.TokenType.Comment).when(token).type();
        doReturn(commentToken).when(token).asComment();

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(commentToken);
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = mock(Token.Character.class);
        doReturn("a character").when(characterToken).getData();
        doReturn(Token.TokenType.Character).when(token).type();
        doReturn(characterToken).when(token).asCharacter();

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(characterToken);
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = mock(Token.Doctype.class);
        doReturn("html").when(doctype).getName();
        doReturn(Token.TokenType.Doctype).when(token).type();
        doReturn(doctype).when(token).asDoctype();

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).insert(doctype);
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(element);

        xmlTreeBuilder.insertNode(node);

        verify(element).appendChild(node);
    }

    @Test
    public void testInsert_StartTag() {
        Tag tag = mock(Tag.class);
        when(startTag.name()).thenReturn("div");
        when(startTag.attributes()).thenReturn(new Attributes());
        when(xmlTreeBuilder.currentElement()).thenReturn(mock(Element.class));
        when(xmlTreeBuilder.insertNode(any(Element.class))).thenReturn(mock(Element.class));
        when(tag.isKnownTag()).thenReturn(true);
        when(startTag.isSelfClosing()).thenReturn(false);

        Element el = xmlTreeBuilder.insert(startTag);

        assertFalse(tag.isSelfClosing());
        verify(xmlTreeBuilder).insertNode(any(Element.class));
        verify(xmlTreeBuilder.getStack()).add(any(Element.class));
        assertEquals(el, xmlTreeBuilder.insert(startTag));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Tag tag = mock(Tag.class);
        when(startTag.name()).thenReturn("div");
        when(startTag.attributes()).thenReturn(new Attributes());
        when(xmlTreeBuilder.currentElement()).thenReturn(mock(Element.class));
        when(xmlTreeBuilder.insertNode(any(Element.class))).thenReturn(mock(Element.class));
        when(tag.isKnownTag()).thenReturn(false);
        when(startTag.isSelfClosing()).thenReturn(true);

        Element el = xmlTreeBuilder.insert(startTag);

        assertTrue(tag.isSelfClosing());
        assertEquals(el, xmlTreeBuilder.insert(startTag));
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = mock(Token.Comment.class);
        Comment comment = mock(Comment.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(mock(Element.class));
        when(xmlTreeBuilder.insertNode(any(Comment.class))).thenReturn(comment);

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insert(commentToken);
        verify(xmlTreeBuilder).insertNode(comment);
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = mock(Token.Character.class);
        TextNode textNode = mock(TextNode.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(mock(Element.class));
        when(xmlTreeBuilder.insertNode(any(TextNode.class))).thenReturn(textNode);

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder).insert(characterToken);
        verify(xmlTreeBuilder).insertNode(textNode);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = mock(Token.Doctype.class);
        DocumentType documentType = mock(DocumentType.class);
        when(doctype.getName()).thenReturn("test");
        when(doctype.getPublicIdentifier()).thenReturn("publicId");
        when(doctype.getSystemIdentifier()).thenReturn("systemId");
        when(xmlTreeBuilder.currentElement()).thenReturn(mock(Element.class));
        when(xmlTreeBuilder.insertNode(any(DocumentType.class))).thenReturn(documentType);

        xmlTreeBuilder.insert(doctype);

        verify(xmlTreeBuilder).insert(doctype);
        verify(xmlTreeBuilder).insertNode(documentType);
    }

    @Test
    public void testPopStackToClose_ElementExists() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        Element element = mock(Element.class);
        when(xmlTreeBuilder.getStack()).thenReturn(new Stack());
        Stack stack = xmlTreeBuilder.getStack();
        stack.add(element);
        when(endTag.name()).thenReturn("div");

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).popStackToClose(endTag);
        assertEquals(0, stack.size());
    }

    @Test
    public void testPopStackToClose_ElementDoesNotExist() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        when(xmlTreeBuilder.getStack()).thenReturn(new Stack());
        Stack stack = xmlTreeBuilder.getStack();
        when(endTag.name()).thenReturn("div");

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).popStackToClose(endTag);
        verify(xmlTreeBuilder, never()).insertNode(any(Node.class));
        assertEquals(0, stack.size());
    }
}