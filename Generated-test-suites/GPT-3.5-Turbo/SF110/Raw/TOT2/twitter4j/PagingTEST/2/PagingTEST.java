import static org.junit.Assert.*;
import org.junit.Test;

public class PagingTest {
    
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 123, 456);
        List<HttpParameter> parameters = paging.asPostParameterList();
        assertNotNull(parameters);
        assertEquals(4, parameters.size());
    }
    
    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 123, 456);
        HttpParameter[] parameters = paging.asPostParameterArray();
        assertNotNull(parameters);
        assertEquals(4, parameters.length);
    }
    
    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = new ArrayList<HttpParameter>();
        paging.addPostParameter(new char[] {'s', 'm', 'c', 'p'}, 's', parameters, "test", 123);
        assertEquals(1, parameters.size());
    }
    
    @Test
    public void testPageSetter() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }
    
    @Test
    public void testCountSetter() {
        Paging paging = new Paging();
        paging.setCount(20);
        assertEquals(20, paging.getCount());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativePage() {
        Paging paging = new Paging();
        paging.setPage(-1);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeCount() {
        Paging paging = new Paging();
        paging.setCount(-10);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(-100);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(-200);
    }
}