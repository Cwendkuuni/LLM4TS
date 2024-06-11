import org.junit.*;
import static org.junit.Assert.*;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageConstructor() {
        Paging paging = new Paging(2);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageConstructorWithInvalidPage() {
        try {
            Paging paging = new Paging(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("page should be positive integer. passed:-1", e.getMessage());
        }
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(100);
        paging.setSinceId(500);
        assertEquals(100, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(500L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructorWithInvalidSinceId() {
        try {
            Paging paging = new Paging(1);
            paging.setSinceId(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("since_id should be positive integer. passed:-1", e.getMessage());
        }
    }

    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(2, 50);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(2);
        paging.setSinceId(500);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(500L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageSinceIdConstructorWithInvalidSinceId() {
        try {
            Paging paging = new Paging(1);
            paging.setSinceId(-1);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("since_id should be positive integer. passed:-1", e.getMessage());
        }
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(2, 50);
        paging.setSinceId(500);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(500L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(2, 50, 500, 1000);
        assertEquals(2, paging.getPage());
        assertEquals(50, paging.getCount());
        assertEquals(500L, paging.getSinceId());
        assertEquals(1000L, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        paging.setPage(3);
        assertEquals(3, paging.getPage());
    }

    @Test
    public void testSetPageWithInvalidPage() {
        try {
            paging.setPage(-2);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("page should be positive integer. passed:-2", e.getMessage());
        }
    }

    @Test
    public void testSetCount() {
        paging.setCount(100);
        assertEquals(100, paging.getCount());
    }

    @Test
    public void testSetCountWithInvalidCount() {
        try {
            paging.setCount(-10);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("count should be positive integer. passed:-10", e.getMessage());
        }
    }

    @Test
    public void testCountMethod() {
        Paging result = paging.count(50);
        assertEquals(50, paging.getCount());
        assertSame(result, paging);
    }

    @Test
    public void testSetSinceId() {
        paging.setSinceId(123);
        assertEquals(123L, paging.getSinceId());
    }

    @Test
    public void testSetSinceIdWithInvalidSinceId() {
        try {
            paging.setSinceId(-50);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("since_id should be positive integer. passed:-50", e.getMessage());
        }
    }

    @Test
    public void testSinceIdMethod() {
        Paging result = paging.sinceId(500);
        assertEquals(500L, paging.getSinceId());
        assertSame(result, paging);
    }

    @Test
    public void testSetMaxId() {
        paging.setMaxId(1000);
        assertEquals(1000L, paging.getMaxId());
    }

    @Test
    public void testSetMaxIdWithInvalidMaxId() {
        try {
            paging.setMaxId(-100);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            assertEquals("max_id should be positive integer. passed:-100", e.getMessage());
        }
    }

    @Test
    public void testMaxIdMethod() {
        Paging result = paging.maxId(2000);
        assertEquals(2000L, paging.getMaxId());
        assertSame(result, paging);
    }

    @Test
    public void testEqualsWithEqualObjects() {
        Paging paging1 = new Paging(2, 50, 500, 1000);
        Paging paging2 = new Paging(2, 50, 500, 1000);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testEqualsWithDifferentObjects() {
        Paging paging1 = new Paging(2, 50, 500, 1000);
        Paging paging2 = new Paging(3, 50, 500, 1000);
        assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 50, 500, 1000);
        Paging paging2 = new Paging(2, 50, 500, 1000);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(2, 50, 500, 1000);
        String expected = "Paging{page=2, count=50, sinceId=500, maxId=1000}";
        assertEquals(expected, paging.toString());
    }

}