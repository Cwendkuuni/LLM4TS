import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 @Test
 public void testInitialiseParse() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child/>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertEquals("http://test.com", xmlTreeBuilder.doc.baseUri());
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child/>\n";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 Token token = new Token(1, "invalid", ""); // Invalid token type
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testProcessStartTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child/>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token(1, "StartTag", "<root>");
 Element el = xmlTreeBuilder.insert(startTag);
 assertEquals("root", el.nodeName());
 assertEquals(baseUri, el.baseUri());
}

 @Test
 public void testProcessEndTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child>\n</child>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeTreeBuilder.initialiseParse(input, baseUri, errors);

 Token endTag = new Token(1, "EndTag", "</child>");
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(1, xmlTreeBuilder.stack.size());
}

 @Test
 public void testProcessComment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<!-- This is a comment -->\n<child/>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token commentToken = new Token(1, "Comment", "<!-- This is a comment -->");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
}

 @Test
 public void testProcessCharacter() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child>Hello World!</child>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token characterToken = new Token(1, "Character", "Hello World!");
 xmlTreeBuilder.insert(characterToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 assertEquals("Hello World!", xmlTreeBuilder.doc.childNodes().get(0).text());
}

 @Test
 public void testProcessDoctype() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<!DOCTYPE xml [ <!ATTLIST xml xmlns CDATA \"#IMPLIED\" > ]>\n<root>\n<child/>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token doctypeToken = new Token(1, "Doctype", "<!DOCTYPE xml");
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 assertEquals("xml", xmlTreeBuilder.doc.documentType().nodeName());
}

 @Test
 public void testParseFragment() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String inputFragment = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<child/>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
 assertEquals(2, nodes.size());
 assertEquals("root", nodes.get(0).nodeName());
 assertEquals("child", nodes.get(1).nodeName());
}

 @Test
 public void testUnknownTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<unknown/>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token(1, "StartTag", "<unknown>");
 Element el = xmlTreeBuilder.insert(startTag);
 assertTrue(el.isUnknown());
}

 @Test
 public void testSelfClosingTag() {
 XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 String input = "<xml version=\"1.0\" encoding=\"UTF-8\">\n<root>\n<selfClosing/>\n</root>";
 String baseUri = "http://test.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);

 Token startTag = new Token(1, "StartTag", "<selfClosing/>");
 Element el = xmlTreeBuilder.insert(startTag);
 assertTrue(el.isSelfClosing());
}
}