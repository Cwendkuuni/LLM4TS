import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = Mockito.spy(new XmlTreeBuilder());
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void testParseWithReader() {
        Reader input = new StringReader("");
        String baseUri = "https://example.com";
        Document expectedDocument = mock(Document.class);

        when(xmlTreeBuilder.parse(eq(input), eq(baseUri), any(ParseErrorList.class), any(ParseSettings.class))).thenReturn(expectedDocument);

        Document actualDocument = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expectedDocument, actualDocument);

        verify(xmlTreeBuilder).parse(eq(input), eq(baseUri), any(ParseErrorList.class), any(ParseSettings.class));
    }

    @Test
    public void testParseWithStringInput() {
        String input = "<xml></xml>";
        String baseUri = "https://example.com";
        Document expectedDocument = mock(Document.class);

        when(xmlTreeBuilder.parse(eq(new StringReader(input)), eq(baseUri), any(ParseErrorList.class), any(ParseSettings.class))).thenReturn(expectedDocument);

        Document actualDocument = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expectedDocument, actualDocument);

        verify(xmlTreeBuilder).parse(eq(new StringReader(input)), eq(baseUri), any(ParseErrorList.class), any(ParseSettings.class));
    }

    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("");
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        verify(xmlTreeBuilder).initialiseParse(eq(input), eq(baseUri), eq(errors), eq(settings));

        // TODO: Assert other behavior and verify other method calls
    }

    @Test
    public void testProcess() {
        Token token = mock(Token.class);

        xmlTreeBuilder.process(token);

        verify(xmlTreeBuilder).process(eq(token));

        // TODO: Assert behavior and verify other method calls
    }

    @Test
    public void testInsertNode() {
        Node node = mock(Node.class);

        xmlTreeBuilder.insertNode(node);

        verify(xmlTreeBuilder).insertNode(eq(node));

        // TODO: Assert behavior and verify other method calls
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = mock(Token.StartTag.class);

        Element expectedElement = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(expectedElement);

        Element actualElement = xmlTreeBuilder.insert(startTag);
        assertEquals(expectedElement, actualElement);

        verify(xmlTreeBuilder).insert(eq(startTag));

        // TODO: Assert behavior and verify other method calls
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = mock(Token.Comment.class);

        xmlTreeBuilder.insert(commentToken);

        verify(xmlTreeBuilder).insert(eq(commentToken));

        // TODO: Assert behavior and verify other method calls
    }

    @Test
    public void testInsertCharacter() {
        Token.Character token = mock(Token.Character.class);

        xmlTreeBuilder.insert(token);

        verify(xmlTreeBuilder).insert(eq(token));

        // TODO: Assert behavior and verify other method calls
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype d = mock(Token.Doctype.class);

        xmlTreeBuilder.insert(d);

        verify(xmlTreeBuilder).insert(eq(d));

        // TODO: Assert behavior and verify other method calls
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = mock(Token.EndTag.class);

        xmlTreeBuilder.popStackToClose(endTag);

        verify(xmlTreeBuilder).popStackToClose(eq(endTag));

        // TODO: Assert behavior and verify other method calls
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<xml></xml>";
        String baseUri = "https://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> expectedNodes = mock(List.class);

        when(xmlTreeBuilder.parseFragment(eq(inputFragment), eq(baseUri), eq(errors), eq(settings))).thenReturn(expectedNodes);

        List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(expectedNodes, actualNodes);

        verify(xmlTreeBuilder).parseFragment(eq(inputFragment), eq(baseUri), eq(errors), eq(settings));
    }
}