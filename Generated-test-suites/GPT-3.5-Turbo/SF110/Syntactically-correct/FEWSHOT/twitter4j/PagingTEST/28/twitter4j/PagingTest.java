import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class PagingTest {
    @Test
    public void testConstructorWithNoArguments() {
        Paging paging = new Paging();
        assertEquals(paging.getPage(), -1);
        assertEquals(paging.getCount(), -1);
        assertEquals(paging.getSinceId(), -1L);
        assertEquals(paging.getMaxId(), -1L);
    }

    @Test
    public void testConstructorWithPageArgument() {
        Paging paging = new Paging(2);
        assertEquals(paging.getPage(), 2);
        assertEquals(paging.getCount(), -1);
        assertEquals(paging.getSinceId(), -1L);
        assertEquals(paging.getMaxId(), -1L);
    }

    @Test
    public void testConstructorWithSinceIdArgument() {
        Paging paging = new Paging(123456789L);
        assertEquals(paging.getPage(), -1);
        assertEquals(paging.getCount(), -1);
        assertEquals(paging.getSinceId(), 123456789L);
        assertEquals(paging.getMaxId(), -1L);
    }

    @Test
    public void testConstructorWithPageAndCountArguments() {
        Paging paging = new Paging(2, 100);
        assertEquals(paging.getPage(), 2);
        assertEquals(paging.getCount(), 100);
        assertEquals(paging.getSinceId(), -1L);
        assertEquals(paging.getMaxId(), -1L);
    }

    @Test
    public void testConstructorWithPageAndSinceIdArguments() {
        Paging paging = new Paging(2, 123456789L);
        assertEquals(paging.getPage(), 2);
        assertEquals(paging.getCount(), -1);
        assertEquals(paging.getSinceId(), 123456789L);
        assertEquals(paging.getMaxId(), -1L);
    }

    @Test
    public void testConstructorWithPageCountAndSinceIdArguments() {
        Paging paging = new Paging(2, 100, 123456789L);
        assertEquals(paging.getPage(), 2);
        assertEquals(paging.getCount(), 100);
        assertEquals(paging.getSinceId(), 123456789L);
        assertEquals(paging.getMaxId(), -1L);
    }

    @Test
    public void testConstructorWithAllArguments() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        assertEquals(paging.getPage(), 2);
        assertEquals(paging.getCount(), 100);
        assertEquals(paging.getSinceId(), 123456789L);
        assertEquals(paging.getMaxId(), 987654321L);
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(3);
        assertEquals(paging.getPage(), 3);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(100);
        assertEquals(paging.getCount(), 100);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testCountMethod() {
        Paging paging = new Paging();
        Paging updatedPaging = paging.count(100);
        assertEquals(updatedPaging.getCount(), 100);
        assertTrue(updatedPaging == paging);
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(123456789L);
        assertEquals(paging.getSinceId(), 123456789L);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSinceIdMethod() {
        Paging paging = new Paging();
        Paging updatedPaging = paging.sinceId(123456789L);
        assertEquals(updatedPaging.getSinceId(), 123456789L);
        assertTrue(updatedPaging == paging);
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(987654321L);
        assertEquals(paging.getMaxId(), 987654321L);
    }

    @Test(expected=IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testMaxIdMethod() {
        Paging paging = new Paging();
        Paging updatedPaging = paging.maxId(987654321L);
        assertEquals(updatedPaging.getMaxId(), 987654321L);
        assertTrue(updatedPaging == paging);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 100, 123456789L, 987654321L);
        Paging paging2 = new Paging(2, 100, 123456789L, 987654321L);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        assertEquals(paging.hashCode(), 288135853);
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        String expected = "Paging{page=2, count=100, sinceId=123456789, maxId=987654321}";
        assertEquals(paging.toString(), expected);
    }

    // Additional Test Cases
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        // The test cases for asPostParameterList() method can be added here
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        // The test cases for asPostParameterArray() method can be added here
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        // The test cases for asPostParameterList(char[] supportedParams) method can be added here
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        // The test cases for asPostParameterArray(char[] supportedParams) method can be added here
    }

    @Test
    public void testAsPostParameterListWithSupportedParamsAndPerPageParamName() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        // The test cases for asPostParameterList(char[] supportedParams, String perPageParamName) method can be added here
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParamsAndPerPageParamName() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        // The test cases for asPostParameterArray(char[] supportedParams, String perPageParamName) method can be added here
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging(2, 100, 123456789L, 987654321L);
        // The test cases for addPostParameter() method can be added here
    }

    //You can add more test cases for the remaining methods in the Paging class

}