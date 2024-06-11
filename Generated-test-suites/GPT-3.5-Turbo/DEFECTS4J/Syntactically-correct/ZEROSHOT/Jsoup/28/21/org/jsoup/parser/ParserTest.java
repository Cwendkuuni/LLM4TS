import org.junit.Test;
import org.mockito.Mock;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.TreeBuilder;
import java.util.List;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ParserTest {
    
    @Mock
    private TreeBuilder mockedTreeBuilder;
    
    @Mock
    private Document mockedDocument;
    
    @Mock
    private Element mockedElement;
    
    @Mock
    private ParseErrorList mockedParseErrorList;
    
    @Mock
    private List<Node> mockedNodeList;
    
    @Mock
    private ParseError mockedParseError;
   
    @Test
    public void testParseInput() {
        String html = "<html><body><h1>Test HTML</h1></body></html>";
        String baseUri = "https://example.com";
        
        when(mockedTreeBuilder.parse(html, baseUri, mockedParseErrorList)).thenReturn(mockedDocument);
        
        Parser parser = new Parser(mockedTreeBuilder);
        parser.setTrackErrors(5);
        
        Document result = parser.parseInput(html, baseUri);
        
        assertEquals(mockedDocument, result);
        assertTrue(parser.isTrackErrors());
        assertEquals(mockedParseErrorList, parser.getErrors());
        
        verify(mockedTreeBuilder).parse(html, baseUri, mockedParseErrorList);
        verifyNoMoreInteractions(mockedTreeBuilder);
    }
    
    @Test
    public void testGetAndSetTreeBuilder() {
        TreeBuilder newTreeBuilder = new HtmlTreeBuilder();
        
        Parser parser = new Parser(mockedTreeBuilder);
        parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(newTreeBuilder, parser.getTreeBuilder());
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser1 = new Parser(mockedTreeBuilder);
        assertFalse(parser1.isTrackErrors());
        
        Parser parser2 = new Parser(mockedTreeBuilder);
        parser2.setTrackErrors(3);
        assertTrue(parser2.isTrackErrors());
    }
    
    @Test
    public void testGetAndSetTrackErrors() {
        Parser parser = new Parser(mockedTreeBuilder);
        parser.setTrackErrors(10);
        
        assertEquals(10, parser.maxErrors);
    }
    
    @Test
    public void testGetErrors() {
        Parser parser = new Parser(mockedTreeBuilder);
        parser.setTrackErrors(5);
        parser.errors = mockedParseErrorList;
        
        assertEquals(mockedParseErrorList, parser.getErrors());
    }
    
    @Test
    public void testParse() {
        String html = "<html><body><h1>Test HTML</h1></body></html>";
        String baseUri = "https://example.com";
        
        when(mockedTreeBuilder.parse(html, baseUri, ParseErrorList.noTracking())).thenReturn(mockedDocument);
        
        Document result = Parser.parse(html, baseUri);
        
        assertEquals(mockedDocument, result);
        
        verify(mockedTreeBuilder).parse(html, baseUri, ParseErrorList.noTracking());
        verifyNoMoreInteractions(mockedTreeBuilder);
    }
    
    @Test
    public void testParseFragment() {
        String fragmentHtml = "<h1>Test Fragment HTML</h1>";
        
        when(mockedTreeBuilder.parseFragment(fragmentHtml, mockedElement, "https://example.com", ParseErrorList.noTracking())).thenReturn(mockedNodeList);
        
        List<Node> result = Parser.parseFragment(fragmentHtml, mockedElement, "https://example.com");
        
        assertEquals(mockedNodeList, result);
        
        verify(mockedTreeBuilder).parseFragment(fragmentHtml, mockedElement, "https://example.com", ParseErrorList.noTracking());
        verifyNoMoreInteractions(mockedTreeBuilder);
    }
    
    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Test Body Fragment HTML</h1>";
        String baseUri = "https://example.com";
        
        when(mockedDocument.body()).thenReturn(mockedElement);
        when(mockedTreeBuilder.parseFragment(bodyHtml, mockedElement, baseUri, ParseErrorList.noTracking())).thenReturn(mockedNodeList);
        when(mockedNodeList.toArray(new Node[mockedNodeList.size()])).thenReturn(new Node[]{mockedElement});
        
        Document result = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals(mockedDocument, result);
        
        verify(mockedDocument).body();
        verify(mockedTreeBuilder).parseFragment(bodyHtml, mockedElement, baseUri, ParseErrorList.noTracking());
        verify(mockedElement).appendChild(mockedElement);
        verifyNoMoreInteractions(mockedDocument, mockedTreeBuilder);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<h1>Test Body Fragment HTML</h1>";
        String baseUri = "https://example.com";
        
        when(mockedTreeBuilder.parse(bodyHtml, baseUri, ParseErrorList.noTracking())).thenReturn(mockedDocument);
        
        Document result = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals(mockedDocument, result);
        
        verify(mockedTreeBuilder).parse(bodyHtml, baseUri, ParseErrorList.noTracking());
        verifyNoMoreInteractions(mockedTreeBuilder);
    }
    
    @Test
    public void testHtmlParser() {
        Parser parser = Parser.htmlParser();
        
        assertNotNull(parser);
        assertEquals(HtmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
    @Test
    public void testXmlParser() {
        Parser parser = Parser.xmlParser();
        
        assertNotNull(parser);
        assertEquals(XmlTreeBuilder.class, parser.getTreeBuilder().getClass());
    }
    
}