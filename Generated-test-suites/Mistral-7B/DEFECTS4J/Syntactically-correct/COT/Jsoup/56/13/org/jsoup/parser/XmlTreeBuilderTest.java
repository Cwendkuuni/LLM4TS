import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("<xml/>", "", Parser.xmlParser());
 }

 // Test defaultSettings()
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 // Test parse()
 @Test
 public void testParse() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 doc = xmlTreeBuilder.parse(xmlString, "");
 assertTrue(doc.tagName().equals("xml"));
 }

 // Test initialiseParse()
 @Test
 public void testInitialiseParse() {
 String xmlString = "<xml><tag attr='value'></tag></xml>";
 xmlTreeBuilder.initialiseParse(xmlString, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(xmlTreeBuilder.doc, doc);
 }

 // Test process() - StartTag
 @Test
 public void testProcessStartTag() {
 Token startTag = new Token.StartTag("tag", null, null, null, null, null, null, null, null, 1, 1, 1, 1, 1);
 xmlTreeBuilder.process(startTag);
 Element tagElement = (Element) xmlTreeBuilder.stack.peek();
 assertEquals("tag", tagElement.nodeName());
 }

 // Test process() - EndTag
 @Test
 public void testProcessEndTag() {
 Token endTag = new Token.EndTag("tag", null, null, null, null, null, null, null, null, 1, 1, 1, 1, 1);
 xmlTreeBuilder.process(endTag);
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test process() - Comment
 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("-- This is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(xmlTreeBuilder.doc.childNodes().size(), 1);
 Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("-- This is a comment", comment.text());
 }

 // Test process() - Character
 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("A", textNode.text());
 }

 // Test process() - Doctype
 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("html", doctypeNode.nodeName());
 }

 // Test insertNode()
 @Test
 public void testInsertNode() {
 Element tagElement = new Element("tag", "", new Attribute("attr", "value"));
 xmlTreeBuilder.insertNode(tagElement);
 assertEquals(xmlTreeBuilder.doc.childNodes().size(), 1);
 Element tagElementInDoc = (Element) xmlTreeBuilder.doc.childNodes().get(0);
 assertEquals("tag", tagElementInDoc.nodeName());
 assertEquals("value", tagElementInDoc.attribute("attr").toString());
 }

 // Test popStackToClose()
 @Test
 public void testPopStackToClose() {
 String xmlString = "<xml><tag/><tag><tag/></tag></xml>";
 xmlTreeBuilder.initialiseParse(xmlString, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.next()); // StartTag: <xml>
 xmlTreebuilder.process(xmlTreeBuilder.tokeniser.next()); // StartTag: <tag>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.next()); // EndTag: </tag>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.next()); // StartTag: <tag>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.next()); // EndTag: </tag>
 xmlTreeBuilder.process(xmlTreeBuilder.tokeniser.next()); // EOF
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 assertEquals(xmlTreeBuilder.stack.size(), 0);
 }

 // Test parseFragment() - normal case
 @Test
 public void testParseFragmentNormalCase() {
 String xmlFragment = "<tag attr='value'></tag>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertEquals(nodes.size(), 1);
 Element tagElement = (Element) nodes.get(0);
 assertEquals("tag", tagElement.nodeName());
 assertEquals("value", tagElement.attribute("attr").toString());
 }

 // Test parseFragment() - empty input
 @Test (expected = IllegalArgumentException.class)
 public void testParseFragmentEmptyInput() {
 xmlTreeBuilder.parseFragment("", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 // Test parseFragment() - null input
 @Test (expected = IllegalArgumentException.class)
 public void testParseFragmentNullInput() {
 xmlTreeBuilder.parseFragment(null, "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 // Test parseFragment() - invalid input
 @Test (expected = IllegalArgumentException.class)
 public void testParseFragmentInvalidInput() {
 xmlTreeBuilder.parseFragment("&%$@", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }
}