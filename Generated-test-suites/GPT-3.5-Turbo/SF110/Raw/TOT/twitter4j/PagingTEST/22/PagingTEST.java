
import org.junit.Before;
import org.junit.Test;
import twitter4j.internal.http.HttpParameter;

import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    // Testing asPostParameterList() method
    @Test
    public void testAsPostParameterList() {
        List<HttpParameter> parameterList = paging.asPostParameterList();
        assertNotNull(parameterList);
        assertEquals(1, parameterList.size());
        HttpParameter parameter = parameterList.get(0);
        assertEquals("count", parameter.getName());
        assertNull(parameter.getValue());
    }

    // Testing asPostParameterArray() method
    @Test
    public void testAsPostParameterArray() {
        HttpParameter[] parameterArray = paging.asPostParameterArray();
        assertNotNull(parameterArray);
        assertEquals(1, parameterArray.length);
        HttpParameter parameter = parameterArray[0];
        assertEquals("count", parameter.getName());
        assertNull(parameter.getValue());
    }

    // Testing asPostParameterList(char[] supportedParams) method
    @Test
    public void testAsPostParameterListWithSupportedParams() {
        char[] supportedParams = {'s', 'm'};
        List<HttpParameter> parameterList = paging.asPostParameterList(supportedParams);
        assertNotNull(parameterList);
        assertEquals(2, parameterList.size());
        HttpParameter sinceIdParameter = parameterList.get(0);
        assertEquals("since_id", sinceIdParameter.getName());
        assertNull(sinceIdParameter.getValue());
        HttpParameter maxIdParameter = parameterList.get(1);
        assertEquals("max_id", maxIdParameter.getName());
        assertNull(maxIdParameter.getValue());
    }

    // Testing asPostParameterArray(char[] supportedParams) method
    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        char[] supportedParams = {'s', 'm'};
        HttpParameter[] parameterArray = paging.asPostParameterArray(supportedParams);
        assertNotNull(parameterArray);
        assertEquals(2, parameterArray.length);
        HttpParameter sinceIdParameter = parameterArray[0];
        assertEquals("since_id", sinceIdParameter.getName());
        assertNull(sinceIdParameter.getValue());
        HttpParameter maxIdParameter = parameterArray[1];
        assertEquals("max_id", maxIdParameter.getName());
        assertNull(maxIdParameter.getValue());
    }

    // Testing asPostParameterList(char[] supportedParams, String perPageParamName) method
    @Test
    public void testAsPostParameterListWithSupportedParamsAndPerPageParamName() {
        char[] supportedParams = {'s', 'm', 'c'};
        String perPageParamName = "per_page";
        List<HttpParameter> parameterList = paging.asPostParameterList(supportedParams, perPageParamName);
        assertNotNull(parameterList);
        assertEquals(3, parameterList.size());
        HttpParameter sinceIdParameter = parameterList.get(0);
        assertEquals("since_id", sinceIdParameter.getName());
        assertNull(sinceIdParameter.getValue());
        HttpParameter maxIdParameter = parameterList.get(1);
        assertEquals("max_id", maxIdParameter.getName());
        assertNull(maxIdParameter.getValue());
        HttpParameter countParameter = parameterList.get(2);
        assertEquals(perPageParamName, countParameter.getName());
        assertNull(countParameter.getValue());
    }

    // Testing asPostParameterArray(char[] supportedParams, String perPageParamName) method
    @Test
    public void testAsPostParameterArrayWithSupportedParamsAndPerPageParamName() {
        char[] supportedParams = {'s', 'm', 'c'};
        String perPageParamName = "per_page";
        HttpParameter[] parameterArray = paging.asPostParameterArray(supportedParams, perPageParamName);
        assertNotNull(parameterArray);
        assertEquals(3, parameterArray.length);
        HttpParameter sinceIdParameter = parameterArray[0];
        assertEquals("since_id", sinceIdParameter.getName());
        assertNull(sinceIdParameter.getValue());
        HttpParameter maxIdParameter = parameterArray[1];
        assertEquals("max_id", maxIdParameter.getName());
        assertNull(maxIdParameter.getValue());
        HttpParameter countParameter = parameterArray[2];
        assertEquals(perPageParamName, countParameter.getName());
        assertNull(countParameter.getValue());
    }

    // Testing setPage(int page) method
    @Test
    public void testSetPage() {
        int page = 5;
        paging.setPage(page);
        assertEquals(page, paging.getPage());
    }

    // Testing incorrect input for setPage(int page) method
    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativePage() {
        int page = -5;
        paging.setPage(page);
    }

    // Testing getCount() method
    @Test
    public void testGetCount() {
        assertEquals(-1, paging.getCount());
    }

    // Testing setCount(int count) method
    @Test
    public void testSetCount() {
        int count = 20;
        paging.setCount(count);
        assertEquals(count, paging.getCount());
    }

    // Testing incorrect input for setCount(int count) method
    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeCount() {
        int count = -10;
        paging.setCount(count);
    }

    // Testing count(int count) method
    @Test
    public void testCount() {
        int count = 30;
        paging.count(count);
        assertEquals(count, paging.getCount());
    }

    // Testing getSinceId() method
    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }

    // Testing setSinceId(long sinceId) method
    @Test
    public void testSetSinceId() {
        long sinceId = 100L;
        paging.setSinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }

    // Testing incorrect input for setSinceId(long sinceId) method
    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeSinceId() {
        long sinceId = -100L;
        paging.setSinceId(sinceId);
    }

    // Testing sinceId(long sinceId) method
    @Test
    public void testSinceId() {
        long sinceId = 200L;
        paging.sinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }

    // Testing getMaxId() method
    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }

    // Testing setMaxId(long maxId) method
    @Test
    public void testSetMaxId() {
        long maxId = 300L;
        paging.setMaxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }

    // Testing incorrect input for setMaxId(long maxId) method
    @Test(expected = IllegalArgumentException.class)
    public void testSetNegativeMaxId() {
        long maxId = -300L;
        paging.setMaxId(maxId);
    }

    // Testing maxId(long maxId) method
    @Test
    public void testMaxId() {
        long maxId = 400L;
        paging.maxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }

    // Testing equals(Object o)
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 20, 100L, 200L);
        Paging paging2 = new Paging(1, 20, 100L, 200L);
        assertTrue(paging1.equals(paging2));
    }

    // Testing equals(Object o) with different objects
    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 20, 100L, 200L);
        Paging paging2 = new Paging(2, 20, 100L, 200L);
        assertFalse(paging1.equals(paging2));
    }

    // Testing hashCode()
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 20, 100L, 200L);
        Paging paging2 = new Paging(1, 20, 100L, 200L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    // Testing toString()
    @Test
    public void testToString() {
        int page = 1;
        int count = 20;
        long sinceId = 100L;
        long maxId = 200L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        String expected = "Paging{page=" + page + ", count=" + count + ", sinceId=" + sinceId + ", maxId=" + maxId + '}';
        assertEquals(expected, paging.toString());
    }
}

