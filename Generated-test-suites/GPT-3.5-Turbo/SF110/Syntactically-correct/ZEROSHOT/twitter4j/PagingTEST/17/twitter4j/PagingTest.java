import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import twitter4j.internal.http.HttpParameter;

import java.util.ArrayList;
import java.util.List;

public class PagingTest {

    private Paging paging;

    @Before
    public void setUp() {
        paging = new Paging();
    }

    @Test
    public void testDefaultConstructor() {
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPageConstructor() {
        paging = new Paging(2);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testSinceIdConstructor() {
        paging = new Paging(123456);
        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(123456, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPageAndCountConstructor() {
        paging = new Paging(2, 10);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(-1, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPageAndSinceIdConstructor() {
        paging = new Paging(2, 123456);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(123456, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPageCountAndSinceIdConstructor() {
        paging = new Paging(2, 10, 123456);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(123456, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testAllConstructor() {
        paging = new Paging(2, 10, 123456, 789012);
        Assert.assertEquals(2, paging.getPage());
        Assert.assertEquals(10, paging.getCount());
        Assert.assertEquals(123456, paging.getSinceId());
        Assert.assertEquals(789012, paging.getMaxId());
    }

    @Test
    public void testSetPage() {
        paging.setPage(5);
        Assert.assertEquals(5, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithInvalidValue() {
        paging.setPage(-2);
    }

    @Test
    public void testSetCount() {
        paging.setCount(20);
        Assert.assertEquals(20, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithInvalidValue() {
        paging.setCount(-5);
    }

    @Test
    public void testCountMethod() {
        paging.count(15);
        Assert.assertEquals(15, paging.getCount());
    }

    @Test
    public void testSetSinceId() {
        paging.setSinceId(987654);
        Assert.assertEquals(987654, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithInvalidValue() {
        paging.setSinceId(-654321);
    }

    @Test
    public void testSinceIdMethod() {
        paging.sinceId(345678);
        Assert.assertEquals(345678, paging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        paging.setMaxId(321098);
        Assert.assertEquals(321098, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithInvalidValue() {
        paging.setMaxId(-890123);
    }

    @Test
    public void testMaxIdMethod() {
        paging.maxId(567890);
        Assert.assertEquals(567890, paging.getMaxId());
    }

    @Test
    public void testAsPostParameterList() {
        List<HttpParameter> parameterList = paging.asPostParameterList();
        Assert.assertEquals(0, parameterList.size());
    }

    @Test
    public void testAsPostParameterArray() {
        HttpParameter[] parameters = paging.asPostParameterArray();
        Assert.assertEquals(0, parameters.length);
    }

    @Test
    public void testAsPostParameterListWithSupportedParams() {
        char[] supportedParams = {'m', 'c', 'p'};
        List<HttpParameter> parameterList = paging.asPostParameterList(supportedParams);
        Assert.assertEquals(0, parameterList.size());
    }

    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        char[] supportedParams = {'m', 'c', 'p'};
        HttpParameter[] parameters = paging.asPostParameterArray(supportedParams);
        Assert.assertEquals(0, parameters.length);
    }

    @Test
    public void testAddPostParameter() {
        List<HttpParameter> parameterList = new ArrayList<>();
        paging.addPostParameter(new char[]{'m', 'c', 'p'}, 'm', parameterList, "param", 123);
        Assert.assertEquals(1, parameterList.size());
        Assert.assertEquals("param", parameterList.get(0).getName());
        Assert.assertEquals("123", parameterList.get(0).getValue());

        parameterList.clear();
        paging.addPostParameter(new char[]{'m', 'c', 'p'}, 's', parameterList, "param", -1);
        Assert.assertEquals(0, parameterList.size());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameterWithUnsupportedKey() {
        List<HttpParameter> parameterList = new ArrayList<>();
        paging.addPostParameter(new char[]{'m', 'c', 'p'}, 's', parameterList, "param", 456);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(2, 10, 123456, 789012);
        Paging paging2 = new Paging(2, 10, 123456, 789012);
        Assert.assertEquals(paging1, paging2);
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(2, 10, 123456, 789012);
        Paging paging2 = new Paging(2, 10, 123456, 789012);
        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging1 = new Paging(2, 10, 123456, 789012);
        String expected = "Paging{page=2, count=10, sinceId=123456, maxId=789012}";
        Assert.assertEquals(expected, paging1.toString());
    }
}