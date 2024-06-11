import org.junit.*;
import twitter4j.Paging;

public class PagingTest {
    
    @Test
    public void testGetPage() {
        Paging paging = new Paging(1);
        Assert.assertEquals(1, paging.getPage());
    }
    
    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        Assert.assertEquals(2, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegative() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testGetCount() {
        Paging paging = new Paging(1, 10);
        Assert.assertEquals(10, paging.getCount());
    }
    
    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(20);
        Assert.assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegative() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        paging.count(30);
        Assert.assertEquals(30, paging.getCount());
    }
    
    @Test
    public void testGetSinceId() {
        Paging paging = new Paging(1, 10, 100, 200);
        Assert.assertEquals(100, paging.getSinceId());
    }
    
    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(500);
        Assert.assertEquals(500, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegative() {
        Paging paging = new Paging();
        paging.setSinceId(-1);
    }
    
    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        paging.sinceId(600);
        Assert.assertEquals(600, paging.getSinceId());
    }
    
    @Test
    public void testGetMaxId() {
        Paging paging = new Paging(1, 10, 100, 200);
        Assert.assertEquals(200, paging.getMaxId());
    }
    
    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(800);
        Assert.assertEquals(800, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegative() {
        Paging paging = new Paging();
        paging.setMaxId(-1);
    }
    
    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        paging.maxId(900);
        Assert.assertEquals(900, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(1, 10, 100, 200);
        Assert.assertTrue(paging1.equals(paging2));
    }
    
    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10, 100, 200);
        int hashCode = paging.hashCode();
        Assert.assertEquals(hashCode, paging.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 100, 200);
        String expected = "Paging{page=1, count=10, sinceId=100, maxId=200}";
        Assert.assertEquals(expected, paging.toString());
    }
}