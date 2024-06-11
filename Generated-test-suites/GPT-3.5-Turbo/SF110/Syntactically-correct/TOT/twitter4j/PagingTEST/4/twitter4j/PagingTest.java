import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        
        //typical use case
        assertEquals(paging.asPostParameterList().size(), 1);
        
        //edge case
        paging.setCount(-1);
        assertEquals(paging.asPostParameterList().size(), 0);
        
        //error scenario
        try {
            paging.asPostParameterList(Paging.SMCP, "perPage");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            //pass
        }
    }
    
    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        
        //typical use case
        assertEquals(paging.asPostParameterArray().length, 1);
        
        //edge case
        paging.setCount(-1);
        assertEquals(paging.asPostParameterArray().length, 0);
        
        //error scenario
        try {
            paging.asPostParameterArray(Paging.SMCP, "perPage");
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            //pass
        }
    }
    
    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        
        //typical use case
        paging.addPostParameter(Paging.SMCP, 's', new List<HttpParameter>(), "since_id", paging.getSinceId());
        assertEquals(paging.asPostParameterList().size(), 1);
        
        //edge case
        paging.addPostParameter(Paging.SMCP, 'x', new List<HttpParameter>(), "not_supported", 5);
        assertEquals(paging.asPostParameterList().size(), 0);
        
        //error scenario
        try {
            paging.addPostParameter(Paging.SMCP, 's', new List<HttpParameter>(), "since_id", -1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testPagingConstructor() {
        Paging paging = new Paging();
        
        //typical use case
        assertEquals(paging.getPage(), -1);
        assertEquals(paging.getCount(), -1);
        assertEquals(paging.getSinceId(), -1L);
        assertEquals(paging.getMaxId(), -1L);
        
        //edge case
        Paging paging2 = new Paging(-1);
        assertEquals(paging2.getPage(), -1);
        
        //error scenario
        try {
            Paging paging3 = new Paging(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        
        //typical use case
        paging.setPage(5);
        assertEquals(paging.getPage(), 5);
        
        //edge case
        paging.setPage(0);
        assertEquals(paging.getPage(), 0);
        
        //error scenario
        try {
            paging.setPage(-2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        
        //typical use case
        paging.setCount(10);
        assertEquals(paging.getCount(), 10);
        
        //edge case
        paging.setCount(0);
        assertEquals(paging.getCount(), 0);
        
        //error scenario
        try {
            paging.setCount(-5);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        
        //typical use case
        paging.setSinceId(1234567890L);
        assertEquals(paging.getSinceId(), 1234567890L);
        
        //edge case
        paging.setSinceId(0L);
        assertEquals(paging.getSinceId(), 0L);
        
        //error scenario
        try {
            paging.setSinceId(-5L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        
        //typical use case
        paging.setMaxId(9876543210L);
        assertEquals(paging.getMaxId(), 9876543210L);
        
        //edge case
        paging.setMaxId(0L);
        assertEquals(paging.getMaxId(), 0L);
        
        //error scenario
        try {
            paging.setMaxId(-5L);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            //pass
        }
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging3 = new Paging(2, 20, 1111111111L, 2222222222L);
        
        //typical use case
        assertTrue(paging1.equals(paging2));
        
        //edge case
        assertFalse(paging1.equals(paging3));
        assertFalse(paging2.equals(paging3));
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(1, 10, 1234567890L, 9876543210L);
        
        //typical use case
        assertEquals(paging1.hashCode(), paging2.hashCode());
        
        //edge case
        assertNotEquals(paging1.hashCode(), 0);
        assertNotEquals(paging2.hashCode(), 0);
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 1234567890L, 9876543210L);
        String expected = "Paging{page=1, count=10, sinceId=1234567890, maxId=9876543210}";
        
        //typical use case
        assertEquals(paging.toString(), expected);
    }
    
}