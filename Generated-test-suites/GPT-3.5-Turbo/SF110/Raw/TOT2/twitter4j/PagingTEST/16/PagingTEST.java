import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging();
        assertNotNull(paging.asPostParameterList());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging();
        assertNotNull(paging.asPostParameterArray());
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        assertThrows(IllegalStateException.class, () -> paging.addPostParameter(Paging.S, 's', new ArrayList<>(), "since_id", -1L));
    }

    @Test
    public void testSetPage() {
        Paging paging = new Paging();
        assertThrows(IllegalArgumentException.class, () -> paging.setPage(-5));
    }

    @Test
    public void testSetCount() {
        Paging paging = new Paging();
        assertThrows(IllegalArgumentException.class, () -> paging.setCount(0));
    }

    @Test
    public void testSetSinceId() {
        Paging paging = new Paging();
        assertThrows(IllegalArgumentException.class, () -> paging.setSinceId(0L));
    }

    @Test
    public void testSetMaxId() {
        Paging paging = new Paging();
        assertThrows(IllegalArgumentException.class, () -> paging.setMaxId(0L));
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10);
        Paging paging2 = new Paging(1, 10);
        assertTrue(paging1.equals(paging2));
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10);
        assertEquals(185105807, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 100, 200);
        assertEquals("Paging{page=1, count=10, sinceId=100, maxId=200}", paging.toString());
    }

}