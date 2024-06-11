import org.junit.Assert;
import org.junit.Test;
import java.util.List;
import twitter4j.HttpParameter;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> parameterList = paging.asPostParameterList();

        Assert.assertNotNull(parameterList);
        Assert.assertEquals(1, parameterList.size());
        Assert.assertEquals("count", parameterList.get(0).getName());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] parameterArray = paging.asPostParameterArray();

        Assert.assertNotNull(parameterArray);
        Assert.assertEquals(1, parameterArray.length);
        Assert.assertEquals("count", parameterArray[0].getName());
    }

    @Test
    public void testAsPostParameterList_SupportedParams() {
        Paging paging = new Paging();
        List<HttpParameter> parameterList = paging.asPostParameterList(new char[] {'s', 'm', 'c'});

        Assert.assertNotNull(parameterList);
        Assert.assertEquals(3, parameterList.size());
        Assert.assertEquals("since_id", parameterList.get(0).getName());
        Assert.assertEquals("max_id", parameterList.get(1).getName());
        Assert.assertEquals("count", parameterList.get(2).getName());
    }

    @Test
    public void testAsPostParameterArray_SupportedParams() {
        Paging paging = new Paging();
        HttpParameter[] parameterArray = paging.asPostParameterArray(new char[]{'s', 'm', 'c'});

        Assert.assertNotNull(parameterArray);
        Assert.assertEquals(3, parameterArray.length);
        Assert.assertEquals("since_id", parameterArray[0].getName());
        Assert.assertEquals("max_id", parameterArray[1].getName());
        Assert.assertEquals("count", parameterArray[2].getName());
    }

    @Test
    public void testAddPostParameter_SupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> parameterList = new ArrayList<>();
        String paramName = "param";
        long paramValue = 100L;

        paging.addPostParameter(new char[]{'s', 'm'}, 's', parameterList, paramName, paramValue);

        Assert.assertEquals(1, parameterList.size());
        Assert.assertEquals("param", parameterList.get(0).getName());
        Assert.assertEquals("100", parameterList.get(0).getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameter_UnsupportedParam() {
        Paging paging = new Paging();
        List<HttpParameter> parameterList = new ArrayList<>();
        String paramName = "param";
        long paramValue = 100L;

        paging.addPostParameter(new char[]{'s', 'm'}, 'c', parameterList, paramName, paramValue);
    }

    @Test
    public void testPaging_DefaultConstructor() {
        Paging paging = new Paging();

        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPaging_PageConstructor() {
        Paging paging = new Paging(1);

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(-1, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPaging_SinceIdConstructor() {
        Paging paging = new Paging(12345L);

        Assert.assertEquals(-1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(12345L, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPaging_PageCountConstructor() {
        Paging paging = new Paging(1, 100);

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(100, paging.getCount());
        Assert.assertEquals(-1, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPaging_PageSinceIdConstructor() {
        Paging paging = new Paging(1, 12345L);

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(-1, paging.getCount());
        Assert.assertEquals(12345L, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPaging_PageCountSinceIdConstructor() {
        Paging paging = new Paging(1, 100, 12345L);

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(100, paging.getCount());
        Assert.assertEquals(12345L, paging.getSinceId());
        Assert.assertEquals(-1, paging.getMaxId());
    }

    @Test
    public void testPaging_AllParametersConstructor() {
        Paging paging = new Paging(1, 100, 12345L, 67890L);

        Assert.assertEquals(1, paging.getPage());
        Assert.assertEquals(100, paging.getCount());
        Assert.assertEquals(12345L, paging.getSinceId());
        Assert.assertEquals(67890L, paging.getMaxId());
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        paging.setPage(1);

        Assert.assertEquals(1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_NegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        paging.setCount(100);

        Assert.assertEquals(100, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_NegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging result = paging.count(100);

        Assert.assertSame(paging, result);
        Assert.assertEquals(100, paging.getCount());
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(12345L);

        Assert.assertEquals(12345L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_NegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(12345L);

        Assert.assertSame(paging, result);
        Assert.assertEquals(12345L, paging.getSinceId());
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(67890L);

        Assert.assertEquals(67890L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_NegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }

    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging result = paging.maxId(67890L);

        Assert.assertSame(paging, result);
        Assert.assertEquals(67890L, paging.getMaxId());
    }

    @Test
    public void testEquals_True() {
        Paging paging1 = new Paging(1, 100, 12345L, 67890L);
        Paging paging2 = new Paging(1, 100, 12345L, 67890L);

        Assert.assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testEquals_False() {
        Paging paging1 = new Paging(1, 100, 12345L, 67890L);
        Paging paging2 = new Paging(2, 200, 54321L, 9876L);

        Assert.assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode_EqualObjects() {
        Paging paging1 = new Paging(1, 100, 12345L, 67890L);
        Paging paging2 = new Paging(1, 100, 12345L, 67890L);

        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testHashCode_DifferentObjects() {
        Paging paging1 = new Paging(1, 100, 12345L, 67890L);
        Paging paging2 = new Paging(2, 200, 54321L, 9876L);

        Assert.assertNotEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 100, 12345L, 67890L);
        String expectedString = "Paging{page=1, count=100, sinceId=12345, maxId=67890}";

        Assert.assertEquals(expectedString, paging.toString());
    }
}