import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;

public class CleanerTest {

 private Whitelist whitelist;
 private Cleaner cleaner;

 @Before
 public void setup() {
 whitelist = Whitelist.simpleTextOnly(); // Use a simple whitelist for testing
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanerConstructor() {
 Cleaner cleaner = new Cleaner(whitelist);
 Assert.assertNotNull(cleaner);
 }

 @Test
 public void testCleanMethod() {
 Document dirtyDocument = Parser.parseBodyFragment("<html><body><p>Test</p><script>alert('XSS')</script></body></html>");
 Document cleanDocument = cleaner.clean(dirtyDocument);
 Assert.assertNotNull(cleanDocument);
 // Check if the clean document only contains the allowed elements and attributes
 Element root = cleanDocument.body();
 Assert.assertEquals("p", root.tagName());
 Assert.assertTrue(root.children().isEmpty());
 }

 @Test
 public void testIsValidMethod() {
 Document invalidDocument = Parser.parseBodyFragment("<html><body><p>Test</p><script>alert('XSS')</script></body></html>");
 assertTrue(cleaner.isValid(invalidDocument)); // The method should consider the invalid document as valid, but clean it properly
 Document validDocument = Parser.parseBodyFragment("<html><body><p>Test</p></body></html>");
 assertTrue(cleaner.isValid(validDocument)); // A valid document should not need any elements or attributes discarded
 }

 @Test
 public void testCopySafeNodesMethod() {
 Element sourceElement = Parser.parseBodyFragment("<html><body><div id='test' class='test'><p>Test</p><script>alert('XSS')</script></div></body></html>").body().child(0);
 Element destinationElement = new Element("div");
 int numDiscarded = cleaner.copySafeNodes(sourceElement, destinationElement);
 Assert.assertEquals(2, numDiscarded); // The 'script' tag and the 'class' attribute are not allowed by the whitelist
 }

 @Test(expected = NullPointerException.class)
 public void testCleanMethodWithNullDocument() {
 cleaner.clean(null);
 }

 @Test(expected = NullPointerException.class)
 public void testIsValidMethodWithNullDocument() {
 cleaner.isValid(null);
 }

}