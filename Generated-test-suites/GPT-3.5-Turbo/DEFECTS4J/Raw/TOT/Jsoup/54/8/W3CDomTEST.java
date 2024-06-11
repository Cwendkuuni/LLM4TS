import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    
    private W3CDom w3cDom;
    
    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testFromJsoup_withValidDocument_returnW3CDocument() {
        Document document = getJsoupDocument("valid_document.html");
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(document);
        assertNotNull(w3cDocument);
    }
    
    @Test(expected = NullPointerException.class)
    public void testFromJsoup_withNullDocument_throwNullPointerException() {
        w3cDom.fromJsoup(null);
    }
    
    @Test
    public void testConvert_withValidDocuments_returnConvertedW3CDocument() {
        Document document = getJsoupDocument("valid_document.html");
        org.w3c.dom.Document w3cDocument = getW3CDocument("valid_w3c_document.xml");
        
        w3cDom.convert(document, w3cDocument);
        
        // Assert the conversion is successful
        assertNotNull(w3cDocument);
        
        // Assert the root element of the converted document
        Element rootElement = w3cDocument.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
    }
    
    @Test
    public void testConvert_withDocumentWithNamespacePrefix_returnConvertedW3CDocumentWithNamespace() {
        Document document = getJsoupDocument("document_with_namespace_prefix.html");
        org.w3c.dom.Document w3cDocument = getW3CDocument("valid_w3c_document.xml");
        
        w3cDom.convert(document, w3cDocument);
        
        // Assert the conversion is successful
        assertNotNull(w3cDocument);
        
        // Assert the root element of the converted document
        Element rootElement = w3cDocument.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("ns1:html", rootElement.getTagName());
        assertEquals("http://www.example.com/ns1", rootElement.getNamespaceURI());
    }
    
    @Test
    public void testConvert_withDocumentWithInvalidNamespacePrefix_returnConvertedW3CDocumentWithDefaultNamespace() {
        Document document = getJsoupDocument("document_with_invalid_namespace_prefix.html");
        org.w3c.dom.Document w3cDocument = getW3CDocument("valid_w3c_document.xml");
        
        w3cDom.convert(document, w3cDocument);
        
        // Assert the conversion is successful
        assertNotNull(w3cDocument);
        
        // Assert the root element of the converted document
        Element rootElement = w3cDocument.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
        assertEquals("", rootElement.getNamespaceURI());
    }
    
    @Test
    public void testConvert_withDocumentWithNamespaces_returnConvertedW3CDocumentWithNamespaces() {
        Document document = getJsoupDocument("document_with_namespaces.html");
        org.w3c.dom.Document w3cDocument = getW3CDocument("valid_w3c_document.xml");
        
        w3cDom.convert(document, w3cDocument);
        
        // Assert the conversion is successful
        assertNotNull(w3cDocument);
        
        // Assert the root element of the converted document
        Element rootElement = w3cDocument.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", rootElement.getNamespaceURI());
        
        // Assert the child elements with namespaces
        Element headElement = (Element) rootElement.getFirstChild();
        assertNotNull(headElement);
        assertEquals("head", headElement.getTagName());
        assertEquals("http://www.w3.org/1999/xhtml", headElement.getNamespaceURI());
        
        Element bodyElement = (Element) rootElement.getLastChild();
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.getTagName());
        assertEquals("http://www.example.com/ns1", bodyElement.getNamespaceURI());
        
        // Assert the text nodes within the elements
        Text headText = (Text) headElement.getFirstChild();
        assertNotNull(headText);
        assertEquals("This is the head element.", headText.getWholeText().trim());
        
        Text bodyText = (Text) bodyElement.getFirstChild();
        assertNotNull(bodyText);
        assertEquals("This is the body element.", bodyText.getWholeText().trim());
    }
    
    @Test(expected = IllegalStateException.class)
    public void testConvert_withParserConfigurationException_throwIllegalStateException() {
        DocumentBuilderMock documentBuilderMock = new DocumentBuilderMock();
        w3cDom.factory = documentBuilderMock;
        
        Document document = getJsoupDocument("valid_document.html");
        org.w3c.dom.Document w3cDocument = getW3CDocument("valid_w3c_document.xml");
        
        w3cDom.convert(document, w3cDocument);
    }
    
    @Test
    public void testAsString_withValidDocument_returnSerializedDocumentAsString() {
        org.w3c.dom.Document document = getW3CDocument("valid_w3c_document.xml");
        String serializedDocument = w3cDom.asString(document);
        String expectedSerialization = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html></html>";
        
        assertEquals(expectedSerialization, serializedDocument);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAsString_withTransformerException_throwIllegalStateException() {
        TransformerFactoryMock transformerFactoryMock = new TransformerFactoryMock();
        TransformerMock transformerMock = new TransformerMock();
        transformerFactoryMock.transformer = transformerMock;
        
        org.w3c.dom.Document document = getW3CDocument("valid_w3c_document.xml");
        w3cDom.asString(document);
    }
    
    private Document getJsoupDocument(String fileName) {
        try {
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test_files/" + fileName);
            return Parser.parse(inputStream, "UTF-8", "");
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private org.w3c.dom.Document getW3CDocument(String fileName) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream("test_files/" + fileName);
            return builder.parse(inputStream);
        } catch (IOException | ParserConfigurationException | org.xml.sax.SAXException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private class DocumentBuilderMock extends DocumentBuilder {
        @Override
        public DOMImplementation getDOMImplementation() {
            return null;
        }
        
        @Override
        public boolean isNamespaceAware() {
            return false;
        }
        
        @Override
        public boolean isValidating() {
            return false;
        }
        
        @Override
        public org.w3c.dom.Document newDocument() {
            return null;
        }
        
        @Override
        public org.w3c.dom.Document parse(InputStream is) throws org.xml.sax.SAXException, IOException {
            throw new org.xml.sax.SAXParseException("Test Exception", null);
        }
    }
    
    private class TransformerFactoryMock extends TransformerFactory {
        public Transformer transformer;
        
        @Override
        public Transformer newTransformer() throws TransformerConfigurationException {
            return transformer;
        }
        
        @Override
        public Transformer newTransformer(Source source) throws TransformerConfigurationException {
            return null;
        }
    }
    
    private class TransformerMock extends Transformer {
        @Override
        public void transform(Source source, Result result) throws TransformerException {
            throw new TransformerException("Test Exception");
        }
        
        @Override
        public void setParameter(String name, Object value) {}
        
        @Override
        public Object getParameter(String name) {
            return null;
        }
        
        @Override
        public void clearParameters() {}
        
        @Override
        public void setURIResolver(URIResolver resolver) {}
        
        @Override
        public URIResolver getURIResolver() {
            return null;
        }
        
        @Override
        public void setOutputProperties(Properties oformat) throws IllegalArgumentException {}
        
        @Override
        public Properties getOutputProperties() {
            return null;
        }
        
        @Override
        public void setOutputProperty(String name, String value) throws IllegalArgumentException {}
        
        @Override
        public String getOutputProperty(String name) throws IllegalArgumentException {
            return null;
        }
        
        @Override
        public void setErrorListener(ErrorListener listener) throws IllegalArgumentException {}
        
        @Override
        public ErrorListener getErrorListener() {
            return null;
        }
        
        @Override
        public Object getContentHandler() {
            return null;
        }
        
        @Override
        public Object getDTDHandler() {
            return null;
        }
        
        @Override
        public Object getEntityResolver() {
            return null;
        }
        
        @Override
        public Object getErrorHandler() {
            return null;
        }
        
        @Override
        public void parse(Source input) throws IOException, org.xml.sax.SAXException {}
        
        @Override
        public void parse(String systemId) throws IOException, org.xml.sax.SAXException {}
    }
}