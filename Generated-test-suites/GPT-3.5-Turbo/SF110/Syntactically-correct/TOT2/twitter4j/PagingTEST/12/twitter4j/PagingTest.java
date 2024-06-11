package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {
    
    @Test
    public void testGetPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }
    
    @Test
    public void testSetPageNegative() {
        Paging paging = new Paging();
        try {
            paging.setPage(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging(1, 10);
        assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }
    
    @Test
    public void testSetCountNegative() {
        Paging paging = new Paging();
        try {
            paging.setCount(-10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Exception caught, test passed
        }
    }
    
    // Add more test cases for other methods and edge cases
    
}