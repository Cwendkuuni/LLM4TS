import org.junit.*;
import static org.junit.Assert.*;

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
        Paging paging = new Paging(5);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(123456);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(5, 20);
        assertEquals(5, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(5, 123456);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(5, 20, 123456);
        assertEquals(5, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(123456, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(5, 20, 123456, 7891011);
        assertEquals(5, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(123456, paging.getSinceId());
        assertEquals(7891011, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-123456);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-7891011);
    }
    
    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        Paging returnedPaging = paging.count(20);
        assertEquals(20, returnedPaging.getCount());
    }
    
    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        Paging returnedPaging = paging.sinceId(123456);
        assertEquals(123456, returnedPaging.getSinceId());
    }
    
    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        Paging returnedPaging = paging.maxId(7891011);
        assertEquals(7891011, returnedPaging.getMaxId());
    }
    
    @Test
    public void testEqualObjects() {
        Paging paging1 = new Paging(5, 20, 123456, 7891011);
        Paging paging2 = new Paging(5, 20, 123456, 7891011);
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testNotEqualObjects() {
        Paging paging1 = new Paging(5, 20, 123456, 7891011);
        Paging paging2 = new Paging(10, 30, 7891011, 123456);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testToStringMethod() {
        Paging paging = new Paging(5, 20, 123456, 7891011);
        assertEquals("Paging{page=5, count=20, sinceId=123456, maxId=7891011}", paging.toString());
    }
}