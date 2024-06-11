import org.junit.Assert;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class PagingTest {

    @Test
    public void testAsPostParameterList_DefaultConstructor_ReturnsEmptyList() {
        Paging paging = new Paging();
        List<HttpParameter> result = paging.asPostParameterList();
        Assert.assertEquals(0, result.size());
    }

    @Test
    public void testAsPostParameterArray_DefaultConstructor_ReturnsEmptyArray() {
        Paging paging = new Paging();
        HttpParameter[] result = paging.asPostParameterArray();
        Assert.assertEquals(0, result.length);
    }

    @Test
    public void testAsPostParameterList_SupportedParamsConstructor_ReturnsExpectedList() {
        Paging paging = new Paging(1, 10, 100, 200);
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        List<HttpParameter> result = paging.asPostParameterList(supportedParams, "per_page");
        Assert.assertEquals(4, result.size());
        Assert.assertTrue(result.contains(new HttpParameter("since_id", "100")));
        Assert.assertTrue(result.contains(new HttpParameter("max_id", "200")));
        Assert.assertTrue(result.contains(new HttpParameter("count", "10")));
        Assert.assertTrue(result.contains(new HttpParameter("page", "1")));
    }

    @Test
    public void testAsPostParameterArray_SupportedParamsConstructor_ReturnsExpectedArray() {
        Paging paging = new Paging(1, 10, 100, 200);
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        HttpParameter[] result = paging.asPostParameterArray(supportedParams, "per_page");
        Assert.assertEquals(4, result.length);
        Assert.assertTrue(Arrays.asList(result).contains(new HttpParameter("since_id", "100")));
        Assert.assertTrue(Arrays.asList(result).contains(new HttpParameter("max_id", "200")));
        Assert.assertTrue(Arrays.asList(result).contains(new HttpParameter("count", "10")));
        Assert.assertTrue(Arrays.asList(result).contains(new HttpParameter("page", "1")));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameter_UnsupportedParamValue_ThrowsException() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] {'m', 'c'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(supportedParams, 's', pagingParams, "since_id", 100);
    }

    @Test
    public void testAddPostParameter_SupportedParamValue_AddsToPagingParams() {
        Paging paging = new Paging();
        char[] supportedParams = new char[] {'s', 'm', 'c', 'p'};
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(supportedParams, 's', pagingParams, "since_id", 100);
        Assert.assertEquals(1, pagingParams.size());
        Assert.assertTrue(pagingParams.contains(new HttpParameter("since_id", "100")));
    }

    @Test
    public void testPaging_DefaultConstructor_PageIsSetCorrectly() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_InvalidPageValue_ThrowsException() {
        Paging paging = new Paging();
        paging.setPage(0);
    }

    @Test
    public void testSetPage_ValidPageValue_PageIsSetCorrectly() {
        Paging paging = new Paging();
        paging.setPage(1);
        Assert.assertEquals(1, paging.getPage());
    }

    @Test
    public void testPaging_DefaultConstructor_CountIsSetCorrectly() {
        Paging paging = new Paging();
        Assert.assertEquals(-1, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_InvalidCountValue_ThrowsException() {
        Paging paging = new Paging();
        paging.setCount(0);
    }

    @Test
    public void testSetCount_ValidCountValue_CountIsSetCorrectly() {
        Paging paging = new Paging();
        paging.setCount(10);
        Assert.assertEquals(10, paging.getCount());
    }

    @Test
    public void testPaging_DefaultConstructor_SinceIdIsSetCorrectly() {
        Paging paging = new Paging();
        Assert.assertEquals(-1L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_InvalidSinceIdValue_ThrowsException() {
        Paging paging = new Paging();
        paging.setSinceId(-10L);
    }

    @Test
    public void testSetSinceId_ValidSinceIdValue_SinceIdIsSetCorrectly() {
        Paging paging = new Paging();
        paging.setSinceId(100L);
        Assert.assertEquals(100L, paging.getSinceId());
    }

    @Test
    public void testPaging_DefaultConstructor_MaxIdIsSetCorrectly() {
        Paging paging = new Paging();
        Assert.assertEquals(-1L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_InvalidMaxIdValue_ThrowsException() {
        Paging paging = new Paging();
        paging.setMaxId(-10L);
    }

    @Test
    public void testSetMaxId_ValidMaxIdValue_MaxIdIsSetCorrectly() {
        Paging paging = new Paging();
        paging.setMaxId(200L);
        Assert.assertEquals(200L, paging.getMaxId());
    }

    @Test
    public void testEquals_SameInstances_ReturnsTrue() {
        Paging paging = new Paging(1, 10, 100, 200);
        Assert.assertTrue(paging.equals(paging));
    }

    @Test
    public void testEquals_DifferentClass_ReturnsFalse() {
        Paging paging = new Paging(1, 10, 100, 200);
        Assert.assertFalse(paging.equals("invalidType"));
    }

    @Test
    public void testEquals_EqualPaging_ReturnsTrue() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(1, 10, 100, 200);
        Assert.assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testEquals_DifferentPaging_ReturnsFalse() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(2, 20, 200, 300);
        Assert.assertFalse(paging1.equals(paging2));
    }

    @Test
    public void testHashCode_EqualPaging_ReturnsSameHashCode() {
        Paging paging1 = new Paging(1, 10, 100, 200);
        Paging paging2 = new Paging(1, 10, 100, 200);
        Assert.assertEquals(paging1.hashCode(), paging2.hashCode());
    }

    @Test
    public void testToString_ReturnsExpectedString() {
        Paging paging = new Paging(1, 10, 100, 200);
        String expected = "Paging{page=1, count=10, sinceId=100, maxId=200}";
        Assert.assertEquals(expected, paging.toString());
    }

}