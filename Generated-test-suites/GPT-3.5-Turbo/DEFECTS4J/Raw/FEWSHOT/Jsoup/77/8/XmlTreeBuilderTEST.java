import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.Reader;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class XmlTreeBuilderTest {

    @Mock
    private Reader reader;

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String baseUri = "http://example.com";
        Document document = treeBuilder.parse(reader, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testParseWithString() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<xml><body></body></xml>";
        String baseUri = "http://example.com";
        Document document = treeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(false, false);

        treeBuilder.initialiseParse(reader, "http://example.com", errors, settings);

        assertTrue(treeBuilder.stack.contains(treeBuilder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessTokenStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Tag tag = new Tag("body");
        StartTag startTag = new StartTag(tag);
        Node expectedNode = new Element(tag, "http://example.com");

        boolean result = treeBuilder.process(startTag);

        assertTrue(result);
        assertTrue(treeBuilder.stack.contains(expectedNode));
    }

    @Test
    public void testProcessTokenEndTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Tag tag = new Tag("body");
        EndTag endTag = new EndTag(tag);

        treeBuilder.stack.push(new Element(tag, "http://example.com"));
        boolean result = treeBuilder.process(endTag);

        assertTrue(result);
        assertFalse(treeBuilder.stack.contains(tag));
    }

    @Test
    public void testProcessTokenComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Comment comment = new Comment("test comment");
        Node expectedNode = new Comment("test comment");

        boolean result = treeBuilder.process(comment);

        assertTrue(result);
        assertTrue(treeBuilder.stack.getLast().childNodes().contains(expectedNode));
    }

    @Test
    public void testProcessTokenCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Character character = new Character("test character");
        Node expectedNode = new TextNode("test character");

        boolean result = treeBuilder.process(character);

        assertTrue(result);
        assertTrue(treeBuilder.stack.getLast().childNodes().contains(expectedNode));
    }

    @Test
    public void testProcessTokenDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Doctype doctype = new Doctype("test doctype");
        Node expectedNode = new DocumentType("test doctype", "", "");

        boolean result = treeBuilder.process(doctype);

        assertTrue(result);
        assertTrue(treeBuilder.stack.getLast().childNodes().contains(expectedNode));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = Mockito.spy(new XmlTreeBuilder());
        Node node = new Element(Tag.valueOf("body"), "http://example.com");

        doNothing().when(treeBuilder).insertNode(node);

        treeBuilder.insertNode(node);

        verify(treeBuilder, times(1)).insertNode(node);
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Tag tag = Tag.valueOf("div");
        StartTag startTag = new StartTag(tag);
        Element expectedElement = new Element(tag, "http://example.com");

        Element result = treeBuilder.insert(startTag);

        assertTrue(result.isEqualTo(expectedElement));
        assertTrue(result.isSelfClosing());
        assertTrue(treeBuilder.stack.contains(expectedElement));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Comment comment = new Comment("test comment");
        Node expectedNode = new Comment("test comment");

        treeBuilder.insert(comment);

        assertTrue(treeBuilder.stack.getLast().childNodes().contains(expectedNode));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Character character = new Character("test character");
        Node expectedNode = new TextNode("test character");

        treeBuilder.insert(character);

        assertTrue(treeBuilder.stack.getLast().childNodes().contains(expectedNode));
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Doctype doctype = new Doctype("test doctype");
        Node expectedNode = new DocumentType("test doctype", "", "");

        treeBuilder.insert(doctype);

        assertTrue(treeBuilder.stack.getLast().childNodes().contains(expectedNode));
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Tag tag = Tag.valueOf("body");
        EndTag endTag = new EndTag(tag);

        Element element1 = new Element(tag, "http://example.com");
        Element element2 = new Element(tag, "http://example.com");
        Element element3 = new Element(tag, "http://example.com");
        Element element4 = new Element(Tag.valueOf("div"), "http://example.com");

        treeBuilder.stack.push(element4);
        treeBuilder.stack.push(element1);
        treeBuilder.stack.push(element2);
        treeBuilder.stack.push(element3);

        treeBuilder.popStackToClose(endTag);

        assertFalse(treeBuilder.stack.contains(element1));
        assertFalse(treeBuilder.stack.contains(element2));
        assertFalse(treeBuilder.stack.contains(element3));
        assertTrue(treeBuilder.stack.contains(element4));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div>Test fragment</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings(false, false);

        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(nodes);
        assertFalse(nodes.isEmpty());
        assertEquals(1, nodes.size());
    }
}