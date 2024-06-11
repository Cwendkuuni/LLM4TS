import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import static org.junit.Assert.*;

public class W3CDomTest {

 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setup() {
 jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
 }

 @Test
 public void testFromJsoupToW3CDoc() {
 w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<html xmlns='http://www.w3.org/1999/xhtml'>\n<body xmlns='http://www.w3.org/1999/xhtml'>\n<h1>Hello World</h1>\n</body>\n</html>\n", W3CDomTest.getExpectedW3CDoc());
 }

 @Test
 public void testEmptyDocument() {
 Document emptyJsoupDocument = new Document();
 w3cDocument = new W3CDom().fromJsoup(emptyJsoupDocument);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>\n<html xmlns='http://www.w3.org/1999/xhtml'>\n<body>\n</body>\n</html>\n", W3CDomTest.getExpectedEmptyW3CDoc());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullJsoupDocument() {
 W3CDom w3CDom = new W3CDom();
 try {
 w3cDocument = w3CDom.fromJsoup(null);
 } catch (IllegalArgumentException e) {
 // expected
 }
 }

 @Test
 public void testConvertJsoupDocumentToW3CDoc() {
 jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1><p id='test'><a href='#'></a></p></body></html>");
 w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
 W3CDomTest.assertW3CDocEqualsXML(W3CDomTest.getExpectedW3CDocWithAttributes(), w3cDocument);
 }

 @Test
 public void testAsString() {
 String w3cString = new W3CDom().asString(w3cDocument);
 String expectedW3CString = "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head></head><body><h1>Hello World</h1></body></html>";
 assertEquals(expectedW3CString, w3cString);
 }

 @Test
 public void testAsStringWithAttributes() {
 String w3cString = new W3CDom().asString(w3cDocument);
 String expectedW3CString = "<html xmlns=\"http://www.w3.org/1999/xhtml\" xml:lang=\"en\">\n<head>\n<meta http-equiv=\"Content-Type\" content=\"text/html; charset=UTF-8\"/>\n<title>Hello World</title>\n</head>\n<body>\n<h1>Hello World</h1>\n<p id=\"test\"></p>\n</body>\n</html>";
 assertEquals(expectedW3CString, w3cString);
 }

 private static String getExpectedW3CDoc() {
 // ...
 }

 private static String getExpectedEmptyW3CDoc() {
 // ...
 }

 private static void assertW3CDocEqualsXML(String expectedXML, Document w3cDocument) {
 // ...
 }

}