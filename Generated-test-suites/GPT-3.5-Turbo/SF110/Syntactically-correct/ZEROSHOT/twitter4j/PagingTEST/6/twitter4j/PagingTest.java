import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> expected = new ArrayList<>();
        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] expected = new HttpParameter[0];
        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterListChar() {
        Paging paging = new Paging();
        List<HttpParameter> expected = new ArrayList<>();
        List<HttpParameter> actual = paging.asPostParameterList(Paging.SMCP);
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArrayCharString() {
        Paging paging = new Paging();
        HttpParameter[] expected = new HttpParameter[0];
        HttpParameter[] actual = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        String paramName = "since_id";
        long paramValue = 1L;
        paging.addPostParameter(supportedParams, 's', pagingParams, paramName, paramValue);
        assertEquals(1, pagingParams.size());
        assertEquals(new HttpParameter(paramName, "1"), pagingParams.get(0));
    }

    @Test
    public void testPaging() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingInt() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingLong() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingIntInt() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingIntLong() {
        Paging paging = new Paging(1, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingIntIntLong() {
        Paging paging = new Paging(1, 10, 1L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingIntIntLongLong() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(100L, paging.getMaxId());
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging result = paging.count(10);
        assertEquals(10, paging.getCount());
        assertSame(paging, result);
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(1L);
        assertEquals(1L, paging.getSinceId());
        assertSame(paging, result);
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(1L);
        assertEquals(1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging result = paging.maxId(1L);
        assertEquals(1L, paging.getMaxId());
        assertSame(paging, result);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        Paging paging3 = new Paging(2, 20, 2L, 200L);

        assertTrue(paging1.equals(paging1));
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1L, 100L);
        Paging paging2 = new Paging(1, 10, 1L, 100L);
        Paging paging3 = new Paging(2, 20, 2L, 200L);

        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1L, 100L);
        String expected = "Paging{page=1, count=10, sinceId=1, maxId=100}";
        String actual = paging.toString();
        assertEquals(expected, actual);
    }
}