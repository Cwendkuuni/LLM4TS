import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(100L);
        assertEquals(100L, paging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(200L);
        assertEquals(200L, paging.getMaxId());
    }
    
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
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(50L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(50L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(3, 20);
        assertEquals(3, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(4, 100L);
        assertEquals(4, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(100L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceId() {
        Paging paging = new Paging(5, 30, 200L);
        assertEquals(5, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(200L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(6, 50, 300L, 400L);
        assertEquals(6, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(300L, paging.getSinceId());
        assertEquals(400L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageValue() {
        Paging paging = new Paging();
        paging.setPage(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdValue() {
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdValue() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }

}