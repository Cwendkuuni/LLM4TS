import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.List;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document xmlDoc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlDoc = xmlTreeBuilder.parse("<root><child><grandchild/></child></root>", null);
 }

 @Test
 public void testParse() {
 Assert.assertThat(xmlDoc, is(notNullValue()));
 Assert.assertThat(xmlDoc.childNodeSize(), is(3));
 Assert.assertThat(xmlDoc.child(0), instanceOf(Element.class));
 Assert.assertThat(xmlDoc.child(0).nodeName(), is("root"));
 Assert.assertThat(xmlDoc.child(1), instanceOf(Element.class));
 Assert.assertThat(xmlDoc.child(1).nodeName(), is("child"));
 Assert.assertThat(xmlDoc.child(2), instanceOf(Element.class));
 Assert.assertThat(xmlDoc.child(2).nodeName(), is("grandchild"));
 }

 @Test(expected = AssertionError.class)
 public void testParseInvalidInput() {
 xmlTreeBuilder.parse("", null);
 }

 @Test
 public void testParseFragment() {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<child><grandchild/></child>", null, null, xmlTreeBuilder.defaultSettings());
 Assert.assertThat(fragmentNodes, notNullValue());
 Assert.assertThat(fragmentNodes.size(), is(3));
 Assert.assertThat(fragmentNodes.get(0), instanceOf(Element.class));
 Assert.assertThat(fragmentNodes.get(0).nodeName(), is("child"));
 Assert.assertThat(fragmentNodes.get(1), instanceOf(Element.class));
 Assert.assertThat(fragmentNodes.get(1).nodeName(), is("grandchild"));
 Assert.assertThat(fragmentNodes.get(2), instanceOf(TextNode.class));
 }

 @Test
 public void testProcessStartTag() {
 Element childElement = new Element("child", null, null);
 xmlTreeBuilder.insert(new Token.StartTag("child"));
 Assert.assertEquals(childElement, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 @Test
 public void testProcessEndTag() {
 xmlTreeBuilder.insert(new Token.EndTag("child"));
 Assert.assertThat(xmlTreeBuilder.stack.size(), is(2));
 }

 @Test
 public void testProcessCharacter() {
 xmlTreeBuilder.insert(new Token.Character(' '));
 Assert.assertEquals(xmlTreeBuilder.doc.childNodes().get(xmlTreeBuilder.doc.childNodes().size() - 1), new TextNode(" "));
 }

 @Test
 public void testProcessComment() {
 xmlTreeBuilder.insert(new Token.Comment("<!-- comment -->"));
 Assert.assertEquals(xmlTreeBuilder.doc.childNodes().get(xmlTreeBuilder.doc.childNodes().size() - 1), new Comment(" comment ", null));
 }

 @Test
 public void testProcessDoctype() {
 xmlTreeBuilder.insert(new Token.Doctype("html", null, null, null));
 Assert.assertEquals(xmlTreeBuilder.doc.childNodes().get(xmlTreeBuilder.doc.childNodes().size() - 1), new DocumentType("html", null, null, null));
 }
}