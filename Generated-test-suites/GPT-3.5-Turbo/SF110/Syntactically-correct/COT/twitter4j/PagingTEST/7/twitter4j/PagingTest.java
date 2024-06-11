
import org.junit.Before;
import org.junit.Test;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageConstructor() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(10L);
        assertEquals(10L, paging.getSinceId());
    }

    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(1, 10L);
        assertEquals(1, paging.getPage());
        assertEquals(10L, paging.getSinceId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(1, 10, 10L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(10L, paging.getSinceId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(1, 10, 10L, 20L);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(10L, paging.getSinceId());
        assertEquals(20L, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageInvalid() {
        paging.setPage(0);
    }

    @Test
    public void testSetCount() {
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountInvalid() {
        paging.setCount(0);
    }

    @Test
    public void testCountChain() {
        Paging updatedPaging = paging.count(20);
        assertEquals(20, updatedPaging.getCount());
    }

    @Test
    public void testSetSinceId() {
        paging.setSinceId(100L);
        assertEquals(100L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdInvalid() {
        paging.setSinceId(0L);
    }

    @Test
    public void testSinceIdChain() {
        Paging updatedPaging = paging.sinceId(200L);
        assertEquals(200L, updatedPaging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        paging.setMaxId(100L);
        assertEquals(100L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdInvalid() {
        paging.setMaxId(0L);
    }

    @Test
    public void testMaxIdChain() {
        Paging updatedPaging = paging.maxId(200L);
        assertEquals(200L, updatedPaging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 10L, 20L);
        Paging paging2 = new Paging(1, 10, 10L, 20L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 10, 10L, 20L);
        Paging paging2 = new Paging(2, 10, 10L, 20L);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 10L, 20L);
        Paging paging2 = new Paging(1, 10, 10L, 20L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 10L, 20L);
        assertEquals("Paging{page=1, count=10, sinceId=10, maxId=20}", paging.toString());
    }
}
