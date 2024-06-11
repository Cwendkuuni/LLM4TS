import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import javax.swing.*;

public class MainMenuTest {

    private MainMenu mainMenu;

    @Before
    public void setUp() {
        mainMenu = new MainMenu(new InspirentoMediator());
    }

    // createMenu() method tests

    @Test
    public void testCreateMenu_withValidMenuLabel_shouldCreateJMenu() {
        String menuLabel = "File";
        JMenu result = mainMenu.createMenu(menuLabel);
        assertNotNull(result);
        assertEquals(menuLabel, result.getText());
    }

    @Test
    public void testCreateMenu_withInvalidMenuLabel_shouldReturnNull() {
        String menuLabel = "InvalidMenu";
        JMenu result = mainMenu.createMenu(menuLabel);
        assertNull(result);
    }

    @Test
    public void testCreateMenu_withMenuLabelStartingWithMultipleStar_shouldCreateJMenuWithCorrectLabelAndMnemonic() {
        String menuLabel = "EditLabel**";
        char expectedMnemonic = '*';
        String expectedLabelText = "EditLabel*";
        JMenu result = mainMenu.createMenu(menuLabel);
        assertNotNull(result);
        assertEquals(expectedLabelText, result.getText());
        assertEquals(expectedMnemonic, result.getMnemonic());
    }

    @Test
    public void testCreateMenu_withMenuLabelNotStartingWithMultipleStar_shouldCreateJMenuWithCorrectLabelAndNoMnemonic() {
        String menuLabel = "EditLabel";
        JMenu result = mainMenu.createMenu(menuLabel);
        assertNotNull(result);
        assertEquals(menuLabel, result.getText());
        assertEquals('\u0000', result.getMnemonic());
    }

    // addShortcutAndIcon() method tests

    @Test
    public void testAddShortcutAndIcon_withValidKey_shouldSetAcceleratorAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "File";
        mainMenu.addShortcutAndIcon(item, key);
        assertNotNull(item.getAccelerator());
        assertNotNull(item.getIcon());
    }

    @Test
    public void testAddShortcutAndIcon_withInvalidKey_shouldNotSetAcceleratorAndIcon() {
        JMenuItem item = new JMenuItem();
        String key = "InvalidKey";
        mainMenu.addShortcutAndIcon(item, key);
        assertNull(item.getAccelerator());
        assertNull(item.getIcon());
    }

    // Integration test

    @Test
    public void testMainMenuCreation() {
        assertNotNull(mainMenu);
    }

    // Additional tests
    // ...

}