
import org.junit.Test;
import twitter4j.HttpParameter;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10, 12345, 67890);
        List<HttpParameter> result = paging.asPostParameterList();
        assertEquals(4, result.size());
        assertNotNull(result.get(0).getName());
        assertNotNull(result.get(0).getValue());
        // Add more assertions based on expected results
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging(1, 10, 12345, 67890);
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 'c', pagingParams, "count", 10);
        assertEquals(1, pagingParams.size());
        assertNotNull(pagingParams.get(0).getName());
        assertNotNull(pagingParams.get(0).getValue());
        // Add more assertions based on expected results
    }

    @Test
    public void testHashCode() {
        Paging paging1 = new Paging(1, 10, 12345, 67890);
        Paging paging2 = new Paging(1, 10, 12345, 67890);
        assertEquals(paging1.hashCode(), paging2.hashCode());
        // Add more assertions based on expected results
    }

}
