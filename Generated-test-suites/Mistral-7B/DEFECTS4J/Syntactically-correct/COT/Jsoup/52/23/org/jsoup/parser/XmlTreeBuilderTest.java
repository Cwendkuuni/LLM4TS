import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private Document doc;

 @Test
 public void testInitialiseParse() {
 doc = new XmlTreeBuilder().parseFragment("<xml></xml>", null, new ParseErrorList());
 Assert.assertNotNull(doc);
 }

 @Test
 public void testProcessStartTag() {
 doc = new XmlTreeBuilder().parseFragment("<xml><tag></tag></xml>", null, new ParseErrorList());
 Element tag = doc.childNodes().get(0);
 Assert.assertEquals("tag", tag.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 doc = new XmlTreeBuilder().parseFragment("<xml><tag/></xml>", null, new ParseErrorList());
 Element tag = doc.childNodes().get(0);
 Assert.assertEquals("tag", tag.nodeName());
 doc.childNodes().remove(0);
 Element closingTag = doc.childNodes().get(0);
 Assert.assertEquals("tag", closingTag.nodeName());
 Assert.assertEquals("", closingTag.attributes().toString());
 }

 @Test
 public void testProcessComment() {
 doc = new XmlTreeBuilder().parseFragment("<xml><!-- comment --></xml>", null, new ParseErrorList());
 Comment comment = (Comment) doc.childNodes().get(0);
 Assert.assertEquals(" comment ", comment.getData());
 }

 @Test
 public void testProcessCharacter() {
 doc = new XmlTreeBuilder().parseFragment("<xml>text</xml>", null, new ParseErrorList());
 TextNode textNode = (TextNode) doc.childNodes().get(0);
 Assert.assertEquals("text", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 doc = new XmlTreeBuilder().parseFragment("<xml><!DOCTYPE xml [ <!ATTLIST xml foo CDATA #REQUIRED > ]>", null, new ParseErrorList());
 DocumentType doctypeNode = (DocumentType) doc.childNodes().get(0);
 Assert.assertEquals("xml", doctypeNode.nodeName());
 }

 @Test (expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 doc = new XmlTreeBuilder().parseFragment("<xml>", null, new ParseErrorList());
 }

 @Test
 public void testInsertNode() {
 doc = new XmlTreeBuilder().parseFragment("<xml></xml>", null, new ParseErrorList());
 Element root = doc.childNodes().get(0);
 Element tag = new Element("tag", null, null);
 XmlTreeBuilder.this.insertNode(tag);
 root.appendChild(tag);
 Assert.assertEquals(1, doc.childNodes().size());
 }

 @Test
 public void testPopStackToClose() {
 doc = new XmlTreeBuilder().parseFragment("<xml><tag><tag/></tag></xml>", null, new ParseErrorList());
 doc.childNodes().remove(2); // remove the self closing tag
 Element tag = doc.childNodes().get(1);
 XmlTreeBuilder.this.popStackToClose((Token.EndTag) tag.tagName());
 Assert.assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 doc = new XmlTreeBuilder().parseFragment("", null, new ParseErrorList());
 Assert.assertNotNull(doc);
 Assert.assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testParseFragmentWithBogusComment() {
 doc = new XmlTreeBuilder().parseFragment("<xml><!--<?xml version='1.0' encoding='UTF-8'?>\n-->", null, new ParseErrorList());
 Assert.assertNotNull(doc);
 Assert.assertEquals(1, doc.childNodes().size());
 XmlDeclaration xmlDeclaration = (XmlDeclaration) doc.childNodes().get(0);
 Assert.assertEquals("xml", xmlDeclaration.nodeName());
 Assert.assertEquals("UTF-8", xmlDeclaration.encoding());
 Assert.assertEquals("1.0", xmlDeclaration.version());
 }

}