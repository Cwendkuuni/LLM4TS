
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
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
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
        Paging paging = new Paging(1, 100);
        assertEquals(1, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 1234L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceId() {
        Paging paging = new Paging(1, 100, 1234L);
        assertEquals(1, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(1, 100, 1234L, 5678L);
        assertEquals(1, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(1234L, paging.getSinceId());
        assertEquals(5678L, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageInvalid() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountInvalid() {
        Paging paging = new Paging();
        paging.setCount(0);
    }
    
    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging newPaging = paging.count(100);
        assertEquals(100, newPaging.getCount());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1234L);
        assertEquals(1234L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdInvalid() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging newPaging = paging.sinceId(1234L);
        assertEquals(1234L, newPaging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(5678L);
        assertEquals(5678L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdInvalid() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging newPaging = paging.maxId(5678L);
        assertEquals(5678L, newPaging.getMaxId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 100, 1234L);
        Paging paging2 = new Paging(1, 100, 1234L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 100, 1234L);
        Paging paging2 = new Paging(2, 100, 1234L);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 100, 1234L);
        assertEquals(215669539, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 100, 1234L);
        assertEquals("Paging{page=1, count=100, sinceId=1234, maxId=-1}", paging.toString());
    }
}
