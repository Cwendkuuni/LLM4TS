import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import twitter4j.internal.http.*;
import static org.junit.Assert.*;

public class PagingTest {
    
    private Paging paging;
    
    @Before
    public void setUp() {
        paging = new Paging();
    }
    
    @Test
    public void testAsPostParameterListWithDefaults() {
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter("count", "-1"));
        assertEquals(expected, paging.asPostParameterList());
    }
    
    @Test
    public void testAsPostParameterList() {
        paging.setSinceId(12345L);
        paging.setCount(50);
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter("since_id", "12345"));
        expected.add(new HttpParameter("count", "50"));
        assertEquals(expected, paging.asPostParameterList());
    }
    
    @Test
    public void testAsPostParameterArrayWithDefaults() {
        HttpParameter[] expected = new HttpParameter[0];
        assertArrayEquals(expected, paging.asPostParameterArray());
    }
    
    @Test
    public void testAsPostParameterArray() {
        paging.setSinceId(12345L);
        paging.setCount(50);
        HttpParameter[] expected = new HttpParameter[2];
        expected[0] = new HttpParameter("since_id", "12345");
        expected[1] = new HttpParameter("count", "50");
        assertArrayEquals(expected, paging.asPostParameterArray());
    }
    
    @Test
    public void testAddPostParameter() {
        List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
        paging.addPostParameter(new char[] {'s', 'm', 'c'}, 's', pagingParams, "param", 123);
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter("param", "123"));
        assertEquals(expected, pagingParams);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterNotSupported() {
        List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
        paging.addPostParameter(new char[] {'m', 'c'}, 's', pagingParams, "param", 123);
    }
    
    @Test
    public void testPagingConstructor() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetPage() {
        paging.setPage(3);
        assertEquals(3, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        paging.setPage(-2);
    }
    
    @Test
    public void testGetCount() {
        assertEquals(-1, paging.getCount());
    }
    
    @Test
    public void testSetCount() {
        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        paging.setCount(-50);
    }
    
    @Test
    public void testCountMethod() {
        Paging modified = paging.count(200);
        assertEquals(200, modified.getCount());
        assertEquals(paging, modified);
    }
    
    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }
    
    @Test
    public void testSetSinceId() {
        paging.setSinceId(9876L);
        assertEquals(9876L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        paging.setSinceId(-12345L);
    }
    
    @Test
    public void testSinceIdMethod() {
        Paging modified = paging.sinceId(54321L);
        assertEquals(54321L, modified.getSinceId());
        assertEquals(paging, modified);
    }
    
    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetMaxId() {
        paging.setMaxId(1111L);
        assertEquals(1111L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        paging.setMaxId(-2222L);
    }
    
    @Test
    public void testMaxIdMethod() {
        Paging modified = paging.maxId(3333L);
        assertEquals(3333L, modified.getMaxId());
        assertEquals(paging, modified);
    }
    
    @Test
    public void testEquals() {
        Paging other = new Paging();
        assertTrue(paging.equals(paging));
        assertTrue(paging.equals(other));
        assertFalse(paging.equals(null));
        assertFalse(paging.equals("test"));
        
        paging.setPage(2);
        assertFalse(paging.equals(other));
        
        paging.setPage(1);
        paging.setCount(50);
        assertFalse(paging.equals(other));
        
        other.setPage(1);
        assertFalse(paging.equals(other));
        
        other.setCount(50);
        assertTrue(paging.equals(other));
        
        paging.setSinceId(1234L);
        assertFalse(paging.equals(other));
        
        other.setSinceId(1234L);
        assertTrue(paging.equals(other));
        
        paging.setMaxId(5678L);
        assertFalse(paging.equals(other));
        
        other.setMaxId(5678L);
        assertTrue(paging.equals(other));
    }
    
    @Test
    public void testHashCode() {
        int expected = 31 * (31 * (31 * 1 + 0) + -1L) + -1L;
        assertEquals(expected, paging.hashCode());
    }
    
    @Test
    public void testToString() {
        String expected = "Paging{page=-1, count=-1, sinceId=-1, maxId=-1}";
        assertEquals(expected, paging.toString());
    }
}