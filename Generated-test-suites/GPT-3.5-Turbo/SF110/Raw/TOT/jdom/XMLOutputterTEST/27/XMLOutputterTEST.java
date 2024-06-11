import org.junit.*;
import static org.junit.Assert.*;
import org.jdom2.*;
import org.jdom2.output.*;
import java.io.*;

public class XMLOutputterTest {
  
  private XMLOutputter xmlo;
  private Document document;
  private StringWriter stringWriter;
  
  @Before
  public void setUp() throws Exception {
    xmlo = new XMLOutputter();
    document = new Document();
    stringWriter = new StringWriter();
  }
  
  @Test
  public void testSetFormat() {
    Format format = Format.getPrettyFormat();
    xmlo.setFormat(format);
    assertEquals(format, xmlo.getFormat());
  }
  
  @Test
  public void testXMLOutputProcessor() {
    XMLOutputProcessor processor = new DefaultXMLOutputProcessor();
    xmlo.setXMLOutputProcessor(processor);
    assertEquals(processor, xmlo.getXMLOutputProcessor());
  }
  
  @Test
  public void testOutputDocument() throws IOException {
    Element root = new Element("root");
    document.setRootElement(root);
    xmlo.output(document, stringWriter);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator() + "<root />", stringWriter.toString());
  }
  
  @Test
  public void testOutputDocType() throws IOException {
    DocType doctype = new DocType("root");
    xmlo.output(doctype, stringWriter);
    assertEquals("<!DOCTYPE root>", stringWriter.toString());
  }
  
  @Test
  public void testOutputElement() throws IOException {
    Element element = new Element("name");
    xmlo.output(element, stringWriter);
    assertEquals("<name />", stringWriter.toString());
  }
  
  @Test
  public void testOutputElementContent() throws IOException {
    Element element = new Element("name");
    element.addContent(new Text("John"));
    xmlo.outputElementContent(element, stringWriter);
    assertEquals("John", stringWriter.toString());
  }
  
  @Test
  public void testOutputList() throws IOException {
    Element element1 = new Element("name");
    Element element2 = new Element("age");
    java.util.List<Content> list = new java.util.ArrayList<Content>();
    list.add(element1);
    list.add(element2);
    xmlo.output(list, stringWriter);
    assertEquals("<name /><age />", stringWriter.toString());
  }
  
  @Test
  public void testOutputCDATA() throws IOException {
    CDATA cdata = new CDATA("cdata");
    xmlo.output(cdata, stringWriter);
    assertEquals("<![CDATA[cdata]]>", stringWriter.toString());
  }
  
  @Test
  public void testOutputText() throws IOException {
    Text text = new Text("text");
    xmlo.output(text, stringWriter);
    assertEquals("text", stringWriter.toString());
  }
  
  @Test
  public void testOutputComment() throws IOException {
    Comment comment = new Comment("comment");
    xmlo.output(comment, stringWriter);
    assertEquals("<!--comment-->", stringWriter.toString());
  }
  
  @Test
  public void testOutputProcessingInstruction() throws IOException {
    ProcessingInstruction pi = new ProcessingInstruction("target", "data");
    xmlo.output(pi, stringWriter);
    assertEquals("<?target data?>", stringWriter.toString());
  }
  
  @Test
  public void testOutputEntityRef() throws IOException {
    EntityRef entity = new EntityRef("entity");
    xmlo.output(entity, stringWriter);
    assertEquals("&entity;", stringWriter.toString());
  }
  
  @Test
  public void testOutputStringDocument() {
    Element root = new Element("root");
    document.setRootElement(root);
    String output = xmlo.outputString(document);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator() + "<root />", output);
  }
  
  @Test
  public void testOutputStringDocType() {
    DocType doctype = new DocType("root");
    String output = xmlo.outputString(doctype);
    assertEquals("<!DOCTYPE root>", output);
  }
  
  @Test
  public void testOutputStringElement() {
    Element element = new Element("name");
    String output = xmlo.outputString(element);
    assertEquals("<name />", output);
  }
  
  @Test
  public void testOutputStringList() {
    Element element1 = new Element("name");
    Element element2 = new Element("age");
    java.util.List<Content> list = new java.util.ArrayList<Content>();
    list.add(element1);
    list.add(element2);
    String output = xmlo.outputString(list);
    assertEquals("<name /><age />", output);
  }
  
  @Test
  public void testOutputStringCDATA() {
    CDATA cdata = new CDATA("cdata");
    String output = xmlo.outputString(cdata);
    assertEquals("<![CDATA[cdata]]>", output);
  }
  
  @Test
  public void testOutputStringText() {
    Text text = new Text("text");
    String output = xmlo.outputString(text);
    assertEquals("text", output);
  }
  
  @Test
  public void testOutputStringComment() {
    Comment comment = new Comment("comment");
    String output = xmlo.outputString(comment);
    assertEquals("<!--comment-->", output);
  }
  
  @Test
  public void testOutputStringProcessingInstruction() {
    ProcessingInstruction pi = new ProcessingInstruction("target", "data");
    String output = xmlo.outputString(pi);
    assertEquals("<?target data?>", output);
  }
  
  @Test
  public void testOutputStringEntityRef() {
    EntityRef entity = new EntityRef("entity");
    String output = xmlo.outputString(entity);
    assertEquals("&entity;", output);
  }
  
  @Test
  public void testOutputStringElementContent() {
    Element element = new Element("name");
    element.addContent(new Text("John"));
    String output = xmlo.outputElementContentString(element);
    assertEquals("John", output);
  }
  
  @Test
  public void testEscapeAttributeEntities() {
    String str = "abc\"<>&";
    String result = xmlo.escapeAttributeEntities(str);
    assertEquals("abc&quot;&lt;&gt;&amp;", result);
  }
  
  @Test
  public void testEscapeElementEntities() {
    String str = "abc\"<>&";
    String result = xmlo.escapeElementEntities(str);
    assertEquals("abc&quot;&lt;&gt;&amp;", result);
  }
  
  @Test
  public void testClone() {
    XMLOutputter cloneObj = xmlo.clone();
    assertEquals(xmlo.getFormat(), cloneObj.getFormat());
    assertEquals(xmlo.getXMLOutputProcessor(), cloneObj.getXMLOutputProcessor());
  }
  
  @Test
  public void testToString() {
    String expected = "XMLOutputter[omitDeclaration = false, encoding = UTF-8, omitEncoding = false, indent = '  ', expandEmptyElements = true, lineSeparator = '\\n', textMode = PRESERVE]";
    String result = xmlo.toString();
    assertEquals(expected, result);
  }
  
  @After
  public void tearDown() throws Exception {
    xmlo = null;
    document = null;
    stringWriter = null;
  }
}