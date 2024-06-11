package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(123456789);
        assertNotNull(paging);
        assertEquals(-1, paging.getPage());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 123456789);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(1, 10, 123456789);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(1, 10, 123456789, 987654321);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
        assertEquals(10, paging.getCount());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValueShouldThrowException() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValueShouldThrowException() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        paging.count(30);
        assertEquals(30, paging.getCount());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValueShouldThrowException() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        paging.sinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValueShouldThrowException() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        paging.maxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }
}