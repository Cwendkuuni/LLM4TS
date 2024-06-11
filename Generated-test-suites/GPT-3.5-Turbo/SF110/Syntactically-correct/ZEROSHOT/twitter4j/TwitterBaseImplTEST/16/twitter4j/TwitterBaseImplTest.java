import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TwitterBaseImplTest {
  
  private TwitterBaseImpl twitter;
  private Configuration conf;
  private Authorization auth;
  
  @Before
  public void setUp() {
    conf = new ConfigurationBuilder()
            .setDebugEnabled(true)
            .setOAuthConsumerKey("consumerKey")
            .setOAuthConsumerSecret("consumerSecret")
            .setOAuthAccessToken("accessToken")
            .setOAuthAccessTokenSecret("accessTokenSecret")
            .build();
    auth = new OAuthAuthorization(conf);
    twitter = new TwitterBaseImpl(conf, auth) {
      // Implement abstract methods from TwitterBase
    };
  }
  
  @Test
  public void testGetScreenName() throws TwitterException, IllegalStateException {
    String screenName = twitter.getScreenName();
    assertNotNull(screenName);
  }
  
  @Test
  public void testGetId() throws TwitterException, IllegalStateException {
    long id = twitter.getId();
    assertNotEquals(0, id);
  }
  
  @Test
  public void testFillInIDAndScreenName() throws TwitterException {
    User user = twitter.fillInIDAndScreenName();
    assertNotNull(user);
  }
  
  @Test
  public void testAddRateLimitStatusListener() {
    RateLimitStatusListener listener = new RateLimitStatusListener() {
      public void onRateLimitStatus(RateLimitStatusEvent event) {}
      public void onRateLimitReached(RateLimitStatusEvent event) {}
    };
    twitter.addRateLimitStatusListener(listener);
    assertEquals(1, twitter.rateLimitStatusListeners.size());
  }
  
  @Test
  public void testOnRateLimitStatus() {
    twitter.onRateLimitStatus(event -> {});
    assertEquals(1, twitter.rateLimitStatusListeners.size());
  }
  
  @Test
  public void testOnRateLimitReached() {
    twitter.onRateLimitReached(event -> {});
    assertEquals(1, twitter.rateLimitStatusListeners.size());
  }
  
  @Test
  public void testHttpResponseReceived() {
    HttpResponseEvent event = new HttpResponseEvent(new HttpResponse(), null);
    twitter.httpResponseReceived(event);
    assertEquals(0, twitter.rateLimitStatusListeners.size());
  }
  
  @Test
  public void testGetAuthorization() {
    assertEquals(auth, twitter.getAuthorization());
  }
  
  @Test
  public void testGetConfiguration() {
    assertEquals(conf, twitter.getConfiguration());
  }
  
  @Test
  public void testEnsureAuthorizationEnabled() {
    twitter.ensureAuthorizationEnabled();
    assertTrue(auth.isEnabled());
  }
  
  @Test
  public void testEnsureOAuthEnabled() {
    twitter.ensureOAuthEnabled();
    assertTrue(auth instanceof OAuthAuthorization);
  }
}