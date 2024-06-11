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
    public void testPageConstructor() {
        int page = 5;
        Paging paging = new Paging(page);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        long sinceId = 123456789L;
        Paging paging = new Paging(sinceId);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndCountConstructor() {
        int page = 2;
        int count = 10;
        Paging paging = new Paging(page, count);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndSinceIdConstructor() {
        int page = 3;
        long sinceId = 987654321L;
        Paging paging = new Paging(page, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountAndSinceIdConstructor() {
        int page = 2;
        int count = 5;
        long sinceId = 123456789L;
        Paging paging = new Paging(page, count, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testAllParamsConstructor() {
        int page = 1;
        int count = 20;
        long sinceId = 111222333L;
        long maxId = 444555666L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(maxId, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        int page = 3;
        Paging paging = new Paging();
        paging.setPage(page);
        assertEquals(page, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        int page = -1;
        Paging paging = new Paging();
        paging.setPage(page);
    }

    @Test
    public void testSetCount() {
        int count = 10;
        Paging paging = new Paging();
        paging.setCount(count);
        assertEquals(count, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        int count = -1;
        Paging paging = new Paging();
        paging.setCount(count);
    }

    @Test
    public void testCountMethod() {
        int count = 20;
        Paging paging = new Paging();
        Paging result = paging.count(count);
        assertEquals(count, paging.getCount());
        assertSame(paging, result);
    }

    @Test
    public void testSetSinceId() {
        long sinceId = 123456789L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        long sinceId = -1L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
    }

    @Test
    public void testSinceIdMethod() {
        long sinceId = 987654321L;
        Paging paging = new Paging();
        Paging result = paging.sinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
        assertSame(paging, result);
    }

    @Test
    public void testSetMaxId() {
        long maxId = 444555666L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        long maxId = -1L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
    }

    @Test
    public void testMaxIdMethod() {
        long maxId = 777888999L;
        Paging paging = new Paging();
        Paging result = paging.maxId(maxId);
        assertEquals(maxId, paging.getMaxId());
        assertSame(paging, result);
    }
}