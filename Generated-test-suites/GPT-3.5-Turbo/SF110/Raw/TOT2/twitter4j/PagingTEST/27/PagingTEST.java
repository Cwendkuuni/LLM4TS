import org.junit.Test;
import static org.junit.Assert.*;

public class PagingTest {

    @Test
    public void testAsPostParameterList() {
        Paging paging = new Paging(1, 10);
        assertEquals(10, paging.asPostParameterList().size());
    }

    @Test
    public void testAsPostParameterArray() {
        Paging paging = new Paging(1, 10, 123456789);
        assertEquals(4, paging.asPostParameterArray().length);
    }

    @Test
    public void testAddPostParameter() {
        Paging paging = new Paging();
        assertThrows(IllegalStateException.class, () -> paging.addPostParameter(new char[]{'s'}, 'p', new ArrayList<>(), "page", 1L));
    }

    @Test
    public void testEquals() {
        Paging paging1 = new Paging(1, 10, 123456, 987654);
        Paging paging2 = new Paging(1, 10, 123456, 987654);
        assertEquals(paging1, paging2);
    }

    @Test
    public void testEqualsNotEqual() {
        Paging paging1 = new Paging(1, 10, 123456, 987654);
        Paging paging2 = new Paging(2, 15, 789456, 123456);
        assertNotEquals(paging1, paging2);
    }

    @Test
    public void testHashCode() {
        Paging paging = new Paging(1, 10, 123456, 987654);
        assertEquals(186717717, paging.hashCode());
    }

    @Test
    public void testToString() {
        Paging paging = new Paging(1, 10, 123456, 987654);
        assertEquals("Paging{page=1, count=10, sinceId=123456, maxId=987654}", paging.toString());
    }
}