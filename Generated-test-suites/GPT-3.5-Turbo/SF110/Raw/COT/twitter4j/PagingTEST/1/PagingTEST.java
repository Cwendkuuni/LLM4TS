
import org.junit.Test;
import static org.junit.Assert.*;

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
    public void testConstructorWithPage() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(1234L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(2, 1234L);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging paging = new Paging(2, 10, 1234L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testConstructorWithPageCountSinceIdAndMaxId() {
        Paging paging = new Paging(2, 10, 1234L, 5678L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(5678L, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(3);
        assertEquals(3, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
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
    public void testSetCountWithInvalidValue() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1000L);
        assertEquals(1000L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(2000L);
        assertEquals(2000L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 10, 1234L, 5678L);
        Paging paging2 = new Paging(2, 10, 1234L, 5678L);
        assertEquals(paging1, paging2);
    }

    @Test
    public void testEqualsWithDifferentPagingObject() {
        Paging paging1 = new Paging(2, 10, 1234L, 5678L);
        Paging paging2 = new Paging(3, 20, 4321L, 8765L);
        assertNotEquals(paging1, paging2);
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 10, 1234L, 5678L);
        Paging paging2 = new Paging(2, 10, 1234L, 5678L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(2, 10, 1234L, 5678L);
        String expected = "Paging{page=2, count=10, sinceId=1234, maxId=5678}";
        assertEquals(expected, paging.toString());
    }

}
