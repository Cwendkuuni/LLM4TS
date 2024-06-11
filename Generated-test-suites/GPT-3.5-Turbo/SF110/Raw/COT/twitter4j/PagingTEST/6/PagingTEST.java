import org.junit.Before;
import org.junit.Test;
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
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegativeValue() {
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testSetCount() {
        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegativeValue() {
        paging.setCount(-1);
    }

    @Test
    public void testCount() {
        Paging updatedPaging = paging.count(50);
        assertEquals(50, updatedPaging.getCount());
    }

    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        paging.setSinceId(12345L);
        assertEquals(12345L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegativeValue() {
        paging.setSinceId(-1L);
    }

    @Test
    public void testSinceId() {
        Paging updatedPaging = paging.sinceId(67890L);
        assertEquals(67890L, updatedPaging.getSinceId());
    }

    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        paging.setMaxId(56789L);
        assertEquals(56789L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegativeValue() {
        paging.setMaxId(-1L);
    }

    @Test
    public void textMaxId() {
        Paging updatedPaging = paging.maxId(98765L);
        assertEquals(98765L, updatedPaging.getMaxId());
    }
}