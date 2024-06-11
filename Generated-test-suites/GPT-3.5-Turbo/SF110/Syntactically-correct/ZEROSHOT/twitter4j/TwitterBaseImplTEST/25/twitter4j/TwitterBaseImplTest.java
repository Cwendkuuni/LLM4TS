import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    
    @Test
    public void testGetScreenName() {
        TwitterBaseImpl twitter = new TwitterBaseImpl(null, null) {
            @Override
            void setFactory() { }
            
            @Override
            User fillInIDAndScreenName() throws TwitterException {
                return null;
            }
        };
        
        try {
            twitter.getScreenName();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception expected, test passes
        } catch (TwitterException e) {
            fail("Unexpected TwitterException thrown");
        }
    }
    
    @Test
    public void testGetId() {
        TwitterBaseImpl twitter = new TwitterBaseImpl(null, null) {
            @Override
            void setFactory() { }
            
            @Override
            User fillInIDAndScreenName() throws TwitterException {
                return null;
            }
        };
        
        try {
            twitter.getId();
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException e) {
            // Exception expected, test passes
        } catch (TwitterException e) {
            fail("Unexpected TwitterException thrown");
        }
    }
    
    // Add more test methods for each public method in the TwitterBaseImpl class
    
}