package com.allenstudio.ir.ui;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MainMenuTest {
  
  private MainMenu mainMenu;
  
  @Before
  public void setUp() {
    mainMenu = new MainMenu(null);
  }
  
  @Test
  public void testCreateMenu() {
    String menu = "menu";
    assertNotNull(mainMenu.createMenu(menu));
  }
  
  @Test
  public void testAddShortcutAndIcon() {
    JMenuItem item = new JMenuItem();
    String key = "key";
    mainMenu.addShortcutAndIcon(item, key);
    assertNotNull(item.getAccelerator());
  }
  
  @Test
  public void testChanged() {
    mainMenu.changed();
    // No assertion as this method has no return value
  }
  
  // Additional test cases for edge cases and exceptions
  
  @Test(expected = MissingResourceException.class)
  public void testCreateMenuWithMissingResourceException() {
    String menu = "missingMenu";
    mainMenu.createMenu(menu);
  }
  
  @Test
  public void testAddShortcutAndIconWithMissingResourceException() {
    JMenuItem item = new JMenuItem();
    String key = "missingKey";
    mainMenu.addShortcutAndIcon(item, key);
    assertNotNull(item.getAccelerator()); // Even if resource is missing, method should not throw exception
  }
  
  @Test
  public void testCreateMenuWithSubMenu() {
    String menu = "subMenu";
    assertNotNull(mainMenu.createMenu(menu));
  }
  
  @Test
  public void testCreateMenuWithSeparator() {
    String menu = "separatorMenu";
    assertNotNull(mainMenu.createMenu(menu));
  }
}