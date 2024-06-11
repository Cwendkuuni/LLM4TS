import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(3);
        assertEquals(3, paging.getPage());
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging result = paging.count(20);
        assertEquals(20, paging.getCount());
        assertEquals(paging, result);
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(12345L);
        assertEquals(12345L, paging.getSinceId());
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(54321L);
        assertEquals(54321L, paging.getSinceId());
        assertEquals(paging, result);
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(9999L);
        assertEquals(9999L, paging.getMaxId());
    }

    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging result = paging.maxId(8888L);
        assertEquals(8888L, paging.getMaxId());
        assertEquals(paging, result);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1234L, 5678L);
        Paging paging2 = new Paging(1, 10, 1234L, 5678L);
        Paging paging3 = new Paging(2, 10, 1234L, 5678L);
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1234L, 5678L);
        Paging paging2 = new Paging(1, 10, 1234L, 5678L);
        Paging paging3 = new Paging(2, 10, 1234L, 5678L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1234L, 5678L);
        String expected = "Paging{page=1, count=10, sinceId=1234, maxId=5678}";
        assertEquals(expected, paging.toString());
    }
}