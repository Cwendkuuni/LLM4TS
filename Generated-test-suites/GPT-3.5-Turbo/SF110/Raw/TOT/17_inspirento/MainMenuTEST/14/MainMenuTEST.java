import org.junit.*;
import static org.junit.Assert.*;
import java.awt.Component;
import java.awt.MenuShortcut;
import javax.swing.*;

public class MainMenuTest {
  
  @Test
  public void testCreateMenu_ValidMenu_ReturnsJMenuWithLabel() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("File");
    
    assertEquals("File", jMenu.getText());
  }
  
  @Test
  public void testCreateMenu_ValidMenuWithShortcut_ReturnsJMenuWithShortcut() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("File");
    
    MenuShortcut expectedShortcut = new MenuShortcut("shortcut");
    assertEquals(expectedShortcut, jMenu.getAccelerator());
  }
  
  @Test
  public void testCreateMenu_ValidMenuWithIcon_ReturnsJMenuWithIcon() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("File");
    
    ImageIcon expectedIcon = new ImageIcon(getClass().getResource("file.png"));
    assertEquals(expectedIcon, jMenu.getIcon());
  }
  
  @Test
  public void testCreateMenu_ValidSubMenu_ReturnsJMenuWithSubMenu() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("Edit");
    
    JMenu subMenu = (JMenu) jMenu.getMenu(0);
    assertEquals("Copy", subMenu.getText());
  }
  
  @Test
  public void testCreateMenu_ValidSubMenuWithShortCut_ReturnsSubMenuWithShortcut() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("Edit");
    
    JMenu subMenu = (JMenu) jMenu.getMenu(0);
    MenuShortcut expectedShortcut = new MenuShortcut("ctrl+c");
    assertEquals(expectedShortcut, subMenu.getAccelerator());
  }
  
  @Test
  public void testCreateMenu_ValidSubMenuWithIcon_ReturnsSubMenuWithIcon() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("Edit");
    
    JMenu subMenu = (JMenu) jMenu.getMenu(0);
    ImageIcon expectedIcon = new ImageIcon(getClass().getResource("copy.png"));
    assertEquals(expectedIcon, subMenu.getIcon());
  }
  
  @Test
  public void testCreateMenu_MenuLabelEndsWithAsterisk_ReturnsJMenuWithLabelWithoutAsterisk() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("Help");
    
    assertEquals("About", jMenu.getText());
  }
  
  @Test
  public void testCreateMenu_MenuLabelEndsWithAsteriskAndMnemonic_ReturnsJMenuWithLabelWithoutAsteriskAndMnemonic() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenu jMenu = mainMenu.createMenu("Help");
    
    assertEquals("About", jMenu.getText());
    assertEquals('A', jMenu.getMnemonic());
  }
  
  @Test
  public void testAddShortcutAndIcon_ValidShortcut_AddsShortcutToMenuItem() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenuItem menuItem = new JMenuItem();
    
    mainMenu.addShortcutAndIcon(menuItem, "File");
    
    MenuShortcut expectedShortcut = new MenuShortcut("ctrl+n");
    assertEquals(expectedShortcut, menuItem.getAccelerator());
  }
  
  @Test
  public void testAddShortcutAndIcon_ValidIconPath_AddsIconToMenuItem() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenuItem menuItem = new JMenuItem();
    
    mainMenu.addShortcutAndIcon(menuItem, "File");
    
    ImageIcon expectedIcon = new ImageIcon(getClass().getResource("file.png"));
    assertEquals(expectedIcon, menuItem.getIcon());
  }
  
  @Test
  public void testAddShortcutAndIcon_InvalidShortcut_DoesNotAddShortcutToMenuItem() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenuItem menuItem = new JMenuItem();
    
    mainMenu.addShortcutAndIcon(menuItem, "InvalidShortcut");
    
    assertNull(menuItem.getAccelerator());
  }
  
  @Test
  public void testAddShortcutAndIcon_InvalidIconPath_DoesNotAddIconToMenuItem() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    JMenuItem menuItem = new JMenuItem();
    
    mainMenu.addShortcutAndIcon(menuItem, "InvalidIcon");
    
    assertNull(menuItem.getIcon());
  }
  
  @Test
  public void testChanged_NoActionRequired_NoExceptionThrown() {
    MainMenu mainMenu = new MainMenu(new InspirentoMediator());
    
    mainMenu.changed();
    // No assertion, just checking for exception
  }
}