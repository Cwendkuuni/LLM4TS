import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder treeBuilder;
    private Element doc;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.Doctype doctype;

    @Before
    public void setUp() {
        treeBuilder = new XmlTreeBuilder();
        doc = mock(Element.class);
        startTag = mock(Token.StartTag.class);
        endTag = mock(Token.EndTag.class);
        commentToken = mock(Token.Comment.class);
        characterToken = mock(Token.Character.class);
        doctype = mock(Token.Doctype.class);

        when(startTag.name()).thenReturn("start tag");
        when(endTag.name()).thenReturn("end tag");
        when(commentToken.getData()).thenReturn("comment data");
        when(characterToken.getData()).thenReturn("character data");
        when(doctype.getName()).thenReturn("doctype name");

        when(treeBuilder.currentElement()).thenReturn(doc);
    }

    @Test
    public void testInitialiseParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = mock(ParseErrorList.class);

        treeBuilder.initialiseParse(input, baseUri, errors);

        verify(treeBuilder).super.initialiseParse(input, baseUri, errors);
        verify(treeBuilder.stack).add(doc);
    }

    @Test
    public void testProcessStartTag() {
        treeBuilder.process(startTag);

        verify(treeBuilder).insert(startTag.asStartTag());
    }

    @Test
    public void testProcessEndTag() {
        treeBuilder.process(endTag);

        verify(treeBuilder).popStackToClose(endTag.asEndTag());
    }

    @Test
    public void testProcessComment() {
        treeBuilder.process(commentToken);

        verify(treeBuilder).insert(commentToken.asComment());
    }

    @Test
    public void testProcessCharacter() {
        treeBuilder.process(characterToken);

        verify(treeBuilder).insert(characterToken.asCharacter());
    }

    @Test
    public void testProcessDoctype() {
        treeBuilder.process(doctype);

        verify(treeBuilder).insert(doctype.asDoctype());
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);

        treeBuilder.insertNode(node);

        verify(treeBuilder.currentElement()).appendChild(node);
    }

    @Test
    public void testInsertStartTagSelfClosing() {
        when(startTag.isSelfClosing()).thenReturn(true);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);
        when(startTag.name()).thenReturn("tag");
        when(startTag.attributes()).thenReturn(null);
        when(treeBuilder.currentElement()).thenReturn(el);
        when(el.nodeName()).thenReturn("el");

        when(Tag.valueOf("tag")).thenReturn(tag);
        when(new Element(tag, treeBuilder.baseUri, null)).thenReturn(el);

        Element result = treeBuilder.insert(startTag);

        verify(treeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(tag).setSelfClosing();
        verify(treeBuilder.stack, never()).add(el);
        assertEquals(el, result);
    }

    @Test
    public void testInsertStartTagNotSelfClosing() {
        when(startTag.isSelfClosing()).thenReturn(false);
        Tag tag = mock(Tag.class);
        Element el = mock(Element.class);
        when(startTag.name()).thenReturn("tag");
        when(startTag.attributes()).thenReturn(null);
        when(treeBuilder.currentElement()).thenReturn(el);
        when(el.nodeName()).thenReturn("el");

        when(Tag.valueOf("tag")).thenReturn(tag);
        when(new Element(tag, treeBuilder.baseUri, null)).thenReturn(el);

        Element result = treeBuilder.insert(startTag);

        verify(treeBuilder.tokeniser, never()).acknowledgeSelfClosingFlag();
        verify(tag, never()).setSelfClosing();
        verify(treeBuilder.stack).add(el);
        assertEquals(el, result);
    }

    @Test
    public void testInsertComment() {
        Comment comment = mock(Comment.class);
        when(commentToken.getData()).thenReturn("data");
        when(commentToken.getBaseUri()).thenReturn("baseUri");
        when(new Comment("data", "baseUri")).thenReturn(comment);

        treeBuilder.insert(commentToken);

        verify(treeBuilder).insertNode(comment);
    }

    @Test
    public void testInsertCharacter() {
        TextNode textNode = mock(TextNode.class);
        when(characterToken.getData()).thenReturn("data");
        when(characterToken.getBaseUri()).thenReturn("baseUri");
        when(new TextNode("data", "baseUri")).thenReturn(textNode);

        treeBuilder.insert(characterToken);

        verify(treeBuilder).insertNode(textNode);
    }

    @Test
    public void testInsertDoctype() {
        DocumentType doctypeNode = mock(DocumentType.class);
        when(doctype.getName()).thenReturn("name");
        when(doctype.getPublicIdentifier()).thenReturn("publicIdentifier");
        when(doctype.getSystemIdentifier()).thenReturn("systemIdentifier");
        when(doctype.getBaseUri()).thenReturn("baseUri");
        when(new DocumentType("name", "publicIdentifier", "systemIdentifier", "baseUri")).thenReturn(doctypeNode);

        treeBuilder.insert(doctype);

        verify(treeBuilder).insertNode(doctypeNode);
    }

    @Test
    public void testPopStackToCloseNotFound() {
        when(endTag.name()).thenReturn("unknown");
        Iterator<Element> iterator = mock(Iterator.class);
        when(treeBuilder.stack.descendingIterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(false);

        treeBuilder.popStackToClose(endTag.asEndTag());

        verify(treeBuilder.stack, never()).descendingIterator();
        verify(iterator, never()).hasNext();
    }

    @Test
    public void testPopStackToCloseFound() {
        when(endTag.name()).thenReturn("tag");
        Iterator<Element> iterator = mock(Iterator.class);
        Element element = mock(Element.class);
        when(treeBuilder.stack.descendingIterator()).thenReturn(iterator);
        when(iterator.hasNext()).thenReturn(true, false);
        when(iterator.next()).thenReturn(element);
        when(element.nodeName()).thenReturn("tag");

        treeBuilder.popStackToClose(endTag.asEndTag());

        verify(treeBuilder.stack).descendingIterator();
        verify(iterator, times(2)).hasNext();
        verify(iterator, times(2)).next();
        verify(iterator).remove();
        verify(treeBuilder.stack, times(2)).descendingIterator();
        verify(iterator, times(2)).hasNext();
        verify(iterator, times(2)).next();
        verify(iterator).remove();
    }
}