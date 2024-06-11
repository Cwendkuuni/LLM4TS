import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {

    @Mock
    private Reader mockReader;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        when(mockReader.toString()).thenReturn("test string");
        Document doc = xmlTreeBuilder.parse(mockReader, "http://www.example.com");

        // Assert
        // Verify that the expected document is returned
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<html></html>", "http://www.example.com");

        // Assert
        // Verify that the expected document is returned
        assertEquals("http://www.example.com", doc.baseUri());
        assertEquals(0, doc.childNodeSize());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document("");
        xmlTreeBuilder.initialiseParse(mockReader, "http://www.example.com", new ParseErrorList(), ParseSettings.preserveCase);
        assertEquals(doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String tag = "div";
        Token token = new Token.StartTag(tag);
        Tag expectedTag = Tag.valueOf(tag, ParseSettings.preserveCase);

        // Mock an empty stack
        List<Node> stack = mock(List.class);
        when(stack.size()).thenReturn(0);
        xmlTreeBuilder.setStack(stack);

        Element element = xmlTreeBuilder.insert(token);

        // Assert
        // Verify that the expected element and tag are created and inserted
        assertEquals(expectedTag, element.tag());
        assertEquals(1, element.siblingIndex());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String tag = "div";
        Token token = new Token.EndTag(tag);

        // Mock a stack with one element
        Element element = mock(Element.class);
        List<Node> stack = mock(List.class);
        when(stack.size()).thenReturn(1);
        when(stack.get(0)).thenReturn(element);
        xmlTreeBuilder.setStack(stack);

        xmlTreeBuilder.popStackToClose(token);

        // Assert
        // Verify that the stack is popped correctly
        verify(stack).remove(0);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String comment = "test comment";
        Token.Comment token = new Token.Comment(comment);

        // Mock an empty stack
        List<Node> stack = mock(List.class);
        when(stack.size()).thenReturn(0);
        xmlTreeBuilder.setStack(stack);

        xmlTreeBuilder.insert(token);

        // Assert
        // Verify that the expected comment node is inserted
        verify(stack).add(any(Comment.class));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String data = "test data";
        Token.Character token = new Token.Character(false, data);

        // Mock an empty stack
        List<Node> stack = mock(List.class);
        when(stack.size()).thenReturn(0);
        xmlTreeBuilder.setStack(stack);

        xmlTreeBuilder.insert(token);

        // Assert
        // Verify that the expected text node is inserted
        verify(stack).add(any(TextNode.class));
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String name = "html";
        String publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        String systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        Token.Doctype token = new Token.Doctype(name, publicIdentifier, systemIdentifier);

        // Mock an empty stack
        List<Node> stack = mock(List.class);
        when(stack.size()).thenReturn(0);
        xmlTreeBuilder.setStack(stack);

        xmlTreeBuilder.insert(token);

        // Assert
        // Verify that the expected doctype node is inserted
        verify(stack).add(any(DocumentType.class));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div>test fragment</div>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        // Mock an empty stack
        List<Node> stack = mock(List.class);
        when(stack.size()).thenReturn(0);
        xmlTreeBuilder.setStack(stack);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        // Assert
        // Verify that the expected nodes are returned
        assertEquals(1, nodes.size());
        assertEquals("div", nodes.get(0).nodeName());
    }

}