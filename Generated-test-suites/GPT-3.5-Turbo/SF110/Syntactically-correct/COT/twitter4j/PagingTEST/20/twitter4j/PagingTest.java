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
        Paging paging = new Paging(2);
        
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(1234L);
        
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(3, 10);
        
        assertEquals(3, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(4, 12345L);
        
        assertEquals(4, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(12345L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(5, 20, 54321L);
        
        assertEquals(5, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(54321L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testAllArgsConstructor() {
        Paging paging = new Paging(6, 30, 1111L, 2222L);
        
        assertEquals(6, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(1111L, paging.getSinceId());
        assertEquals(2222L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        Paging paging = new Paging();
        paging.setPage(-2);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        paging.setSinceId(-1234L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        Paging paging = new Paging();
        paging.setMaxId(-5678L);
    }
}