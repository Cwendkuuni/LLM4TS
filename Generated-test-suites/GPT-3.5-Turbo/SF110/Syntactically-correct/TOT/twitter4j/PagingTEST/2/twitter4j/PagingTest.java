import static org.junit.Assert.*;
import org.junit.Test;

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
    public void testPageSetter() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageSetter() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testCountSetter() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountSetter() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testSinceIdSetter() {
        Paging paging = new Paging();
        paging.setSinceId(1000);
        assertEquals(1000, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdSetter() {
        Paging paging = new Paging();
        paging.setSinceId(-1);
    }

    @Test
    public void testMaxIdSetter() {
        Paging paging = new Paging();
        paging.setMaxId(2000);
        assertEquals(2000, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdSetter() {
        Paging paging = new Paging();
        paging.setMaxId(-1);
    }

    @Test
    public void testPageAndCountConstructor() {
        Paging paging = new Paging(3, 15);
        assertEquals(3, paging.getPage());
        assertEquals(15, paging.getCount());
    }

    @Test
    public void testPageAndSinceIdConstructor() {
        Paging paging = new Paging(3, 1000);
        assertEquals(3, paging.getPage());
        assertEquals(1000, paging.getSinceId());
    }

    @Test
    public void testPageCountAndSinceIdConstructor() {
        Paging paging = new Paging(3, 15, 1000);
        assertEquals(3, paging.getPage());
        assertEquals(15, paging.getCount());
        assertEquals(1000, paging.getSinceId());
    }

    @Test
    public void testPageCountSinceIdAndMaxIdConstructor() {
        Paging paging = new Paging(3, 15, 1000, 2000);
        assertEquals(3, paging.getPage());
        assertEquals(15, paging.getCount());
        assertEquals(1000, paging.getSinceId());
        assertEquals(2000, paging.getMaxId());
    }

}