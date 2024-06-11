import static org.junit.Assert.*;
import org.junit.Test;

public class SAXOutputterTest {
    
    @Test
    public void testConstructorNoArgs() {
        SAXOutputter outputter = new SAXOutputter();
        
        assertNotNull(outputter.getContentHandler());
        assertNull(outputter.getErrorHandler());
        assertNull(outputter.getDTDHandler());
        assertNull(outputter.getEntityResolver());
        assertNull(outputter.getLexicalHandler());
    }
    
    @Test
    public void testConstructorAllArgs() {
        ContentHandler contentHandler = new DefaultHandler();
        ErrorHandler errorHandler = new DefaultHandler();
        DTDHandler dtdHandler = new DefaultHandler();
        EntityResolver entityResolver = new DefaultHandler();
        LexicalHandler lexicalHandler = new DefaultHandler();
        
        SAXOutputter outputter = new SAXOutputter(contentHandler, errorHandler, dtdHandler, entityResolver, lexicalHandler);
        
        assertEquals(contentHandler, outputter.getContentHandler());
        assertEquals(errorHandler, outputter.getErrorHandler());
        assertEquals(dtdHandler, outputter.getDTDHandler());
        assertEquals(entityResolver, outputter.getEntityResolver());
        assertEquals(lexicalHandler, outputter.getLexicalHandler());
    }
    
    // Add more test cases for other methods
    
}