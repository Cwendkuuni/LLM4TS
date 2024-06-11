
import org.junit.Test;

public class PagingTest {

    @Test
    public void testDefaultConstructor() {
        Paging paging = new Paging();
        assert paging.getPage() == -1;
        assert paging.getCount() == -1;
        assert paging.getSinceId() == -1L;
        assert paging.getMaxId() == -1L;
    }

    @Test
    public void testPageConstructor() {
        Paging paging = new Paging(1);
        assert paging.getPage() == 1;
        assert paging.getCount() == -1;
        assert paging.getSinceId() == -1L;
        assert paging.getMaxId() == -1L;
    }

    @Test
    public void testSinceIdConstructor() {
        Paging paging = new Paging(1234L);
        assert paging.getPage() == -1;
        assert paging.getCount() == -1;
        assert paging.getSinceId() == 1234L;
        assert paging.getMaxId() == -1L;
    }

    @Test
    public void testPageCountConstructor() {
        Paging paging = new Paging(2, 10);
        assert paging.getPage() == 2;
        assert paging.getCount() == 10;
        assert paging.getSinceId() == -1L;
        assert paging.getMaxId() == -1L;
    }

    @Test
    public void testPageSinceIdConstructor() {
        Paging paging = new Paging(3, 1234L);
        assert paging.getPage() == 3;
        assert paging.getCount() == -1;
        assert paging.getSinceId() == 1234L;
        assert paging.getMaxId() == -1L;
    }

    @Test
    public void testPageCountSinceIdConstructor() {
        Paging paging = new Paging(4, 20, 5678L);
        assert paging.getPage() == 4;
        assert paging.getCount() == 20;
        assert paging.getSinceId() == 5678L;
        assert paging.getMaxId() == -1L;
    }

    @Test
    public void testPageCountSinceIdMaxIdConstructor() {
        Paging paging = new Paging(5, 30, 5678L, 9999L);
        assert paging.getPage() == 5;
        assert paging.getCount() == 30;
        assert paging.getSinceId() == 5678L;
        assert paging.getMaxId() == 9999L;
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPageWithNegativeValue() {
        Paging paging = new Paging();
        paging.setPage(-2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetCountWithNegativeValue() {
        Paging paging = new Paging();
        paging.setCount(-10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetSinceIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setSinceId(-1234L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxIdWithNegativeValue() {
        Paging paging = new Paging();
        paging.setMaxId(-9999L);
    }
}
