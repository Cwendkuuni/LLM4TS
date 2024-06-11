import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

    @Mock
    Document docMock;

    @Mock
    Token.StartTag startTagMock;

    @Mock
    Token.EndTag endTagMock;

    @Mock
    Token.Comment commentTokenMock;

    @Mock
    Token.Character characterTokenMock;

    @Mock
    Token.Doctype doctypeMock;

    @Test
    public void testInitialiseParse() {
        String input = "<html></html>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(baseUri, xmlTreeBuilder.baseUri);
        assertEquals(errors, xmlTreeBuilder.errors);
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Tag tag = Tag.valueOf("div");
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute("class", "header"));
        when(startTagMock.name()).thenReturn(tag.toString());
        when(startTagMock.attributes).thenReturn(attributes);

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.doc = docMock;
        xmlTreeBuilder.stack = new ArrayList<>();
        xmlTreeBuilder.tokeniser = mock(Tokeniser.class);

        Element result = xmlTreeBuilder.insert(startTagMock);

        assertEquals("div", result.tagName());
        assertEquals(attributes, result.attributes().asList());
        verify(xmlTreeBuilder.tokeniser).acknowledgeSelfClosingFlag();
        verify(tag).setSelfClosing();
    }

    @Test
    public void testProcess_EndTag() {
        String elName = "div";
        when(endTagMock.name()).thenReturn(elName);

        Element firstFound = mock(Element.class);
        when(firstFound.nodeName()).thenReturn(elName);

        List<Element> stack = new ArrayList<>();
        stack.add(firstFound);
        stack.add(mock(Element.class));

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack = stack;

        xmlTreeBuilder.popStackToClose(endTagMock);

        assertEquals(1, xmlTreeBuilder.stack.size());
        verify(stack.get(0)).nodeName();
        verify(xmlTreeBuilder.stack, never()).remove(anyInt());
    }

    @Test
    public void testProcess_Comment() {
        String commentData = "This is a comment";
        when(commentTokenMock.getData()).thenReturn(commentData);
        when(commentTokenMock.bogus).thenReturn(false);

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.baseUri = "http://www.example.com";
        xmlTreeBuilder.doc = docMock;
        
        xmlTreeBuilder.insert(commentTokenMock);

        verify(docMock).appendChild(any(Comment.class));
    }

    @Test
    public void testProcess_Comment_Bogus() {
        String commentData = "This is a bogus comment";
        when(commentTokenMock.getData()).thenReturn(commentData);
        when(commentTokenMock.bogus).thenReturn(true);

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.baseUri = "http://www.example.com";
        xmlTreeBuilder.doc = docMock;

        xmlTreeBuilder.insert(commentTokenMock);

        verify(docMock).appendChild(any(Comment.class));
    }

    @Test
    public void testProcess_Character() {
        String characterData = "Character data";
        when(characterTokenMock.getData()).thenReturn(characterData);

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.baseUri = "http://www.example.com";
        xmlTreeBuilder.doc = docMock;

        xmlTreeBuilder.insert(characterTokenMock);

        verify(docMock).appendChild(any(TextNode.class));
    }

    @Test
    public void testProcess_Doctype() {
        String doctypeName = "doctype";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        when(doctypeMock.getName()).thenReturn(doctypeName);
        when(doctypeMock.getPublicIdentifier()).thenReturn(publicIdentifier);
        when(doctypeMock.getSystemIdentifier()).thenReturn(systemIdentifier);

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.baseUri = "http://www.example.com";
        xmlTreeBuilder.doc = docMock;

        xmlTreeBuilder.insert(doctypeMock);

        verify(docMock).appendChild(any(DocumentType.class));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Test</p>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}