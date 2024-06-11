import org.junit.Test;
import twitter4j.HttpParameter;

import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1);
        assertEquals(Paging.NULL_PARAMETER_LIST, paging.asPostParameterList());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1);
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray());
    }

    @Test
    public void testAsPostParameterListWithParams() {
        Paging paging = new Paging(1, 10, 1234, 5678);
        assertEquals(4, paging.asPostParameterList(Paging.SMCP, "count").size());
    }

    @Test
    public void testAsPostParameterArrayWithParams() {
        Paging paging = new Paging(1, 10, 1234, 5678);
        assertArrayEquals(new HttpParameter[] {
                new HttpParameter("since_id", "1234"),
                new HttpParameter("max_id", "5678"),
                new HttpParameter("count", "10"),
                new HttpParameter("page", "1")
        }, paging.asPostParameterArray(Paging.SMCP, "count"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging(1);
        paging.addPostParameter(Paging.S, 'm', null, "max_id", 1234);
    }

    @Test
    public void testConstructorDefault() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1234);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1234);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1234);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 1234, 5678);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(5678L, paging.getMaxId());
    }

    @Test
    public void testGettersAndSetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
        paging.setCount(10);
        assertEquals(10, paging.getCount());
        paging.setSinceId(1234);
        assertEquals(1234L, paging.getSinceId());
        paging.setMaxId(5678);
        assertEquals(5678L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1234, 5678);
        Paging paging2 = new Paging(1, 10, 1234, 5678);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10, 1234, 5678);
        assertEquals(1062216873, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1234, 5678);
        assertEquals("Paging{page=1, count=10, sinceId=1234, maxId=5678}", paging.toString());
    }

}