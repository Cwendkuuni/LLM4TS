import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.output.support.SAXOutputter;
import java.util.ArrayList;
import java.util.List;

public class SAXOutputterTest {
    
    @Test
    public void testConstructor1() {
        SAXOutputter outputter = new SAXOutputter();
        assertNotNull(outputter);
    }
    
    @Test
    public void testConstructor2() {
        SAXOutputter outputter = new SAXOutputter(null);
        assertNotNull(outputter);
    }
    
    @Test
    public void testSetContentHandler() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setContentHandler(null);
        assertNull(outputter.getContentHandler());
    }
    
    @Test
    public void testSetErrorHandler() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setErrorHandler(null);
        assertNull(outputter.getErrorHandler());
    }
    
    @Test
    public void testSetDTDHandler() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setDTDHandler(null);
        assertNull(outputter.getDTDHandler());
    }
    
    @Test
    public void testSetEntityResolver() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setEntityResolver(null);
        assertNull(outputter.getEntityResolver());
    }
    
    @Test
    public void testSetLexicalHandler() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setLexicalHandler(null);
        assertNull(outputter.getLexicalHandler());
    }
    
    @Test
    public void testSetDeclHandler() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setDeclHandler(null);
        assertNull(outputter.getDeclHandler());
    }
    
    @Test
    public void testSetReportNamespaceDeclarations() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportNamespaceDeclarations(true);
        assertTrue(outputter.getReportNamespaceDeclarations());
    }
    
    @Test
    public void testSetReportDTDEvents() {
        SAXOutputter outputter = new SAXOutputter();
        outputter.setReportDTDEvents(true);
        assertTrue(outputter.getReportDTDEvents());
    }
    
    @Test(expected = JDOMException.class)
    public void testOutputDocumentException() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        outputter.output((Document)null);
    }
    
    @Test
    public void testOutputNodes() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        List<Element> nodes = new ArrayList<>();
        Element element = new Element("test");
        nodes.add(element);
        outputter.output(nodes);
    }
    
    @Test
    public void testOutputNode() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        Element element = new Element("test");
        outputter.output(element);
    }
    
    @Test
    public void testOutputFragmentNodes() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        List<Element> nodes = new ArrayList<>();
        Element element = new Element("test");
        nodes.add(element);
        outputter.outputFragment(nodes);
    }
    
    @Test
    public void testOutputFragmentNode() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        Element element = new Element("test");
        outputter.outputFragment(element);
    }
    
    @Test(expected = JDOMException.class)
    public void testOutputFragmentNodeException() throws JDOMException {
        SAXOutputter outputter = new SAXOutputter();
        outputter.outputFragment(null);
    }
    
    @Test
    public void testHandleError() {
        SAXOutputter outputter = new SAXOutputter();
        try {
            outputter.handleError(new JDOMException("Test"));
        } catch (JDOMException e) {
            assertEquals("Test", e.getMessage());
        }
    }
}