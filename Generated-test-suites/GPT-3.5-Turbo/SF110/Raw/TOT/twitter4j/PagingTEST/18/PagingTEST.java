import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

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
        Paging paging = new Paging(1000L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
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
        Paging paging = new Paging(1, 1000L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 10, 1000L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 1000L, 2000L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(2000L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
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
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 1000L, 2000L);
        Paging paging3 = new Paging(2, 10, 1000L, 2000L);
        assertEquals(paging1, paging2);
        assertNotEquals(paging1, paging3);
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1000L, 2000L);
        Paging paging2 = new Paging(1, 10, 1000L, 2000L);
        Paging paging3 = new Paging(2, 10, 1000L, 2000L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1000L, 2000L);
        String expected = "Paging{page=1, count=10, sinceId=1000, maxId=2000}";
        assertEquals(expected, paging.toString());
    }

}