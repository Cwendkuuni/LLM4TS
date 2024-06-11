
import org.junit.Test;
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
        Paging paging = new Paging(1);
        
        assertEquals(1, paging.getPage());
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
        Paging paging = new Paging(1, 10);
        
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(1, 123456789L);
        
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(1, 10, 123456789L);
        
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
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
        paging.setCount(10);
        
        assertEquals(10, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }
    
    @Test
    public void testSetSinceIdPositive() {
        Paging paging = new Paging();
        paging.setSinceId(123456789L);
        
        assertEquals(123456789L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testSetMaxIdPositive() {
        Paging paging = new Paging();
        paging.setMaxId(987654321L);
        
        assertEquals(987654321L, paging.getMaxId());
    }
}
