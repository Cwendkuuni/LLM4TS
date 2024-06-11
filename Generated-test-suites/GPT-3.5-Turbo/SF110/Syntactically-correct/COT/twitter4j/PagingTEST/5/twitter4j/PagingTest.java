
import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(12345);
        assertEquals(12345, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        paging.setSinceId(-1);
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(54321);
        assertEquals(54321, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        Paging paging = new Paging();
        paging.setMaxId(-1);
    }
    
    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        Paging newPaging = paging.count(10);
        assertEquals(newPaging, paging);
        assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        Paging newPaging = paging.sinceId(12345);
        assertEquals(newPaging, paging);
        assertEquals(12345, paging.getSinceId());
    }
    
    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        Paging newPaging = paging.maxId(54321);
        assertEquals(newPaging, paging);
        assertEquals(54321, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 12345, 54321);
        Paging paging2 = new Paging(1, 10, 12345, 54321);
        
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testHashcode() {
        Paging paging1 = new Paging(1, 10, 12345, 54321);
        Paging paging2 = new Paging(1, 10, 12345, 54321);
        
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 12345, 54321);
        assertEquals("Paging{page=1, count=10, sinceId=12345, maxId=54321}", paging.toString());
    }
    
}

