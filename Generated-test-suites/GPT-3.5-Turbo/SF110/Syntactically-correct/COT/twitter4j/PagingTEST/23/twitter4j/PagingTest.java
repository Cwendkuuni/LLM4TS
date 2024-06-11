
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray());
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList(supportedParams));
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        assertEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray(supportedParams));
    }

    @Test(expected = IllegalStateException.class)
    public void testAsPostParameterListUnsupportedParam() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        paging.asPostParameterList(supportedParams, "invalidParam");
    }

    @Test(expected = IllegalStateException.class)
    public void testAsPostParameterArrayUnsupportedParam() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        paging.asPostParameterArray(supportedParams, "invalidParam");
    }

    @Test
    public void testAddPostParameterSupported() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(new char[] {'s', 'm', 'c', 'p'}, 's', pagingParams, "since_id", 1234);
        HttpParameter expectedParam = new HttpParameter("since_id", "1234");
        assertEquals(expectedParam, pagingParams.get(0));
    }

    @Test
    public void testAddPostParameterUnsupported() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(new char[] {'s', 'm', 'c', 'p'}, 'x', pagingParams, "invalidParam", 1234);
        assertEquals(0, pagingParams.size());
    }

    @Test
    public void testPagingDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPage() {
        Paging paging = new Paging(10);
        assertEquals(10, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithSinceId() {
        Paging paging = new Paging(1234);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndCount() {
        Paging paging = new Paging(10, 50);
        assertEquals(10, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageAndSinceId() {
        Paging paging = new Paging(10, 1234);
        assertEquals(10, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(10, 50, 1234);
        assertEquals(10, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(10, 50, 1234, 5678);
        assertEquals(10, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(5678L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testPagingCountMethod() {
        Paging paging = new Paging();
        Paging result = paging.count(50);
        assertEquals(50, result.getCount());
    }

    @Test
    public void testPagingSinceIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(1234);
        assertEquals(1234L, result.getSinceId());
    }

    @Test
    public void testPagingMaxIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.maxId(5678);
        assertEquals(5678L, result.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(10, 50, 1234, 5678);
        Paging paging2 = new Paging(10, 50, 1234, 5678);
        assertEquals(true, paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(10, 50, 1234, 5678);
        int expectedHashCode = 10 * 31 * 31 * 31 + 50 * 31 * 31 + 1234 * 31 + 5678;
        assertEquals(expectedHashCode, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(10, 50, 1234, 5678);
        String expectedToString = "Paging{page=10, count=50, sinceId=1234, maxId=5678}";
        assertEquals(expectedToString, paging.toString());
    }
}

