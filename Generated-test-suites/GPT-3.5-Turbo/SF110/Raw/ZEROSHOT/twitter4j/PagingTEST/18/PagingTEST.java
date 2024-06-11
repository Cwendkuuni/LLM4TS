import static org.junit.Assert.*;
import org.junit.Test;
import twitter4j.*;
import java.util.ArrayList;
import java.util.List;

public class PagingTest {
    
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        List<HttpParameter> result = paging.asPostParameterList();
        assertEquals(expected, result);
    }
    
    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] expected = new HttpParameter[0];
        HttpParameter[] result = paging.asPostParameterArray();
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] { 's', 'm', 'c', 'p' };
        String perPageParamName = "count";
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        List<HttpParameter> result = paging.asPostParameterList(supportedParams, perPageParamName);
        assertEquals(expected, result);
    }
    
    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] { 's', 'm', 'c', 'p' };
        String perPageParamName = "count";
        HttpParameter[] expected = new HttpParameter[0];
        HttpParameter[] result = paging.asPostParameterArray(supportedParams, perPageParamName);
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] { 's', 'm', 'c', 'p' };
        List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
        String paramName = "since_id";
        long paramValue = 1L;
        paging.addPostParameter(supportedParams, 's', pagingParams, paramName, paramValue);
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter(paramName, String.valueOf(paramValue)));
        assertEquals(expected, pagingParams);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] { 's', 'm', 'p' };
        List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
        String paramName = "since_id";
        long paramValue = 1L;
        paging.addPostParameter(supportedParams, 'c', pagingParams, paramName, paramValue);
    }
    
    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPage() {
        int page = 1;
        Paging paging = new Paging(page);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithSinceId() {
        long sinceId = 1L;
        Paging paging = new Paging(sinceId);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        int page = 1;
        int count = 20;
        Paging paging = new Paging(page, count);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        int page = 1;
        long sinceId = 1L;
        Paging paging = new Paging(page, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountAndSinceId() {
        int page = 1;
        int count = 20;
        long sinceId = 1L;
        Paging paging = new Paging(page, count, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        int page = 1;
        int count = 20;
        long sinceId = 1L;
        long maxId = 100L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(maxId, paging.getMaxId());
    }
    
    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        int expected = -1;
        int result = paging.getPage();
        assertEquals(expected, result);
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        int page = 1;
        paging.setPage(page);
        assertEquals(page, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        int page = -1;
        paging.setPage(page);
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        int expected = -1;
        int result = paging.getCount();
        assertEquals(expected, result);
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        int count = 20;
        paging.setCount(count);
        assertEquals(count, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        int count = -1;
        paging.setCount(count);
    }
    
    @Test
    public void testCount() {
        Paging paging = new Paging();
        int count = 10;
        Paging result = paging.count(count);
        assertEquals(count, result.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        long expected = -1L;
        long result = paging.getSinceId();
        assertEquals(expected, result);
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        long sinceId = 1L;
        paging.setSinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        long sinceId = -1L;
        paging.setSinceId(sinceId);
    }
    
    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        long sinceId = 1L;
        Paging result = paging.sinceId(sinceId);
        assertEquals(sinceId, result.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        long expected = -1L;
        long result = paging.getMaxId();
        assertEquals(expected, result);
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        long maxId = 100L;
        paging.setMaxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        long maxId = -1L;
        paging.setMaxId(maxId);
    }
    
    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        long maxId = 100L;
        Paging result = paging.maxId(maxId);
        assertEquals(maxId, result.getMaxId());
    }
    
    @Test
    public void testEqualsSameObject() {
        Paging paging = new Paging();
        assertTrue(paging.equals(paging));
    }
    
    @Test
    public void testEqualsDifferentObject() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging();
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentClass() {
        Paging paging = new Paging();
        String str = "string";
        assertFalse(paging.equals(str));
    }
    
    @Test
    public void testEqualsDifferentPage() {
        Paging paging1 = new Paging(1);
        Paging paging2 = new Paging(2);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentCount() {
        Paging paging1 = new Paging(1, 10);
        Paging paging2 = new Paging(1, 20);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentSinceId() {
        Paging paging1 = new Paging(1, 10, 1L);
        Paging paging2 = new Paging(1, 10, 2L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentMaxId() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 200L);
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        Paging paging = new Paging();
        int expected = -1880317816;
        int result = paging.hashCode();
        assertEquals(expected, result);
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging();
        String expected = "Paging{page=-1, count=-1, sinceId=-1, maxId=-1}";
        String result = paging.toString();
        assertEquals(expected, result);
    }

}