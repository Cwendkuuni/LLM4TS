import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import static org.junit.Assert.*;

public class W3CDomTest {

 @Test
 public void testFromJsoupEmptyDocument() {
 Document jsoupDocument = new Document(); // empty jsoup document
 W3CDom w3cDom = new W3CDom();
 Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

 assertEquals("", w3cDocument.getDocumentURI()); // checks the documentURI is empty
 assertNull(w3cDocument.getDocumentElement()); // checks there's no root element
 }

 @Test
 public void testFromJsoupDocumentWithRootElement() {
 Document jsoupDocument = new Document(); // empty jsoup document
 Element rootElement = jsoupDocument.createElement("root");
 jsoupDocument.appendChild(rootElement);
 W3CDom w3cDom = new W3CDom();
 Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

 assertEquals("root", w3cDocument.getDocumentElement().getNodeName()); // checks the root element name
 assertEquals("", w3cDocument.getDocumentURI()); // checks the documentURI is empty
 }

 @Test
 public void testFromJsoupDocumentWithElementsAndTextContent() {
 Document jsoupDocument = new Document(); // empty jsoup document
 Element rootElement = jsoupDocument.createElement("root");
 Element childElement = jsoupDocument.createElement("child");
 Text textNode = jsoupDocument.createTextNode("Hello, World!");
 childElement.appendChild(textNode);
 rootElement.appendChild(childElement);
 jsoupDocument.appendChild(rootElement);
 W3CDom w3cDom = new W3CDom();
 Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);

 assertEquals("Hello, World!", w3cDocument.getDocumentElement()
 .getElementsByTagName("child")
 .get(0)
 .getTextContent()); // checks the text content is correctly converted
 }

 @Test
 public void testConvertEmptyDocument() {
 Document jsoupDocument = new Document(); // empty jsoup document
 Document w3cDocument = new Document();
 W3CDom w3cDom = new W3CDom();
 w3cDom.convert(jsoupDocument, w3cDocument);

 assertEquals(0, w3cDocument.getChildNodes().getLength()); // checks there are no child nodes
 }

 @Test
 public void testConvertDocumentWithRootElement() {
 Document jsoupDocument = new Document(); // empty jsoup document
 Element rootElement = jsoupDocument.createElement("root");
 jsoupDocument.appendChild(rootElement);
 Document w3cDocument = new Document();
 W3CDom w3cDom = new W3CDom();
 w3cDom.convert(jsoupDocument, w3cDocument);

 assertEquals("root", w3cDocument.getDocumentElement().getNodeName()); // checks the root element name
 assertEquals(1, w3cDocument.getChildNodes().getLength()); // checks there's one child node (the root)
 }

 @Test
 public void testConvertDocumentWithElementsAndTextContent() {
 Document jsoupDocument = new Document(); // empty jsoup document
 Element rootElement = jsoupDocument.createElement("root");
 Element childElement = jsoupDocument.createElement("child");
 Text textNode = jsoupDocument.createTextNode("Hello, World!");
 childElement.appendChild(textNode);
 rootElement.appendChild(childElement);
 jsoupDocument.appendChild(rootElement);
 Document w3cDocument = new Document();
 W3CDom w3cDom = new W3CDom();
 w3cDom.convert(jsoupDocument, w3cDocument);

 assertEquals("Hello, World!", w3cDocument.getDocumentElement()
 .getElementsByTagName("child")
 .get(0)
 .getTextContent()); // checks the text content is correctly converted
 }

 @Test
 public void testAsStringEmptyDocument() {
 Document w3cDocument = new Document();
 W3CDom w3cDom = new W3CDom();
 Document jsoupDocument = w3cDom.fromJsoup(w3cDocument);
 String w3cString = w3cDom.asString(w3cDocument);

 assertTrue(w3cString.isEmpty()); // checks the string is empty
 }

 @Test
 public void testAsStringDocumentWithRootElement() {
 Document w3cDocument = new Document();
 Element rootElement = w3cDocument.createElement("root");
 w3cDocument.appendChild(rootElement);
 Document jsoupDocument = new Document();
 W3CDom w3cDom = new W3CDom();
 jsoupDocument = w3cDom.fromJsoup(w3cDocument);
 String w3cString = w3cDom.asString(w3cDocument);

 assertEquals("<root></root>", w3cString); // checks the string correctly represents the document
 }

 @Test
 public void testAsStringDocumentWithElementsAndTextContent() {
 Document w3cDocument = new Document();
 Element rootElement = w3cDocument.createElement("root");
 Element childElement = w3cDocument.createElement("child");
 Text textNode = w3cDocument.createTextNode("Hello, World!");
 childElement.appendChild(textNode);
 rootElement.appendChild(childElement);
 w3cDocument.appendChild(rootElement);
 Document jsoupDocument = new Document();
 W3CDom w3cDom = new W3CDom();
 jsoupDocument = w3cDom.fromJsoup(w3cDocument);
 String w3cString = w3cDom.asString(w3cDocument);

 assertEquals("<root><child>Hello, World!</child></root>", w3cString); // checks the string correctly represents the document
 }
}