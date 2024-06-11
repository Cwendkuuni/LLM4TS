import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import org.jsoup.helper.Validate;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XMLElement;
import org.jsoup.nodes.XMLDocument;
import org.jsoup.nodes.XMLDeclaration;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BaseElement;
import org.jsoup.nodes.Tokens;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Tokenizer;
import java.util.List;
import java.util.ArrayList;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;
 private Tokenizer tokenizer;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = Jsoup.parse("<xml><element attr='value'><subElement/><![CDATA[This is CDATA]]></element></xml>", "http://test.com");
 tokenizer = document.tokenizer();
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("", "", null);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = Validate.MalformedDocumentException.class)
 public void testInitialiseParseWithInvalidInput() {
 xmlTreeBuilder.initialiseParse("InvalidXML", "", null);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Tokens.StartTag, "<element>", null, null);
 Element element = xmlTreeBuilder.insert(startTagToken);
 assertNotNull(element);
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Tokens.EndTag, "</element>", null, null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Tokens.Text, "Text", null, null);
 xmlTreeBuilder.insert(characterToken);
 Node node = xmlTreeBuilder.doc.childNodes().get(0);
 assertInstanceOf(TextNode.class, node);
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Tokens.Comment, "This is a comment", null, null);
 xmlTreeBuilder.insert(commentToken);
 Node node = xmlTreeBuilder.doc.childNodes().get(1);
 assertInstanceOf(Comment.class, node);
 }

 @Test
 public void testProcessDocumentType() {
 Token documentTypeToken = new Token(Tokens.Doctype, "<!DOCTYPE xml SYSTEM \"xml.dtd\">", null, null);
 DocumentType documentTypeNode = (DocumentType) xmlTreeBuilder.insert(documentTypeToken);
 Node node = xmlTreeBuilder.doc.childNodes().get(0);
 assertInstanceOf(DocumentType.class, node);
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "http://test.com", null);
 assertEquals(0, nodes.size());
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<xml><element attr='value'><subElement/><![CDATA[This is CDATA]]></element></xml>", "http://test.com", null);
 assertEquals(3, nodes.size());
 assertInstanceOf(Element.class, nodes.get(0));
 assertInstanceOf(Element.class, nodes.get(1));
 assertInstanceOf(TextNode.class, nodes.get(2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "http://test.com", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullBaseUri() {
 xmlTreeBuilder.parseFragment("<xml><element attr='value'><subElement/><![CDATA[This is CDATA]]></element></xml>", null, null);
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullErrors() {
 xmlTreeBuilder.parseFragment("<xml><element attr='value'><subElement/><![CDATA[This is CDATA]]></element></xml>", "http://test.com", null);
 tokenizer = null;
 xmlTreeBuilder.runParser();
 }

 @Test(expected = IllegalStateException.class)
 public void testParseFragmentWithMissingDocumentObject() {
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parseFragment("<xml><element attr='value'><subElement/><![CDATA[This is CDATA]]></element></xml>", "http://test.com", null);
 }
}