
import org.junit.*;
import java.util.*;

public class PagingTest {

    @Test
    public void testPagingConstructor1() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructor2() {
        Paging paging = new Paging(2);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructor3() {
        Paging paging = new Paging(2, 50);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(50, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructor4() {
        Paging paging = new Paging(2, 50, 123456);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(50, paging.getCount());
        Assert.assertEquals(123456, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPagingConstructor5() {
        Paging paging = new Paging(2, 50, 123456, 654321);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(50, paging.getCount());
        Assert.assertEquals(123456, paging.getSinceId());
        Assert.assertEquals(654321, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeNumber() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 50, 123456, 654321);
        Paging paging2 = new Paging(2, 50, 123456, 654321);
        Assert.assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 50, 123456, 654321);
        Paging paging2 = new Paging(2, 50, 123456, 654321);
        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(2, 50, 123456, 654321);
        String expected = "Paging{page=2, count=50, sinceId=123456, maxId=654321}";
        Assert.assertEquals(expected, paging.toString());
    }

}
