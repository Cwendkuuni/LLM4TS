import org.junit.*;
import java.util.*;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {
    
    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageConstructor() {
        Paging paging = new Paging(2);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(123456789L);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(123456789L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageAndCountConstructor() {
        Paging paging = new Paging(3, 10);
        Assert.assertEquals(3, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageAndSinceIdConstructor() {
        Paging paging = new Paging(4, 987654321L);
        Assert.assertEquals(4, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(987654321L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(5, 20, 12345L);
        Assert.assertEquals(5, paging.getPage());
        Assert.assertEquals(20, paging.getCount());
        Assert.assertEquals(12345L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testAllParamsConstructor() {
        Paging paging = new Paging(6, 30, 1111L, 9999L);
        Assert.assertEquals(6, paging.getPage());
        Assert.assertEquals(30, paging.getCount());
        Assert.assertEquals(1111L, paging.getSinceId());
        Assert.assertEquals(9999L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageInConstructor() {
        Paging paging = new Paging(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountInConstructor() {
        Paging paging = new Paging(7, -1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdInConstructor() {
        Paging paging = new Paging(-1L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdInConstructor() {
        Paging paging = new Paging(8, 40, 1234L, -1L);
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(9);
        Assert.assertEquals(9, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageInSetPage() {
        Paging paging = new Paging();
        paging.setPage(0);
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(50);
        Assert.assertEquals(50, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountInSetCount() {
        Paging paging = new Paging();
        paging.setCount(0);
    }
    
    @Test
    public void testCountMethodChaining() {
        Paging paging = new Paging().count(60);
        Assert.assertEquals(60, paging.getCount());
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1234567890L);
        Assert.assertEquals(1234567890L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdInSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }
    
    @Test
    public void testSinceIdMethodChaining() {
        Paging paging = new Paging().sinceId(9876543210L);
        Assert.assertEquals(9876543210L, paging.getSinceId());
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(9876543210L);
        Assert.assertEquals(9876543210L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdInSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }
    
    @Test
    public void testMaxIdMethodChaining() {
        Paging paging = new Paging().maxId(1234567890L);
        Assert.assertEquals(1234567890L, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(10, 100, 1234L, 9876L);
        Paging paging2 = new Paging(10, 100, 1234L, 9876L);
        Paging paging3 = new Paging(5, 50, 4321L, 9876L);
        
        Assert.assertTrue(paging1.equals(paging2));
        Assert.assertFalse(paging1.equals(paging3));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(10, 100, 1234L, 9876L);
        Paging paging2 = new Paging(10, 100, 1234L, 9876L);
        Paging paging3 = new Paging(5, 50, 4321L, 9876L);
        
        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
        Assert.assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(10, 100, 1234L, 9876L);
        Assert.assertEquals("Paging{page=10, count=100, sinceId=1234, maxId=9876}", paging.toString());
    }
    
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(10, 100, 1234L, 9876L);
        List<HttpParameter> params = paging.asPostParameterList();
        
        Assert.assertEquals(4, params.size());
        Assert.assertEquals("since_id", params.get(0).getName());
        Assert.assertEquals("1234", params.get(0).getValue());
        Assert.assertEquals("max_id", params.get(1).getName());
        Assert.assertEquals("9876", params.get(1).getValue());
        Assert.assertEquals("count", params.get(2).getName());
        Assert.assertEquals("100", params.get(2).getValue());
        Assert.assertEquals("page", params.get(3).getName());
        Assert.assertEquals("10", params.get(3).getValue());
    }
    
    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(10, 100, 1234L, 9876L);
        HttpParameter[] params = paging.asPostParameterArray();
        
        Assert.assertEquals(4, params.length);
        Assert.assertEquals("since_id", params[0].getName());
        Assert.assertEquals("1234", params[0].getValue());
        Assert.assertEquals("max_id", params[1].getName());
        Assert.assertEquals("9876", params[1].getValue());
        Assert.assertEquals("count", params[2].getName());
        Assert.assertEquals("100", params[2].getValue());
        Assert.assertEquals("page", params[3].getName());
        Assert.assertEquals("10", params[3].getValue());
    }
    
    @Test
    public void testAsPostParameterList_WithSupportedParams() {
        char[] supportedParams = {'s', 'm', 'c'};
        Paging paging = new Paging(10, 100, 1234L, 9876L);
        List<HttpParameter> params = paging.asPostParameterList(supportedParams);
        
        Assert.assertEquals(3, params.size());
        Assert.assertEquals("since_id", params.get(0).getName());
        Assert.assertEquals("1234", params.get(0).getValue());
        Assert.assertEquals("max_id", params.get(1).getName());
        Assert.assertEquals("9876", params.get(1).getValue());
        Assert.assertEquals("count", params.get(2).getName());
        Assert.assertEquals("100", params.get(2).getValue());
    }
    
    @Test
    public void testAsPostParameterArray_WithSupportedParams() {
        char[] supportedParams = {'s', 'm', 'c'};
        Paging paging = new Paging(10, 100, 1234L, 9876L);
        HttpParameter[] params = paging.asPostParameterArray(supportedParams);
        
        Assert.assertEquals(3, params.length);
        Assert.assertEquals("since_id", params[0].getName());
        Assert.assertEquals("1234", params[0].getValue());
        Assert.assertEquals("max_id", params[1].getName());
        Assert.assertEquals("9876", params[1].getValue());
        Assert.assertEquals("count", params[2].getName());
        Assert.assertEquals("100", params[2].getValue());
    }
    
}