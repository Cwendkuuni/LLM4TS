import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;

public class W3CDomTest {

 private W3CDom w3CDom = new W3CDom();

 @Test
 public void testFromJsoup() {
 Document jsoupDoc = createJsoupDocument();
 Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
 assertNotNull(w3cDoc);
 assertEquals(jsoupDoc.documentElement().nodeName(), w3cDoc.getDocumentElement().getNodeName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFromJsoupWithNullDocument() {
 w3CDom.fromJsoup(null);
 }

 @Test
 public void testConvert() {
 Document jsoupDoc = createJsoupDocument();
 Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
 w3CDom.convert(jsoupDoc, w3cDoc);
 assertSame(jsoupDoc, w3cDoc);
 }

 @Test
 public void testW3CBuilder() {
 Document w3cDoc = w3CDom.fromJsoup(createJsoupDocument());
 Element rootEl = w3cDoc.getDocumentElement();

 assertThat(rootEl.getNodeName(), is("html"));
 assertNotNull(rootEl.getElementsByTagName("head").getItem(0));
 assertNotNull(rootEl.getElementsByTagName("body").getItem(0));
 }

 @Test
 public void testAsString() {
 Document w3cDoc = w3CDom.fromJsoup(createJsoupDocument());
 String w3cString = w3CDom.asString(w3cDoc);
 assertNotNull(w3cString);
 }

 @Test(expected = TransformerException.class)
 public void testAsStringWithNullDocument() {
 w3CDom.asString(null);
 }

 private Document createJsoupDocument() {
 Document doc = new Document();
 doc.appendElement("html").appendElement("head").appendElement("title").appendText("Test title");
 doc.appendElement("body").appendElement("h1", "Test heading");
 return doc;
 }

}