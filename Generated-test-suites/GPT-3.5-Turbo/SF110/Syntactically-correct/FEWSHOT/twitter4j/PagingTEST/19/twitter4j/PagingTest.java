
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

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
        Paging paging = new Paging(100L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(100L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndCountConstructor() {
        Paging paging = new Paging(3, 20);
        assertEquals(3, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndSinceIdConstructor() {
        Paging paging = new Paging(5, 200L);
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(200L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndCountAndSinceIdConstructor() {
        Paging paging = new Paging(7, 50, 1000L);
        assertEquals(7, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(1000L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndCountAndSinceIdAndMaxIdConstructor() {
        Paging paging = new Paging(9, 100, 1500L, 2000L);
        assertEquals(9, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(1500L, paging.getSinceId());
        assertEquals(2000L, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        Paging paging = new Paging();
        paging.setPage(-2);
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(50);
        assertEquals(50, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        Paging updatedPaging = paging.count(30);
        assertEquals(30, updatedPaging.getCount());
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(500L);
        assertEquals(500L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setSinceId(-100L);
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        Paging updatedPaging = paging.sinceId(100L);
        assertEquals(100L, updatedPaging.getSinceId());
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(1000L);
        assertEquals(1000L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setMaxId(-500L);
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        Paging updatedPaging = paging.maxId(200L);
        assertEquals(200L, updatedPaging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 100L, 200L);
        Paging paging2 = new Paging(1, 10, 100L, 200L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(2, 20, 500L, 1000L);
        Paging paging2 = new Paging(3, 30, 100L, 200L);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(5, 50, 500L, 200L);
        Paging paging2 = new Paging(5, 50, 500L, 200L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(10, 100, 1000L, 2000L);
        assertEquals("Paging{page=10, count=100, sinceId=1000, maxId=2000}", paging.toString());
    }
}
