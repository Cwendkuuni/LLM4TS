import org.junit.*;
import java.util.*;

public class PagingTest {
    
    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageConstructor() {
        Paging paging = new Paging(2);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testPageConstructorInvalidPage() {
        Paging paging = new Paging(-1);
    }
    
    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(1234567890L);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1234567890L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSinceIdConstructorInvalidSinceId() {
        Paging paging = new Paging(-1L);
    }
    
    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(2, 10);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(2, 1234567890L);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(1234567890L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(2, 10, 1234567890L);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1234567890L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(2, 10, 1234567890L, 9876543210L);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(1234567890L, paging.getSinceId());
        Assert.assertEquals(9876543210L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageInvalidPage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testSetPageValidPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        Assert.assertEquals(2, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountInvalidCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test
    public void testSetCountValidCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        Assert.assertEquals(10, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdInvalidSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }
    
    @Test
    public void testSetSinceIdValidSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(1234567890L);
        Assert.assertEquals(1234567890L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdInvalidMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testSetMaxIdValidMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(9876543210L);
        Assert.assertEquals(9876543210L, paging.getMaxId());
    }
    
    @Test
    public void testEqualsSameObject() {
        Paging paging = new Paging(2, 10, 1234567890L, 9876543210L);
        Assert.assertTrue(paging.equals(paging));
    }
    
    @Test
    public void testEqualsDifferentObject() {
        Paging paging1 = new Paging(2, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(2, 10, 1234567890L, 9876543210L);
        Assert.assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentPage() {
        Paging paging1 = new Paging(2, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(3, 10, 1234567890L, 9876543210L);
        Assert.assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentCount() {
        Paging paging1 = new Paging(2, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(2, 20, 1234567890L, 9876543210L);
        Assert.assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentSinceId() {
        Paging paging1 = new Paging(2, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(2, 10, 9876543210L, 9876543210L);
        Assert.assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testEqualsDifferentMaxId() {
        Paging paging1 = new Paging(2, 10, 1234567890L, 9876543210L);
        Paging paging2 = new Paging(2, 10, 1234567890L, 1234567890L);
        Assert.assertFalse(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        Paging paging = new Paging(2, 10, 1234567890L, 9876543210L);
        int expectedHashCode = Objects.hash(2, 10, 1234567890L, 9876543210L);
        Assert.assertEquals(expectedHashCode, paging.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(2, 10, 1234567890L, 9876543210L);
        String expectedToString = "Paging{page=2, count=10, sinceId=1234567890, maxId=9876543210}";
        Assert.assertEquals(expectedToString, paging.toString());
    }
    
    // Add test methods for other methods in Paging class
    
}