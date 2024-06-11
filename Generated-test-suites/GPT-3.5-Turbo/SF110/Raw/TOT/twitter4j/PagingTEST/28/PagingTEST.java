import org.junit.Test;
 
public class PagingTest {
 
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] expected = Paging.NULL_PARAMETER_ARRAY;
        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }
 
    @Test
    public void testAsPostParameterList_CharArray() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        List<HttpParameter> actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);
    }
 
    @Test
    public void testAsPostParameterList_CharArray_String() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;
        String perPageParamName = "count";
        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        List<HttpParameter> actual = paging.asPostParameterList(supportedParams, perPageParamName);
        assertEquals(expected, actual);
    }
 
    @Test
    public void testAsPostParameterArray_CharArray_String() {
        Paging paging = new Paging();
        char[] supportedParams = Paging.SMCP;
        String perPageParamName = "count";
        HttpParameter[] expected = Paging.NULL_PARAMETER_ARRAY;
        HttpParameter[] actual = paging.asPostParameterArray(supportedParams, perPageParamName);
        assertArrayEquals(expected, actual);
    }
 
    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        char paramKey = 's';
        List<HttpParameter> pagingParams = new ArrayList<HttpParameter>();
        String paramName = "since_id";
        long paramValue = paging.getSinceId();
        Assert.fail("Test case not implemented");
    }
 
    @Test
    public void testPaging_DefaultConstructor() {
        Paging paging = new Paging();
        int expected = -1;
        int actual = paging.getPage();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testPaging_IntConstructor() {
        int page = 2;
        Paging paging = new Paging(page);
        int expected = page;
        int actual = paging.getPage();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testPaging_LongConstructor() {
        long sinceId = 12345L;
        Paging paging = new Paging(sinceId);
        long expected = sinceId;
        long actual = paging.getSinceId();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testPaging_IntIntConstructor() {
        int page = 2;
        int count = 20;
        Paging paging = new Paging(page, count);
        int expectedPage = page;
        int actualPage = paging.getPage();
        assertEquals(expectedPage, actualPage);
        int expectedCount = count;
        int actualCount = paging.getCount();
        assertEquals(expectedCount, actualCount);
    }
 
    @Test
    public void testPaging_IntLongConstructor() {
        int page = 2;
        long sinceId = 12345L;
        Paging paging = new Paging(page, sinceId);
        int expectedPage = page;
        int actualPage = paging.getPage();
        assertEquals(expectedPage, actualPage);
        long expectedSinceId = sinceId;
        long actualSinceId = paging.getSinceId();
        assertEquals(expectedSinceId, actualSinceId);
    }
 
    @Test
    public void testPaging_IntIntLongConstructor() {
        int page = 2;
        int count = 20;
        long sinceId = 12345L;
        Paging paging = new Paging(page, count, sinceId);
        int expectedPage = page;
        int actualPage = paging.getPage();
        assertEquals(expectedPage, actualPage);
        int expectedCount = count;
        int actualCount = paging.getCount();
        assertEquals(expectedCount, actualCount);
        long expectedSinceId = sinceId;
        long actualSinceId = paging.getSinceId();
        assertEquals(expectedSinceId, actualSinceId);
    }
 
    @Test
    public void testPaging_IntIntLongLongConstructor() {
        int page = 2;
        int count = 20;
        long sinceId = 12345L;
        long maxId = 54321L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        int expectedPage = page;
        int actualPage = paging.getPage();
        assertEquals(expectedPage, actualPage);
        int expectedCount = count;
        int actualCount = paging.getCount();
        assertEquals(expectedCount, actualCount);
        long expectedSinceId = sinceId;
        long actualSinceId = paging.getSinceId();
        assertEquals(expectedSinceId, actualSinceId);
        long expectedMaxId = maxId;
        long actualMaxId = paging.getMaxId();
        assertEquals(expectedMaxId, actualMaxId);
    }
 
    @Test
    public void testGetPage() {
        int page = 2;
        Paging paging = new Paging(page);
        int expected = page;
        int actual = paging.getPage();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testSetPage_ValidInput() {
        int page = 2;
        Paging paging = new Paging();
        paging.setPage(page);
        int expected = page;
        int actual = paging.getPage();
        assertEquals(expected, actual);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_InvalidInput() {
        int page = -2;
        Paging paging = new Paging();
        paging.setPage(page);
    }
 
    @Test
    public void testGetCount() {
        int count = 20;
        Paging paging = new Paging();
        paging.setCount(count);
        int expected = count;
        int actual = paging.getCount();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testSetCount_ValidInput() {
        int count = 20;
        Paging paging = new Paging();
        paging.setCount(count);
        int expected = count;
        int actual = paging.getCount();
        assertEquals(expected, actual);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_InvalidInput() {
        int count = -20;
        Paging paging = new Paging();
        paging.setCount(count);
    }
 
    @Test
    public void testCount() {
        int count = 20;
        Paging paging = new Paging();
        Paging updatedPaging = paging.count(count);
        assertEquals(paging, updatedPaging);
    }
 
    @Test
    public void testGetSinceId() {
        long sinceId = 12345L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
        long expected = sinceId;
        long actual = paging.getSinceId();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testSetSinceId_ValidInput() {
        long sinceId = 12345L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
        long expected = sinceId;
        long actual = paging.getSinceId();
        assertEquals(expected, actual);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_InvalidInput() {
        long sinceId = -12345L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
    }
 
    @Test
    public void testSinceId() {
        long sinceId = 12345L;
        Paging paging = new Paging();
        Paging updatedPaging = paging.sinceId(sinceId);
        assertEquals(paging, updatedPaging);
    }
 
    @Test
    public void testGetMaxId() {
        long maxId = 54321L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        long expected = maxId;
        long actual = paging.getMaxId();
        assertEquals(expected, actual);
    }
 
    @Test
    public void testSetMaxId_ValidInput() {
        long maxId = 54321L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        long expected = maxId;
        long actual = paging.getMaxId();
        assertEquals(expected, actual);
    }
 
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_InvalidInput() {
        long maxId = -54321L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
    }
 
    @Test
    public void testMaxId() {
        long maxId = 54321L;
        Paging paging = new Paging();
        Paging updatedPaging = paging.maxId(maxId);
        assertEquals(paging, updatedPaging);
    }
 
    @Test
    public void testEquals_SameObject() {
        Paging paging = new Paging();
        assertTrue(paging.equals(paging));
    }
 
    @Test
    public void testEquals_NullObject() {
        Paging paging = new Paging();
        assertFalse(paging.equals(null));
    }
 
    @Test
    public void testEquals_DifferentObject() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging(2);
        assertFalse(paging1.equals(paging2));
    }
 
    @Test
    public void testHashCode_SameObject() {
        Paging paging = new Paging();
        int hashCode1 = paging.hashCode();
        int hashCode2 = paging.hashCode();
        assertEquals(hashCode1, hashCode2);
    }
 
    @Test
    public void testHashCode_DifferentObjects() {
        Paging paging1 = new Paging();
        Paging paging2 = new Paging(2);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }
 
    @Test
    public void testToString() {
        Paging paging = new Paging();
        String expected = "Paging{page=-1, count=-1, sinceId=-1, maxId=-1}";
        String actual = paging.toString();
        assertEquals(expected, actual);
    }
 
}