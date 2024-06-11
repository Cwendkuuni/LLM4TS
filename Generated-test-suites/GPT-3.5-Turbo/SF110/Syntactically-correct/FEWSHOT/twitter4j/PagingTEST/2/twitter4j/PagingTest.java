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
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testCountConstructor() {
        Paging paging = new Paging(10, 100);
        assertEquals(10, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(1, 500);
        paging.setSinceId(1234L);
        assertEquals(1, paging.getPage());
        assertEquals(500, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testMaxIdConstructor() {
        Paging paging = new Paging(1, 200, 5678L);
        paging.setMaxId(9876L);
        assertEquals(1, paging.getPage());
        assertEquals(200, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(9876L, paging.getMaxId());
    }

    @Test
    public void testFullConstructor() {
        Paging paging = new Paging(1, 100, 1234L, 5678L);
        assertEquals(1, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(5678L, paging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 100, 1234L, 5678L);
        Paging paging2 = new Paging(1, 100, 1234L, 5678L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testEqualsSameObject() {
        Paging paging = new Paging(1, 100, 1234L, 5678L);
        assertTrue(paging.equals(paging));
    }

    @Test
    public void testEqualsDifferentClass() {
        Paging paging = new Paging(1, 100, 1234L, 5678L);
        assertFalse(paging.equals("not a Paging object"));
    }

    @Test
    public void testEqualsNull() {
        Paging paging = new Paging(1, 100, 1234L, 5678L);
        assertFalse(paging.equals(null));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 100, 1234L, 5678L);
        Paging paging2 = new Paging(1, 100, 1234L, 5678L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 100, 1234L, 5678L);
        assertEquals("Paging{page=1, count=100, sinceId=1234, maxId=5678}", paging.toString());
    }
    
    @Test(expected=IllegalArgumentException.class)
    public void testSetPageNegativePage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetCountNegativeCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetSinceIdNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetMaxIdNegativeMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
}