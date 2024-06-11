package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        paging = new Paging(2, 50);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        paging = new Paging(2, 50, 1000000L);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(1000000L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());

        paging = new Paging(2, 50, 1000000L, 2000000L);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(1000000L, paging.getSinceId());
        assertEquals(2000000L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-5);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1000000L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-2000000L);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> expectedList = UtilTest.NULL_PARAMETER_LIST;
        List<HttpParameter> actualList = paging.asPostParameterList();
        assertEquals(expectedList, actualList);

        paging.setSinceId(1000000L);
        expectedList = Arrays.asList(new HttpParameter("since_id", "1000000"));
        actualList = paging.asPostParameterList();
        assertEquals(expectedList, actualList);

        paging.setMaxId(2000000L);
        expectedList = Arrays.asList(new HttpParameter("since_id", "1000000"), new HttpParameter("max_id", "2000000"));
        actualList = paging.asPostParameterList();
        assertEquals(expectedList, actualList);

        paging.setCount(50);
        expectedList = Arrays.asList(new HttpParameter("since_id", "1000000"), new HttpParameter("max_id", "2000000"), new HttpParameter("count", "50"));
        actualList = paging.asPostParameterList();
        assertEquals(expectedList, actualList);

        paging.setPage(2);
        expectedList = Arrays.asList(new HttpParameter("since_id", "1000000"), new HttpParameter("max_id", "2000000"), new HttpParameter("count", "50"), new HttpParameter("page", "2"));
        actualList = paging.asPostParameterList();
        assertEquals(expectedList, actualList);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] expectedArray = UtilTest.NULL_PARAMETER_ARRAY;
        HttpParameter[] actualArray = paging.asPostParameterArray();
        assertArrayEquals(expectedArray, actualArray);

        paging.setSinceId(1000000L);
        expectedArray = new HttpParameter[]{new HttpParameter("since_id", "1000000")};
        actualArray = paging.asPostParameterArray();
        assertArrayEquals(expectedArray, actualArray);

        paging.setMaxId(2000000L);
        expectedArray = new HttpParameter[]{new HttpParameter("since_id", "1000000"), new HttpParameter("max_id", "2000000")};
        actualArray = paging.asPostParameterArray();
        assertArrayEquals(expectedArray, actualArray);

        paging.setCount(50);
        expectedArray = new HttpParameter[]{new HttpParameter("since_id", "1000000"), new HttpParameter("max_id", "2000000"), new HttpParameter("count", "50")};
        actualArray = paging.asPostParameterArray();
        assertArrayEquals(expectedArray, actualArray);

        paging.setPage(2);
        expectedArray = new HttpParameter[]{new HttpParameter("since_id", "1000000"), new HttpParameter("max_id", "2000000"), new HttpParameter("count", "50"), new HttpParameter("page", "2")};
        actualArray = paging.asPostParameterArray();
        assertArrayEquals(expectedArray, actualArray);
    }

    private void assertArrayEquals(HttpParameter[] expected, HttpParameter[] actual) {
        assertEquals(expected.length, actual.length);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(expected[i], actual[i]);
        }
    }
}