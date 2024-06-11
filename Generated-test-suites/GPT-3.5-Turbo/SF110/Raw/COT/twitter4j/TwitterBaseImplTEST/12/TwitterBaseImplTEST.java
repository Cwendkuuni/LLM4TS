import org.junit.*;
import static org.junit.Assert.*;

public class TwitterBaseImplTest {
    
    private TwitterBaseImpl twitter;
    
    @Before
    public void setUp() {
        twitter = new TwitterBaseImpl(new ConfigurationBuilder().build(), new BasicAuthorization("username", "password"));
    }
    
    @Test
    public void testGetScreenName() {
        assertEquals("username", twitter.getScreenName());
    }
    
    @Test
    public void testGetId() {
        assertEquals(0L, twitter.getId());
    }
    
    @Test(expected=IllegalStateException.class)
    public void testGetScreenNameWithoutAuth() {
        twitter = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
        twitter.getScreenName();
    }
    
    @Test(expected=IllegalStateException.class)
    public void testGetIdWithoutAuth() {
        twitter = new TwitterBaseImpl(new ConfigurationBuilder().build(), null);
        twitter.getId();
    }
    
    // Add more test cases for other methods
    
}