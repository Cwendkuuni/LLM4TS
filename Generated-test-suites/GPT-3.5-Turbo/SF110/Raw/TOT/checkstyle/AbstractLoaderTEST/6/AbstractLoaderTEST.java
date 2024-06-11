import org.junit.Before;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

public class AbstractLoaderTest {

  private AbstractLoader abstractLoader;

  @Before
  public void setup() throws SAXException, ParserConfigurationException {
    abstractLoader = spy(AbstractLoader.class);
  }

  @Test
  public void testParseInputSource_Success() throws IOException, SAXException {
    // Arrange
    InputSource inputSource = mock(InputSource.class);

    // Act
    abstractLoader.parseInputSource(inputSource);

    // Assert
    verify(abstractLoader.parser).parse(inputSource);
  }

  @Test(expected = IOException.class)
  public void testParseInputSource_IOException() throws IOException, SAXException {
    // Arrange
    InputSource inputSource = mock(InputSource.class);
    doThrow(new IOException()).when(abstractLoader.parser).parse(inputSource);

    // Act
    abstractLoader.parseInputSource(inputSource);
  }

  @Test(expected = SAXException.class)
  public void testParseInputSource_SAXException() throws IOException, SAXException {
    // Arrange
    InputSource inputSource = mock(InputSource.class);
    doThrow(new SAXException()).when(abstractLoader.parser).parse(inputSource);

    // Act
    abstractLoader.parseInputSource(inputSource);
  }

  @Test
  public void testResolveEntity_ExistingPublicId() throws IOException, SAXException {
    // Arrange
    String publicId = "somePublicId";
    String systemId = "someSystemId";
    Map<String, String> publicIdToResourceNameMap = new HashMap<>();
    publicIdToResourceNameMap.put(publicId, "someResourceName");
    abstractLoader.publicIdToResourceNameMap = publicIdToResourceNameMap;

    // Act
    InputSource result = abstractLoader.resolveEntity(publicId, systemId);

    // Assert
    assertThat(result).isNotNull();
  }

  @Test
  public void testResolveEntity_NonExistingPublicId() throws IOException, SAXException {
    // Arrange
    String publicId = "nonExistingPublicId";
    String systemId = "someSystemId";

    // Act
    InputSource result = abstractLoader.resolveEntity(publicId, systemId);

    // Assert
    assertThat(result).isNull();
    verify(abstractLoader, never()).getClass();
  }

  @Test
  public void testError() {
    // Arrange
    SAXParseException exception = mock(SAXParseException.class);

    // Act & Assert
    assertThatThrownBy(() -> abstractLoader.error(exception)).isSameAs(exception);
  }

  @Test
  public void testFatalError() {
    // Arrange
    SAXParseException exception = mock(SAXParseException.class);

    // Act & Assert
    assertThatThrownBy(() -> abstractLoader.fatalError(exception)).isSameAs(exception);
  }

}