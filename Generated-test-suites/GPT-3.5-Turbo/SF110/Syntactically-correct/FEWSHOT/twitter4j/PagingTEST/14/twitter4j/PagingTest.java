
package twitter4j;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import java.util.List;

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
    public void testPageConstructor() {
        Paging paging = new Paging(5);
        
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(123456L);
        
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndCountConstructor() {
        Paging paging = new Paging(5, 10);
        
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndSinceIdConstructor() {
        Paging paging = new Paging(5, 123456L);
        
        assertEquals(5, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(5, 10, 123456L);
        
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(5, 10, 123456L, 7891011L);
        
        assertEquals(5, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(7891011L, paging.getMaxId());
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        Paging paging = new Paging();
        
        paging.setPage(-5); // Negative page value should throw IllegalArgumentException
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        Paging paging = new Paging();
        
        paging.setCount(-10); // Negative count value should throw IllegalArgumentException
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        
        Paging newPaging = paging.count(20);
        
        assertEquals(20, newPaging.getCount());
        assertEquals(paging.getPage(), newPaging.getPage());
        assertEquals(paging.getSinceId(), newPaging.getSinceId());
        assertEquals(paging.getMaxId(), newPaging.getMaxId());
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        
        paging.setSinceId(123456L);
        assertEquals(123456L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        
        paging.setSinceId(-123456L); // Negative sinceId value should throw IllegalArgumentException
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        
        Paging newPaging = paging.sinceId(123456L);
        
        assertEquals(123456L, newPaging.getSinceId());
        assertEquals(paging.getPage(), newPaging.getPage());
        assertEquals(paging.getCount(), newPaging.getCount());
        assertEquals(paging.getMaxId(), newPaging.getMaxId());
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        
        paging.setMaxId(7891011L);
        assertEquals(7891011L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        Paging paging = new Paging();
        
        paging.setMaxId(-7891011L); // Negative maxId value should throw IllegalArgumentException
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        
        Paging newPaging = paging.maxId(7891011L);
        
        assertEquals(7891011L, newPaging.getMaxId());
        assertEquals(paging.getPage(), newPaging.getPage());
        assertEquals(paging.getCount(), newPaging.getCount());
        assertEquals(paging.getSinceId(), newPaging.getSinceId());
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(5, 10, 123456L, 7891011L);
        Paging paging2 = new Paging(5, 10, 123456L, 7891011L);
        
        assertEquals(paging1, paging2);
    }

    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(5, 10, 123456L, 7891011L);
        Paging paging2 = new Paging(2, 10, 123456L, 7891011L);
        
        assertNotEquals(paging1, paging2);
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(5, 10, 123456L, 7891011L);
        Paging paging2 = new Paging(5, 10, 123456L, 7891011L);
        
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(5, 10, 123456L, 7891011L);
        
        assertNotNull(paging.toString());
    }

    // Add more test cases to cover all possible methods and scenarios
    
}
