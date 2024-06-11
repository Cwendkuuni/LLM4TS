import org.junit.Test;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class TwitterBaseImplTest {

    @Test
    public void testGetScreenName() {
        // Test typical case
        // Mock Authorization object
        Authorization authMock = mock(Authorization.class);
        when(authMock.isEnabled()).thenReturn(true);
        
        // Create instance of TwitterBaseImpl
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), authMock);
        
        try {
            assertEquals("ScreenName123", twitterBase.getScreenName());
        } catch (TwitterException e) {
            fail("Exception should not be thrown");
        }
        
        // Test case where auth is not enabled
        Authorization authMock2 = mock(Authorization.class);
        when(authMock2.isEnabled()).thenReturn(false);
        
        TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(new ConfigurationBuilder().build(), authMock2);
        
        try {
            twitterBase2.getScreenName();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testGetId() {
        // Test typical case
        // Mock Authorization object
        Authorization authMock = mock(Authorization.class);
        when(authMock.isEnabled()).thenReturn(true);
        
        // Create instance of TwitterBaseImpl
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(new ConfigurationBuilder().build(), authMock);
        
        try {
            assertEquals(1234, twitterBase.getId());
        } catch (TwitterException e) {
            fail("Exception should not be thrown");
        }
        
        // Test case where auth is not enabled
        Authorization authMock2 = mock(Authorization.class);
        when(authMock2.isEnabled()).thenReturn(false);
        
        TwitterBaseImpl twitterBase2 = new TwitterBaseImpl(new ConfigurationBuilder().build(), authMock2);
        
        try {
            twitterBase2.getId();
            fail("Should throw IllegalStateException");
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    // Add more test cases for other methods as per requirements

}