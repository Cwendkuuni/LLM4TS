
package twitter4j;

import org.junit.Test;

import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1234567890);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234567890, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(3, 1234567890);
        assertEquals(3, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234567890, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(4, 20, 1234567890);
        assertEquals(4, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(1234567890, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(5, 50, 1234567890, 9876543210L);
        assertEquals(5, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(1234567890, paging.getSinceId());
        assertEquals(9876543210L, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());

        paging.setPage(6);
        assertEquals(6, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        Paging paging = new Paging();
        paging.setPage(0);
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());

        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testCount() {
        Paging paging = new Paging();
        assertEquals(null, paging.count(50).asPostParameterArray());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getSinceId());

        paging.setSinceId(1234567890);
        assertEquals(1234567890, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setSinceId(0);
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        assertEquals(null, paging.sinceId(1234567890).asPostParameterArray());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getMaxId());

        paging.setMaxId(9876543210L);
        assertEquals(9876543210L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setMaxId(0);
    }

    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        assertEquals(null, paging.maxId(9876543210L).asPostParameterArray());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1);
        Paging paging2 = new Paging(1);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1);
        Paging paging2 = new Paging(2);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1);
        Paging paging2 = new Paging(1);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(2, 10, 1234567890, 9876543210L);
        String expected = "Paging{page=2, count=10, sinceId=1234567890, maxId=9876543210}";
        assertEquals(expected, paging.toString());
    }
}
