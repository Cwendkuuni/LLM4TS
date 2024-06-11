import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

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
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageConstructor() {
        Paging paging = new Paging(0);
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(5);
        assertEquals(5, paging.getSinceId());
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdConstructor() {
        Paging paging = new Paging(0L);
    }

    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(3, 20);
        assertEquals(3, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(4, 30, 50L);
        assertEquals(4, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(50L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(5, 40, 60L, 80L);
        assertEquals(5, paging.getPage());
        assertEquals(40, paging.getCount());
        assertEquals(60L, paging.getSinceId());
        assertEquals(80L, paging.getMaxId());
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetPage() {
        Paging paging = new Paging();
        paging.setPage(0);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetCount() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        Paging result = paging.count(5);
        assertEquals(5, result.getCount());
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(20L);
        assertEquals(20L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(25L);
        assertEquals(25L, result.getSinceId());
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(30L);
        assertEquals(30L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        Paging result = paging.maxId(35L);
        assertEquals(35L, result.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 20L, 30L);
        Paging paging2 = new Paging(1, 10, 20L, 30L);
        assertEquals(paging1, paging2);
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 10, 20L, 30L);
        Paging paging2 = new Paging(2, 10, 20L, 30L);
        assertNotEquals(paging1, paging2);
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        assertEquals(-2071679163, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 20L, 30L);
        String expected = "Paging{page=1, count=10, sinceId=20, maxId=30}";
        assertEquals(expected, paging.toString());
    }
}