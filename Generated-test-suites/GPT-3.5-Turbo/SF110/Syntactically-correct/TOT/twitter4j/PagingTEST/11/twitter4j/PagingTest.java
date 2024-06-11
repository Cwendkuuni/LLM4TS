import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testConstructorDefaultValues() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        int page = 1;
        Paging paging = new Paging(page);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        long sinceId = 1000L;
        Paging paging = new Paging(sinceId);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        int page = 2;
        int count = 10;
        Paging paging = new Paging(page, count);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        int page = 3;
        long sinceId = 2000L;
        Paging paging = new Paging(page, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        int page = 4;
        int count = 20;
        long sinceId = 3000L;
        Paging paging = new Paging(page, count, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        int page = 5;
        int count = 30;
        long sinceId = 4000L;
        long maxId = 5000L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(maxId, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidPageNumber() {
        Paging paging = new Paging();
        paging.setPage(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidCount() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(0);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 1000L, 2000L);
        assertEquals(paging1, paging2);
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(2, 10, 1000L, 2000L);
        assertNotEquals(paging1, paging2);
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 1000L, 2000L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1000L, 2000L);
        String expected = "Paging{page=1, count=10, sinceId=1000, maxId=2000}";
        assertEquals(expected, paging.toString());
    }
}