
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    @Test
    public void testGetPage() {
        assertEquals(-1, paging.getPage());
    }

    @Test
    public void testSetPage() {
        paging.setPage(10);
        assertEquals(10, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testSetCount() {
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        paging.setCount(-1);
    }

    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        paging.setSinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        paging.setSinceId(-1L);
    }

    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        paging.setMaxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 123, 456);
        Paging paging2 = new Paging(1, 10, 123, 456);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 123, 456);
        Paging paging2 = new Paging(1, 10, 123, 456);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        paging.setPage(1);
        paging.setCount(10);
        paging.setSinceId(123);
        paging.setMaxId(456);
        assertEquals("Paging{page=1, count=10, sinceId=123, maxId=456}", paging.toString());
    }
}
