import org.junit.*;
import static org.junit.Assert.*;
import java.util.List;
import java.util.ArrayList;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageConstructor() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(1L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(1, 10L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(10L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(1, 10, 100L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(100L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testAllConstructor() {
        Paging paging = new Paging(1, 10, 100L, 200L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(100L, paging.getSinceId());
        assertEquals(200L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 100L, 200L);
        char[] supportedParams = new char[] { 's', 'm', 'c', 'p' };
        List<HttpParameter> expected = new ArrayList<>();
        expected.add(new HttpParameter("since_id", "100"));
        expected.add(new HttpParameter("max_id", "200"));
        expected.add(new HttpParameter("count", "10"));
        expected.add(new HttpParameter("page", "1"));
        assertEquals(expected, paging.asPostParameterList(supportedParams));
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 100L, 200L);
        char[] supportedParams = new char[] { 's', 'm', 'c', 'p' };
        HttpParameter[] expected = new HttpParameter[4];
        expected[0] = new HttpParameter("since_id", "100");
        expected[1] = new HttpParameter("max_id", "200");
        expected[2] = new HttpParameter("count", "10");
        expected[3] = new HttpParameter("page", "1");
        assertArrayEquals(expected, paging.asPostParameterArray(supportedParams));
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 100L, 200L);
        Paging paging2 = new Paging(1, 10, 100L, 200L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 10, 100L, 200L);
        Paging paging2 = new Paging(2, 10, 100L, 200L);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10, 100L, 200L);
        assertEquals(349379271, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 100L, 200L);
        assertEquals("Paging{page=1, count=10, sinceId=100, maxId=200}", paging.toString());
    }
}