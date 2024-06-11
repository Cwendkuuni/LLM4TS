import static org.junit.Assert.*;
import org.junit.*;

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
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(123456789L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(2, 123456789L);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(2, 10, 123456789L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(2, 10, 123456789L, 987654321L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
    }
    
    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageInvalid() {
        Paging paging = new Paging();
        paging.setPage(-5);
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());
        
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountInvalid() {
        Paging paging = new Paging();
        paging.setCount(-5);
    }
    
    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging result = paging.count(30);
        assertEquals(30, paging.getCount());
        assertSame(paging, result);
    }
    
    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
        
        paging.setSinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdInvalid() {
        Paging paging = new Paging();
        paging.setSinceId(-5L);
    }
    
    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
        assertSame(paging, result);
    }
    
    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
        
        paging.setMaxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdInvalid() {
        Paging paging = new Paging();
        paging.setMaxId(-5L);
    }
    
    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging result = paging.maxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
        assertSame(paging, result);
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(2, 10, 123456789L, 987654321L);
        Paging paging3 = new Paging(3, 20, 123456789L, 987654321L);
        
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertFalse(paging2.equals(paging3));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(2, 10, 123456789L, 987654321L);
        Paging paging3 = new Paging(3, 20, 123456789L, 987654321L);
        
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertNotEquals(paging2.hashCode(), paging3.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(2, 10, 123456789L, 987654321L);
        String expected = "Paging{page=2, count=10, sinceId=123456789, maxId=987654321}";
        
        assertEquals(expected, paging.toString());
    }
}