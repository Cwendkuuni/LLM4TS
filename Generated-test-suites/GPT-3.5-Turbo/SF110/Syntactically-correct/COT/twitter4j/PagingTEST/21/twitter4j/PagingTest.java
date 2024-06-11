import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testConstructorWithPage() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(12345L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(12345L, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(3, 10);
        assertEquals(3, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(4, 56789L);
        assertEquals(4, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(56789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(5, 20, 98765L);
        assertEquals(5, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(98765L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(6, 30, 12345L, 67890L);
        assertEquals(6, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(12345L, paging.getSinceId());
        assertEquals(67890L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 12345L, 67890L);
        Paging paging2 = new Paging(1, 10, 12345L, 67890L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 10, 12345L, 67890L);
        Paging paging2 = new Paging(2, 10, 12345L, 67890L);
        assertFalse(paging1.equals(paging2));
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
        assertEquals(expected, paging.toString());
    }

    // Add additional test cases for edge cases and exception handling scenarios
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithZeroValue() {
        Paging paging = new Paging();
        paging.setPage(0);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedParam() {
        Paging paging = new Paging();
        paging.addPostParameter(new char[] {'x'}, 'x', new ArrayList<HttpParameter>(), "param", 123);
    }

    @Test
    public void testAsPostParameterArrayWithEmptyList() {
        Paging paging = new Paging();
        HttpParameter[] params = paging.asPostParameterArray();
        assertEquals(Paging.NULL_PARAMETER_ARRAY, params);
    }

    // ... add more test cases for edge cases and exception handling scenarios
    
}