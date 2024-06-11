package twitter4j;
import org.junit.Test;
import static org.junit.Assert.*;
import twitter4j.Paging;

public class PagingTest {

    @Test
    public void testGetPage() {
        Paging paging = new Paging(1, 10);
        assertEquals(1, paging.getPage());
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        paging.setPage(2);
        assertEquals(2, paging.getPage());
    }

    // Additional test cases for other methods
}