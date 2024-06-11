import org.junit.Before;
import org.junit.Test;
import twitter4j.Paging;

import static org.junit.Assert.*;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    @Test
    public void testGetPage() {
        assertEquals(-1, paging.getPage());
    }

    @Test
    public void testSetPage() {
        paging.setPage(10);
        assertEquals(10, paging.getPage());
    }

    @Test
    public void testGetCount() {
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testSetCount() {
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageInvalid() {
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountInvalid() {
        paging.setCount(-5);
    }

    @Test
    public void testGetSinceId() {
        assertEquals(-1, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        paging.setSinceId(100);
        assertEquals(100, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdInvalid() {
        paging.setSinceId(-5);
    }

    @Test
    public void testGetMaxId() {
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        paging.setMaxId(200);
        assertEquals(200, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdInvalid() {
        paging.setMaxId(-10);
    }

    @Test
    public void testEquals() {
        assertTrue(paging.equals(new Paging()));
    }

    @Test
    public void testHashCode() {
        Paging paging2 = new Paging();
        assertEquals(paging.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals("Paging{page=-1, count=-1, sinceId=-1, maxId=-1}", paging.toString());
    }
}