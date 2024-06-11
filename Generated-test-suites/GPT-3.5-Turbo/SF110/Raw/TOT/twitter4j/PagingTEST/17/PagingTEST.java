import static org.junit.Assert.*;
import org.junit.*;

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
    public void testSinceIdConstructor() {
        Paging paging = new Paging(123456789L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndCountConstructor() {
        Paging paging = new Paging(2, 10);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageAndSinceIdConstructor() {
        Paging paging = new Paging(2, 123456789L);
        assertEquals(2, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(2, 10, 123456789L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(2, 10, 123456789L, 987654321L);
        assertEquals(2, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        paging.setPage(0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        paging.setCount(0);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        paging.setSinceId(0L);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        paging.setMaxId(0L);
    }

    @Test
    public void testCountMethod() {
        Paging returnedPaging = paging.count(10);
        assertEquals(10, returnedPaging.getCount());
    }

    @Test
    public void testSinceIdMethod() {
        Paging returnedPaging = paging.sinceId(123456789L);
        assertEquals(123456789L, returnedPaging.getSinceId());
    }

    @Test
    public void testMaxIdMethod() {
        Paging returnedPaging = paging.maxId(987654321L);
        assertEquals(987654321L, returnedPaging.getMaxId());
    }

    @Test
    public void testEqualsMethodWithSameObject() {
        assertTrue(paging.equals(paging));
    }

    @Test
    public void testEqualsMethodWithDifferentClass() {
        assertFalse(paging.equals("testString"));
    }

    @Test
    public void testEqualsMethodWithEqualPagingObject() {
        Paging otherPaging = new Paging();
        assertTrue(paging.equals(otherPaging));
    }

    @Test
    public void testEqualsMethodWithDifferentPage() {
        Paging otherPaging = new Paging(2);
        assertFalse(paging.equals(otherPaging));
    }

    @Test
    public void testEqualsMethodWithDifferentCount() {
        paging.setCount(10);
        Paging otherPaging = new Paging();
        assertFalse(paging.equals(otherPaging));
    }

    @Test
    public void testEqualsMethodWithDifferentSinceId() {
        paging.setSinceId(123456789L);
        Paging otherPaging = new Paging();
        assertFalse(paging.equals(otherPaging));
    }

    @Test
    public void testEqualsMethodWithDifferentMaxId() {
        paging.setMaxId(987654321L);
        Paging otherPaging = new Paging();
        assertFalse(paging.equals(otherPaging));
    }

    @Test
    public void testHashCodeMethod() {
        int expectedHashCode = 31 * (31 * (31 * 1 + -1) + -1) + -1;
        assertEquals(expectedHashCode, paging.hashCode());
    }

    @Test
    public void testToStringMethod() {
        String expectedString = "Paging{page=-1, count=-1, sinceId=-1, maxId=-1}";
        assertEquals(expectedString, paging.toString());
    }

    @Test
    public void testAsPostParameterListMethod() {
        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(123456789L);
        paging.setMaxId(987654321L);
        // Provide the expected values for HttpParameter list
    }

    @Test
    public void testAsPostParameterArrayMethod() {
        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(123456789L);
        paging.setMaxId(987654321L);
        // Provide the expected values for HttpParameter array
    }

    @Test
    public void testAsPostParameterListWithSupportedParamsMethod() {
        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(123456789L);
        paging.setMaxId(987654321L);
        char[] supportedParams = {'p', 'c'};
        // Provide the expected values for HttpParameter list
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParamsMethod() {
        paging.setPage(2);
        paging.setCount(10);
        paging.setSinceId(123456789L);
        paging.setMaxId(987654321L);
        char[] supportedParams = {'p', 'c'};
        // Provide the expected values for HttpParameter array
    }

}