

import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testPageConstructor() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(1000L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(1, 20);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(1, 10L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(10L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(1, 20, 1000L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(1, 20, 1000L, 2000L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(2000L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testSetPagePositive() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test
    public void testSetCountPositive() {
        Paging paging = new Paging();
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }
    
    @Test
    public void testSetSinceIdPositive() {
        Paging paging = new Paging();
        paging.setSinceId(1000L);
        assertEquals(1000L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testSetMaxIdPositive() {
        Paging paging = new Paging();
        paging.setMaxId(2000L);
        assertEquals(2000L, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 20, 1000L, 2000L);
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsSameObject() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        assertTrue(paging1.equals(paging1));
    }
    
    @Test
    public void testEqualsDifferentCounts() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 30, 1000L, 2000L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentMaxIds() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 20, 1000L, 3000L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentPage() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(2, 20, 1000L, 2000L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentSinceIds() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 20, 2000L, 2000L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 20, 1000L, 2000L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentCounts() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 30, 1000L, 2000L);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentMaxIds() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 20, 1000L, 3000L);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentPage() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(2, 20, 1000L, 2000L);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testHashCodeDifferentSinceIds() {
        Paging paging1 = new Paging(1, 20, 1000L, 2000L);
        Paging paging2 = new Paging(1, 20, 2000L, 2000L);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 20, 1000L, 2000L);
        assertEquals("Paging{page=1, count=20, sinceId=1000, maxId=2000}", paging.toString());
    }
}
