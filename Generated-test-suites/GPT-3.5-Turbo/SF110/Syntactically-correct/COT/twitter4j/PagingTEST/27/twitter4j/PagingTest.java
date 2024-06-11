
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testConstructorDefaultValues() {
        Paging paging = new Paging();
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPage() {
        Paging paging = new Paging(1);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithSinceId() {
        Paging paging = new Paging(123456789L);
        assertEquals(-1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndCount() {
        Paging paging = new Paging(1, 20);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(-1L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageAndSinceId() {
        Paging paging = new Paging(1, 123456789L);
        assertEquals(1, paging.getPage());
        assertEquals(-1, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceId() {
        Paging paging = new Paging(1, 20, 123456789L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(-1L, paging.getMaxId());
    }
    
    @Test
    public void testConstructorWithPageCountSinceIdMaxId() {
        Paging paging = new Paging(1, 20, 123456789L, 987654321L);
        assertEquals(1, paging.getPage());
        assertEquals(20, paging.getCount());
        assertEquals(123456789L, paging.getSinceId());
        assertEquals(987654321L, paging.getMaxId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test
    public void testSetPageWithPositiveValue() {
        Paging paging = new Paging();
        paging.setPage(1);
        assertEquals(1, paging.getPage());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-1);
    }
    
    @Test
    public void testSetCountWithPositiveValue() {
        Paging paging = new Paging();
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1L);
    }
    
    @Test
    public void testSetSinceIdWithPositiveValue() {
        Paging paging = new Paging();
        paging.setSinceId(123456789L);
        assertEquals(123456789L, paging.getSinceId());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-1L);
    }
    
    @Test
    public void testSetMaxIdWithPositiveValue() {
        Paging paging = new Paging();
        paging.setMaxId(987654321L);
        assertEquals(987654321L, paging.getMaxId());
    }
    
    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 20, 123456789L, 987654321L);
        Paging paging2 = new Paging(1, 20, 123456789L, 987654321L);
        assertEquals(paging1, paging2);
    }
    
    @Test
    public void testNotEquals() {
        Paging paging1 = new Paging(1, 20, 123456789L, 987654321L);
        Paging paging2 = new Paging(2, 20, 123456789L, 987654321L);
        assertNotEquals(paging1, paging2);
    }
    
    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 20, 123456789L, 987654321L);
        Paging paging2 = new Paging(1, 20, 123456789L, 987654321L);
        assertEquals(paging1.hashCode(), paging2.hashCode());
    }
    
    @Test
    public void testToString() {
        Paging paging = new Paging(1, 20, 123456789L, 987654321L);
        assertEquals("Paging{page=1, count=20, sinceId=123456789, maxId=987654321}", paging.toString());
    }
    
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 20, 123456789L, 987654321L);
        List<HttpParameter> parameterList = paging.asPostParameterList();
        assertEquals(4, parameterList.size());
    }
    
    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 20, 123456789L, 987654321L);
        HttpParameter[] parameterArray = paging.asPostParameterArray();
        assertEquals(4, parameterArray.length);
    }
    
    @Test
    public void testAsPostParameterListWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        List<HttpParameter> parameterList = paging.asPostParameterList(supportedParams);
        assertEquals(0, parameterList.size());
    }
    
    @Test
    public void testAsPostParameterArrayWithSupportedParams() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        HttpParameter[] parameterArray = paging.asPostParameterArray(supportedParams);
        assertEquals(0, parameterArray.length);
    }
    
    @Test
    public void testAsPostParameterListWithSupportedParamsAndPerPageParamName() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        String perPageParamName = "per_page";
        List<HttpParameter> parameterList = paging.asPostParameterList(supportedParams, perPageParamName);
        assertEquals(0, parameterList.size());
    }
    
    @Test
    public void testAsPostParameterArrayWithSupportedParamsAndPerPageParamName() {
        Paging paging = new Paging();
        char[] supportedParams = {'s', 'm', 'c', 'p'};
        String perPageParamName = "per_page";
        HttpParameter[] parameterArray = paging.asPostParameterArray(supportedParams, perPageParamName);
        assertEquals(0, parameterArray.length);
    }

}
