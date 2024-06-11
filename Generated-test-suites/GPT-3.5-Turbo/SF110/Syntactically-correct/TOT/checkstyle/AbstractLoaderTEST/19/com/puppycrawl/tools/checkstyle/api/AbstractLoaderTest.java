import org.junit.*;
import org.mockito.*;
import org.xml.sax.*;

public class AbstractLoaderTest {
    
    @Test
    public void testParseInputSource_SuccessfulParsing() throws Exception {
        // Arrange
        AbstractLoader loader = Mockito.mock(AbstractLoader.class);
        InputSource inputSource = Mockito.mock(InputSource.class);
        
        // Act
        loader.parseInputSource(inputSource);
        
        // Assert
        Mockito.verify(loader.parser).parse(inputSource);
    }
    
    @Test(expected = SAXException.class)
    public void testParseInputSource_InvalidXML() throws Exception {
        // Arrange
        AbstractLoader loader = Mockito.mock(AbstractLoader.class);
        InputSource inputSource = Mockito.mock(InputSource.class);
        Mockito.doThrow(new SAXException()).when(loader.parser).parse(inputSource);
        
        // Act
        loader.parseInputSource(inputSource);
        
        // Assert
        // Expects SAXException to be thrown
    }
    
    @Test
    public void testResolveEntity_ExistingPublicId() throws IOException, SAXException {
        // Arrange
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>() {{
            put("publicId1", "resourceName1.dtd");
            put("publicId2", "resourceName2.dtd");
        }});
        String publicId = "publicId1";
        String systemId = "systemId";
        
        // Act
        InputSource inputSource = loader.resolveEntity(publicId, systemId);
        InputStream inputStream = inputSource.getByteStream();
        
        // Assert
        Assert.assertNotNull(inputStream);
        Assert.assertEquals(loader.getClass().getClassLoader().getResourceAsStream("resourceName1.dtd"), inputStream);
    }
    
    @Test
    public void testResolveEntity_NonExistingPublicId() throws IOException, SAXException {
        // Arrange
        AbstractLoader loader = new AbstractLoader(new HashMap<String, String>() {{
            put("publicId1", "resourceName1.dtd");
            put("publicId2", "resourceName2.dtd");
        }});
        String publicId = "invalidPublicId";
        String systemId = "systemId";
        
        // Act
        InputSource inputSource = loader.resolveEntity(publicId, systemId);
        InputStream inputStream = inputSource.getByteStream();
        
        // Assert
        Assert.assertNull(inputStream);
    }
    
    @Test
    public void testError_ThrowsSAXException() throws SAXException {
        // Arrange
        AbstractLoader loader = Mockito.mock(AbstractLoader.class);
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        
        // Act & Assert
        Assert.assertThrows(SAXException.class, () -> loader.error(exception));
    }
    
    @Test
    public void testFatalError_ThrowsSAXException() throws SAXException {
        // Arrange
        AbstractLoader loader = Mockito.mock(AbstractLoader.class);
        SAXParseException exception = Mockito.mock(SAXParseException.class);
        
        // Act & Assert
        Assert.assertThrows(SAXException.class, () -> loader.fatalError(exception));
    }
}