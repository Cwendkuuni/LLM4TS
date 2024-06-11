
import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {
    
    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }
    
    @Test
    public void testPageConstructor() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }
    
    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(123456L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }
    
    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(2, 10, 123456L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }
    
    @Test
    public void testFullConstructor() {
        Paging paging = new Paging(2, 10, 123456L, 7891011L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(7891011L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageInvalid() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountInvalid() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdInvalid() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdInvalid() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        assertEquals(paging, paging.count(10));
        assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        assertEquals(paging, paging.sinceId(123456L));
        assertEquals(123456L, paging.getSinceId());
    }
    
    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        assertEquals(paging, paging.maxId(7891011L));
        assertEquals(7891011L, paging.getMaxId());
    }
    
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(2, 10, 123456L, 7891011L);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(4, params.size());
        assertEquals("since_id", params.get(0).getName());
        assertEquals("123456", params.get(0).getValue());
        assertEquals("max_id", params.get(1).getName());
        assertEquals("7891011", params.get(1).getValue());
        assertEquals("per_page", params.get(2).getName());
        assertEquals("10", params.get(2).getValue());
        assertEquals("page", params.get(3).getName());
        assertEquals("2", params.get(3).getValue());
    }
    
    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(2, 10, 123456L, 7891011L);
        HttpParameter[] params = paging.asPostParameterArray();
        assertEquals(4, params.length);
        assertEquals("since_id", params[0].getName());
        assertEquals("123456", params[0].getValue());
        assertEquals("max_id", params[1].getName());
        assertEquals("7891011", params[1].getValue());
        assertEquals("per_page", params[2].getName());
        assertEquals("10", params[2].getValue());
        assertEquals("page", params[3].getName());
        assertEquals("2", params[3].getValue());
    }
}
