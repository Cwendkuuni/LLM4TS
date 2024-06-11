import org.junit.Before;
import org.junit.Test;
import twitter4j.*;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;

import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    
    private TwitterBaseImpl twitterBase;
    
    @Before
    public void setUp() {
        ConfigurationBuilder confBuilder = new ConfigurationBuilder();
        confBuilder.setDebugEnabled(true)
                .setOAuthConsumerKey("consumerKey")
                .setOAuthConsumerSecret("consumerSecret")
                .setOAuthAccessToken("accessToken")
                .setOAuthAccessTokenSecret("accessTokenSecret");
        
        Configuration conf = confBuilder.build();
        Authorization auth = AuthorizationFactory.getInstance(conf);
        
        twitterBase = new TwitterBaseImpl(conf, auth) {
            @Override
            void setFactory() {
                // Mock setFactory method
            }
        };
    }
    
    @Test(expected = IllegalStateException.class)
    public void getScreenName_NotEnabled_ExceptionThrown() throws TwitterException {
        // Arrange
        twitterBase.getAuthorization().setEnabled(false);
        
        // Act
        twitterBase.getScreenName();
        
        // Assert
        // Exception is thrown
    }
    
    @Test(expected = IllegalStateException.class)
    public void getId_NotEnabled_ExceptionThrown() throws TwitterException {
        // Arrange
        twitterBase.getAuthorization().setEnabled(false);
        
        // Act
        twitterBase.getId();
        
        // Assert
        // Exception is thrown
    }
    
    @Test(expected = IllegalStateException.class)
    public void ensureAuthorizationEnabled_AuthorizationDisabled_ExceptionThrown() {
        // Arrange
        twitterBase.getAuthorization().setEnabled(false);
        
        // Act
        twitterBase.ensureAuthorizationEnabled();
        
        // Assert
        // Exception is thrown
    }
    
    @Test(expected = IllegalStateException.class)
    public void ensureOAuthEnabled_NotOAuthAuthorization_ExceptionThrown() {
        // Arrange
        Authorization basicAuth = BasicAuthorization.getInstance("userId", "password");
        twitterBase.getAuthorization().setEnabled(true);
        twitterBase.getAuthorization().set(basicAuth);
        
        // Act
        twitterBase.ensureOAuthEnabled();
        
        // Assert
        // Exception is thrown
    }
    
    @Test
    public void setOAuthConsumer_NullConsumerKey_ExceptionThrown() {
        // Arrange
        String consumerSecret = "consumerSecret";
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            twitterBase.setOAuthConsumer(null, consumerSecret);
        });
    }
    
    @Test
    public void setOAuthConsumer_NullConsumerSecret_ExceptionThrown() {
        // Arrange
        String consumerKey = "consumerKey";
        
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            twitterBase.setOAuthConsumer(consumerKey, null);
        });
    }
    
    // More test cases for each method
    
}