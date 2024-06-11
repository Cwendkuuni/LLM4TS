import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    private XmlTreeBuilder builder;
    
    @Before
    public void setup() {
        builder = new XmlTreeBuilder();
    }
    
    @Test
    public void testDefaultSettings() {
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
    }
    
    @Test
    public void testParse() {
        Reader reader = new StringReader("<xml><element>value</element></xml>");
        String baseUri = "http://example.com";
        
        Document document = builder.parse(reader, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testParseString() {
        String input = "<xml><element>value</element></xml>";
        String baseUri = "http://example.com";
        
        Document document = builder.parse(input, baseUri);
        assertNotNull(document);
    }
    
    @Test
    public void testInitialiseParse() {
        Reader reader = new StringReader("<xml><element>value</element></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        builder.initialiseParse(reader, baseUri, errors, settings);
        
        // No assertion as this method does not return anything
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("element");
        
        Element element = builder.insert(startTag);
        assertNotNull(element);
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("element");
        
        builder.popStackToClose(endTag);
        
        // No assertion as this method does not return anything
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        builder.insert(commentToken);
        
        // No assertion as this method does not return anything
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        
        builder.insert(characterToken);
        
        // No assertion as this method does not return anything
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html");
        
        builder.insert(doctype);
        
        // No assertion as this method does not return anything
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<element>value</element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);
        assertNotNull(nodes);
    }
}