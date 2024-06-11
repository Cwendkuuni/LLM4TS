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
        int page = 10;
        Paging paging = new Paging(page);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        long sinceId = 12345L;
        Paging paging = new Paging(sinceId);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountConstructor() {
        int page = 5;
        int count = 20;
        Paging paging = new Paging(page, count);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        int page = 7;
        long sinceId = 54321L;
        Paging paging = new Paging(page, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        int page = 3;
        int count = 15;
        long sinceId = 98765L;
        Paging paging = new Paging(page, count, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        int page = 2;
        int count = 10;
        long sinceId = 11111L;
        long maxId = 99999L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(maxId, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageConstructor() {
        int page = -1;
        Paging paging = new Paging(page);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountConstructor() {
        int count = 0;
        Paging paging = new Paging(count);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdConstructor() {
        long sinceId = -1L;
        Paging paging = new Paging(sinceId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdConstructor() {
        long maxId = -1L;
        Paging paging = new Paging(maxId);
    }

    @Test
    public void testSetPage() {
        int page = 5;
        Paging paging = new Paging();
        paging.setPage(page);
        assertEquals(page, paging.getPage());
    }

    @Test
    public void testSetCount() {
        int count = 20;
        Paging paging = new Paging();
        paging.setCount(count);
        assertEquals(count, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetCount() {
        int count = 0;
        Paging paging = new Paging();
        paging.setCount(count);
    }

    @Test
    public void testCount() {
        int count = 15;
        Paging paging = new Paging();
        Paging modified = paging.count(count);
        assertEquals(count, modified.getCount());
        assertNotSame(paging, modified);
    }

    @Test
    public void testSetSinceId() {
        long sinceId = 54321L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetSinceId() {
        long sinceId = -1L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
    }

    @Test
    public void testSinceId() {
        long sinceId = 98765L;
        Paging paging = new Paging();
        Paging modified = paging.sinceId(sinceId);
        assertEquals(sinceId, modified.getSinceId());
        assertNotSame(paging, modified);
    }

    @Test
    public void testSetMaxId() {
        long maxId = 99999L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSetMaxId() {
        long maxId = -1L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
    }

    @Test
    public void testMaxId() {
        long maxId = 11111L;
        Paging paging = new Paging();
        Paging modified = paging.maxId(maxId);
        assertEquals(maxId, modified.getMaxId());
        assertNotSame(paging, modified);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(5, 20, 12345L);
        Paging paging2 = new Paging(5, 20, 12345L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(5, 20, 12345L);
        Paging paging2 = new Paging(10, 15, 54321L);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(5, 20, 12345L);
        int expectedHashCode = Objects.hash(5, 20, 12345L, -1L);
        assertEquals(expectedHashCode, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(5, 20, 12345L);
        String expectedString = "Paging{page=5, count=20, sinceId=12345, maxId=-1}";
        assertEquals(expectedString, paging.toString());
    }

    // addPostParameterList and addPostParameterArray methods are implementation specific. Testing is not necessary.

}