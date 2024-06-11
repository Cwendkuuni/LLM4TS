
import org.junit.*;
import static org.junit.Assert.*;

public class SAXOutputterTest {

  @Test
  public void testConstructors() {
    // Test default constructor
    SAXOutputter outputter = new SAXOutputter();
    assertNotNull(outputter.getContentHandler());
    assertNotNull(outputter.getErrorHandler());
    assertNotNull(outputter.getDTDHandler());
    assertNotNull(outputter.getEntityResolver());
    assertNotNull(outputter.getLexicalHandler());
    assertNotNull(outputter.getDeclHandler());
    assertFalse(outputter.getReportNamespaceDeclarations());
    assertTrue(outputter.getReportDTDEvents());

    // Test constructor with content handler parameter
    ContentHandler contentHandler = new DefaultHandler();
    outputter = new SAXOutputter(contentHandler);
    assertEquals(contentHandler, outputter.getContentHandler());
    assertNull(outputter.getErrorHandler());
    assertNull(outputter.getDTDHandler());
    assertNull(outputter.getEntityResolver());
    assertNull(outputter.getLexicalHandler());
    assertNull(outputter.getDeclHandler());
    assertFalse(outputter.getReportNamespaceDeclarations());
    assertTrue(outputter.getReportDTDEvents());

    // Test constructor with full parameters
    ErrorHandler errorHandler = new DefaultHandler();
    DTDHandler dtdHandler = new DefaultHandler();
    EntityResolver entityResolver = new DefaultHandler();
    LexicalHandler lexicalHandler = new DefaultHandler();
    outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
    assertEquals(contentHandler, outputter.getContentHandler());
    assertEquals(errorHandler, outputter.getErrorHandler());
    assertEquals(dtdHandler, outputter.getDTDHandler());
    assertEquals(entityResolver, outputter.getEntityResolver());
    assertEquals(lexicalHandler, outputter.getLexicalHandler());
    assertNull(outputter.getDeclHandler());
    assertFalse(outputter.getReportNamespaceDeclarations());
    assertTrue(outputter.getReportDTDEvents());
  }

  @Test
  public void testGettersAndSetters() {
    SAXOutputter outputter = new SAXOutputter();
    ContentHandler contentHandler = new DefaultHandler();
    ErrorHandler errorHandler = new DefaultHandler();
    DTDHandler dtdHandler = new DefaultHandler();
    EntityResolver entityResolver = new DefaultHandler();
    LexicalHandler lexicalHandler = new DefaultHandler();
    DeclHandler declHandler = new DefaultHandler();

    outputter.setContentHandler(contentHandler);
    assertEquals(contentHandler, outputter.getContentHandler());

    outputter.setErrorHandler(errorHandler);
    assertEquals(errorHandler, outputter.getErrorHandler());

    outputter.setDTDHandler(dtdHandler);
    assertEquals(dtdHandler, outputter.getDTDHandler());

    outputter.setEntityResolver(entityResolver);
    assertEquals(entityResolver, outputter.getEntityResolver());

    outputter.setLexicalHandler(lexicalHandler);
    assertEquals(lexicalHandler, outputter.getLexicalHandler());

    outputter.setDeclHandler(declHandler);
    assertEquals(declHandler, outputter.getDeclHandler());

    outputter.setReportNamespaceDeclarations(true);
    assertTrue(outputter.getReportNamespaceDeclarations());

    outputter.setReportDTDEvents(false);
    assertFalse(outputter.getReportDTDEvents());
  }

  @Test(expected = SAXNotRecognizedException.class)
  public void testSetFeatureInvalid() throws SAXNotRecognizedException, SAXNotSupportedException {
    SAXOutputter outputter = new SAXOutputter();
    outputter.setFeature("invalid_feature", true);
  }

  @Test
  public void testSetFeatureNamespacePrefixes() throws SAXNotRecognizedException, SAXNotSupportedException {
    SAXOutputter outputter = new SAXOutputter();
    outputter.setFeature("http://xml.org/sax/features/namespace-prefixes", false);
    assertFalse(outputter.getReportNamespaceDeclarations());
  }

  @Test(expected = SAXNotSupportedException.class)
  public void testSetFeatureNamespaces() throws SAXNotRecognizedException, SAXNotSupportedException {
    SAXOutputter outputter = new SAXOutputter();
    outputter.setFeature("http://xml.org/sax/features/namespaces", false);
  }

  @Test
  public void testGetFeature() throws SAXNotRecognizedException, SAXNotSupportedException {
    SAXOutputter outputter = new SAXOutputter();
    assertTrue(outputter.getFeature("http://xml.org/sax/features/namespaces"));
    assertFalse(outputter.getFeature("http://xml.org/sax/features/namespace-prefixes"));
    assertTrue(outputter.getFeature("http://xml.org/sax/features/validation"));
  }

  @Test(expected = SAXNotRecognizedException.class)
  public void testGetPropertyInvalid() throws SAXNotRecognizedException, SAXNotSupportedException {
    SAXOutputter outputter = new SAXOutputter();
    outputter.getProperty("invalid_property");
  }

  @Test
  public void testGetPropertyLexicalHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
    SAXOutputter outputter = new SAXOutputter();
    assertNull(outputter.getProperty("http://xml.org/sax/properties/lexical-handler"));
  }

  @Test
  public void testGetPropertyDeclarationHandler() throws SAXNotRecognizedException, SAXNotSupportedException {
    SAXOutputter outputter = new SAXOutputter();
    assertNull(outputter.getProperty("http://xml.org/sax/properties/declaration-handler"));
  }

  @Test
  public void testGetSAXOutputProcessor() {
    SAXOutputter outputter = new SAXOutputter();
    assertNotNull(outputter.getSAXOutputProcessor());
  }

  @Test
  public void testSetSAXOutputProcessor() {
    SAXOutputter outputter = new SAXOutputter();
    SAXOutputProcessor processor = new DefaultSAXOutputProcessor();
    outputter.setSAXOutputProcessor(processor);
    assertEquals(processor, outputter.getSAXOutputProcessor());
  }

  @Test
  public void testGetFormat() {
    SAXOutputter outputter = new SAXOutputter();
    assertNotNull(outputter.getFormat());
  }

  @Test
  public void testSetFormat() {
    SAXOutputter outputter = new SAXOutputter();
    Format format = Format.getPrettyFormat();
    outputter.setFormat(format);
    assertEquals(format, outputter.getFormat());
  }

  @Test
  public void testBuildTarget() {
    SAXOutputter outputter = new SAXOutputter();
    Document doc = new Document(new Element("root"));
    SAXTarget target = outputter.buildTarget(doc);
    assertNotNull(target);
    assertEquals(outputter.getContentHandler(), target.getContentHandler());
    assertEquals(outputter.getErrorHandler(), target.getErrorHandler());
    assertEquals(outputter.getDTDHandler(), target.getDTDHandler());
    assertEquals(outputter.getEntityResolver(), target.getEntityResolver());
    assertEquals(outputter.getLexicalHandler(), target.getLexicalHandler());
    assertEquals(outputter.getDeclHandler(), target.getDeclHandler());
    assertFalse(target.getReportNamespaceDeclarations());
    assertTrue(target.getReportDTDEvents());
    assertNull(target.getPublicID());
    assertNull(target.getSystemID());
  }

  // Add more test cases for other methods if necessary

}

