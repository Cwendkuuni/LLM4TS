import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Tag;
import java.util.ArrayList;
import java.util.List;

public class DocumentTest {

 private Document doc;

 @Before
 public void setup() {
 doc = Document.createShell("http://example.com");
 }

 @Test
 public void testCreateDocument() {
 Document newDoc = new Document("http://example.com");
 assertNotNull(newDoc);
 }

 @Test
 public void testCreateShell() {
 Document shellDoc = Document.createShell("http://example.com");
 assertNotNull(shellDoc.head());
 assertNotNull(shellDoc.body());
 }

 @Test
 public void testGetHeadElement() {
 Element head = doc.head();
 assertNotNull(head);
 }

 @Test
 public void testGetBodyElement() {
 Element body = doc.body();
 assertNotNull(body);
 }

 @Test
 public void testGetTitle() {
 assertEquals("", doc.title());
 }

 @Test
 public void testSetTitle() {
 doc.title("Test Title");
 assertEquals("Test Title", doc.title());
 }

 @Test
 public void testCreateElement() {
 Element newElement = doc.createElement("a");
 assertNotNull(newElement);
 }

 @Test
 public void testNormalise() {
 doc.title("Test Title");
 doc.appendText("Test text");
 Element body = doc.body();
 List<Node> bodyChildren = body.children();
 assertEquals(2, bodyChildren.size());
 doc.normalise();
 bodyChildren = body.children();
 assertEquals(1, bodyChildren.size());
 assertInstanceOf(TextNode.class, bodyChildren.get(0));
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseURI() {
 try {
 Document doc = new Document(null);
 } catch (NullPointerException e) {
 fail("Expected NullPointerException but got: " + e.getMessage());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidBaseURI() {
 try {
 Document doc = new Document(" ");
 } catch (IllegalArgumentException e) {
 fail("Expected IllegalArgumentException but got: " + e.getMessage());
 }
 }

 @Test(expected = NullPointerException.class)
 public void testNullTagName() {
 try {
 Element element = doc.createElement(null);
 } catch (NullPointerException e) {
 fail("Expected NullPointerException but got: " + e.getMessage());
 }
 }

 @Test
 public void testTitleWithExistingTitle() {
 doc.title("Test Title");
 doc.title("Updated Test Title");
 assertEquals("Updated Test Title", doc.title());
 }

 @Test
 public void testAddTextToBody() {
 doc.text("Test text");
 Element body = doc.body();
 List<Node> bodyChildren = body.children();
 assertEquals(1, bodyChildren.size());
 assertInstanceOf(TextNode.class, bodyChildren.get(0));
 }

 @Test
 public void testNormaliseWithEmptyDocument() {
 doc.title("Test Title");
 doc.appendText("Test text");
 doc.normalise();
 assertEquals("", doc.html());
 }
}