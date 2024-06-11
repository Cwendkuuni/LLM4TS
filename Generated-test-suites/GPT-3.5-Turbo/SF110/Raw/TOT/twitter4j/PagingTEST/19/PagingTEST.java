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
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSetter() {
        paging.setPage(1);
        assertEquals(1, paging.getPage());
        paging.setPage(100);
        assertEquals(100, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPageSetterNegativeValue() {
        paging.setPage(-1);
    }

    @Test
    public void testCountSetter() {
        paging.setCount(1);
        assertEquals(1, paging.getCount());
        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCountSetterNegativeValue() {
        paging.setCount(-1);
    }

    @Test
    public void testSinceIdSetter() {
        paging.setSinceId(1L);
        assertEquals(1L, paging.getSinceId());
        paging.setSinceId(100L);
        assertEquals(100L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSinceIdSetterNegativeValue() {
        paging.setSinceId(-1L);
    }

    @Test
    public void testMaxIdSetter() {
        paging.setMaxId(1L);
        assertEquals(1L, paging.getMaxId());
        paging.setMaxId(100L);
        assertEquals(100L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxIdSetterNegativeValue() {
        paging.setMaxId(-1L);
    }

    @Test
    public void testPagingConstructor() {
        Paging customPaging = new Paging(2, 10, 5L, 50L);
        assertEquals(2, customPaging.getPage());
        assertEquals(10, customPaging.getCount());
        assertEquals(5L, customPaging.getSinceId());
        assertEquals(50L, customPaging.getMaxId());
    }

    @Test
    public void testPagingCountMethod() {
        Paging customPaging = new Paging().count(20);
        assertEquals(20, customPaging.getCount());
    }

    @Test
    public void testPagingSinceIdMethod() {
        Paging customPaging = new Paging().sinceId(10L);
        assertEquals(10L, customPaging.getSinceId());
    }

    @Test
    public void testPagingMaxIdMethod() {
        Paging customPaging = new Paging().maxId(100L);
        assertEquals(100L, customPaging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 10);
        Paging paging2 = new Paging(2, 10);
        Paging paging3 = new Paging(2, 20);
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 10);
        Paging paging2 = new Paging(2, 10);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging customPaging = new Paging(2, 10, 5L, 50L);
        String expected = "Paging{page=2, count=10, sinceId=5, maxId=50}";
        assertEquals(expected, customPaging.toString());
    }
}