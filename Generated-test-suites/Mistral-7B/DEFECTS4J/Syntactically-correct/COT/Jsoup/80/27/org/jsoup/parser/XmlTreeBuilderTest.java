import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 Document document = xmlTreeBuilder.defaultSettings().parse(new StringReader("<!-- Comment -->\n<root><tag /></root>"), "baseUri");
 Assert.assertEquals("baseUri", document.baseURI());
 }

 @Test
 public void testParseWithBaseUri() {
 Document document = xmlTreeBuilder.parse(new StringReader("<root><tag attr=\"value\"/></root>"), "baseUri");
 Assert.assertEquals("baseUri", document.baseURI());
 }

 @Test
 public void testParseWithEmptyInput() {
 Document document = xmlTreeBuilder.parse(new StringReader(""), "baseUri");
 Assert.assertEquals(true, document.childNodes().isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInput() {
 xmlTreeBuilder.parse(null, "baseUri"); // should throw an IllegalArgumentException
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseWithNullInputUri() {
 xmlTreeBuilder.parse(new StringReader("<root></root>"), null); // should throw an IllegalArgumentException
 }

 @Test
 public void testInitialiseParse() {
 Document document = new Document();
 XmlTreeBuilder xmlTreeBuilderSpy = spy(new XmlTreeBuilder());
 doReturn(document).when(xmlTreeBuilderSpy).doc;
 xmlTreeBuilderSpy.initialiseParse(new StringReader("<root></root>"), "baseUri", eq(ParseErrorList.noTracking()), eq(ParseSettings.preserveCase));
 verify(xmlTreeBuilderSpy).stack.add(document);
 }

 @Test
 public void testProcessStartTag() {
 Element rootElement = new Element("root");
 XmlTreeBuilder xmlTreeBuilderSpy = spy(new XmlTreeBuilder());
 doReturn(rootElement).when(xmlTreeBuilderSpy).insert(any(Token.StartTag.class));
 Token startTag = mock(Token.StartTag.class);
 xmlTreeBuilderSpy.process(startTag);
 verify(xmlTreeBuilderSpy).insertNode(rootElement);
 }

 @Test
 public void testProcessEndTag() {
 Element rootElement = new Element("root");
 Element tagElement = new Element("tag");
 stack.add(rootElement);
 stack.add(tagElement);
 XmlTreeBuilder xmlTreeBuilderSpy = spy(new XmlTreeBuilder());
 doReturn(tagElement).when(xmlTreeBuilderSpy).popStackToClose(any(Token.EndTag.class));
 Token endTag = mock(Token.EndTag.class);
 xmlTreeBuilderSpy.process(endTag);
 verify(xmlTreeBuilderSpy).popStackToClose(endTag);
 verify(xmlTreeBuilderSpy, times(2)).stack.get(anyInt());
 verify(xmlTreeBuilderSpy, atLeastOnce()).stack.remove(anyInt());
 }

 @Test
 public void testParseFragmentWithEmptyInput() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("", "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Assert.assertEquals(true, nodes.isEmpty());
 }

 @Test
 public void testParseFragmentWithValidXml() {
 String xmlFragment = "<root><tag attr=\"value\"/></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase);
 Element rootElement = (Element) nodes.get(0);
 Element tagElement = (Element) nodes.get(1);
 Assert.assertEquals("tag", tagElement.tagName());
 Assert.assertEquals("value", tagElement.attr("attr"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentWithNullInput() {
 xmlTreeBuilder.parseFragment(null, "baseUri", ParseErrorList.noTracking(), ParseSettings.preserveCase); // should throw an IllegalArgumentException
 }

}