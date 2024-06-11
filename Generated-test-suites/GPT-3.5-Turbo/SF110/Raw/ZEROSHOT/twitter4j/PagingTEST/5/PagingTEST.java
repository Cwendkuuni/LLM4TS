import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        List<HttpParameter> result = paging.asPostParameterList();
        assertEquals(Paging.NULL_PARAMETER_LIST, result);
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        HttpParameter[] result = paging.asPostParameterArray();
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, result);
    }

    @Test
    public void testAsPostParameterList_CharArray() {
        Paging paging = new Paging();
        List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP);
        assertEquals(Paging.NULL_PARAMETER_LIST, result);
    }

    @Test
    public void testAsPostParameterList_CharArray_String() {
        Paging paging = new Paging();
        List<HttpParameter> result = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(Paging.NULL_PARAMETER_LIST, result);
    }

    @Test
    public void testAsPostParameterArray_CharArray_String() {
        Paging paging = new Paging();
        HttpParameter[] result = paging.asPostParameterArray(Paging.SMCP, "count");
        assertArrayEquals(Paging.NULL_PARAMETER_ARRAY, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.S, 's', pagingParams, "since_id", -1L);
    }

    @Test
    public void testAddPostParameter_ParamNotSupported() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'x', pagingParams, "some_param", 123);
        assertEquals(0, pagingParams.size());
    }

    @Test
    public void testAddPostParameter_ParamSupported_ValueNotNegativeOne() {
        Paging paging = new Paging();
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", 123);
        assertEquals(1, pagingParams.size());
        HttpParameter param = pagingParams.get(0);
        assertEquals("since_id", param.getName());
        assertEquals("123", param.getValue());
    }

    @Test
    public void testPaging_DefaultConstructor() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageConstructor() {
        Paging paging = new Paging(3);
        assertEquals(3, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_SinceIdConstructor() {
        Paging paging = new Paging(123);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageCountConstructor() {
        Paging paging = new Paging(3, 10);
        assertEquals(3, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageSinceIdConstructor() {
        Paging paging = new Paging(3, 123);
        assertEquals(3, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_PageCountSinceIdConstructor() {
        Paging paging = new Paging(3, 10, 123);
        assertEquals(3, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testPaging_AllArgsConstructor() {
        Paging paging = new Paging(3, 10, 123, 456);
        assertEquals(3, paging.getPage());
        assertEquals(10, paging.getCount());
        assertEquals(123L, paging.getSinceId());
        assertEquals(456L, paging.getMaxId());
    }

    @Test
    public void testGetPage() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(3);
        assertEquals(3, paging.getPage());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPage_NegativePage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }

    @Test
    public void testGetCount() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getCount());
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(10);
        assertEquals(10, paging.getCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCount_NegativeCount() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }

    @Test
    public void testCount() {
        Paging paging = new Paging();
        Paging result = paging.count(10);
        assertEquals(10, paging.getCount());
        assertSame(paging, result);
    }

    @Test
    public void testGetSinceId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getSinceId());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(123);
        assertEquals(123L, paging.getSinceId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceId_NegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-1);
    }

    @Test
    public void testSinceId() {
        Paging paging = new Paging();
        Paging result = paging.sinceId(123);
        assertEquals(123L, paging.getSinceId());
        assertSame(paging, result);
    }

    @Test
    public void testGetMaxId() {
        Paging paging = new Paging();
        assertEquals(-1L, paging.getMaxId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(456);
        assertEquals(456L, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxId_NegativeMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-1);
    }

    @Test
    public void testMaxId() {
        Paging paging = new Paging();
        Paging result = paging.maxId(456);
        assertEquals(456L, paging.getMaxId());
        assertSame(paging, result);
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(3, 10, 123, 456);
        Paging paging2 = new Paging(3, 10, 123, 456);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testEquals_SameObject() {
        Paging paging = new Paging(3, 10, 123, 456);
        assertTrue(paging.equals(paging));
    }

    @Test
    public void testEquals_NullObject() {
        Paging paging = new Paging(3, 10, 123, 456);
        assertFalse(paging.equals(null));
    }

    @Test
    public void testEquals_DifferentClass() {
        Paging paging = new Paging(3, 10, 123, 456);
        assertFalse(paging.equals("string"));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(3, 10, 123, 456);
        int hashCode = 31 * (31 * (31 * 3 + 10) + (int) (123 ^ (123 >>> 32))) + (int) (456 ^ (456 >>> 32));
        assertEquals(hashCode, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(3, 10, 123, 456);
        String expected = "Paging{page=3, count=10, sinceId=123, maxId=456}";
        assertEquals(expected, paging.toString());
    }
}