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
    public void testSetPage() {
        int page = 5;
        paging.setPage(page);
        assertEquals(page, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegativeValue() {
        int page = -1;
        paging.setPage(page);
    }

    @Test
    public void testSetCount() {
        int count = 100;
        paging.setCount(count);
        assertEquals(count, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegativeValue() {
        int count = -100;
        paging.setCount(count);
    }

    @Test
    public void testCountMethod() {
        int count = 50;
        Paging updatedPaging = paging.count(count);
        assertEquals(count, updatedPaging.getCount());
        assertEquals(count, paging.getCount());
    }

    @Test
    public void testSetSinceId() {
        long sinceId = 123456789;
        paging.setSinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegativeValue() {
        long sinceId = -123456789;
        paging.setSinceId(sinceId);
    }

    @Test
    public void testSinceIdMethod() {
        long sinceId = 987654321;
        Paging updatedPaging = paging.sinceId(sinceId);
        assertEquals(sinceId, updatedPaging.getSinceId());
        assertEquals(sinceId, paging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        long maxId = 987654321;
        paging.setMaxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegativeValue() {
        long maxId = -987654321;
        paging.setMaxId(maxId);
    }

    @Test
    public void testMaxIdMethod() {
        long maxId = 123456789;
        Paging updatedPaging = paging.maxId(maxId);
        assertEquals(maxId, updatedPaging.getMaxId());
        assertEquals(maxId, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(5, 100, 123456789, 987654321);
        Paging paging2 = new Paging(5, 100, 123456789, 987654321);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(5, 100, 123456789, 987654321);
        Paging paging2 = new Paging(5, 100, 123456789, 987654321);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        int page = 5;
        int count = 100;
        long sinceId = 123456789;
        long maxId = 987654321;
        Paging paging = new Paging(page, count, sinceId, maxId);
        String expected = "Paging{page=" + page + ", count=" + count + ", sinceId=" + sinceId + ", maxId=" + maxId + '}';
        assertEquals(expected, paging.toString());
    }
}