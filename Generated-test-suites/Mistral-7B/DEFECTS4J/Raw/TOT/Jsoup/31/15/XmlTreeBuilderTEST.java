import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root", null);
 Element rootElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Element rootElement = new Element("root");
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.insert(rootElement);
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertTrue(xmlTreeBuilder.stack.isEmpty());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("-- This is a comment");
 xmlTreeBuilder.insert(commentToken);
 Element commentElement = (Element) xmlTreeBuilder.currentElement();
 assertEquals("-- This is a comment", commentElement.text());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 Element textNode = (Element) xmlTreeBuilder.currentElement();
 assertEquals("A", textNode.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.currentElement();
 assertEquals("html", doctypeNode.name());
 }

 @Test
 public void testProcessUnknownTag() {
 Token startTagToken = new Token.StartTag("unknown", null);
 Element unknownElement = xmlTreeBuilder.insert(startTagToken);
 assertEquals("unknown", unknownElement.nodeName());
 // Since the test does not know if this tag is self-closing or not, it cannot check that.
 }

 @Test(expected = RuntimeException.class)
 public void testProcessInvalidToken() {
 Token invalidToken = new Token.Unknown();
 xmlTreeBuilder.process(invalidToken);
 }
}