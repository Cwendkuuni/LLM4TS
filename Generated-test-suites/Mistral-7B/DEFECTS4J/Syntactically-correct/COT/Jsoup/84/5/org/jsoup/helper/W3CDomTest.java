package org.jsoup.helper;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Text;
import org.w3c.dom.Node;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class W3CDomTest {

 private Document createJsoupDocument() {
 return new W3CDom().fromJsoup(createJsoupSampleDocument());
 }

 private org.jsoup.nodes.Document createJsoupSampleDocument() {
 // Sample jsoup Document for testing
 Document doc = new Document();
 Element root = doc.createElement("html");
 doc.appendChild(root);
 Element body = doc.createElement("body");
 root.appendChild(body);
 Element div = doc.createElement("div");
 body.appendChild(div);
 Element p = doc.createElement("p");
 div.appendChild(p);
 Text text1 = doc.createTextNode("Hello World");
 p.appendChild(text1);
 Comment comment = doc.createComment("This is a comment");
 div.appendChild(comment);
 return doc;
 }

 @Test
 public void testFromJsoup() {
 Document w3cDoc = new W3CDom().fromJsoup(createJsoupDocument());
 Assert.assertNotNull(w3cDoc);
 }

 @Test(expected = IllegalStateException.class)
 public void testFromJsoupWithNullInput() {
 new W3CDom().fromJsoup(null);
 }

 @Test
 public void testConvert() {
 Document jsoupDoc = createJsoupDocument();
 Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);
 new W3CDom().convert(jsoupDoc, w3cDoc);
 // Check the structure of the W3C Document
 Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());
 Element html = (Element) w3cDoc.getDocumentElement();
 Assert.assertEquals("body", html.getElementsByTagName("body").item(0).getTagName());
 Element body = (Element) html.getElementsByTagName("body").item(0);
 Assert.assertEquals("div", body.getElementsByTagName("div").item(0).getTagName());
 Element div = (Element) body.getElementsByTagName("div").item(0);
 Assert.assertEquals("p", div.getElementsByTagName("p").item(0).getTagName());
 Element p = (Element) div.getElementsByTagName("p").item(0);
 Text text = (Text) p.getChildNodes().item(0);
 Assert.assertEquals("Hello World", text.getNodeValue());
 Comment comment = (Comment) div.getChildNodes().item(1);
 Assert.assertEquals("This is a comment", comment.getNodeValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testConvertNullInput() {
 Document w3cDoc = new Document();
 new W3CDom().convert(null, w3cDoc);
 }

 @Test
 public void testAsString() {
 Document w3cDoc = createJsoupDocument();
 w3cDoc = new W3CDom().fromJsoup(w3cDoc);
 new W3CDom().convert(w3cDoc, w3cDoc);
 String w3cDocAsString = new W3CDom().asString(w3cDoc);
 }

 @Test(expected = IllegalStateException.class)
 public void testAsStringNullInput() {
 String w3cDocAsString = new W3CDom().asString(null);
 }

}