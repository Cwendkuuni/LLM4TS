
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
  
    @Test
    public void testDefaultSettings() {
        ParseSettings expected = ParseSettings.preserveCase;
        ParseSettings actual = xmlTreeBuilder.defaultSettings();
        assertEquals(expected, actual);
    }
  
    @Test
    public void testParseReaderString() {
        Reader input = new StringReader("xml input");
        String baseUri = "http://example.com";
        Document expected = new Document("");
        Document actual = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expected, actual);
    }
  
    @Test
    public void testParseStringString() {
        String input = "xml input";
        String baseUri = "http://example.com";
        Document expected = new Document("");
        Document actual = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(expected, actual);
    }
  
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("xml input");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        // Check if the document is placed onto the stack
        assertEquals(doc, xmlTreeBuilder.stack.get(0));
        // Check if the syntax is set to XML
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
  
    @Test
    public void testProcess() {
        // Test StartTag
        Token token = new Token.StartTag("");
        assertTrue(xmlTreeBuilder.process(token));
      
        // Test EndTag
        token = new Token.EndTag("");
        assertTrue(xmlTreeBuilder.process(token));
      
        // Test Comment
        token = new Token.Comment("");
        assertTrue(xmlTreeBuilder.process(token));
      
        // Test Character
        token = new Token.Character("");
        assertTrue(xmlTreeBuilder.process(token));
      
        // Test Doctype
        token = new Token.Doctype("");
        assertTrue(xmlTreeBuilder.process(token));
      
        // Test EOF
        token = new Token.EOF();
        assertTrue(xmlTreeBuilder.process(token));
    }
  
    @Test
    public void testInsertNode() {
        Node node = new Node("");
        xmlTreeBuilder.insertNode(node);
        assertEquals(node, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
  
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("");
        Element expected = new Element(Tag.valueOf(startTag.name(), settings), baseUri, settings.normalizeAttributes(startTag.attributes));
        Element actual = xmlTreeBuilder.insert(startTag);
        assertEquals(expected, actual);
    }
  
    @Test
    public void testInsertCommentToken() {
        Token.Comment commentToken = new Token.Comment("");
        Comment expected = new Comment(commentToken.getData());
        Node insert = expected;
        if (commentToken.bogus) {
            Document doc = Jsoup.parse("<" + commentToken.getData().substring(1, commentToken.getData().length() -1) + ">", baseUri, Parser.xmlParser());
            Element el = doc.child(0);
            insert = new XmlDeclaration(settings.normalizeTag(el.tagName()), commentToken.getData().startsWith("!"));
            insert.attributes().addAll(el.attributes());
        }
        xmlTreeBuilder.insert(commentToken);
        assertEquals(insert, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
  
    @Test
    public void testInsertCharacterToken() {
        Token.Character token = new Token.Character("");
        String data = token.getData();
        Node expected = (token.isCData() ? new CDataNode(data) : new TextNode(data));
        xmlTreeBuilder.insert(token);
        assertEquals(expected, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
  
    @Test
    public void testInsertDoctype() {
        Token.Doctype d = new Token.Doctype("");
        DocumentType expected = new DocumentType(settings.normalizeTag(d.getName()), d.getPublicIdentifier(), d.getSystemIdentifier());
        expected.setPubSysKey(d.getPubSysKey());
        xmlTreeBuilder.insert(d);
        assertEquals(expected, xmlTreeBuilder.currentElement().childNodes().get(0));
    }
  
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("");
        String elName = endTag.name();
        Element firstFound = null;

        for (int pos = xmlTreeBuilder.stack.size() -1; pos >= 0; pos--) {
            Element next = xmlTreeBuilder.stack.get(pos);
            if (next.nodeName().equals(elName)) {
                firstFound = next;
                break;
            }
        }
        if (firstFound == null)
            return; // not found, skip

        for (int pos = xmlTreeBuilder.stack.size() -1; pos >= 0; pos--) {
            Element next = xmlTreeBuilder.stack.get(pos);
            stack.remove(pos);
            if (next == firstFound)
                break;
        }
    }
  
    @Test
    public void testParseFragment() {
        String inputFragment = "xml fragment";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> expected = new List<>();
        List<Node> actual = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(expected, actual);
    }
}
