
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import twitter4j.internal.http.HttpParameter;

public class PagingTest {

    // Test the default constructor
    @Test
    public void testConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    // Test the constructor with only page parameter
    @Test
    public void testConstructorWithPage() {
        int page = 3;
        Paging paging = new Paging(page);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    // Test the constructor with only sinceId parameter
    @Test
    public void testConstructorWithSinceId() {
        long sinceId = 12345678;
        Paging paging = new Paging(sinceId);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    // Test the constructor with page and count parameters
    @Test
    public void testConstructorWithPageAndCount() {
        int page = 2;
        int count = 50;
        Paging paging = new Paging(page, count);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(-1, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    // Test the constructor with page and sinceId parameters
    @Test
    public void testConstructorWithPageAndSinceId() {
        int page = 1;
        long sinceId = 98765432;
        Paging paging = new Paging(page, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    // Test the constructor with page, count, and sinceId parameters
    @Test
    public void testConstructorWithPageCountAndSinceId() {
        int page = 3;
        int count = 10;
        long sinceId = 11223344;
        Paging paging = new Paging(page, count, sinceId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(-1, paging.getMaxId());
    }

    // Test the constructor with all parameters
    @Test
    public void testConstructorWithAllParameters() {
        int page = 2;
        int count = 20;
        long sinceId = 55667788;
        long maxId = 99887766;
        Paging paging = new Paging(page, count, sinceId, maxId);
        assertEquals(page, paging.getPage());
        assertEquals(count, paging.getCount());
        assertEquals(sinceId, paging.getSinceId());
        assertEquals(maxId, paging.getMaxId());
    }

    // Test the setPage method with a positive page number
    @Test
    public void testSetPagePositiveNumber() {
        int page = 5;
        Paging paging = new Paging();
        paging.setPage(page);
        assertEquals(page, paging.getPage());
    }

    // Test the setPage method with a negative page number
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageNegativeNumber() {
        int page = -2;
        Paging paging = new Paging();
        paging.setPage(page);
    }

    // Test the setCount method with a positive count
    @Test
    public void testSetCountPositiveNumber() {
        int count = 100;
        Paging paging = new Paging();
        paging.setCount(count);
        assertEquals(count, paging.getCount());
    }

    // Test the setCount method with a negative count
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountNegativeNumber() {
        int count = -50;
        Paging paging = new Paging();
        paging.setCount(count);
    }

    // Test the setSinceId method with a positive sinceId
    @Test
    public void testSetSinceIdPositiveNumber() {
        long sinceId = 98765432;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
        assertEquals(sinceId, paging.getSinceId());
    }

    // Test the setSinceId method with a negative sinceId
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdNegativeNumber() {
        long sinceId = -12345678;
        Paging paging = new Paging();
        paging.setSinceId(sinceId);
    }

    // Test the setMaxId method with a positive maxId
    @Test
    public void testSetMaxIdPositiveNumber() {
        long maxId = 99887766;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
        assertEquals(maxId, paging.getMaxId());
    }

    // Test the setMaxId method with a negative maxId
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdNegativeNumber() {
        long maxId = -11223344;
        Paging paging = new Paging();
        paging.setMaxId(maxId);
    }

    // Test the equals method for equality
    @Test
    public void testEqualsEqualObject() {
        Paging paging1 = new Paging(1, 10, 12345678, 98765432);
        Paging paging2 = new Paging(1, 10, 12345678, 98765432);
        assertTrue(paging1.equals(paging2));
    }

    // Test the equals method for inequality
    @Test
    public void testEqualsUnequalObject() {
        Paging paging1 = new Paging(1, 10, 12345678, 98765432);
        Paging paging2 = new Paging(2, 20, 87654321, 98765432);
        assertFalse(paging1.equals(paging2));
    }

    // Test the hashCode method for equality
    @Test
    public void testHashCodeEqualObject() {
        Paging paging1 = new Paging(1, 10, 12345678, 98765432);
        Paging paging2 = new Paging(1, 10, 12345678, 98765432);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    // Test the hashCode method for inequality
    @Test
    public void testHashCodeUnequalObject() {
        Paging paging1 = new Paging(1, 10, 12345678, 98765432);
        Paging paging2 = new Paging(2, 20, 87654321, 98765432);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }

    // Test the toString method
    @Test
    public void testToString() {
        int page = 2;
        int count = 20;
        long sinceId = 55667788;
        long maxId = 99887766;
        Paging paging = new Paging(page, count, sinceId, maxId);
        String expected = "Paging{page=" + page + ", count=" + count + ", sinceId=" + sinceId + ", maxId=" + maxId + "}";
        assertEquals(expected, paging.toString());
    }

    // Test the asPostParameterList method with default parameters
    @Test
    public void testAsPostParameterListDefaultParameters() {
        Paging paging = new Paging();
        List<HttpParameter> parameterList = paging.asPostParameterList();
        assertEquals(0, parameterList.size());
    }

    // Test the asPostParameterArray method with default parameters
    @Test
    public void testAsPostParameterArrayDefaultParameters() {
        Paging paging = new Paging();
        HttpParameter[] parameterArray = paging.asPostParameterArray();
        assertEquals(0, parameterArray.length);
    }

    // Test the asPostParameterList method with custom parameters
    @Test
    public void testAsPostParameterListCustomParameters() {
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        String perPageParamName = "per_page";
        Paging paging = new Paging(1, 10, 12345678, 98765432);
        List<HttpParameter> parameterList = paging.asPostParameterList(supportedParams, perPageParamName);
        assertEquals(4, parameterList.size());
    }

    // Test the asPostParameterArray method with custom parameters
    @Test
    public void testAsPostParameterArrayCustomParameters() {
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        String perPageParamName = "per_page";
        Paging paging = new Paging(1, 10, 12345678, 98765432);
        HttpParameter[] parameterArray = paging.asPostParameterArray(supportedParams, perPageParamName);
        assertEquals(4, parameterArray.length);
    }

}
