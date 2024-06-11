import org.junit.Test;
import twitter4j.Paging;

import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        
        // Typical use case
        paging.setPage(1);
        assertEquals(1, paging.getPage());
        
        // Edge case - page is at the lower limit
        paging.setPage(0);
        assertEquals(0, paging.getPage());
        
        // Error scenario - page is negative
        try {
            paging.setPage(-1);
            fail("IllegalArgumentException should be thrown when passing negative page");
        } catch (IllegalArgumentException e) {
            assertEquals("page should be positive integer. passed:-1", e.getMessage());
        }
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());
        
        // Typical use case
        paging.setCount(10);
        assertEquals(10, paging.getCount());
        
        // Edge case - count is at the lower limit
        paging.setCount(1);
        assertEquals(1, paging.getCount());
        
        // Error scenario - count is negative
        try {
            paging.setCount(-1);
            fail("IllegalArgumentException should be thrown when passing negative count");
        } catch (IllegalArgumentException e) {
            assertEquals("count should be positive integer. passed:-1", e.getMessage());
        }
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
        
        // Typical use case
        paging.setSinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
        
        // Edge case - sinceId is at the lower limit
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
        
        // Error scenario - sinceId is negative
        try {
            paging.setSinceId(-1L);
            fail("IllegalArgumentException should be thrown when passing negative sinceId");
        } catch (IllegalArgumentException e) {
            assertEquals("since_id should be positive integer. passed:-1", e.getMessage());
        }
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
        
        // Typical use case
        paging.setMaxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
        
        // Edge case - maxId is at the lower limit
        paging.setMaxId(1L);
        assertEquals(1L, paging.getMaxId());
        
        // Error scenario - maxId is negative
        try {
            paging.setMaxId(-1L);
            fail("IllegalArgumentException should be thrown when passing negative maxId");
        } catch (IllegalArgumentException e) {
            assertEquals("max_id should be positive integer. passed:-1", e.getMessage());
        }
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging3 = new Paging(2, 20, 123456789L, 987654321L);
        
        // Typical use case - equal objects
        assertTrue(paging1.equals(paging2));
        
        // Typical use case - non-equal objects
        assertFalse(paging1.equals(paging3));
        
        // Edge case - comparing object with itself
        assertTrue(paging1.equals(paging1));
        
        // Edge case - comparing object with null
        assertFalse(paging1.equals(null));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(1, 10, 123456789L, 987654321L);
        
        // Typical use case - equal objects
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        assertEquals("Paging{page=1, count=10, sinceId=123456789, maxId=987654321}", paging.toString());
    }
}