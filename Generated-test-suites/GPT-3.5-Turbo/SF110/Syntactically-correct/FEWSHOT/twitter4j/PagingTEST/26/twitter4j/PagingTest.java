
package twitter4j;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testSettersAndGetters() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
        paging.setCount(100);
        assertEquals(100, paging.getCount());
        paging.setSinceId(12345);
        assertEquals(12345, paging.getSinceId());
        paging.setMaxId(54321);
        assertEquals(54321, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testChainableMethods() {
        Paging paging = new Paging();
        paging.page(1).count(100).sinceId(12345).maxId(54321);
        assertEquals(1, paging.getPage());
        assertEquals(100, paging.getCount());
        assertEquals(12345, paging.getSinceId());
        assertEquals(54321, paging.getMaxId());
    }
}
