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
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(1234567890L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234567890L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
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
        Paging paging = new Paging(3, 20, 9876543210L);
        assertEquals(3, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(9876543210L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(4, 30, 1234567890L, 9876543210L);
        assertEquals(4, paging.getPage());
        assertEquals(30, paging.getCount());
        assertEquals(1234567890L, paging.getSinceId());
        assertEquals(9876543210L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePage() {
        Paging paging = new Paging(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        Paging paging = new Paging(1);
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSinceId() {
        Paging paging = new Paging(1);
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxId() {
        Paging paging = new Paging(1);
        paging.setMaxId(-1L);
    }

    @Test
    public void testCount() {
        Paging paging = new Paging(1);
        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging(1);
        paging.setSinceId(1234567890L);
        assertEquals(1234567890L, paging.getSinceId());
    }

    @Test
    public void testMaxId() {
        Paging paging = new Paging(1);
        paging.setMaxId(9876543210L);
        assertEquals(9876543210L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(1, 10, 1234567890L, 9876543210L);
        assertEquals(paging1, paging2);
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
}