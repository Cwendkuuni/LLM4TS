import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {
    
    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 100, 1000);
        List<HttpParameter> result = paging.asPostParameterList();
        assertNotNull(result);
        assertEquals(4, result.size());
    }
}