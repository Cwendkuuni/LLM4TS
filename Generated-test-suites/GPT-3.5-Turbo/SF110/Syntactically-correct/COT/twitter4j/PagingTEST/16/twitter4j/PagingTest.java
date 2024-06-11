
import org.junit.Assert;
import org.junit.Test;
import twitter4j.HttpParameter;

import java.util.ArrayList;
import java.util.List;

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
        int page = 2;
        Paging paging = new Paging(page);
        Assert.assertEquals(page, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        long sinceId = 123456789L;
        Paging paging = new Paging(sinceId);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(sinceId, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountConstructor() {
        int page = 2;
        int count = 50;
        Paging paging = new Paging(page, count);
        Assert.assertEquals(page, paging.getPage());
        Assert.assertEquals(count, paging.getCount());
        Assert.assertEquals(-1L, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        int page = 2;
        long sinceId = 123456789L;
        Paging paging = new Paging(page, sinceId);
        Assert.assertEquals(page, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(sinceId, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        int page = 2;
        int count = 50;
        long sinceId = 123456789L;
        Paging paging = new Paging(page, count, sinceId);
        Assert.assertEquals(page, paging.getPage());
        Assert.assertEquals(count, paging.getCount());
        Assert.assertEquals(sinceId, paging.getSinceId());
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        int page = 2;
        int count = 50;
        long sinceId = 123456789L;
        long maxId = 987654321L;
        Paging paging = new Paging(page, count, sinceId, maxId);
        Assert.assertEquals(page, paging.getPage());
        Assert.assertEquals(count, paging.getCount());
        Assert.assertEquals(sinceId, paging.getSinceId());
        Assert.assertEquals(maxId, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        int page = 5;
        Paging paging = new Paging();
        paging.setPage(page);
        Assert.assertEquals(page, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegativePage() {
        int page = -1;
        Paging paging = new Paging();
        paging.setPage(page);
    }

    @Test
    public void testSetCount() {
        int count = 100;
        Paging paging = new Paging();
        paging.setCount(count);
        Assert.assertEquals(count, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegativeCount() {
        int count = -1;
        Paging paging = new Paging();
        paging.setCount(count);
    }

    @Test
    public void testCountMethod() {
        int count = 100;
        Paging paging = new Paging();
        Paging newPaging = paging.count(count);
        Assert.assertEquals(count, paging.getCount());
        Assert.assertEquals(paging, newPaging);
    }

    @Test
    public void testGetSinceId() {
        long sinceId = 123456789L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
        Assert.assertEquals(sinceId, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegativeSinceId() {
        long sinceId = -1L;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
    }

    @Test
    public void testSinceIdMethod() {
        long sinceId = 123456789L;
        Paging paging = new Paging();
        Paging newPaging = paging.sinceId(sinceId);
        Assert.assertEquals(sinceId, paging.getSinceId());
        Assert.assertEquals(paging, newPaging);
    }

    @Test
    public void testGetMaxId() {
        long maxId = 987654321L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        Assert.assertEquals(maxId, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegativeMaxId() {
        long maxId = -1L;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
    }

    @Test
    public void testMaxIdMethod() {
        long maxId = 987654321L;
        Paging paging = new Paging();
        Paging newPaging = paging.maxId(maxId);
        Assert.assertEquals(maxId, paging.getMaxId());
        Assert.assertEquals(paging, newPaging);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 100, 1234L, 5678L);
        Paging paging2 = new Paging(1, 100, 1234L, 5678L);
        Paging paging3 = new Paging(2, 100, 1234L, 5678L);

        Assert.assertTrue(paging1.equals(paging2));
        Assert.assertFalse(paging1.equals(paging3));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 100, 1234L, 5678L);
        Paging paging2 = new Paging(1, 100, 1234L, 5678L);
        Paging paging3 = new Paging(2, 100, 1234L, 5678L);

        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
        Assert.assertNotEquals(paging1.hashCode(), paging3.hashCode());
    }

    @Test
    public void testToString() {
        int page = 1;
        int count = 100;
        long sinceId = 1234L;
        long maxId = 5678L;

        Paging paging = new Paging(page, count, sinceId, maxId);
        String expected = "Paging{page=" + page + ", count=" + count + ", sinceId=" + sinceId + ", maxId=" + maxId + '}';
        Assert.assertEquals(expected, paging.toString());
    }
}
