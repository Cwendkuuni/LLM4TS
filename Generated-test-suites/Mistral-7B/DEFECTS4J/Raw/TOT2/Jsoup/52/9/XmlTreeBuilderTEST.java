import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parseFragment("<?xml version='1.0'?>\n<root>\n<element attribute='value'>Content</element>\n</root>", "", new ParseErrorList());
 }

 // Test for the initialiseParse method
 @Test
 public void testInitialiseParse() {
 // Basic test
 assertEquals("root", document.tagName());

 // Test for empty input
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parseFragment("", "", new ParseErrorList());
 assertNull(document);

 // Test for baseUri and ParseErrorList arguments
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parseFragment("<?xml version='1.0'?>\n<root>\n<element attribute='value'>Content</element>\n", "baseUrl", new ParseErrorList());
 assertEquals("baseUrl", document.baseUri());
 }

 // Test for process method
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token(Token.StartTag.class, "<element attribute='value'>");
 xmlTreeBuilder.process(startTag);
 assertEquals(1, document.childNodes().size());
 Element element = (Element) document.childNodes().get(0);
 assertEquals("element", element.tagName());
 assertEquals("value", element.tagName(true).attribute("attribute").get());
 }

 @Test
 public void testProcessEndTag() {
 // Basic test
 Token endTag = new Token(Token.EndTag.class, "</element>");
 xmlTreeBuilder.process(endTag);
 assertEquals(0, document.childNodes().size());

 // Test for non-existent tag
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parseFragment("<?xml version='1.0'?>\n<root>\n<root>\n</root>", "", new ParseErrorList());
 xmlTreeBuilder.process(new Token(Token.EndTag.class, "</non-existent>"));
 assertEquals(1, document.childNodes().size());
 }

 // Test for processComment method
 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.Comment.class, "-- Comment --");
 xmlTreeBuilder.process(commentToken);
 assertEquals(1, document.childNodes().size());
 Comment comment = (Comment) document.childNodes().get(0);
 assertEquals("Comment", comment.nodeName());
 assertEquals("-- Comment --", comment.getData());
 }

 // Test for processCharacter method
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.Character.class, 'A');
 xmlTreeBuilder.process(characterToken);
 assertEquals(1, document.childNodes().size());
 TextNode textNode = (TextNode) document.childNodes().get(0);
 assertEquals("A", textNode.text());
 }

 // Test for processDoctype method
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE root [ ]>");
 xmlTreeBuilder.process(doctypeToken);
 assertEquals(1, document.childNodes().size());
 DocumentType documentTypeNode = (DocumentType) document.childNodes().get(0);
 assertEquals("root", documentTypeNode.getName());
 assertNull(documentTypeNode.getPublicIdentifier());
 assertNull(documentTypeNode.getSystemIdentifier());
 }

 // Test for popStackToClose method
 @Test
 public void testPopStackToClose() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = xmlTreeBuilder.parseFragment("<root><element><element></element></element></root>", "", new ParseErrorList());
 xmlTreeBuilder.process(new Token(Token.EndTag.class, "</element>"));
 xmlTreeBuilder.process(new Token(Token.EndTag.class, "</element>"));
 xmlTreeBuilder.process(new Token(Token.EndTag.class, "</root>"));
 assertEquals(0, document.childNodes().size());
 }
}