import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PagingTest {
    
    Paging paging;
    
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
        paging = new Paging(5);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithSinceId() {
        paging = new Paging(1234L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        paging = new Paging(3, 9876L);
        assertEquals(3, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(9876L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountAndSinceId() {
        paging = new Paging(4, 20, 5678L);
        assertEquals(4, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(5678L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        paging = new Paging(6, 30, 4321L, 8765L);
        assertEquals(6, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(4321L, paging.getSinceId());
        assertEquals(8765L, paging.getMaxId());
    }
    
    @Test
    public void testSetPage() {
        paging.setPage(7);
        assertEquals(7, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        paging.setPage(-5);
    }
    
    @Test
    public void testSetCount() {
        paging.setCount(50);
        assertEquals(50, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        paging.setCount(-10);
    }
    
    @Test
    public void testCountMethod() {
        Paging newPaging = paging.count(15);
        assertEquals(15, newPaging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }
    
    @Test
    public void testSetSinceId() {
        paging.setSinceId(987654L);
        assertEquals(987654L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        paging.setSinceId(-1234567L);
    }
    
    @Test
    public void testSinceIdMethod() {
        Paging newPaging = paging.sinceId(7654321L);
        assertEquals(7654321L, newPaging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetMaxId() {
        paging.setMaxId(54321L);
        assertEquals(54321L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        paging.setMaxId(-98765L);
    }
    
    @Test
    public void testMaxIdMethod() {
        Paging newPaging = paging.maxId(12345L);
        assertEquals(12345L, newPaging.getMaxId());
    }
    
    @Test
    public void testEqualsMethodWithEqualObjects() {
        Paging paging1 = new Paging(10, 20, 1234L, 5678L);
        Paging paging2 = new Paging(10, 20, 1234L, 5678L);
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsMethodWithDifferentObjects() {
        Paging paging1 = new Paging(5, 10, 4321L, 9876L);
        Paging paging2 = new Paging(7, 15, 1234L, 5678L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCodeMethod() {
        Paging paging1 = new Paging(10, 20, 1234L, 5678L);
        Paging paging2 = new Paging(10, 20, 1234L, 5678L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToStringMethod() {
        paging = new Paging(5, 10, 1234L, 5678L);
        String expected = "Paging{page=5, count=10, sinceId=1234, maxId=5678}";
        assertEquals(expected, paging.toString());
    }

}