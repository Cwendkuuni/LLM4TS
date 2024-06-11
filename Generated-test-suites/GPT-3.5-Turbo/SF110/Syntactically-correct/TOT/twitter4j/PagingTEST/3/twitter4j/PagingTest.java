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
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(123456789L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(2, 123456789L);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithAllParams() {
        Paging paging = new Paging(3, 20, 987654321L, 123456789L);
        assertEquals(3, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(987654321L, paging.getSinceId());
        assertEquals(123456789L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativePage() {
        Paging paging = new Paging(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        Paging paging = new Paging(1, -10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSinceId() {
        Paging paging = new Paging(-123456789L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxId() {
        Paging paging = new Paging(1, 10, 123456789L, -987654321L);
    }
    
    @Test
    public void testPageSetter() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }
    
    @Test
    public void testCountSetter() {
        Paging paging = new Paging();
        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativePageSetter() {
        Paging paging = new Paging();
        paging.setPage(-5);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCountSetter() {
        Paging paging = new Paging();
        paging.setCount(-100);
    }
    
    @Test
    public void testSinceIdSetter() {
        Paging paging = new Paging();
        paging.setSinceId(987654321L);
        assertEquals(987654321L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSinceIdSetter() {
        Paging paging = new Paging();
        paging.setSinceId(-987654321L);
    }
    
    @Test
    public void testMaxIdSetter() {
        Paging paging = new Paging();
        paging.setMaxId(123456789L);
        assertEquals(123456789L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxIdSetter() {
        Paging paging = new Paging();
        paging.setMaxId(-123456789L);
    }
  
}