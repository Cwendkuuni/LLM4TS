import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.Reader;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errorList;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errorList = new ParseErrorList();
    }
    
    @Test
    public void testDefaultSettings_ReturnsPreserveCase() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertTrue(settings.preserveCase());
    }
    
    @Test
    public void testParse_ReaderAndBaseUri_ReturnsDocument() {
        Reader input = mock(Reader.class);
        String baseUri = "http://example.com";
        
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        verify(xmlTreeBuilder).initialiseParse(input, baseUri, errorList, ParseSettings.preserveCase);
    }
    
    @Test
    public void testParse_StringAndBaseUri_ReturnsDocument() {
        String input = "<xml>Here is an example XML string.</xml>";
        String baseUri = "http://example.com";
                
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertNotNull(document);
        verify(xmlTreeBuilder).initialiseParse(any(Reader.class), eq(baseUri), eq(errorList), eq(ParseSettings.preserveCase));
    }
    
    @Test
    public void testInitialiseParse_ReaderAndBaseUri_CorrectlyInitializesDocument() {
        Reader input = mock(Reader.class);
        String baseUri = "http://example.com";
        
        Document document = mock(Document.class);
        doNothing().when(xmlTreeBuilder).initialiseParse(input, baseUri, errorList, ParseSettings.preserveCase);
        when(xmlTreeBuilder.currentElement()).thenReturn(document);
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errorList, ParseSettings.preserveCase);
        
        verify(xmlTreeBuilder).super.initialiseParse(input, baseUri, errorList, ParseSettings.preserveCase);
        verify(xmlTreeBuilder.stack).add(document);
        verify(document).outputSettings().syntax(Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcess_StartTag_CallsInsertMethod() {
        Token token = mock(Token.class);
        when(token.type).thenReturn(Token.TokenType.StartTag);
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        verify(xmlTreeBuilder).insert(Mockito.any(Token.StartTag.class));
    }
    
    @Test
    public void testProcess_EndTag_CallsPopStackToCloseMethod() {
        Token token = mock(Token.class);
        when(token.type).thenReturn(Token.TokenType.EndTag);
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        verify(xmlTreeBuilder).popStackToClose(Mockito.any(Token.EndTag.class));
    }
    
    @Test
    public void testProcess_Comment_CallsInsertMethod() {
        Token token = mock(Token.class);
        when(token.type).thenReturn(Token.TokenType.Comment);
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        verify(xmlTreeBuilder).insert(Mockito.any(Token.Comment.class));
    }
    
    @Test
    public void testProcess_Character_CallsInsertMethod() {
        Token token = mock(Token.class);
        when(token.type).thenReturn(Token.TokenType.Character);
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        verify(xmlTreeBuilder).insert(Mockito.any(Token.Character.class));
    }
    
    @Test
    public void testProcess_Doctype_CallsInsertMethod() {
        Token token = mock(Token.class);
        when(token.type).thenReturn(Token.TokenType.Doctype);
        
        boolean result = xmlTreeBuilder.process(token);
        
        assertTrue(result);
        verify(xmlTreeBuilder).insert(Mockito.any(Token.Doctype.class));
    }
    
    @Test
    public void testInsertNode_Node_CorrectlyAppendsChildNode() {
        Node node = mock(Node.class);
        Element element = mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        
        xmlTreeBuilder.insertNode(node);
        
        verify(element).appendChild(node);
    }
    
    @Test
    public void testInsert_StartTag_ReturnsElement() {
        Token.StartTag startTag = mock(Token.StartTag.class);
        Tag tag = mock(Tag.class);
        when(startTag.name()).thenReturn("tag");
        when(startTag.isSelfClosing()).thenReturn(true);
        when(tag.isKnownTag()).thenReturn(false);
        when(tag.valueOf("tag", ParseSettings.preserveCase)).thenReturn(tag);
        when(xmlTreeBuilder.settings.normalizeAttributes(startTag.attributes)).thenReturn(new Attributes());
        when(xmlTreeBuilder.currentElement()).thenReturn(mock(Element.class));
        when(xmlTreeBuilder.baseUri).thenReturn("http://example.com");
        doNothing().when(xmlTreeBuilder).insertNode(Mockito.any(Element.class));
        
        Element el = xmlTreeBuilder.insert(startTag);
        
        assertNotNull(el);
        verify(xmlTreeBuilder).insertNode(Mockito.any(Element.class));
        verify(xmlTreeBuilder.stack).add(Mockito.any(Element.class));
        assertTrue(tag.isSelfClosing());
        assertTrue(tag.isKnownTag());
    }
    
    // Test cases for other methods...
    
}