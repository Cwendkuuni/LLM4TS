import org.junit.*;
import static org.junit.Assert.*;
import org.scribe.model.*;
import java.io.*;
import java.net.*;
import java.util.*;

public class ResponseTest {

  private HttpURLConnection mockConnection;
  private InputStream mockInputStream;
  private final Map<String, List<String>> mockHeaderFields = new HashMap<>();

  @Before
  public void setUp() throws IOException {
    // Mock HttpURLConnection
    URL mockUrl = new URL("http://www.example.com");
    this.mockConnection = (HttpURLConnection) mockUrl.openConnection();
    this.mockInputStream = new ByteArrayInputStream("Mock Response Body".getBytes());
    this.mockHeaderFields.put("Content-Type", Arrays.asList("text/plain"));
    this.mockHeaderFields.put("Content-Length", Arrays.asList("15"));
    this.mockConnection.setResponseCode(200);
    this.mockConnection.setHeaderFields(this.mockHeaderFields);
    this.mockConnection.setDoInput(true);

    // Create Response object using the mock HttpURLConnection
    Response response = new Response(this.mockConnection);
  }

  @After
  public void tearDown() {
    // Clean up resources
    if (this.mockConnection != null) {
      this.mockConnection.disconnect();
    }
    if (this.mockInputStream != null) {
      try {
        this.mockInputStream.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Test
  public void testGetBody_DefaultBody_ReturnsResponseBody() {
    // Arrange
    String expectedBody = "Mock Response Body";

    // Act
    String actualBody = response.getBody();

    // Assert
    assertEquals(expectedBody, actualBody);
  }

  @Test
  public void testGetBody_NullBody_ReturnsParsedResponseBody() {
    // Arrange
    response = new Response(this.mockConnection);
    response.getStream().close();
    response = null;
    String expectedBody = "Mock Response Body";

    // Act
    String actualBody = response.getBody();

    // Assert
    assertEquals(expectedBody, actualBody);
  }

  @Test
  public void testGetStream_ReturnsInputStream() {
    // Act
    InputStream actualStream = response.getStream();

    // Assert
    assertNotNull(actualStream);
  }

  @Test
  public void testGetCode_ReturnsResponseCode() {
    // Arrange
    int expectedCode = 200;

    // Act
    int actualCode = response.getCode();

    // Assert
    assertEquals(expectedCode, actualCode);
  }

  @Test
  public void testGetHeaders_ReturnsResponseHeaders() {
    // Arrange
    Map<String, String> expectedHeaders = new HashMap<>();
    expectedHeaders.put("Content-Type", "text/plain");
    expectedHeaders.put("Content-Length", "15");

    // Act
    Map<String, String> actualHeaders = response.getHeaders();

    // Assert
    assertEquals(expectedHeaders, actualHeaders);
  }

  @Test
  public void testGetHeader_ValidHeaderName_ReturnsHeaderValue() {
    // Arrange
    String headerName = "Content-Type";
    String expectedValue = "text/plain";

    // Act
    String actualValue = response.getHeader(headerName);

    // Assert
    assertEquals(expectedValue, actualValue);
  }

  @Test
  public void testGetHeader_InvalidHeaderName_ReturnsNull() {
    // Arrange
    String headerName = "Invalid-Header";

    // Act
    String actualValue = response.getHeader(headerName);

    // Assert
    assertNull(actualValue);
  }

}