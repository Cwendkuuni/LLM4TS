
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PagingTest {

    @Test
    public void testPagingDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingWithInvalidPageLessThanOne() {
        new Paging(-1);
    }

    @Test
    public void testPagingWithValidPage() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingWithInvalidCountLessThanOne() {
        new Paging(1, -1);
    }

    @Test
    public void testPagingWithValidCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingWithInvalidSinceIdLessThanOne() {
        new Paging(1, 10, -1);
    }

    @Test
    public void testPagingWithValidSinceId() {
        Paging paging = new Paging(1, 10, 1000);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPagingWithInvalidMaxIdLessThanOne() {
        new Paging(1, 10, 1000, -1);
    }

    @Test
    public void testPagingWithValidMaxId() {
        Paging paging = new Paging(1, 10, 1000, 2000);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(2000L, paging.getMaxId());
    }

    @Test
    public void testPagingEquals() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(1, 10, 1000, 2000);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testPagingHashCode() {
        Paging paging = new Paging(1, 10, 1000, 2000);
        assertEquals(902371, paging.hashCode());
    }

    @Test
    public void testPagingToString() {
        Paging paging = new Paging(1, 10, 1000, 2000);
        assertEquals("Paging{page=1, count=10, sinceId=1000, maxId=2000}", paging.toString());
    }
}
