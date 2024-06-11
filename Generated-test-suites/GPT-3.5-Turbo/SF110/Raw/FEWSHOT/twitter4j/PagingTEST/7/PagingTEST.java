
package twitter4j;

import static org.junit.Assert.assertEquals;

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
        Paging paging = new Paging(123456789L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndCountConstructor() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndSinceIdConstructor() {
        Paging paging = new Paging(2, 123456789L);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountAndSinceIdConstructor() {
        Paging paging = new Paging(2, 10, 123456789L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdAndMaxIdConstructor() {
        Paging paging = new Paging(2, 10, 123456789L, 987654321L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
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
        paging.setPage(0);
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging().count(10);
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging().sinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging().maxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }
}
