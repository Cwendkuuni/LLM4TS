import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {
    
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        
        // Test if default settings are set correctly
        // ...
    }
    
    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>test</tag></xml>";
        String baseUri = "http://example.com";
        
        // Test parsing xml input
        // ...
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>test</tag></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings();
        
        // Test initialization of parse
        // ...
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token();
        
        // Test processing different token types
        // ...
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Node();
        
        // Test inserting a node into the current element
        // ...
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        
        // Test inserting a start tag and handling self-closing tags
        // ...
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();
        
        // Test inserting a comment node
        // ...
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();
        
        // Test inserting a character node
        // ...
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        
        // Test inserting a doctype node
        // ...
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        
        // Test popping stack to close an element
        // ...
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>test</tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = new ParseSettings();
        
        // Test parsing a fragment of xml input
        // ...
    }

}