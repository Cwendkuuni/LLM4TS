import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testParse() {
        String input = "<xml><tag1>Value1</tag1><tag2>Value2</tag2></xml>";
        String baseUri = "https://www.example.com";
        Document parsedDocument = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(parsedDocument);
        assertEquals("<xml>\n <tag1>Value1</tag1>\n <tag2>Value2</tag2>\n</xml>", parsedDocument.toString());
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag1>Value1</tag1><tag2>Value2</tag2></xml>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(input, xmlTreeBuilder.getTokeniser().reader().toString());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(settings, xmlTreeBuilder.getSettings());
        assertEquals(0, xmlTreeBuilder.getStack().size());
        assertEquals(Tag.valueOf("xml", xmlTreeBuilder.getSettings()), xmlTreeBuilder.getDoc().tag());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag1");
        Tag tag = Tag.valueOf("tag1", xmlTreeBuilder.defaultSettings());
        startTag.name(tag.getName());

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        assertNotNull(insertedElement);
        assertEquals(tag.getName(), insertedElement.nodeName());
        assertEquals(xmlTreeBuilder.getBaseUri(), insertedElement.baseUri());
        assertEquals(tag.getName(), xmlTreeBuilder.currentElement().nodeName());
        assertEquals(1, xmlTreeBuilder.getStack().size());

        // Test self-closing tag
        startTag.selfClosing(true);
        insertedElement = xmlTreeBuilder.insert(startTag);

        assertTrue(tag.isKnownTag());
        assertTrue(insertedElement.tag().isKnownTag());
        assertTrue(insertedElement.tag().isSelfClosing());
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("tag2");
        endTag.name("tag2");

        Element parentElement = new Element(Tag.valueOf("parent", xmlTreeBuilder.defaultSettings()), xmlTreeBuilder.getBaseUri());
        Element endTagElement = new Element(Tag.valueOf("tag2", xmlTreeBuilder.defaultSettings()), xmlTreeBuilder.getBaseUri());
        parentElement.appendChild(endTagElement);
        xmlTreeBuilder.getStack().add(parentElement);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcess_Comment() {
        String commentData = "This is a comment";
        Token.Comment commentToken = new Token.Comment(commentData);
        commentToken.data(commentData);

        xmlTreeBuilder.insert(commentToken);

        assertEquals(commentData, xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcess_Character() {
        String characterData = "This is a character";
        Token.Character characterToken = new Token.Character(characterData);
        characterToken.data(characterData);

        xmlTreeBuilder.insert(characterToken);

        assertEquals(characterData, xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testProcess_Doctype() {
        String doctypeName = "html";
        Token.Doctype doctypeToken = new Token.Doctype(doctypeName);
        doctypeToken.name(doctypeName);

        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(doctypeName, xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsertNode() {
        Element parentElement = new Element(Tag.valueOf("parent", xmlTreeBuilder.defaultSettings()), xmlTreeBuilder.getBaseUri());
        Element childElement = new Element(Tag.valueOf("child", xmlTreeBuilder.defaultSettings()), xmlTreeBuilder.getBaseUri());

        xmlTreeBuilder.getStack().add(parentElement);
        xmlTreeBuilder.insertNode(childElement);

        assertTrue(parentElement.childNodes().contains(childElement));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag>Value</tag>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> parsedFragment = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(parsedFragment);
        assertEquals(1, parsedFragment.size());
        assertTrue(parsedFragment.get(0) instanceof Element);
        assertEquals("tag", parsedFragment.get(0).nodeName());
    }

}