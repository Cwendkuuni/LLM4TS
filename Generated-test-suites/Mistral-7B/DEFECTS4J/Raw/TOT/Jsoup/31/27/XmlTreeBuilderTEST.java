import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementFilter;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.XmlTreeBuilder.Token;
import org.jsoup.parser.XmlTreeBuilder.Token.AttributeValue;
import org.jsoup.parser.XmlTreeBuilder.Token.Comment;
import org.jsoup.parser.XmlTreeBuilder.Token.Doctype;
import org.jsoup.parser.XmlTreeBuilder.Token.EndTag;
import org.jsoup.parser.XmlTreeBuilder.Token.StartTag;
import org.jsoup.parser.XmlTreeBuilder.Token.Character;
import org.jsoup.parser.XmlTreeBuilder.Token.CharacterData;

import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = xmlTreeBuilder.parse("<root><tag attr1=\"value1\" attr2=\"value2\"><subtag/></tag></root>");
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag("tag", null, new Attribute[] {
 new Attribute("attr1", new AttributeValue("value1")),
 new Attribute("attr2", new AttributeValue("value2"))
 });
 Element tag = xmlTreeBuilder.insert(startTag);
 assertEquals("tag", tag.nodeName());
 assertEquals("value1", tag.attr("attr1"));
 assertEquals("value2", tag.attr("attr2"));
 }

 @Test
 public void testSelfClosingTag() {
 StartTag selfClosingTag = new StartTag("subtag", null);
 selfClosingTag.setSelfClosing(true);
 Element subtag = xmlTreeBuilder.insert(selfClosingTag);
 assertNotNull(subtag);
 assertTrue(subtag.tagName().isSelfClosing());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment("This is a comment");
 xmlTreeBuilder.insert(comment);
 Element commentNode = doc.selectSingle("//comment()");
 assertEquals("This is a comment", commentNode.text());
 }

 @Test
 public void testCharacter() {
 Character character = new Character('A');
 xmlTreeBuilder.insert(character);
 TextNode characterNode = (TextNode) doc.text();
 assertEquals("A", characterNode.text());
 }

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctype);
 DocumentType doctypeNode = (DocumentType) doc.doctype();
 assertEquals("html", doctypeNode.name());
 }

 @Test
 public void testEndTag() {
 StartTag startTag = new StartTag("tag", null, null);
 EndTag endTag = new EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTag);
 Element tag = stack.peek();
 assertEquals("tag", tag.nodeName());
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, null);
 xmlTreeBuilder.process(unexpectedToken);
 }
}