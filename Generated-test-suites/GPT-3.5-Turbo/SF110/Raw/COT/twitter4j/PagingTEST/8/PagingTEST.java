
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testDefaultConstructor(){
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageConstructor(){
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageConstructor(){
        Paging paging = new Paging(-1);
    }

    @Test
    public void testSinceIdConstructor(){
        Paging paging = new Paging(123456);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdConstructor(){
        Paging paging = new Paging(-1L);
    }

    @Test
    public void testPageCountConstructor(){
        Paging paging = new Paging(2, 50);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountConstructor(){
        Paging paging = new Paging(2, -1);
    }

    @Test
    public void testPageSinceIdConstructor(){
        Paging paging = new Paging(2, 123456);
        assertEquals(2, paging.getPage());
        assertEquals(123456, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidPageSinceIdConstructor(){
        Paging paging = new Paging(-1, 123456);
    }

    @Test
    public void testCompleteConstructor(){
        Paging paging = new Paging(2, 50, 123456, 654321);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(123456L, paging.getSinceId());
        assertEquals(654321L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdConstructor(){
        Paging paging = new Paging(2, 50, 123456, -1L);
    }

    @Test
    public void testCountSetter(){
        Paging paging = new Paging();
        paging.setCount(50);
        assertEquals(50, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidCountSetter(){
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testSinceIdSetter(){
        Paging paging = new Paging();
        paging.setSinceId(123456L);
        assertEquals(123456L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidSinceIdSetter(){
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testMaxIdSetter(){
        Paging paging = new Paging();
        paging.setMaxId(654321L);
        assertEquals(654321L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxIdSetter(){
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
}
