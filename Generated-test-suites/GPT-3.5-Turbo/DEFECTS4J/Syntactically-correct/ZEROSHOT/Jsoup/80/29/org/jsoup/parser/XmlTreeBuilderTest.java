import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Mock
    private Reader input;
    
    @Mock
    private String baseUri;
    
    @Mock
    private ParseErrorList errors;
    
    @Mock
    private ParseSettings settings;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();
        assertNotNull(defaultSettings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        String inputString = "test";
        Document document = xmlTreeBuilder.parse(inputString, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
    }
    
    @Test
    public void testProcessWithStartTag() {
        Token token = new Token.StartTag("test");
        boolean isSuccess = xmlTreeBuilder.process(token);
        assertTrue(isSuccess);
    }
    
    @Test
    public void testProcessWithEndTag() {
        Token token = new Token.EndTag("test");
        boolean isSuccess = xmlTreeBuilder.process(token);
        assertTrue(isSuccess);
    }
    
    @Test
    public void testProcessWithComment() {
        Token token = new Token.Comment("test");
        boolean isSuccess = xmlTreeBuilder.process(token);
        assertTrue(isSuccess);
    }
    
    @Test
    public void testProcessWithCharacter() {
        Token token = new Token.Character("test");
        boolean isSuccess = xmlTreeBuilder.process(token);
        assertTrue(isSuccess);
    }
    
    @Test
    public void testProcessWithDoctype() {
        Token token = new Token.Doctype("test", "public", "system");
        boolean isSuccess = xmlTreeBuilder.process(token);
        assertTrue(isSuccess);
    }
    
    @Test
    public void testInsertNode() {
        Node node = new Node();
        xmlTreeBuilder.insertNode(node);
    }
    
    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("test");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
    }
    
    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("test");
        xmlTreeBuilder.insert(commentToken);
    }
    
    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("test");
        xmlTreeBuilder.insert(characterToken);
    }
    
    @Test
    public void testInsertWithDoctype() {
        Token.Doctype doctype = new Token.Doctype("test", "public", "system");
        xmlTreeBuilder.insert(doctype);
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("test");
        xmlTreeBuilder.popStackToClose(endTag);
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "test";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
    }
}