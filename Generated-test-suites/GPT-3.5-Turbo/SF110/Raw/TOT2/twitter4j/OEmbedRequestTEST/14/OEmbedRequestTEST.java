import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OEmbedRequestTest {

  private OEmbedRequest oEmbedRequest;

  @Before
  public void setUp() {
    long statusId = 123456789;
    String url = "https://example.com";
    oEmbedRequest = new OEmbedRequest(statusId, url);
  }

  @Test
  public void testSetMaxWidth() {
    int maxWidth = 300;
    oEmbedRequest.setMaxWidth(maxWidth);
    assertEquals(maxWidth, oEmbedRequest.maxWidth);
  }

  @Test
  public void testMaxWidth() {
    int maxWidth = 500;
    OEmbedRequest newRequest = oEmbedRequest.MaxWidth(maxWidth);
    assertEquals(maxWidth, newRequest.maxWidth);
  }

  @Test
  public void testSetHideMedia() {
    boolean hideMedia = true;
    oEmbedRequest.setHideMedia(hideMedia);
    assertEquals(hideMedia, oEmbedRequest.hideMedia);
  }

  @Test
  public void testHideMedia() {
    boolean hideMedia = false;
    OEmbedRequest newRequest = oEmbedRequest.HideMedia(hideMedia);
    assertEquals(hideMedia, newRequest.hideMedia);
  }

  @Test
  public void testSetHideThread() {
    boolean hideThread = true;
    oEmbedRequest.setHideThread(hideThread);
    assertEquals(hideThread, oEmbedRequest.hideThread);
  }

  @Test
  public void testHideThread() {
    boolean hideThread = false;
    OEmbedRequest newRequest = oEmbedRequest.HideThread(hideThread);
    assertEquals(hideThread, newRequest.hideThread);
  }

  // More test methods can be added here to cover all public methods of the OEmbedRequest class

  // Edge case test
  @Test
  public void testAppendParameterWithNameAndValue() {
    // Test when value is null
    List<HttpParameter> params = new ArrayList<>();
    oEmbedRequest.appendParameter("test", null, params);
    assertEquals(0, params.size());
  }

  // Exception handling test
  @Test(expected = IllegalArgumentException.class)
  public void testAppendParameterWithNegativeValue() {
    List<HttpParameter> params = new ArrayList<>();
    oEmbedRequest.appendParameter("test", -1L, params);
  }

}