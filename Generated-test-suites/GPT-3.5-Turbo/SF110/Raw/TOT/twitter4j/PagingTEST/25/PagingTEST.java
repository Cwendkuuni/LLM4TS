package twitter4j;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {
    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        paging = new Paging(1234);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        paging = new Paging(2, 1234);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        paging = new Paging(2, 10, 1234);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1234, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        paging = new Paging(2, 10, 1234, 5678);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1234, paging.getSinceId());
        assertEquals(5678, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        paging.setMaxId(-1L);
    }

    @Test
    public void testCountMethod() {
        paging.count(15);
        assertEquals(15, paging.getCount());
    }

    @Test
    public void testSinceIdMethod() {
        paging.sinceId(9876);
        assertEquals(9876, paging.getSinceId());
    }

    @Test
    public void testMaxIdMethod() {
        paging.maxId(5432);
        assertEquals(5432, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        paging.setMaxId(1234);
        List<HttpParameter> params = paging.asPostParameterList();
        assertEquals(1, params.size());
        assertEquals("max_id", params.get(0).getName());
        assertEquals("1234", params.get(0).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        paging.setSinceId(5678);
        paging.setCount(10);
        HttpParameter[] params = paging.asPostParameterArray();
        assertEquals(2, params.length);
        assertEquals("since_id", params[0].getName());
        assertEquals("5678", params[0].getValue());
        assertEquals("count", params[1].getName());
        assertEquals("10", params[1].getValue());
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        paging.setSinceId(1234);
        paging.setPage(2);
        List<HttpParameter> params = paging.asPostParameterList(new char[]{'s'});
        assertEquals(1, params.size());
        assertEquals("since_id", params.get(0).getName());
        assertEquals("1234", params.get(0).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsPostParameterListWithUnsupportedParams() {
        paging.setSinceId(1234);
        paging.setMaxId(5678);
        paging.asPostParameterList(new char[]{'s'});
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        paging.setSinceId(1234);
        HttpParameter[] params = paging.asPostParameterArray(new char[]{'s'});
        assertEquals(1, params.length);
        assertEquals("since_id", params[0].getName());
        assertEquals("1234", params[0].getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAsPostParameterArrayWithUnsupportedParams() {
        paging.setSinceId(1234);
        paging.setMaxId(5678);
        paging.asPostParameterArray(new char[]{'s'});
    }

    @Test
    public void testEquals() {
        paging.setPage(2);
        Paging anotherPaging = new Paging(2);
        assertTrue(paging.equals(anotherPaging));
    }

    @Test
    public void testNotEquals() {
        paging.setPage(2);
        Paging anotherPaging = new Paging(3);
        assertFalse(paging.equals(anotherPaging));
    }

    @Test
    public void testHashCode() {
        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(1234);
        paging.setMaxId(5678);
        int expectedHashCode = 31 * (31 * (31 * 2 + 10) + Long.hashCode(1234)) + Long.hashCode(5678);
        assertEquals(expectedHashCode, paging.hashCode());
    }

    @Test
    public void testToString() {
        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(1234);
        paging.setMaxId(5678);
        String expectedToString = "Paging{page=2, count=10, sinceId=1234, maxId=5678}";
        assertEquals(expectedToString, paging.toString());
    }
}