import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Doctype;
import org.jsoup.helper.Validate;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.Stack;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private Tokenizer tokeniser;
 private Stack<Element> stack;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 tokeniser = new Tokenizer(xmlTreeBuilder, "<test><tag attr='value'><subtag attr2='value2' /><anotherTag/></tag></test>");
 stack = xmlTreeBuilder.stack;
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = tokeniser.nextToken();
 Validate.isInstanceOf(Token.StartTag.class, startTagToken);
 Element tag = xmlTreeBuilder.insert(startTagToken.asStartTag());
 assertEquals("tag", tag.nodeName());
 assertEquals("value", tag.attr("attr"));
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = tokeniser.nextToken();
 Validate.isInstanceOf(Token.EndTag.class, endTagToken);
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 assertEquals(0, stack.size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = tokeniser.nextToken();
 Validate.isInstanceOf(Token.Comment.class, commentToken);
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, doc.childNodes().size());
 assertTrue(doc.childNodes().get(0) instanceof Comment);
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = tokeniser.nextToken();
 Validate.isInstanceOf(Token.Character.class, characterToken);
 xmlTreeBuilder.insert(characterToken);
 assertEquals(1, doc.textNodes().size());
 assertEquals("test", doc.textNodes().get(0).text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = tokeniser.nextToken();
 Validate.isInstanceOf(Token.Doctype.class, doctypeToken);
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(1, doc.childNodes().size());
 assertEquals(1, doc.childNodes().get(0).childNodes().size());
 assertTrue(doc.childNodes().get(0) instanceof DocumentType);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = tokeniser.nextToken();
 Validate.fail("Unexpected token type: " + unexpectedToken.type);
 }

 @Test
 public void testSelfClosingTag() {
 Token startTagToken = tokeniser.nextToken();
 Validate.isInstanceOf(Token.StartTag.class, startTagToken);
 Token endTagToken = tokeniser.nextToken();
 Validate.isInstanceOf(Token.EndTag.class, endTagToken);
 xmlTreeBuilder.insert(startTagToken.asStartTag());
 xmlTreeBuilder.popStackToClose(endTagToken.asEndTag());
 assertEquals(0, stack.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownTagName() {
 Token startTagToken = new Token(Token.StartTag.class, "unknownTag", null, null, null, null);
 xmlTreeBuilder.insert(startTagToken);
 }
}