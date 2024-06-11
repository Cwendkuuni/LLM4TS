
import org.junit.Test;

public class PagingTest {

    @Test
    public void testAsPostParameterList(){
        Paging paging = new Paging();
        List<HttpParameter> result = paging.asPostParameterList();
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterArray(){
        Paging paging = new Paging();
        HttpParameter[] result = paging.asPostParameterArray();
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterList_CharArray(){
        Paging paging = new Paging();
        char[] supportedParams = {'a', 'b', 'c'};
        List<HttpParameter> result = paging.asPostParameterList(supportedParams);
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterArray_CharArray(){
        Paging paging = new Paging();
        char[] supportedParams = {'a', 'b', 'c'};
        HttpParameter[] result = paging.asPostParameterArray(supportedParams);
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterList_CharArray_String(){
        Paging paging = new Paging();
        char[] supportedParams = {'a', 'b', 'c'};
        String perPageParamName = "per_page";
        List<HttpParameter> result = paging.asPostParameterList(supportedParams, perPageParamName);
        assertNotNull(result);
    }

    @Test
    public void testAsPostParameterArray_CharArray_String(){
        Paging paging = new Paging();
        char[] supportedParams = {'a', 'b', 'c'};
        String perPageParamName = "per_page";
        HttpParameter[] result = paging.asPostParameterArray(supportedParams, perPageParamName);
        assertNotNull(result);
    }

    @Test
    public void testAddPostParameter(){
        Paging paging = new Paging();
        char[] supportedParams = {'a', 'b', 'c'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        String paramName = "since_id";
        long paramValue = 1L;
        paging.addPostParameter(supportedParams, 's', pagingParams, paramName, paramValue);
        assertFalse(pagingParams.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameter_Exception(){
        Paging paging = new Paging();
        char[] supportedParams = {'a', 'b', 'c'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        String paramName = "since_id";
        long paramValue = 1L;
        paging.addPostParameter(supportedParams, 'x', pagingParams, paramName, paramValue);
    }

    @Test
    public void testPaging_DefaultConstructor(){
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageConstructor(){
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_SinceIdConstructor(){
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageCountConstructor(){
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageSinceIdConstructor(){
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageCountSinceIdConstructor(){
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageCountSinceIdMaxIdConstructor(){
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGetPage(){
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_Exception(){
        Paging paging = new Paging();
        paging.setPage(0);
    }

    @Test
    public void testGetCount(){
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_Exception(){
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testCount_Method(){
        Paging paging = new Paging();
        Paging result = paging.count(20);
        assertEquals(20, paging.getCount());
        assertEquals(paging, result);
    }

    @Test
    public void testGetSinceId(){
        Paging paging = new Paging();
        paging.setSinceId(10L);
        assertEquals(10L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_Exception(){
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }

    @Test
    public void testSinceId_Method(){
        Paging paging = new Paging();
        Paging result = paging.sinceId(15L);
        assertEquals(15L, paging.getSinceId());
        assertEquals(paging, result);
    }

    @Test
    public void testGetMaxId(){
        Paging paging = new Paging();
        paging.setMaxId(100L);
        assertEquals(100L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_Exception(){
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }

    @Test
    public void testMaxId_Method(){
        Paging paging = new Paging();
        Paging result = paging.maxId(200L);
        assertEquals(200L, paging.getMaxId());
        assertEquals(paging, result);
    }

    @Test
    public void testEquals(){
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode(){
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString(){
        Paging paging = new Paging(1, 10, 1L, 100L);
        String expected = "Paging{page=1, count=10, sinceId=1, maxId=100}";
        assertEquals(expected, paging.toString());
    }

}
