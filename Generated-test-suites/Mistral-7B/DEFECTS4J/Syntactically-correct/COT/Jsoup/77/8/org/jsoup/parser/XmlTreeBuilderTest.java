import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test defaultSettings method
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 // Test parse method with Reader input
 @Test
 public void testParseWithReaderInput() {
 String xmlInput = "<xml><tag>Test</tag></xml>";
 Reader reader = new StringReader(xmlInput);
 Document xmlDocument = xmlTreeBuilder.parse(reader, "baseUri");
 assertEquals("<xml><tag>Test</tag></xml>", xmlDocument.html());
 }

 // Test parse method with String input
 @Test
 public void testParseWithStringInput() {
 String xmlInput = "<xml><tag>Test</tag></xml>";
 Document xmlDocument = xmlTreeBuilder.parse(xmlInput, "baseUri");
 assertEquals("<xml><tag>Test</tag></xml>", xmlDocument.html());
 }

 // Test initialiseParse method
 @Test
 public void testInitialiseParse() {
 Document xmlDocument = new Document();
 xmlTreeBuilder.initialiseParse(new StringReader("<xml><tag>Test</tag></xml>"), "baseUri", null, null);
 assertEquals(xmlDocument, xmlTreeBuilder.doc);
 }

 // Test process method with StartTag
 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("tag", null, null, null, null, null, null, null, null, null);
 Element tagElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("tag", tagElement.nodeName());
 }

 // Test process method with EndTag
 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 // Test process method with Comment
 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals("This is a comment", xmlTreeBuilder.doc.child(0).text());
 }

 // Test process method with Character
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 assertEquals("A", xmlTreeBuilder.doc.child(0).text());
 }

 // Test process method with Doctype
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals("html", doctypeNode.nodeName());
 }

 // Test popStackToClose method with unknown tag
 @Test(expected = AssertionError.class)
 public void testPopStackToCloseWithUnknownTag() {
 xmlTreeBuilder.popStackToClose(new Token.EndTag("unknownTag"));
 }

 // Test parseFragment method
 @Test
 public void testParseFragment() {
 String xmlFragment = "<fragment><tag>Test</tag></fragment>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", null, null);
 assertEquals(1, nodes.size());
 assertEquals("tag", nodes.get(0).nodeName());
 }

}