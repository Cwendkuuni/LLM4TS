import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private ParseSettings parseSettings;
 private String inputXml;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 parseSettings = xmlTreeBuilder.defaultSettings();
 inputXml = "<root><child tag='test' /><child><grandChild/></child></root>";
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 assertEquals(ParseSettings.preserveCase, settings);
 }

 @Test
 public void testParseReader() {
 Document document = xmlTreeBuilder.parse(new StringReader(inputXml), "baseUri");
 assertNotNull(document);
 assertNotNull(document.childNodes());
 assertEquals(2, document.childNodes().size());
 }

 @Test
 public void testParseString() {
 Document document = xmlTreeBuilder.parse(inputXml, "baseUri");
 assertNotNull(document);
 assertNotNull(document.childNodes());
 assertEquals(2, document.childNodes().size());
 }

 @Test
 public void testInitialiseParse() {
 StringReader reader = new StringReader(inputXml);
 ParseErrorList errorList = ParseErrorList.noTracking();
 ParseSettings settings = ParseSettings.preserveCase;
 xmlTreeBuilder.initialiseParse(reader, "baseUri", errorList, settings);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test(expected = NullPointerException.class)
 public void testProcessNullToken() {
 xmlTreeBuilder.process(null);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root");
 xmlTreeBuilder.process(startTagToken);
 Element rootElement = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("root", rootElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 String rootName = "root";
 Token startTagToken = new Token.StartTag(rootName);
 Token endTagToken = new Token.EndTag(rootName);
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(endTagToken);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testPopStackToCloseUnknownTagWhenNotInStack() {
 Token endTagToken = new Token.EndTag("unknownTag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 }

 @Test
 public void testParseFragment() {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><child tag='test' /><child><grandChild/></child></root>", "baseUri", ParseErrorList.noTracking(), parseSettings);
 assertNotNull(fragmentNodes);
 assertFalse(fragmentNodes.isEmpty());
 assertEquals(2, fragmentNodes.size());
 }

 @Test(expected = ParseError.class)
 public void testParseInvalidInput() {
 ParseError error = assertThrows(ParseError.class, () -> xmlTreeBuilder.parse(new StringReader("invalidXml"), "baseUri"));
 assertTrue(error.toString().contains("invalidXml"));
 }

 @Test(expected = NullPointerException.class)
 public void testParseNullInput() {
 assertThrows(NullPointerException.class, () -> xmlTreeBuilder.parse(null, "baseUri"));
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.insert(commentToken);
 assertEquals("This is a comment", xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).data());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 assertEquals("A", xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1).data());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null);
 xmlTreeBuilder.insert(doctypeToken);
 DocumentType doctype = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("html", doctype.nodeName());
 }
}