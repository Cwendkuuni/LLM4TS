import org.junit.*;
import static org.junit.Assert.*;
import twitter4j.conf.*;
import twitter4j.auth.*;

public class TwitterBaseImplTest {

  @BeforeClass
  public static void setUpClass() {
    // code executed before the first test method
  }

  @AfterClass
  public static void tearDownClass() {
    // code executed after the last test method
  }

  @Before
  public void setUp() {
    // code executed before each test method
  }

  @After
  public void tearDown() {
    // code executed after each test method
  }

  @Test
  public void testGetScreenName_WhenAuthEnabled() throws TwitterException, IllegalStateException {
    // Arrange
    Configuration conf = new ConfigurationBuilder()
                     .setOAuthConsumerKey("consumerKey")
                     .setOAuthConsumerSecret("consumerSecret")
                     .setOAuthAccessToken("accessToken")
                     .setOAuthAccessTokenSecret("accessTokenSecret")
                     .setIncludeEntitiesEnabled(true)
                     .build();
    Authorization auth = new OAuthAuthorization(conf);
    TwitterBaseImpl tbImpl = new TwitterBaseImpl(conf, auth);
  
    // Act
    String screenName = tbImpl.getScreenName();
  
    // Assert
    assertNotNull(screenName);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetScreenName_WhenAuthDisabled() throws TwitterException, IllegalStateException {
    // Arrange
    Configuration conf = new ConfigurationBuilder()
                     .setIncludeEntitiesEnabled(true)
                     .build();
    Authorization auth = NullAuthorization.getInstance();
    TwitterBaseImpl tbImpl = new TwitterBaseImpl(conf, auth);
  
    // Act
    String screenName = tbImpl.getScreenName();
  
    // Assert
    // Exception expected
  }

  @Test
  public void testGetId_WhenAuthEnabled() throws TwitterException, IllegalStateException {
    // Arrange
    Configuration conf = new ConfigurationBuilder()
                     .setOAuthConsumerKey("consumerKey")
                     .setOAuthConsumerSecret("consumerSecret")
                     .setOAuthAccessToken("accessToken")
                     .setOAuthAccessTokenSecret("accessTokenSecret")
                     .setIncludeEntitiesEnabled(true)
                     .build();
    Authorization auth = new OAuthAuthorization(conf);
    TwitterBaseImpl tbImpl = new TwitterBaseImpl(conf, auth);
  
    // Act
    long id = tbImpl.getId();
  
    // Assert
    assertNotEquals(0, id);
  }

  @Test(expected = IllegalStateException.class)
  public void testGetId_WhenAuthDisabled() throws TwitterException, IllegalStateException {
    // Arrange
    Configuration conf = new ConfigurationBuilder()
                     .setIncludeEntitiesEnabled(true)
                     .build();
    Authorization auth = NullAuthorization.getInstance();
    TwitterBaseImpl tbImpl = new TwitterBaseImpl(conf, auth);
  
    // Act
    long id = tbImpl.getId();
  
    // Assert
    // Exception expected
  }

  // Other test cases for the remaining methods in the class

}