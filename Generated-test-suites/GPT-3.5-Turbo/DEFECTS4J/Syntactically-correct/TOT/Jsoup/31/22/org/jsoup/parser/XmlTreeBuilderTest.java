import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;

import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Token token;
    private Element element;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.Doctype doctype;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();

        token = mock(Token.class);
        element = mock(Element.class);
        commentToken = mock(Token.Comment.class);
        characterToken = mock(Token.Character.class);
        doctype = mock(Token.Doctype.class);
    }

    @Test
    public void testInitialiseParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = mock(ParseErrorList.class);

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        verify(xmlTreeBuilder).initialiseParse(input, baseUri, errors);
        verify(xmlTreeBuilder).setBaseUri(baseUri);
        verify(xmlTreeBuilder.getErrors()).addAll(errors);
        verify(xmlTreeBuilder.getHasRoot()).set(true);
    }

    @Test
    public void testProcess_StartTag() {
        when(token.type()).thenReturn(Token.TokenType.StartTag);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).process(token);
        verify(xmlTreeBuilder).insert(token.asStartTag());
    }

    @Test
    public void testProcess_EndTag() {
        when(token.type()).thenReturn(Token.TokenType.EndTag);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).process(token);
        verify(xmlTreeBuilder).popStackToClose(token.asEndTag());
    }

    @Test
    public void testProcess_Comment() {
        when(token.type()).thenReturn(Token.TokenType.Comment);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).process(token);
        verify(xmlTreeBuilder).insert(token.asComment());
    }

    @Test
    public void testProcess_Character() {
        when(token.type()).thenReturn(Token.TokenType.Character);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).process(token);
        verify(xmlTreeBuilder).insert(token.asCharacter());
    }

    @Test
    public void testProcess_Doctype() {
        when(token.type()).thenReturn(Token.TokenType.Doctype);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).process(token);
        verify(xmlTreeBuilder).insert(token.asDoctype());
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);
        Element currentElement = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(currentElement);

        xmlTreeBuilder.insertNode(node);

        verify(xmlTreeBuilder).insertNode(node);
        verify(currentElement).appendChild(node);
    }

    @Test
    public void testInsert_StartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        Attributes startTagAttributes = mock(Attributes.class);
        when(startTag.name()).thenReturn("tag");
        when(xmlTreeBuilder.baseUri()).thenReturn("baseUri");
        when(startTag.attributes()).thenReturn(startTagAttributes);
        when(startTag.isSelfClosing()).thenReturn(false);
        when(tag.isKnownTag()).thenReturn(false);
        when(xmlTreeBuilder.tokeniser()).thenReturn(mock(Tokeniser.class));

        Element element = xmlTreeBuilder.insert(startTag);

        verify(xmlTreeBuilder).insert(startTag);
        verify(xmlTreeBuilder.baseUri()).thenReturn("baseUri", "baseUri");
        verify(startTag.attributes(), times(2)).size(); // ensuring attributes are accessed twice
        verify(xmlTreeBuilder.tokeniser()).acknowledgeSelfClosingFlag();
        verify(tag).setSelfClosing();
        verify(xmlTreeBuilder).currentElement();
        verify(currentElement).appendChild(element);
        // Additional assertions based on test case requirements
    }

    @Test
    public void testInsert_Comment() {
        String data = "commentData";
        when(commentToken.getData()).thenReturn(data);
        when(xmlTreeBuilder.baseUri()).thenReturn("baseUri");

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insert(commentToken);
        verify(xmlTreeBuilder.baseUri()).thenReturn("baseUri");
        verify(xmlTreeBuilder).currentElement().appendChild(any(Comment.class));
        // Additional assertions based on test case requirements
    }

    @Test
    public void testInsert_Character() {
        String data = "characterData";
        when(characterToken.getData()).thenReturn(data);
        when(xmlTreeBuilder.baseUri()).thenReturn("baseUri");

        xmlTreeBuilder.insert(characterToken);

        verify(xmlTreeBuilder).insert(characterToken);
        verify(xmlTreeBuilder.baseUri()).thenReturn("baseUri");
        verify(xmlTreeBuilder).currentElement().appendChild(any(TextNode.class));
        // Additional assertions based on test case requirements
    }

    @Test
    public void testInsert_Doctype() {
        String name = "doctypeName";
        String publicIdentifier = "publicIdentifier";
        String systemIdentifier = "systemIdentifier";
        when(doctype.getName()).thenReturn(name);
        when(doctype.getPublicIdentifier()).thenReturn(publicIdentifier);
        when(doctype.getSystemIdentifier()).thenReturn(systemIdentifier);
        when(xmlTreeBuilder.baseUri()).thenReturn("baseUri");

        xmlTreeBuilder.insert(doctype);

        verify(xmlTreeBuilder).insert(doctype);
        verify(xmlTreeBuilder.baseUri()).thenReturn("baseUri");
        verify(xmlTreeBuilder.currentElement()).appendChild(any(DocumentType.class));
        // Additional assertions based on test case requirements
    }

    @Test
    public void testPopStackToClose_TagFound() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        Element element3 = mock(Element.class);
        Iterator<Element> elementIterator = mock(Iterator.class);
        when(endTag.name()).thenReturn("tag");
        when(xmlTreeBuilder.stack()).thenReturn(Collections.singletonList(element1));
        when(element1.nodeName()).thenReturn("tag");

        when(xmlTreeBuilder.stack().descendingIterator()).thenReturn(elementIterator);
        when(elementIterator.hasNext()).thenReturn(true, true, true, false);
        when(elementIterator.next()).thenReturn(element3, element2, element1);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).popStackToClose(endTag);
        verify(xmlTreeBuilder.stack(), times(2)).descendingIterator();
        verify(elementIterator).hasNext();
        verify(elementIterator, times(3)).next();
        verify(elementIterator, times(2)).remove();
        verify(xmlTreeBuilder.stack(), times(2)).descendingIterator();
        // Additional assertions based on test case requirements
    }

    @Test
    public void testPopStackToClose_TagNotFound() {
        Token.EndTag endTag = mock(Token.EndTag.class);
        Element element1 = mock(Element.class);
        Element element2 = mock(Element.class);
        Element element3 = mock(Element.class);
        Iterator<Element> elementIterator = mock(Iterator.class);
        when(endTag.name()).thenReturn("tag");
        when(xmlTreeBuilder.stack()).thenReturn(Collections.singletonList(element1));
        when(element1.nodeName()).thenReturn("anotherTag");

        when(xmlTreeBuilder.stack().descendingIterator()).thenReturn(elementIterator);
        when(elementIterator.hasNext()).thenReturn(true, true, true, false);
        when(elementIterator.next()).thenReturn(element3, element2, element1);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).popStackToClose(endTag);
        verify(xmlTreeBuilder.stack(), times(2)).descendingIterator();
        verify(elementIterator).hasNext();
        verify(elementIterator, times(3)).next();
        verify(elementIterator, never()).remove();
        verify(xmlTreeBuilder.stack(), times(1)).descendingIterator();
        // Additional assertions based on test case requirements
    }

}