package twitter4j;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PagingTest {

    private Paging paging;

    @Before
    public void setup() {
        paging = new Paging();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testSetPage() {
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        paging.setPage(-1);
    }

    @Test
    public void testSetCount() {
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        paging.setCount(-10);
    }

    @Test
    public void testSetSinceId() {
        paging.setSinceId(1000);
        assertEquals(1000, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        paging.setSinceId(-1000);
    }

    @Test
    public void testSetMaxId() {
        paging.setMaxId(2000);
        assertEquals(2000, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        paging.setMaxId(-2000);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        Paging paging2 = new Paging(1, 10, 1000, 2000);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1000, 2000);
        int hash1 = paging1.hashCode();

        Paging paging2 = new Paging(1, 10, 1000, 2000);
        int hash2 = paging2.hashCode();

        assertEquals(hash1, hash2);
    }

}