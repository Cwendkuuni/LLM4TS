import org.junit.*;
import static org.junit.Assert.*;

public class PagingTest {
    private Paging paging;
    
    @Before
    public void setUp() {
        paging = new Paging();
    }
    
    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPage() {
        Paging p = new Paging(3);
        assertEquals(3, p.getPage());
        assertEquals(-1, p.getCount());
        assertEquals(-1L, p.getSinceId());
        assertEquals(-1L, p.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidPage() {
        new Paging(0);
    }
    
    @Test
    public void testConstructorWithSinceId() {
        Paging p = new Paging(123);
        assertEquals(-1, p.getPage());
        assertEquals(-1, p.getCount());
        assertEquals(123L, p.getSinceId());
        assertEquals(-1L, p.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidSinceId() {
        new Paging(-1L);
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        Paging p = new Paging(4, 10);
        assertEquals(4, p.getPage());
        assertEquals(10, p.getCount());
        assertEquals(-1L, p.getSinceId());
        assertEquals(-1L, p.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithInvalidCount() {
        new Paging(1, -5);
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging p = new Paging(2, 1234);
        assertEquals(2, p.getPage());
        assertEquals(-1, p.getCount());
        assertEquals(1234L, p.getSinceId());
        assertEquals(-1L, p.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging p = new Paging(1, 5, 12345);
        assertEquals(1, p.getPage());
        assertEquals(5, p.getCount());
        assertEquals(12345L, p.getSinceId());
        assertEquals(-1L, p.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging p = new Paging(1, 10, 456789, 9999);
        assertEquals(1, p.getPage());
        assertEquals(10, p.getCount());
        assertEquals(456789L, p.getSinceId());
        assertEquals(9999L, p.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        paging.setPage(0);
    }
    
    @Test
    public void testSetPageWithValidValue() {
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        paging.setCount(0);
    }
    
    @Test
    public void testSetCountWithValidValue() {
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        paging.setSinceId(-1L);
    }
    
    @Test
    public void testSetSinceIdWithValidValue() {
        paging.setSinceId(123456L);
        assertEquals(123456L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testSetMaxIdWithValidValue() {
        paging.setMaxId(98765L);
        assertEquals(98765L, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging p1 = new Paging(1, 10, 123, 456);
        Paging p2 = new Paging(1, 10, 123, 456);
        assertEquals(p1, p2);
    }
    
    @Test
    public void testHashCode() {
        Paging p1 = new Paging(3, 5, 789, 1234);
        Paging p2 = new Paging(3, 5, 789, 1234);
        assertEquals(p1.hashCode(), p2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging p = new Paging(5);
        String expected = "Paging{page=5, count=-1, sinceId=-1, maxId=-1}";
        assertEquals(expected, p.toString());
    }
}