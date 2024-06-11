import org.junit.*;
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
    public void testSetPageWithNegativeValue() {
        paging.setPage(-1);
    }
    
    @Test
    public void testGetCount() {
        assertEquals(-1, paging.getCount());
    }
    
    @Test
    public void testSetCount() {
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        paging.setCount(-10);
    }
    
    @Test
    public void testCount() {
        Paging newPaging = paging.count(20);
        assertEquals(20, newPaging.getCount());
        assertNotEquals(paging, newPaging);
    }
    
    @Test
    public void testGetSinceId() {
        assertEquals(-1L, paging.getSinceId());
    }
    
    @Test
    public void testSetSinceId() {
        paging.setSinceId(1000L);
        assertEquals(1000L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        paging.setSinceId(-1000L);
    }
    
    @Test
    public void testSinceId() {
        Paging newPaging = paging.sinceId(2000L);
        assertEquals(2000L, newPaging.getSinceId());
        assertNotEquals(paging, newPaging);
    }
    
    @Test
    public void testGetMaxId() {
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetMaxId() {
        paging.setMaxId(5000L);
        assertEquals(5000L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        paging.setMaxId(-5000L);
    }
    
    @Test
    public void testMaxId() {
        Paging newPaging = paging.maxId(6000L);
        assertEquals(6000L, newPaging.getMaxId());
        assertNotEquals(paging, newPaging);
    }
    
    @Test
    public void testEqualsForSameInstance() {
        assertTrue(paging.equals(paging));
    }
    
    @Test
    public void testEqualsForDifferentClass() {
        assertFalse(paging.equals(new Object()));
    }
    
    @Test
    public void testEqualsForSameValues() {
        Paging anotherPaging = new Paging();
        assertTrue(paging.equals(anotherPaging));
    }
    
    @Test
    public void testEqualsForDifferentValues() {
        Paging anotherPaging = new Paging(1);
        assertFalse(paging.equals(anotherPaging));
    }
    
    @Test
    public void testHashCode() {
        assertEquals(-1763696584, paging.hashCode());
    }
    
    @Test
    public void testToString() {
        assertEquals("Paging{page=-1, count=-1, sinceId=-1, maxId=-1}", paging.toString());
    }
    
}