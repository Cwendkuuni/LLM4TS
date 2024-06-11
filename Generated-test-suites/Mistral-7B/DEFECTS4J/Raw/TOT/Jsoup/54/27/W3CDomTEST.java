import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;
import static org.junit.Assert.*;

public class W3CDomTest {

 private W3CDom w3CDom;
 private Document jsoupDocument;
 private Document w3cDocument;

 @Before
 public void setup() {
 w3CDom = new W3CDom();
 jsoupDocument = createJsoupDocument();
 w3cDocument = w3CDom.fromJsoup(jsoupDocument);
 }

 @Test
 public void testFromJsoupEmptyDocument() {
 Document emptyJsoupDocument = new org.jsoup.nodes.Document();
 Document emptyW3cDocument = w3CDom.fromJsoup(emptyJsoupDocument);
 assertNotNull(emptyW3cDocument);
 assertFalse(emptyW3cDocument.getDocumentElement().getNodeName().isEmpty());
 }

 @Test
 public void testFromJsoupWithRootElement() {
 Element rootElement = createRootElement("root");
 Document documentWithRoot = new Document(rootElement);
 Document w3cDocumentWithRoot = w3CDom.fromJsoup(documentWithRoot);
 assertEquals(rootElement.getNodeName(), w3cDocumentWithRoot.getDocumentElement().getNodeName());
 }

 @Test
 public void testFromJsoupWithChildElement() {
 Element rootElement = createRootElement("root");
 Element childElement = createChildElement("child", "data");
 rootElement.appendChild(childElement);
 Document documentWithChild = new Document(rootElement);
 Document w3cDocumentWithChild = w3CDom.fromJsoup(documentWithChild);
 assertEquals(childElement.getNodeName(), w3cDocumentWithChild.getDocumentElement().getElementsByTagName(childElement.getNodeName().toLowerCase()).item(0).getNodeName());
 }

 @Test
 public void testConvertEmptyDocument() {
 Document emptyJsoupDocument = new org.jsoup.nodes.Document();
 w3CDom.convert(emptyJsoupDocument, w3cDocument);
 assertFalse(w3cDocument.getDocumentElement().getNodeName().isEmpty());
 }

 @Test
 public void testConvertWithRootElement() {
 Element rootElement = createRootElement("root");
 Document jsoupDocumentWithRoot = new Document(rootElement);
 Document w3cDocumentWithRoot = new Document();
 w3CDom.convert(jsoupDocumentWithRoot, w3cDocumentWithRoot);
 assertEquals(rootElement.getNodeName(), w3cDocumentWithRoot.getDocumentElement().getNodeName());
 }

 @Test
 public void testConvertWithChildElement() {
 Element rootElement = createRootElement("root");
 Element childElement = createChildElement("child", "data");
 rootElement.appendChild(childElement);
 Document jsoupDocumentWithChild = new Document(rootElement);
 Document w3cDocumentWithChild = new Document();
 w3CDom.convert(jsoupDocumentWithChild, w3cDocumentWithChild);
 assertEquals(childElement.getNodeName(), w3cDocumentWithChild.getDocumentElement().getElementsByTagName(childElement.getNodeName().toLowerCase()).item(0).getNodeName());
 }

 @Test
 public void testAsStringEmptyDocument() {
 assertNotNull(w3CDom.asString(new Document()));
 }

 @Test
 public void testAsStringWithRootElement() {
 Element rootElement = createRootElement("root");
 Document documentWithRoot = new Document(rootElement);
 String jsoupString = w3CDom.asString(documentWithRoot);
 Document w3cDocumentWithRoot = w3CDom.fromJsoup(jsoupDocumentFromString(jsoupString));
 assertEquals(w3cDocumentWithRoot.getDocumentElement().getNodeName(), rootElement.getNodeName());
 }

 @Test
 public void testAsStringWithChildElement() {
 Element rootElement = createRootElement("root");
 Element childElement = createChildElement("child", "data");
 rootElement.appendChild(childElement);
 Document documentWithChild = new Document(rootElement);
 String jsoupString = w3CDom.asString(documentWithChild);
 Document w3cDocumentWithChild = w3CDom.fromJsoup(jsoupDocumentFromString(jsoupString));
 assertEquals(w3cDocumentWithChild.getDocumentElement().getElementsByTagName(childElement.getNodeName().toLowerCase()).item(0).getNodeName(), childElement.getNodeName());
 }

 private Document createJsoupDocument() {
 return new Document();
 }

 private Element createRootElement(String tagName) {
 return new Element(tagName);
 }

 private Element createChildElement(String tagName, String text) {
 Element element = new Element(tagName);
 Text textNode = new Text(text);
 element.appendChild(textNode);
 return element;
 }

 private Document jsoupDocumentFromString(String xmlString) {
 try {
 DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
 DocumentBuilder builder = factory.newDocumentBuilder();
 return builder.parse(new InputSource(new StringReader(xmlString)));
 } catch (Exception e) {
 throw new RuntimeException(e);
 }
 }
}