import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        assertEquals(0, paging.asPostParameterList().size());

        paging.setSinceId(123);
        assertEquals(1, paging.asPostParameterList().size());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        assertEquals(Paging.NULL_PARAMETER_ARRAY, paging.asPostParameterArray());

        paging.setPage(1);
        paging.setCount(10);
        assertEquals(2, paging.asPostParameterArray().length);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        paging.setPage(1);
        List<HttpParameter> pagingParams = new ArrayList<>();
        paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", paging.getSinceId());
        assertEquals(1, pagingParams.size());

        try {
            paging.setPage(-1);
            paging.addPostParameter(Paging.SMCP, 's', pagingParams, "since_id", paging.getSinceId());
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            assertEquals("Paging parameter [since_id] is not supported with this operation.", e.getMessage());
        }
    }

    // Add more test methods for the remaining public methods

}