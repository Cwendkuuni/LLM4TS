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
    public void testDefaultConstructor() {
        assertNotNull(paging);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(12345678L);
        assertNotNull(paging);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(12345678L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 10);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 12345678L);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(12345678L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceId() {
        Paging paging = new Paging(1, 10, 12345678L);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(12345678L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(1, 10, 12345678L, 87654321L);
        assertNotNull(paging);
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(12345678L, paging.getSinceId());
        assertEquals(87654321L, paging.getMaxId());
    }
    
    @Test
    public void testSetPage() {
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        paging.setPage(-1);
    }
    
    @Test
    public void testSetCount() {
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        paging.setCount(-1);
    }
    
    @Test
    public void testCount() {
        Paging newPaging = paging.count(30);
        assertEquals(30, newPaging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }
    
    @Test
    public void testSetSinceId() {
        paging.setSinceId(98765432L);
        assertEquals(98765432L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        paging.setSinceId(-1L);
    }
    
    @Test
    public void testSinceId() {
        Paging newPaging = paging.sinceId(11111111L);
        assertEquals(11111111L, newPaging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetMaxId() {
        paging.setMaxId(55555555L);
        assertEquals(55555555L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testMaxId() {
        Paging newPaging = paging.maxId(22222222L);
        assertEquals(22222222L, newPaging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 12345678L, 87654321L);
        Paging paging2 = new Paging(1, 10, 12345678L, 87654321L);
        Paging paging3 = new Paging(2, 10, 12345678L, 87654321L);
        assertTrue(paging1.equals(paging2));
        assertFalse(paging1.equals(paging3));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 12345678L, 87654321L);
        Paging paging2 = new Paging(1, 10, 12345678L, 87654321L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 12345678L, 87654321L);
        String expected = "Paging{page=1, count=10, sinceId=12345678, maxId=87654321}";
        assertEquals(expected, paging.toString());
    }
}