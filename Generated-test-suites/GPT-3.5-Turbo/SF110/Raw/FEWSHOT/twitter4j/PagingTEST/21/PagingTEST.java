import static org.junit.Assert.assertEquals;
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
        Paging paging = new Paging(1234567L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234567L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(2, 100);
        assertEquals(2, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(3, 1234567L);
        assertEquals(3, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234567L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(4, 200, 1234567L);
        assertEquals(4, paging.getPage());
        assertEquals(200, paging.getCount());
        assertEquals(1234567L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(5, 300, 1234567L, 9876543L);
        assertEquals(5, paging.getPage());
        assertEquals(300, paging.getCount());
        assertEquals(1234567L, paging.getSinceId());
        assertEquals(9876543L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPageConstructorWithNegativePage() {
        new Paging(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testCountConstructorWithNegativeCount() {
        new Paging(1, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSinceIdConstructorWithNegativeSinceId() {
        new Paging(-1L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSinceIdConstructorWithZeroSinceId() {
        new Paging(0L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMaxIdConstructorWithNegativeMaxId() {
        new Paging(1, 1, 1L, -1L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMaxIdConstructorWithZeroMaxId() {
        new Paging(1, 1, 1L, 0L);
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(6);
        assertEquals(6, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativePage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(400);
        assertEquals(400, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(7654321L);
        assertEquals(7654321L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(9999999L);
        assertEquals(9999999L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        Paging result = paging.count(500);
        assertEquals(500, paging.getCount());
        assertEquals(paging, result);
    }
    
    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(8888888L);
        assertEquals(8888888L, paging.getSinceId());
        assertEquals(paging, result);
    }
    
    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.maxId(5555555L);
        assertEquals(5555555L, paging.getMaxId());
        assertEquals(paging, result);
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1);
        Paging paging2 = new Paging(1);
        assertEquals(paging1, paging2);
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1);
        Paging paging2 = new Paging(1);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 100, 1234567L, 9876543L);
        String expected = "Paging{page=1, count=100, sinceId=1234567, maxId=9876543}";
        assertEquals(expected, paging.toString());
    }
}