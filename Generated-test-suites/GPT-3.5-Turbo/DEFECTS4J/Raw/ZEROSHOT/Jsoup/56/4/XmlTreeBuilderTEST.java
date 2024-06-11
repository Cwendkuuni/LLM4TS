import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder xmlTreeBuilder;
    private Document doc;
    private Element element;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.Doctype doctype;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        doc = Mockito.mock(Document.class);
        element = Mockito.mock(Element.class);
        startTag = Mockito.mock(Token.StartTag.class);
        endTag = Mockito.mock(Token.EndTag.class);
        commentToken = Mockito.mock(Token.Comment.class);
        characterToken = Mockito.mock(Token.Character.class);
        doctype = Mockito.mock(Token.Doctype.class);
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        // Perform assertion
    }
    
    @Test
    public void testParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = Mockito.mock(ParseErrorList.class);
        Document document = xmlTreeBuilder.parse(input, baseUri, errors, ParseSettings.preserveCase);
        // Perform assertion
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "input";
        String baseUri = "baseUri";
        ParseErrorList errors = Mockito.mock(ParseErrorList.class);
        ParseSettings settings = Mockito.mock(ParseSettings.class);
        
        // Set up the mocks
        when(doc.outputSettings()).thenReturn(Mockito.mock(Document.OutputSettings.class));
        
        // Call the method
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        
        // Perform assertions
        // Assert that the document is added to the stack
        verify(xmlTreeBuilder.stack).add(doc);
    }
    
    @Test
    public void testProcess_withStartTagToken() {
        // Set up the mocks
        when(startTag.type).thenReturn(Token.TokenType.StartTag);
        Element element = Mockito.mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        
        // Call the method
        boolean result = xmlTreeBuilder.process(startTag);
        
        // Perform assertions
        // Assert that the token is inserted
        verify(xmlTreeBuilder).insert(startTag.asStartTag());
        // Assert the result
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withEndTagToken() {
        // Set up the mocks
        when(endTag.type).thenReturn(Token.TokenType.EndTag);
        
        // Call the method
        boolean result = xmlTreeBuilder.process(endTag);
        
        // Perform assertions
        // Assert that the stack is popped to close the tag
        verify(xmlTreeBuilder).popStackToClose(endTag.asEndTag());
        // Assert the result
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withCommentToken() {
        // Set up the mocks
        when(commentToken.type).thenReturn(Token.TokenType.Comment);
        
        // Call the method
        boolean result = xmlTreeBuilder.process(commentToken);
        
        // Perform assertions
        // Assert that the comment is inserted
        verify(xmlTreeBuilder).insert(commentToken.asComment());
        // Assert the result
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withCharacterToken() {
        // Set up the mocks
        when(characterToken.type).thenReturn(Token.TokenType.Character);
        
        // Call the method
        boolean result = xmlTreeBuilder.process(characterToken);
        
        // Perform assertions
        // Assert that the character is inserted
        verify(xmlTreeBuilder).insert(characterToken.asCharacter());
        // Assert the result
        assertTrue(result);
    }
    
    @Test
    public void testProcess_withDoctypeToken() {
        // Set up the mocks
        when(doctype.type).thenReturn(Token.TokenType.Doctype);
        
        // Call the method
        boolean result = xmlTreeBuilder.process(doctype);
        
        // Perform assertions
        // Assert that the doctype is inserted
        verify(xmlTreeBuilder).insert(doctype.asDoctype());
        // Assert the result
        assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        // Set up the mocks
        Node node = Mockito.mock(Node.class);
        Element element = Mockito.mock(Element.class);
        when(xmlTreeBuilder.currentElement()).thenReturn(element);
        
        // Call the method
        xmlTreeBuilder.insertNode(node);
        
        // Perform assertions
        // Assert that the node is appended to the current element
        verify(element).appendChild(node);
    }
    
    @Test
    public void testInsert_withStartTagToken() {
        // Set up the mocks
        when(startTag.name()).thenReturn("tag");
        when(startTag.attributes).thenReturn(Mockito.mock(Token.Character.class));
        Tag tag = Mockito.mock(Tag.class);
        when(startTag.isSelfClosing()).thenReturn(true);
        when(tag.isKnownTag()).thenReturn(false);
        XmlTreeBuilder.Tokeniser tokeniser = Mockito.mock(XmlTreeBuilder.Tokeniser.class);
        when(xmlTreeBuilder.tokeniser).thenReturn(tokeniser);
        
        // Call the method
        Element result = xmlTreeBuilder.insert(startTag);
        
        // Perform assertions
        // Assert that the element is created and inserted
        verify(xmlTreeBuilder).insertNode(element);
        // Assert the result
        assertEquals(element, result);
    }
    
    @Test
    public void testInsert_withCommentToken() {
        // Set up the mocks
        String data = "data";
        when(commentToken.getData()).thenReturn(data);
        Comment comment = Mockito.mock(Comment.class);
        when(commentToken.bogus).thenReturn(true);
        Document doc = Mockito.mock(Document.class);
        when(Jsoup.parse(anyString(), anyString(), any(Parser.class))).thenReturn(doc);
        Element el = Mockito.mock(Element.class);
        when(doc.child(0)).thenReturn(el);
        XmlDeclaration xmlDeclaration = Mockito.mock(XmlDeclaration.class);
        when(settings.normalizeTag(el.tagName())).thenReturn("<" + data.substring(1, data.length() -1) + ">");
        when(xmlDeclaration.attributes()).thenReturn(Mockito.mock(Attributes.class));
        
        // Call the method
        xmlTreeBuilder.insert(commentToken);
        
        // Perform assertions
        // Assert that the comment is inserted
        verify(xmlTreeBuilder).insertNode(comment);
    }
    
    @Test
    public void testInsert_withCharacterToken() {
        // Set up the mocks
        String data = "data";
        when(characterToken.getData()).thenReturn(data);
        TextNode textNode = Mockito.mock(TextNode.class);
        
        // Call the method
        xmlTreeBuilder.insert(characterToken);
        
        // Perform assertions
        // Assert that the character is inserted
        verify(xmlTreeBuilder).insertNode(node);
    }
    
    @Test
    public void testInsert_withDoctypeToken() {
        // Set up the mocks
        String name = "name";
        String publicIdentifier = "publicIdentifier";
        String systemIdentifier = "systemIdentifier";
        when(doctype.getName()).thenReturn(name);
        when(doctype.getPublicIdentifier()).thenReturn(publicIdentifier);
        when(doctype.getSystemIdentifier()).thenReturn(systemIdentifier);
        DocumentType documentType = Mockito.mock(DocumentType.class);
        
        // Call the method
        xmlTreeBuilder.insert(doctype);
        
        // Perform assertions
        // Assert that the doctype is inserted
        verify(xmlTreeBuilder).insertNode(doctypeNode);
    }
    
    @Test
    public void testPopStackToClose() {
        // Set up the mocks
        String elName = "elName";
        when(endTag.name()).thenReturn(elName);
        Element next = Mockito.mock(Element.class);
        when(next.nodeName()).thenReturn(elName);
        when(stack.get(anyInt())).thenReturn(next);
        
        // Call the method
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Perform assertions
        // Assert that the stack is popped
        verify(stack, times(1)).remove(anyInt());
    }
    
    @Test
    public void testParseFragment() {
        // Set up the mocks
        String inputFragment = "inputFragment";
        String baseUri = "baseUri";
        ParseErrorList errors = Mockito.mock(ParseErrorList.class);
        ParseSettings settings = Mockito.mock(ParseSettings.class);
        
        // Call the method
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        
        // Perform assertions
        // Assert that the document is parsed and the child nodes are returned
        verify(xmlTreeBuilder, times(1)).initialiseParse(inputFragment, baseUri, errors, settings);
        verify(xmlTreeBuilder, times(1)).runParser();
        // Assert the result
        assertEquals(doc.childNodes(), result);
    }
}