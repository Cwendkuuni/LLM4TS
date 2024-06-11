import org.junit.*;
import static org.junit.Assert.*;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        paging.setSinceId(12345L);
        paging.setMaxId(67890L);
        paging.setCount(10);
        paging.setPage(1);

        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter("since_id", "12345"));
        expected.add(new HttpParameter("max_id", "67890"));
        expected.add(new HttpParameter("count", "10"));
        expected.add(new HttpParameter("page", "1"));

        List<HttpParameter> actual = paging.asPostParameterList();
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        paging.setSinceId(12345L);
        paging.setMaxId(67890L);
        paging.setCount(10);
        paging.setPage(1);

        HttpParameter[] expected = {
            new HttpParameter("since_id", "12345"),
            new HttpParameter("max_id", "67890"),
            new HttpParameter("count", "10"),
            new HttpParameter("page", "1")
        };

        HttpParameter[] actual = paging.asPostParameterArray();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterList_CharArray() {
        Paging paging = new Paging();
        paging.setSinceId(12345L);
        paging.setMaxId(67890L);
        paging.setCount(10);
        paging.setPage(1);

        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter("since_id", "12345"));
        expected.add(new HttpParameter("max_id", "67890"));
        expected.add(new HttpParameter("count", "10"));
        expected.add(new HttpParameter("page", "1"));

        char[] supportedParams = { 's', 'm', 'c', 'p' };
        List<HttpParameter> actual = paging.asPostParameterList(supportedParams);
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterList_CharArray_String() {
        Paging paging = new Paging();
        paging.setSinceId(12345L);
        paging.setMaxId(67890L);
        paging.setCount(10);
        paging.setPage(1);

        List<HttpParameter> expected = new ArrayList<HttpParameter>();
        expected.add(new HttpParameter("since_id", "12345"));
        expected.add(new HttpParameter("max_id", "67890"));
        expected.add(new HttpParameter("count", "10"));
        expected.add(new HttpParameter("page", "1"));

        char[] supportedParams = { 's', 'm', 'c', 'p' };
        String perPageParamName = "count";
        List<HttpParameter> actual = paging.asPostParameterList(supportedParams, perPageParamName);
        assertEquals(expected, actual);
    }

    @Test
    public void testAsPostParameterArray_CharArray_String() {
        Paging paging = new Paging();
        paging.setSinceId(12345L);
        paging.setMaxId(67890L);
        paging.setCount(10);
        paging.setPage(1);

        HttpParameter[] expected = {
            new HttpParameter("since_id", "12345"),
            new HttpParameter("max_id", "67890"),
            new HttpParameter("count", "10"),
            new HttpParameter("page", "1")
        };

        char[] supportedParams = { 's', 'm', 'c', 'p' };
        String perPageParamName = "count";
        HttpParameter[] actual = paging.asPostParameterArray(supportedParams, perPageParamName);
        assertArrayEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameter_NotSupported() {
        Paging paging = new Paging();
        paging.setMaxId(12345L);
        paging.addPostParameter(Paging.S, 'm', new ArrayList<HttpParameter>(), "max_id", 12345L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 12345L, 67890L);
        Paging paging2 = new Paging(1, 10, 12345L, 67890L);
        Paging paging3 = new Paging(1, 20, 12345L, 67890L);

        assertEquals(paging1, paging2);
        assertNotEquals(paging1, paging3);
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 12345L, 67890L);
        Paging paging2 = new Paging(1, 10, 12345L, 67890L);

        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 12345L, 67890L);

        String expected = "Paging{page=1, count=10, sinceId=12345, maxId=67890}";
        String actual = paging.toString();

        assertEquals(expected, actual);
    }
}