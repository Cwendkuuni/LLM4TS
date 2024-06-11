
import org.junit.Assert;
import org.junit.Test;

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
        Paging paging = new Paging(5);
        Assert.assertEquals(5, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(12345L);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(12345L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(3, 10);
        Assert.assertEquals(3, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(2, 123L);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(123L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(1, 20, 567L);
        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(20, paging.getCount());
        Assert.assertEquals(567L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(4, 15, 789L, 1000L);
        Assert.assertEquals(4, paging.getPage());
        Assert.assertEquals(15, paging.getCount());
        Assert.assertEquals(789L, paging.getSinceId());
        Assert.assertEquals(1000L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
}
