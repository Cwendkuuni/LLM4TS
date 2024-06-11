import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {
    
    @Test
    public void testConstructor_default() {
        Paging paging = new Paging();
        
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor_page() {
        Paging paging = new Paging(1);
        
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor_sinceId() {
        Paging paging = new Paging(123456789L);
        
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor_page_count() {
        Paging paging = new Paging(1, 10);
        
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor_page_sinceId() {
        Paging paging = new Paging(1, 123456789L);
        
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor_page_count_sinceId() {
        Paging paging = new Paging(1, 10, 123456789L);
        
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructor_page_count_sinceId_maxId() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        
        assertEquals(1, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
    }
    
    @Test
    public void testSetPage_valid() {
        Paging paging = new Paging();
        paging.setPage(1);
        
        assertEquals(1, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_invalid() {
        Paging paging = new Paging();
        paging.setPage(0);
    }
    
    @Test
    public void testSetCount_valid() {
        Paging paging = new Paging();
        paging.setCount(10);
        
        assertEquals(10, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_invalid() {
        Paging paging = new Paging();
        paging.setCount(0);
    }
    
    @Test
    public void testSetSinceId_valid() {
        Paging paging = new Paging();
        paging.setSinceId(123456789L);
        
        assertEquals(123456789L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_invalid() {
        Paging paging = new Paging();
        paging.setSinceId(0L);
    }
    
    @Test
    public void testSetMaxId_valid() {
        Paging paging = new Paging();
        paging.setMaxId(987654321L);
        
        assertEquals(987654321L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_invalid() {
        Paging paging = new Paging();
        paging.setMaxId(0L);
    }
    
    @Test
    public void testEquals_sameObject() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        
        assertTrue(paging.equals(paging));
    }
    
    @Test
    public void testEquals_differentObject_sameValues() {
        Paging paging1 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(1, 10, 123456789L, 987654321L);
        
        assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testEquals_differentObject_differentValues() {
        Paging paging1 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(2, 20, 987654321L, 123456789L);
        
        assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode_sameObject_sameValues() {
        Paging paging1 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(1, 10, 123456789L, 987654321L);
        
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testHashCode_differentObject_differentValues() {
        Paging paging1 = new Paging(1, 10, 123456789L, 987654321L);
        Paging paging2 = new Paging(2, 20, 987654321L, 123456789L);
        
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 123456789L, 987654321L);
        String expected = "Paging{page=1, count=10, sinceId=123456789, maxId=987654321}";
        
        assertEquals(expected, paging.toString());
    }
}