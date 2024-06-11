import org.junit.Test;
import org.xml.sax.InputSource;

import static org.mockito.Mockito.*;

public class AbstractLoaderTest {
  
  @Test
  public void testParseInputSource() throws Exception {
    // Prepare
    AbstractLoader loader = new AbstractLoader() {
      @Override
      public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
    };
    
    InputSource inputSource = mock(InputSource.class);
    
    // Execute
    loader.parseInputSource(inputSource);
    
    // Verify
    // TODO: verification
  }
  
  @Test
  public void testResolveEntity() throws Exception {
    // Prepare
    AbstractLoader loader = new AbstractLoader() {
      @Override
      public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
    };
    
    String publicId = "public_id";
    String systemId = "system_id";
    
    // Execute
    loader.resolveEntity(publicId, systemId);
    
    // Verify
    // TODO: verification
  }
  
  @Test(expected = org.xml.sax.SAXException.class)
  public void testError() throws Exception {
    // Prepare
    AbstractLoader loader = new AbstractLoader() {
      @Override
      public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
    };
    
    SAXParseException exception = mock(SAXParseException.class);
    
    // Execute
    loader.error(exception);
    
    // Verify
    // TODO: verification
  }
  
  @Test(expected = org.xml.sax.SAXException.class)
  public void testFatalError() throws Exception {
    // Prepare
    AbstractLoader loader = new AbstractLoader() {
      @Override
      public void startElement(String uri, String localName, String qName, org.xml.sax.Attributes attributes) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void characters(char[] ch, int start, int length) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
      
      @Override
      public void endElement(String uri, String localName, String qName) throws org.xml.sax.SAXException {
        // TODO: implementation
      }
    };
    
    SAXParseException exception = mock(SAXParseException.class);
    
    // Execute
    loader.fatalError(exception);
    
    // Verify
    // TODO: verification
  }
  
}