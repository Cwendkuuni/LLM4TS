import static org.junit.Assert.*;
import org.junit.Test;

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
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(123456);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(3, 100);
        assertEquals(3, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(4, 1234567890L);
        assertEquals(4, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234567890L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(5, 200, 987654321L);
        assertEquals(5, paging.getPage());
        assertEquals(200, paging.getCount());
        assertEquals(987654321L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(6, 300, 123456789L, 987654321L);
        assertEquals(6, paging.getPage());
        assertEquals(300, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 100);
        Paging paging2 = new Paging(2, 200);
        Paging paging3 = new Paging(3, 100);
        Paging paging4 = new Paging(2, 100);
        assertFalse(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
        assertTrue(paging1.equals(paging4));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 100);
        Paging paging2 = new Paging(2, 200);
        Paging paging3 = new Paging(3, 100);
        Paging paging4 = new Paging(2, 100);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
        assertEquals(paging1.hashCode(), paging4.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(2, 100, 123456, 987654);
        assertEquals("Paging{page=2, count=100, sinceId=123456, maxId=987654}", paging.toString());
    }
}