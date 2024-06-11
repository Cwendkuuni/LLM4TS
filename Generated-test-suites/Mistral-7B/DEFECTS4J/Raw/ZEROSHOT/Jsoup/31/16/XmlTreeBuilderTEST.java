import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Tokenizer tokenizer;
 private Document doc;

 @Before
 public void setUp() {
 builder = new XmlTreeBuilder();
 tokenizer = new Tokenizer("<?xml version=\"1.0\"?>\n<root>\n <element1 attr1=\"val1\" attr2=\"val2\"/>\n <element2/>\n <comment>This is a comment</comment>\n <!DOCTYPE html SYSTEM \"About.dtd\" [ \n <!ATTLIST html lang CDATA #REQUIRED>\n ]>\n</root>\n");
 doc = builder.parse(tokenizer);
 }

 @Test
 public void testInitialiseParse() {
 assertEquals("root", doc.tagName());
 assertNotNull(doc.baseUri());
 assertEquals(0, builder.errors().size());
 }

 @Test
 public void testProcessStartTag() {
 Element root = doc.rootElement();
 Element element1 = root.child(0);
 Element element2 = root.child(1);

 assertEquals("root", root.tagName());
 assertEquals("element1", element1.tagName());
 assertEquals("element2", element2.tagName());

 assertNotNull(element1.attr("attr1"));
 assertEquals("val1", element1.attr("attr1").value());
 assertNotNull(element1.attr("attr2"));
 assertEquals("val2", element1.attr("attr2").value());
 assertEquals(0, element2.children().size());
 }

 @Test
 public void testProcessEndTag() {
 Element root = doc.rootElement();
 assertEquals(2, root.childCount());

 Element element1 = root.child(0);
 assertEquals("element1", element1.tagName());
 assertEquals(2, element1.childCount());

 Element comment = element1.child(1);
 assertEquals(Comment.class, comment.getClass());
 assertEquals("This is a comment", comment.text());

 Element element2 = root.child(1);
 assertEquals("element2", element2.tagName());
 assertEquals(0, element2.childCount());

 Stack<Element> stack = builder.stack;
 assertEquals(1, stack.size());
 assertEquals(root, stack.peek());

 root.remove();
 assertEquals(0, stack.size());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessUnknownEndTag() {
 builder.popStackToClose(new Token.EndTag("unknown"));
 }

 @Test
 public void testProcessCharacter() {
 Element root = doc.rootElement();
 Element element1 = root.child(0);

 assertEquals(3, root.childCount());
 assertEquals("root", root.tagName());
 assertEquals("element1", element1.tagName());

 TextNode textNode = (TextNode) element1.child(1);
 assertEquals("This is a comment", textNode.text());
 }

 @Test
 public void testProcessComment() {
 Element root = doc.rootElement();
 Element element1 = root.child(0);

 assertEquals(3, root.childCount());
 assertEquals("root", root.tagName());
 assertEquals("element1", element1.tagName());

 Comment comment = (Comment) element1.child(1);
 assertEquals("This is a comment", comment.text());
 }

 @Test
 public void testProcessDocumentType() {
 Element root = doc.rootElement();
 DocumentType doctype = root.child(2);

 assertEquals(3, root.childCount());
 assertEquals("root", root.tagName());
 assertEquals("html", doctype.name());
 assertNotNull(doctype.publicIdentifier());
 assertNotNull(doctype.systemIdentifier());
 }
}