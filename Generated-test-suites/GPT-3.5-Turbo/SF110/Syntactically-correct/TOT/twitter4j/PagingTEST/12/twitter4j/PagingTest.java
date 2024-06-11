
package twitter4j;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    // Testing constructor with no parameters
    @Test
    public void testConstructorNoParameters() {
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    // Testing constructor with page parameter
    @Test
    public void testConstructorWithPage() {
        Paging newPaging = new Paging(2);
        assertEquals(2, newPaging.getPage());
        assertEquals(-1, newPaging.getCount());
        assertEquals(-1L, newPaging.getSinceId());
        assertEquals(-1L, newPaging.getMaxId());
    }

    // Testing constructor with sinceId parameter
    @Test
    public void testConstructorWithSinceId() {
        Paging newPaging = new Paging(123);
        assertEquals(-1, newPaging.getPage());
        assertEquals(-1, newPaging.getCount());
        assertEquals(123L, newPaging.getSinceId());
        assertEquals(-1L, newPaging.getMaxId());
    }

    // Testing constructor with page and count parameters
    @Test
    public void testConstructorWithPageAndCount() {
        Paging newPaging = new Paging(3, 50);
        assertEquals(3, newPaging.getPage());
        assertEquals(50, newPaging.getCount());
        assertEquals(-1L, newPaging.getSinceId());
        assertEquals(-1L, newPaging.getMaxId());
    }

    // Testing constructor with page and sinceId parameters
    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging newPaging = new Paging(4, 567);
        assertEquals(4, newPaging.getPage());
        assertEquals(-1, newPaging.getCount());
        assertEquals(567L, newPaging.getSinceId());
        assertEquals(-1L, newPaging.getMaxId());
    }

    // Testing constructor with page, count, and sinceId parameters
    @Test
    public void testConstructorWithPageCountAndSinceId() {
        Paging newPaging = new Paging(5, 100, 789);
        assertEquals(5, newPaging.getPage());
        assertEquals(100, newPaging.getCount());
        assertEquals(789L, newPaging.getSinceId());
        assertEquals(-1L, newPaging.getMaxId());
    }

    // Testing constructor with page, count, sinceId, and maxId parameters
    @Test
    public void testConstructorWithAllParameters() {
        Paging newPaging = new Paging(6, 200, 888, 999);
        assertEquals(6, newPaging.getPage());
        assertEquals(200, newPaging.getCount());
        assertEquals(888L, newPaging.getSinceId());
        assertEquals(999L, newPaging.getMaxId());
    }

    // Testing setter and getter for page
    @Test
    public void testSetGetPage() {
        paging.setPage(7);
        assertEquals(7, paging.getPage());
    }

    // Testing setter and getter for count
    @Test
    public void testSetGetCount() {
        paging.setCount(150);
        assertEquals(150, paging.getCount());
    }

    // Testing setter and getter for sinceId
    @Test
    public void testSetGetSinceId() {
        paging.setSinceId(111);
        assertEquals(111L, paging.getSinceId());
    }

    // Testing setter and getter for maxId
    @Test
    public void testSetGetMaxId() {
        paging.setMaxId(222);
        assertEquals(222L, paging.getMaxId());
    }

    // Testing count() method chaining
    @Test
    public void testCountMethodChaining() {
        assertEquals(paging, paging.count(50));
        assertEquals(50, paging.getCount());
    }

    // Testing sinceId() method chaining
    @Test
    public void testSinceIdMethodChaining() {
        assertEquals(paging, paging.sinceId(123));
        assertEquals(123L, paging.getSinceId());
    }

    // Testing maxId() method chaining
    @Test
    public void testMaxIdMethodChaining() {
        assertEquals(paging, paging.maxId(456));
        assertEquals(456L, paging.getMaxId());
    }
    
    // Testing addPostParameter with supported params
    // Expecting a list of HttpParameter with the specified values
    @Test
    public void testAddPostParameterSupportedParams() {
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(new char[]{'m', 'c'}, 's', pagingParams, "since_id", 123);
        assertEquals(1, pagingParams.size());
        assertEquals("since_id=123", pagingParams.get(0).asString());
    }

    // Testing addPostParameter with unsupported param
    // Expecting an IllegalStateException to be thrown
    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterUnsupportedParam() {
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(new char[]{'m', 'c'}, 'p', pagingParams, "page", 1);
    }

    // Testing addPostParameter with -1 param value
    // Expecting the parameter not to be added to the list
    @Test
    public void testAddPostParameterNegativeParamValue() {
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(new char[]{'m', 'c'}, 's', pagingParams, "since_id", -1);
        assertEquals(0, pagingParams.size());
    }

    // Testing asPostParameterList with supported params and perPageParamName = "count"
    // Expecting a list of HttpParameter with the specified values
    @Test
    public void testAsPostParameterListSupportedParams() {
        List<HttpParameter> pagingParams = paging.asPostParameterList(new char[]{'m', 'c'}, "count");
        assertEquals(2, pagingParams.size());
        assertEquals("since_id=-1", pagingParams.get(0).asString());
        assertEquals("count=-1", pagingParams.get(1).asString());
    }

    // Testing asPostParameterList with supported params and perPageParamName = "perPage"
    // Expecting a list of HttpParameter with the specified values
    @Test
    public void testAsPostParameterListSupportedParamsWithPerPageParamName() {
        List<HttpParameter> pagingParams = paging.asPostParameterList(new char[]{'m', 'c'}, "perPage");
        assertEquals(2, pagingParams.size());
        assertEquals("since_id=-1", pagingParams.get(0).asString());
        assertEquals("perPage=-1", pagingParams.get(1).asString());
    }

    // Testing asPostParameterList with unsupported param
    // Expecting an empty list of HttpParameter
    @Test
    public void testAsPostParameterListUnsupportedParam() {
        List<HttpParameter> pagingParams = paging.asPostParameterList(new char[]{'m', 'c'}, "page");
        assertEquals(0, pagingParams.size());
    }

    // Testing asPostParameterList with -1 param values
    // Expecting an empty list of HttpParameter
    @Test
    public void testAsPostParameterListNegativeParamValues() {
        List<HttpParameter> pagingParams = paging.asPostParameterList(new char[]{'m', 'c'}, "count");
        assertEquals(0, pagingParams.size());
    }

    // Testing asPostParameterArray with supported params and perPageParamName = "count"
    // Expecting an array of HttpParameter with the specified values
    @Test
    public void testAsPostParameterArraySupportedParams() {
        HttpParameter[] pagingParams = paging.asPostParameterArray(new char[]{'m', 'c'}, "count");
        assertEquals(2, pagingParams.length);
        assertEquals("since_id=-1", pagingParams[0].asString());
        assertEquals("count=-1", pagingParams[1].asString());
    }

    // Testing asPostParameterArray with supported params and perPageParamName = "perPage"
    // Expecting an array of HttpParameter with the specified values
    @Test
    public void testAsPostParameterArraySupportedParamsWithPerPageParamName() {
        HttpParameter[] pagingParams = paging.asPostParameterArray(new char[]{'m', 'c'}, "perPage");
        assertEquals(2, pagingParams.length);
        assertEquals("since_id=-1", pagingParams[0].asString());
        assertEquals("perPage=-1", pagingParams[1].asString());
    }

    // Testing asPostParameterArray with unsupported param
    // Expecting an empty array of HttpParameter
    @Test
    public void testAsPostParameterArrayUnsupportedParam() {
        HttpParameter[] pagingParams = paging.asPostParameterArray(new char[]{'m', 'c'}, "page");
        assertEquals(0, pagingParams.length);
    }

    // Testing asPostParameterArray with -1 param values
    // Expecting an empty array of HttpParameter
    @Test
    public void testAsPostParameterArrayNegativeParamValues() {
        HttpParameter[] pagingParams = paging.asPostParameterArray(new char[]{'m', 'c'}, "count");
        assertEquals(0, pagingParams.length);
    }

    // Testing equals method for equal Paging objects
    @Test
    public void testEqualsForEqualObjects() {
        Paging paging1 = new Paging(1, 100);
        Paging paging2 = new Paging(1, 100);
        assertTrue(paging1.equals(paging2));
    }

    // Testing equals method for unequal Paging objects
    @Test
    public void testEqualsForUnequalObjects() {
        Paging paging1 = new Paging(1, 100);
        Paging paging2 = new Paging(2, 200);
        assertFalse(paging1.equals(paging2));
    }

    // Testing hashCode method for equal Paging objects
    @Test
    public void testHashCodeForEqualObjects() {
        Paging paging1 = new Paging(1, 100);
        Paging paging2 = new Paging(1, 100);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    // Testing hashCode method for unequal Paging objects
    @Test
    public void testHashCodeForUnequalObjects() {
        Paging paging1 = new Paging(1, 100);
        Paging paging2 = new Paging(2, 200);
        assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }

    // Testing toString method
    @Test
    public void testToString() {
        Paging paging = new Paging(3, 50, 987, 654);
        assertEquals("Paging{page=3, count=50, sinceId=987, maxId=654}", paging.toString());
    }

}

