package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;
import java.util.*;

public class PagingTest {
    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPage() {
        int page = 1;
        Paging paging = new Paging(page);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithSinceId() {
        long sinceId = 12345L;
        Paging paging = new Paging(sinceId);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        int page = 1;
        int count = 50;
        Paging paging = new Paging(page, count);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        int page = 1;
        long sinceId = 12345L;
        Paging paging = new Paging(page, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountAndSinceId() {
        int page = 1;
        int count = 50;
        long sinceId = 12345L;
        Paging paging = new Paging(page, count, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        int page = 1;
        int count = 50;
        long sinceId = 12345L;
        long maxId = 67890L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(maxId, paging.getMaxId());
    }
    
    @Test
    public void testSetPage() {
        int page = 5;
        Paging paging = new Paging();
        paging.setPage(page);
        assertEquals(page, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        int page = -1;
        Paging paging = new Paging();
        paging.setPage(page);
    }
    
    @Test
    public void testSetCount() {
        int count = 50;
        Paging paging = new Paging();
        paging.setCount(count);
        assertEquals(count, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        int count = -1;
        Paging paging = new Paging();
        paging.setCount(count);
    }
    
    @Test
    public void testCountMethod() {
        int count = 50;
        Paging paging = new Paging();
        Paging newPaging = paging.count(count);
        assertEquals(count, newPaging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        long sinceId = 12345L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        long sinceId = -1L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
    }
    
    @Test
    public void testSinceIdMethod() {
        long sinceId = 12345L;
        Paging paging = new Paging();
        Paging newPaging = paging.sinceId(sinceId);
        assertEquals(sinceId, newPaging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        long maxId = 67890L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        long maxId = -1L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
    }
    
    @Test
    public void testMaxIdMethod() {
        long maxId = 67890L;
        Paging paging = new Paging();
        Paging newPaging = paging.maxId(maxId);
        assertEquals(maxId, newPaging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 50, 12345L, 67890L);
        Paging paging2 = new Paging(1, 50, 12345L, 67890L);

        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 50, 12345L, 67890L);
        Paging paging2 = new Paging(2, 50, 12345L, 67890L);

        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 50, 12345L, 67890L);
        Paging paging2 = new Paging(1, 50, 12345L, 67890L);

        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 50, 12345L, 67890L);
        String expected = "Paging{page=1, count=50, sinceId=12345, maxId=67890}";
        assertEquals(expected, paging.toString());
    }
}