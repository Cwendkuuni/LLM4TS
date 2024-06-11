
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(3);
        assertEquals(3, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1234L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(2, 50);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(5, 234L);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(3, 100, 8765L);
        assertEquals(3, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(8765L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(2, 50, 2345L, 7890L);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(2345L, paging.getSinceId());
        assertEquals(7890L, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(7);
        assertEquals(7, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        Paging paging = new Paging();
        paging.setPage(0);
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(30);
        assertEquals(30, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        Paging result = paging.count(50);
        assertEquals(50, result.getCount());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1234L);
        assertEquals(1234L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(8765L);
        assertEquals(8765L, result.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(5678L);
        assertEquals(5678L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.maxId(4321L);
        assertEquals(4321L, result.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(3, 50, 1234L, 5678L);
        Paging paging2 = new Paging(3, 50, 1234L, 5678L);
        assertEquals(paging1, paging2);
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(3, 50, 1234L, 5678L);
        int expectedHashCode = 31 * 31 * 3 + 31 * 50 + (int) (1234L ^ (1234L >>> 32)) + (int) (5678L ^ (5678L >>> 32));
        assertEquals(expectedHashCode, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(3, 50, 1234L, 5678L);
        String expectedString = "Paging{page=3, count=50, sinceId=1234, maxId=5678}";
        assertEquals(expectedString, paging.toString());
    }
}
