import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class CleanerTest {

 private Document dirtyDocument;
 private Document cleanDocument;
 private Cleaner cleaner;
 private Whitelist whitelist;

 @Before
 public void setUp() {
 Parser parser = new Parser.MalformedEncodingDetector();
 String html = "<html><body><div id='test'><script>alert('XSS');</script><h1>Test</h1></div></body></html>";
 dirtyDocument = Jsoup.parse(html, parser);
 cleanDocument = Jsoup.parseBodyFragment("<h1>Test</h1>");
 whitelist = Whitelist.simpleTextOnly();
 cleaner = new Cleaner(whitelist);
 }

 @Test
 public void testCleanDocument() {
 Document cleanedDocument = cleaner.clean(dirtyDocument);
 assertNotNull(cleanedDocument);
 assertFalse(cleanedDocument.body().getElementsByTag("script").isEmpty());
 assertFalse(cleanedDocument.body().getElementsByTag("div").isEmpty());
 assertFalse(cleanedDocument.body().getElementsByTag("h1").isEmpty());
 assertTrue(cleanedDocument.body().getElementsByTag("script").size() <= dirtyDocument.body().getElementsByTag("script").size());
 assertTrue(cleanedDocument.body().getElementsByTag("div").size() <= dirtyDocument.body().getElementsByTag("div").size());
 assertTrue(cleanedDocument.body().getElementsByTag("h1").size() >= dirtyDocument.body().getElementsByTag("h1").size());
 }

 @Test
 public void testIsValidDocument() {
 assertTrue(cleaner.isValid(cleanDocument));
 assertFalse(cleaner.isValid(dirtyDocument));
 }

 @Test
 public void testCleaningVisitor() {
 Element root = Jsoup.parseBodyFragment("<html><body><div id='test'><script>alert('XSS');</script><h1>Test</h1></div></body></html>").body();
 Element destination = Jsoup.parseBodyFragment("<h1>Test</h1>").body();
 CleaningVisitor cleaningVisitor = new Cleaner.CleaningVisitor(root, destination);
 NodeTraversor traversor = new NodeTraversor(cleaningVisitor);
 traversor.traverse(root);
 assertNotNull(destination.getElementsByTag("h1").first());
 assertFalse(destination.getElementsByTag("script").isEmpty());
 }

}