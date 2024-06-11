
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class TwitterExceptionTest {

    @Test
    public void testTwitterExceptionString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionException() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException(cause);
        assertEquals("Test Cause", exception.getCause().getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testTwitterExceptionStringHttpResponse() {
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }

            @Override
            public String getResponseHeader(String name) {
                if (name.equals("Retry-After")) {
                    return "60";
                }
                return null;
            }
        };
        TwitterException exception = new TwitterException("Test Exception", response);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals(420, exception.getStatusCode());
        assertEquals(60, exception.getRetryAfter());
        assertNotNull(exception.getRateLimitStatus());
        assertNull(exception.getFeatureSpecificRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringException() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals("Test Cause", exception.getCause().getMessage());
        assertEquals(-1, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testTwitterExceptionStringExceptionInt() {
        Exception cause = new Exception("Test Cause");
        TwitterException exception = new TwitterException("Test Exception", cause, 404);
        assertEquals("Test Exception", exception.getMessage());
        assertEquals("Test Cause", exception.getCause().getMessage());
        assertEquals(404, exception.getStatusCode());
        assertNull(exception.getRateLimitStatus());
    }

    @Test
    public void testGetStatusCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertEquals(-1, exception.getStatusCode());
    }

    @Test
    public void testGetRateLimitStatus() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNull(exception.getRateLimitStatus());
    }

    @Test(expected = IllegalStateException.class)
    public void testGetRetryAfter() {
        HttpResponse response = new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 200;
            }
        };
        TwitterException exception = new TwitterException("Test Exception", response);
        exception.getRetryAfter();
    }

    @Test
    public void testIsCausedByNetworkIssue() {
        Exception cause = new IOException("Network Issue");
        TwitterException exception = new TwitterException("Test Exception", cause);
        assertTrue(exception.isCausedByNetworkIssue());
    }

    @Test
    public void testExceededRateLimitation() {
        TwitterException exception1 = new TwitterException("Test Exception", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 400;
            }
        });
        
        assertTrue(exception1.exceededRateLimitation());
        
        TwitterException exception2 = new TwitterException("Test Exception", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 420;
            }
        });
        
        assertTrue(exception2.exceededRateLimitation());
        
        TwitterException exception3 = new TwitterException("Test Exception", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 200;
            }
        });
        
        assertFalse(exception3.exceededRateLimitation());
    }

    @Test
    public void testResourceNotFound() {
        TwitterException exception1 = new TwitterException("Test Exception", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        });
        
        assertTrue(exception1.resourceNotFound());
        
        TwitterException exception2 = new TwitterException("Test Exception", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 200;
            }
        });
        
        assertFalse(exception2.resourceNotFound());
    }

    @Test
    public void testGetExceptionCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNotNull(exception.getExceptionCode());
    }

    @Test
    public void testEquals() {
        TwitterException exception1 = new TwitterException("Test Exception");
        TwitterException exception2 = new TwitterException("Test Exception");
        assertTrue(exception1.equals(exception2));
        
        TwitterException exception3 = new TwitterException("Test Exception", new HttpResponse() {
            @Override
            public int getStatusCode() {
                return 404;
            }
        });
        
        assertFalse(exception3.equals(exception1));
    }

    @Test
    public void testHashCode() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNotNull(exception.hashCode());
    }

    @Test
    public void testToString() {
        TwitterException exception = new TwitterException("Test Exception");
        assertNotNull(exception.toString());
    }

}
