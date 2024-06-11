
import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging newPaging = paging.count(20);
        assertEquals(20, newPaging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(100L);
        assertEquals(100L, paging.getSinceId());
    }
    
    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging newPaging = paging.sinceId(200L);
        assertEquals(200L, newPaging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(500L);
        assertEquals(500L, paging.getMaxId());
    }
    
    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging newPaging = paging.maxId(800L);
        assertEquals(800L, newPaging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 100L, 200L);
        Paging paging2 = new Paging(1, 10, 100L, 200L);
        assertEquals(true, paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        Paging paging = new Paging(2, 20, 200L, 300L);
        assertEquals(4040402, paging.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(3, 30, 300L, 400L);
        assertEquals("Paging{page=3, count=30, sinceId=300, maxId=400}", paging.toString());
    }
}
