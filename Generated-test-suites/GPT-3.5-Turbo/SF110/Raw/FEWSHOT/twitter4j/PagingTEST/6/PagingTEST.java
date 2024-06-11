import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    @Test
    public void testConstructor_NoArgs() {
        Paging paging = new Paging();

        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructor_PageArg() {
        int page = 3;
        Paging paging = new Paging(page);

        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructor_SinceIdArg() {
        long sinceId = 123456789L;
        Paging paging = new Paging(sinceId);

        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructor_PageAndCountArgs() {
        int page = 2;
        int count = 100;
        Paging paging = new Paging(page, count);

        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructor_PageAndSinceIdArgs() {
        int page = 4;
        long sinceId = 987654321L;
        Paging paging = new Paging(page, sinceId);

        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructor_PageCountAndSinceIdArgs() {
        int page = 1;
        int count = 50;
        long sinceId = 1234567890L;
        Paging paging = new Paging(page, count, sinceId);

        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructor_AllArgs() {
        int page = 1;
        int count = 50;
        long sinceId = 1234567890L;
        long maxId = 999999999L;
        Paging paging = new Paging(page, count, sinceId, maxId);

        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(maxId, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_NegativePage() {
        Paging paging = new Paging();
        int page = -5;
        paging.setPage(page);
    }

    @Test
    public void testSetPage_PositivePage() {
        Paging paging = new Paging();
        int page = 5;
        paging.setPage(page);

        assertEquals(page, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_NegativeCount() {
        Paging paging = new Paging();
        int count = -50;
        paging.setCount(count);
    }

    @Test
    public void testSetCount_PositiveCount() {
        Paging paging = new Paging();
        int count = 100;
        paging.setCount(count);

        assertEquals(count, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_NegativeSinceId() {
        Paging paging = new Paging();
        long sinceId = -123456789L;
        paging.setSinceId(sinceId);
    }

    @Test
    public void testSetSinceId_PositiveSinceId() {
        Paging paging = new Paging();
        long sinceId = 123456789L;
        paging.setSinceId(sinceId);

        assertEquals(sinceId, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_NegativeMaxId() {
        Paging paging = new Paging();
        long maxId = -987654321L;
        paging.setMaxId(maxId);
    }

    @Test
    public void testSetMaxId_PositiveMaxId() {
        Paging paging = new Paging();
        long maxId = 987654321L;
        paging.setMaxId(maxId);

        assertEquals(maxId, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        List<HttpParameter> parameters = paging.asPostParameterList();

        assertEquals(4, parameters.size());
        assertEquals("s", parameters.get(0).getName());
        assertEquals("since_id", parameters.get(0).getValue());
        assertEquals("m", parameters.get(1).getName());
        assertEquals("max_id", parameters.get(1).getValue());
        assertEquals("c", parameters.get(2).getName());
        assertEquals("count", parameters.get(2).getValue());
        assertEquals("p", parameters.get(3).getName());
        assertEquals("page", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        HttpParameter[] parameters = paging.asPostParameterArray();

        assertEquals(4, parameters.length);
        assertEquals("s", parameters[0].getName());
        assertEquals("since_id", parameters[0].getValue());
        assertEquals("m", parameters[1].getName());
        assertEquals("max_id", parameters[1].getValue());
        assertEquals("c", parameters[2].getName());
        assertEquals("count", parameters[2].getValue());
        assertEquals("p", parameters[3].getName());
        assertEquals("page", parameters[3].getValue());
    }

    @Test
    public void testAsPostParameterList_CharArray() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        char[] supportedParams = new char[]{'s', 'm', 'c', 'p'};
        List<HttpParameter> parameters = paging.asPostParameterList(supportedParams);

        assertEquals(4, parameters.size());
        assertEquals("s", parameters.get(0).getName());
        assertEquals("since_id", parameters.get(0).getValue());
        assertEquals("m", parameters.get(1).getName());
        assertEquals("max_id", parameters.get(1).getValue());
        assertEquals("c", parameters.get(2).getName());
        assertEquals("count", parameters.get(2).getValue());
        assertEquals("p", parameters.get(3).getName());
        assertEquals("page", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray_CharArray() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        char[] supportedParams = new char[]{'s', 'm', 'c', 'p'};
        HttpParameter[] parameters = paging.asPostParameterArray(supportedParams);

        assertEquals(4, parameters.length);
        assertEquals("s", parameters[0].getName());
        assertEquals("since_id", parameters[0].getValue());
        assertEquals("m", parameters[1].getName());
        assertEquals("max_id", parameters[1].getValue());
        assertEquals("c", parameters[2].getName());
        assertEquals("count", parameters[2].getValue());
        assertEquals("p", parameters[3].getName());
        assertEquals("page", parameters[3].getValue());
    }

    @Test
    public void testEquals_SameObject() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);

        assertTrue(paging.equals(paging));
    }

    @Test
    public void testEquals_DifferentClass() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        Object obj = new Object();

        assertTrue(!paging.equals(obj));
    }

    @Test
    public void testEquals_EqualObjects() {
        Paging paging1 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(1, 10, 123456789L, 987654321L);

        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);

        int expectedHashCode = 31 * 31 * 31 * 1 + 31 * 31 * 10 + 31 * Long.valueOf(123456789L).hashCode() + Long.valueOf(987654321L).hashCode();
        assertEquals(expectedHashCode, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);

        String expectedToString = "Paging{page=1, count=10, sinceId=123456789, maxId=987654321}";
        assertEquals(expectedToString, paging.toString());
    }
}