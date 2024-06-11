
import static org.junit.Assert.*;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testConstructor1() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor2() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor3() {
        Paging paging = new Paging(5L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(5L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor4() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor5() {
        Paging paging = new Paging(5, 20L);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(20L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor6() {
        Paging paging = new Paging(2, 10, 5L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(5L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor7() {
        Paging paging = new Paging(5, 20, 5L, 10L);
        assertEquals(5, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(5L, paging.getSinceId());
        assertEquals(10L, paging.getMaxId());
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(3);
        assertEquals(3, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageInvalid() {
        Paging paging = new Paging();
        paging.setPage(0);
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountInvalid() {
        Paging paging = new Paging();
        paging.setCount(0);
    }
    
    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging newPaging = paging.count(10);
        assertEquals(10, newPaging.getCount());
        assertNotSame(newPaging, paging);
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(7L);
        assertEquals(7L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdInvalid() {
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }
    
    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging newPaging = paging.sinceId(7L);
        assertEquals(7L, newPaging.getSinceId());
        assertNotSame(newPaging, paging);
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(10L);
        assertEquals(10L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdInvalid() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }
    
    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging newPaging = paging.maxId(10L);
        assertEquals(10L, newPaging.getMaxId());
        assertNotSame(newPaging, paging);
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 10, 5L, 10L);
        Paging paging2 = new Paging(2, 10, 5L, 10L);
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(2, 10, 5L, 10L);
        Paging paging2 = new Paging(5, 20, 5L, 10L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 10, 5L, 10L);
        Paging paging2 = new Paging(2, 10, 5L, 10L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(2, 10, 5L, 10L);
        assertEquals("Paging{page=2, count=10, sinceId=5, maxId=10}", paging.toString());
    }
}
