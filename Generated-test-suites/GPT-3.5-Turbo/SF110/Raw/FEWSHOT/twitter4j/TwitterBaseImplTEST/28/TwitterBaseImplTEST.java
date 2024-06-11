package twitter4j;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

public class TwitterBaseImplTest {
    private Configuration conf;
    private Authorization auth;

    @Test
    public void testGetScreenName() throws TwitterException, IllegalStateException {
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth) {
            @Override
            void setFactory() {

            }
        };

        String screenName = twitterBase.getScreenName();
        assertNull(screenName);

        twitterBase.screenName = "test";
        screenName = twitterBase.getScreenName();
        assertEquals("test", screenName);
    }

    @Test
    public void testGetId() throws TwitterException, IllegalStateException {
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth) {
            @Override
            void setFactory() {

            }
        };

        long id = twitterBase.getId();
        assertEquals(0L, id);

        twitterBase.id = 123;
        id = twitterBase.getId();
        assertEquals(123L, id);
    }

    @Test
    public void testFillInIDAndScreenName() throws TwitterException {
        TwitterBaseImpl twitterBase = new TwitterBaseImpl(conf, auth) {
            @Override
            void setFactory() {

            }
        };

        User user = twitterBase.fillInIDAndScreenName();
        assertNotNull(user);

        HttpParameter[] parameters = new HttpParameter[0];
        user = twitterBase.fillInIDAndScreenName(parameters);
        assertNotNull(user);
    }


}