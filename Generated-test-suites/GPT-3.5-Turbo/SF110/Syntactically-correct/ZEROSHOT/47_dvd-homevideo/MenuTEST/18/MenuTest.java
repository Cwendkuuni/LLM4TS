import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class MenuTest {
    private Menu menu;

    @Before
    public void setUp() {
        menu = new Menu();
    }

    @Test
    public void testInit() {
        assertTrue(menu.init());
    }

    @Test
    public void testCreateXML() {
        assertFalse(menu.createXML());
    }
}