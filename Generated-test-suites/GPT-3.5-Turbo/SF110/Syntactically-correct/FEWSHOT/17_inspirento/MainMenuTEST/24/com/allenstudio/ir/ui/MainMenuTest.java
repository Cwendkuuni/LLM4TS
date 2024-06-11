
import com.allenstudio.ir.core.*;
import com.allenstudio.ir.util.*;
import java.util.*;
import javax.swing.*;
import java.net.*;
import org.junit.Before;
import org.junit.Test;

public class MainMenuTest {
    private MainMenu mainMenu;

    @Before
    public void setUp() {
        InspirentoMediator mediator = new InspirentoMediator();
        mainMenu = new MainMenu(mediator);
    }

    @Test
    public void testCreateMenu() {
    }

    @Test
    public void testAddShortcutAndIcon() {
    }

    @Test
    public void testChanged() {
    }
}
