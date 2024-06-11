import static org.junit.Assert.*;
import org.junit.*;
import java.io.*;
import twitter4j.*;
import twitter4j.auth.*;

public class TwitterBaseImplTest {
  
  private static final Configuration conf = new ConfigurationBuilder().setOAuthConsumerKey("consumerKey").setOAuthConsumerSecret("consumerSecret").build();
  private static Authorization auth = new BasicAuthorization("userId", "password");
  private TwitterBaseImpl twitterBase;
  
  @Before
  public void setUp() {
    twitterBase = new TwitterBaseImpl(conf, auth) {};
  }
  
  @Test
  public void testGetScreenName_WhenAuthIsEnabled_ReturnScreenName() throws TwitterException, IllegalStateException {
    String expectedScreenName = "screenName";
    twitterBase.screenName = expectedScreenName;
    
    String actualScreenName = twitterBase.getScreenName();
    
    assertEquals(expectedScreenName, actualScreenName);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetScreenName_WhenAuthIsNotEnabled_ThrowsIllegalStateException() throws TwitterException, IllegalStateException {
    twitterBase.auth = NullAuthorization.getInstance();
    
    twitterBase.getScreenName();
  }
  
  @Test
  public void testGetId_WhenAuthIsEnabled_ReturnId() throws TwitterException, IllegalStateException {
    long expectedId = 12345L;
    twitterBase.id = expectedId;
    
    long actualId = twitterBase.getId();
    
    assertEquals(expectedId, actualId);
  }
  
  @Test(expected = IllegalStateException.class)
  public void testGetId_WhenAuthIsNotEnabled_ThrowsIllegalStateException() throws TwitterException, IllegalStateException {
    twitterBase.auth = NullAuthorization.getInstance();
    
    twitterBase.getId();
  }
  
  @Test
  public void testFillInIDAndScreenName_WithParameters_ReturnUser() throws TwitterException {
    User expectedUser = new UserJSONImpl();
    HttpParameter[] parameters = new HttpParameter[1];
    twitterBase.http = new HttpClient() {
      public HttpResponse get(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return new HttpResponse(null, expectedUser.toString(), null, null);
      }
    };
    
    User actualUser = twitterBase.fillInIDAndScreenName(parameters);
    
    assertEquals(expectedUser, actualUser);
  }
  
  @Test
  public void testFillInIDAndScreenName_WithoutParameters_ReturnUser() throws TwitterException {
    User expectedUser = new UserJSONImpl();
    twitterBase.http = new HttpClient() {
      public HttpResponse get(String url, HttpParameter[] parameters, Authorization authorization) throws TwitterException {
        return new HttpResponse(null, expectedUser.toString(), null, null);
      }
    };
    
    User actualUser = twitterBase.fillInIDAndScreenName();
    
    assertEquals(expectedUser, actualUser);
  }
  
  // Other test methods for the remaining methods in TwitterBaseImpl
  
  @After
  public void tearDown() {
    twitterBase = null;
  }
}