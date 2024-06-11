import org.junit.Test;
import twitter4j.Paging;
import twitter4j.internal.http.HttpParameter;
import java.util.List;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 12345, 67890);
        List<HttpParameter> parameters = paging.asPostParameterList();
        assertNotNull(parameters);
        assertEquals(4, parameters.size());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 12345, 67890);
        HttpParameter[] parameters = paging.asPostParameterArray();
        assertNotNull(parameters);
        assertEquals(4, parameters.length);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        List<HttpParameter> parameters = paging.asPostParameterList(Paging.SMCP, "count");
        assertEquals(0, parameters.size());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(5);
        assertEquals(5, paging.getPage());
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        paging.setCount(15);
        assertEquals(15, paging.getCount());
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        paging.setSinceId(100);
        assertEquals(100, paging.getSinceId());
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        paging.setMaxId(500);
        assertEquals(500, paging.getMaxId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativePage() {
        Paging paging = new Paging();
        paging.setPage(-5);
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
        paging.setMaxId(-500);
    }
}