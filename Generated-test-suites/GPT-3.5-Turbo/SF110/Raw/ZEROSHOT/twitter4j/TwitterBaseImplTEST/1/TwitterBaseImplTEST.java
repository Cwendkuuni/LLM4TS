import org.junit.*;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {

  @Test
  public void testGetScreenName() throws TwitterException, IllegalStateException {
    Configuration conf = createMock(Configuration.class);
    Authorization auth = createMock(Authorization.class);
    TwitterBaseImpl twitter = new TwitterBaseImpl(conf, auth);
    
    try {
      twitter.getScreenName();
      fail("Expected IllegalStateException");
    } catch (IllegalStateException e) {
      // Expected exception
    }
    
    String screenName = "testUser";
    BasicAuthorization basicAuth = createMock(BasicAuthorization.class);
    expect(auth.isEnabled()).andReturn(true);
    expect(auth instanceof BasicAuthorization).andReturn(true);
    expect(auth).andReturn(basicAuth);
    expect(basicAuth.getUserId()).andReturn(screenName);
    replay(conf, auth, basicAuth);
    
    assertEquals(screenName, twitter.getScreenName());
    
    verify(conf, auth, basicAuth);
  }
  
  // Add more test methods for other methods in TwitterBaseImpl class
  
}