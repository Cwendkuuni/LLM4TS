import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

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
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSetter() {
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageSetter() {
        paging.setPage(-5);
    }

    @Test
    public void testCountSetter() {
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountSetter() {
        paging.setCount(-10);
    }

    @Test
    public void testSinceIdSetter() {
        paging.setSinceId(1234567890L);
        assertEquals(1234567890L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdSetter() {
        paging.setSinceId(-1234567890L);
    }

    @Test
    public void testMaxIdSetter() {
        paging.setMaxId(9876543210L);
        assertEquals(9876543210L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdSetter() {
        paging.setMaxId(-9876543210L);
    }

    @Test
    public void testPagingConstructorWithPage() {
        Paging paging = new Paging(3);
        assertEquals(3, paging.getPage());
    }

    @Test
    public void testPagingConstructorWithSinceId() {
        Paging paging = new Paging(1234567890L);
        assertEquals(1234567890L, paging.getSinceId());
    }

    @Test
    public void testPagingConstructorWithPageAndCount() {
        Paging paging = new Paging(2, 20);
        assertEquals(2, paging.getPage());
        assertEquals(20, paging.getCount());
    }

    @Test
    public void testPagingConstructorWithPageAndSinceId() {
        Paging paging = new Paging(3, 1234567890L);
        assertEquals(3, paging.getPage());
        assertEquals(1234567890L, paging.getSinceId());
    }

    @Test
    public void testPagingConstructorWithPageCountSinceId() {
        Paging paging = new Paging(4, 30, 1234567890L);
        assertEquals(4, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(1234567890L, paging.getSinceId());
    }

    @Test
    public void testPagingConstructorWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(5, 40, 1234567890L, 9876543210L);
        assertEquals(5, paging.getPage());
        assertEquals(40, paging.getCount());
        assertEquals(1234567890L, paging.getSinceId());
        assertEquals(9876543210L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(1, 10, 1234567890L, 9876543210L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(2, 20, 1234567890L, 9876543210L);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(1, 10, 1234567890L, 9876543210L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        String expected = "Paging{page=1, count=10, sinceId=1234567890, maxId=9876543210}";
        assertEquals(expected, paging.toString());
    }

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        List<HttpParameter> parameters = paging.asPostParameterList();
        assertEquals(4, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("10", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("1234567890", parameters.get(2).getValue());
        assertEquals("max_id", parameters.get(3).getName());
        assertEquals("9876543210", parameters.get(3).getValue());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        HttpParameter[] parameters = paging.asPostParameterArray();
        assertEquals(4, parameters.length);
        assertEquals("page", parameters[0].getName());
        assertEquals("1", parameters[0].getValue());
        assertEquals("count", parameters[1].getName());
        assertEquals("10", parameters[1].getValue());
        assertEquals("since_id", parameters[2].getName());
        assertEquals("1234567890", parameters[2].getValue());
        assertEquals("max_id", parameters[3].getName());
        assertEquals("9876543210", parameters[3].getValue());
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        char[] supportedParams = {'p', 'c', 's'};
        List<HttpParameter> parameters = paging.asPostParameterList(supportedParams);
        assertEquals(3, parameters.size());
        assertEquals("page", parameters.get(0).getName());
        assertEquals("1", parameters.get(0).getValue());
        assertEquals("count", parameters.get(1).getName());
        assertEquals("10", parameters.get(1).getValue());
        assertEquals("since_id", parameters.get(2).getName());
        assertEquals("1234567890", parameters.get(2).getValue());
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        char[] supportedParams = {'p', 'c', 's'};
        HttpParameter[] parameters = paging.asPostParameterArray(supportedParams);
        assertEquals(3, parameters.length);
        assertEquals("page", parameters[0].getName());
        assertEquals("1", parameters[0].getValue());
        assertEquals("count", parameters[1].getName());
        assertEquals("10", parameters[1].getValue());
        assertEquals("since_id", parameters[2].getName());
        assertEquals("1234567890", parameters[2].getValue());
    }
}