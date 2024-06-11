import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private String input;
    private String baseUri;
    private ParseErrorList errors;
    private ParseSettings settings;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        input = "";
        baseUri = "";
        errors = ParseErrorList.noTracking();
        settings = ParseSettings.preserveCase;
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }
    
    @Test
    public void testParseReaderString() {
        Document document = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testParseStringString() {
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader inputReader = new StringReader(input);
        xmlTreeBuilder.initialiseParse(inputReader, baseUri, errors, settings);
        assertEquals(inputReader, xmlTreeBuilder.getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());
        assertEquals(settings, xmlTreeBuilder.getSettings());
    }
    
    @Test
    public void testProcess() {
        Token token = createStartTagToken();
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Element("test");
        xmlTreeBuilder.insertNode(node);
        Element currentElement = xmlTreeBuilder.getCurrentElement();
        assertNotNull(currentElement);
        assertEquals(node, currentElement.childNode(0));
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = createStartTagToken();
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals(startTag.name(), element.tag().getName());
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = createCommentToken();
        xmlTreeBuilder.insert(commentToken);
        Element currentElement = xmlTreeBuilder.getCurrentElement();
        assertNotNull(currentElement);
        assertEquals(commentToken.getData(), currentElement.childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = createCharacterToken();
        xmlTreeBuilder.insert(characterToken);
        Element currentElement = xmlTreeBuilder.getCurrentElement();
        assertNotNull(currentElement);
        assertEquals(characterToken.getData(), currentElement.childNode(0).outerHtml());
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = createDoctypeToken();
        xmlTreeBuilder.insert(doctypeToken);
        Element currentElement = xmlTreeBuilder.getCurrentElement();
        assertNotNull(currentElement);
        assertEquals(doctypeToken.getName(), currentElement.childNode(0).outerHtml());
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTagToken = createEndTagToken();
        xmlTreeBuilder.popStackToClose(endTagToken);
        Element currentElement = xmlTreeBuilder.getCurrentElement();
        assertNull(currentElement);
    }
    
    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, errors, settings);
        assertNotNull(nodes);
    }
    
    private Token.StartTag createStartTagToken() {
        Token.StartTag startTag = new Token.StartTag("");
        startTag.name("test");
        startTag.attributes(new Attributes());
        startTag.selfClosing(false);
        return startTag;
    }
    
    private Token.Comment createCommentToken() {
        Token.Comment commentToken = new Token.Comment("");
        commentToken.data("test comment");
        commentToken.bogus(false);
        return commentToken;
    }
    
    private Token.Character createCharacterToken() {
        Token.Character characterToken = new Token.Character("");
        characterToken.data("test character");
        characterToken.isCData(false);
        return characterToken;
    }
    
    private Token.Doctype createDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("");
        doctypeToken.name("test doctype");
        doctypeToken.publicIdentifier("");
        doctypeToken.systemIdentifier("");
        doctypeToken.pubSysKey("");
        return doctypeToken;
    }
    
    private Token.EndTag createEndTagToken() {
        Token.EndTag endTagToken = new Token.EndTag("");
        endTagToken.name("test");
        return endTagToken;
    }
}