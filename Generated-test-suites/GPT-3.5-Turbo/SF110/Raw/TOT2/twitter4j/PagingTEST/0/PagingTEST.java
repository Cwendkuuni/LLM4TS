
package twitter4j;

import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testGetPage() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging(1, 3);
        assertEquals(3, paging.getCount());
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }
    
    // Add more test cases for other methods as needed
    
}
