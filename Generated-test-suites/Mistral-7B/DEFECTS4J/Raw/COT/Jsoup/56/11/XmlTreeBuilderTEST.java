package org.jsoup.parser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

/**
 * Test class for XmlTreeBuilder
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private Tokenizer tokenizer;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = Jsoup.parse("<root><child/><child/><comment/></root>", "", Parser.xmlParser());
 tokenizer = new Tokenizer(document.outerHtml());
 }

 /**
 * Test defaultSettings method
 */
 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertTrue(settings.isPreserveCase());
 }

 /**
 * Test parse method with valid input
 */
 @Test
 public void testParseValidInput() {
 Document parsedDocument = xmlTreeBuilder.parse(document.outerHtml(), "baseUri");
 assertNotNull(parsedDocument);
 assertNotNull(parsedDocument.childNodes());
 assertEquals(document.childNodes().size(), parsedDocument.childNodes().size());
 }

 /**
 * Test parse method with invalid input
 */
 @Test(expected = RuntimeException.class)
 public void testParseInvalidInput() {
 xmlTreeBuilder.parse("invalidInput", "baseUri");
 }

 /**
 * Test initialiseParse method
 */
 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("input", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertTrue(stackContains(doc));
 }

 /**
 * Test process method with StartTag
 */
 @Test
 public void testProcessStartTag() {
 tokenizer.next();
 Element el = xmlTreeBuilder.insert(tokenizer.next());
 assertEquals("root", el.nodeName());
 }

 /**
 * Test process method with EndTag
 */
 @Test
 public void testProcessEndTag() {
 tokenizer.next(); // StartTag "root"
 tokenizer.next(); // EndTag "root"
 xmlTreeBuilder.popStackToClose(tokenizer.next());
 assertTrue(stackIsEmpty());
 }

 /**
 * Test process method with Comment
 */
 @Test
 public void testProcessComment() {
 tokenizer.next(); // Comment
 Comment comment = (Comment) xmlTreeBuilder.insert(tokenizer.next());
 assertNotNull(comment.getData());
 }

 /**
 * Test process method with Character
 */
 @Test
 public void testProcessCharacter() {
 tokenizer.next(); // Character
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(tokenizer.next());
 assertNotNull(textNode.getData());
 }

 /**
 * Test process method with Doctype
 */
 @Test
 public void testProcessDoctype() {
 tokenizer.next(); // Doctype
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.insert(tokenizer.next());
 assertNotNull(documentTypeNode.getName());
 assertNull(documentTypeNode.getPublicIdentifier());
 assertNull(documentTypeNode.getSystemIdentifier());
 }

 /**
 * Test insertNode method
 */
 @Test
 public void testInsertNode() {
 Element el = new Element("tag");
 xmlTreeBuilder.insertNode(el);
 Element insertedEl = (Element) document.childNodes().get(0);
 assertEquals("tag", insertedEl.nodeName());
 }

 /**
 * Test insert method with StartTag
 */
 @Test
 public void testInsertStartTag() {
 Token startTagToken = tokenizer.next();
 Element el = xmlTreeBuilder.insert(startTagToken.asStartTag());
 assertEquals("tag", el.nodeName());
 }

 /**
 * Test insert method with Comment
 */
 @Test
 public void testInsertComment() {
 Token commentToken = tokenizer.next();
 Comment comment = (Comment) xmlTreeBuilder.insert(commentToken);
 assertEquals("", comment.getData());
 }

 /**
 * Test insert method with Character
 */
 @Test
 public void testInsertCharacter() {
 Token characterToken = tokenizer.next();
 TextNode textNode = (TextNode) xmlTreeBuilder.insert(characterToken);
 assertEquals("", textNode.getData());
 }

 /**
 * Test insert method with Doctype
 */
 @Test
 public void testInsertDoctype() {
 Token doctypeToken = tokenizer.next();
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 assertEquals("", documentTypeNode.getName());
 assertNull(documentTypeNode.getPublicIdentifier());
 assertNull(documentTypeNode.getSystemIdentifier());
 }

 /**
 * Test popStackToClose method
 */
 @Test
 public void testPopStackToClose() {
 Element el1 = new Element("tag1");
 Element el2 = new Element("tag2");
 stack.add(el1);
 stack.add(el2);
 xmlTreeBuilder.popStackToClose(tokenizer.next().asEndTag());
 assertEquals(1, stack.size());
 assertEquals("tag1", stack.get(0).nodeName());
 }

 /**
 * Test parseFragment method with valid input
 */
 @Test
 public void testParseFragmentValidInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<root><child/><child/></root>", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 assertNotNull(nodes);
 assertEquals(2, nodes.size());
 }

 /**
 * Test parseFragment method with invalid input
 */
 @Test(expected = RuntimeException.class)
 public void testParseFragmentInvalidInput() {
 xmlTreeBuilder.parseFragment("invalidInput", "", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 }

 /**
 * Helper method to check if the stack contains the given element
 *
 * @param element the element to check
 * @return true if the stack contains the given element, false otherwise
 */
 private boolean stackContains(Element element) {
 for (Object obj : stack) {
 if (obj instanceof Element && obj.equals(element)) {
 return true;
 }
 }
 return false;
 }

 /**
 * Helper method to check if the stack is empty
 *
 * @return true if the stack is empty, false otherwise
 */
 private boolean stackIsEmpty() {
 return stack.isEmpty();
 }

 /**
 * Helper method to get the stack
 */
 private List<?> stack = document.outputSettings().stack;
}