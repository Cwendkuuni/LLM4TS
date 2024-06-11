package osa.ora.server.admin.ui;

import osa.ora.server.admin.*;
import osa.ora.server.beans.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UpdateUserPanelTest {
  
  private AdminApp mockAdminApp;
  private ControlPanel mockControlPanel;
  private UpdateUserPanel updateUserPanel;
  
  @Before
  public void setup() {
    mockAdminApp = new AdminApp();
    mockControlPanel = new ControlPanel();
    updateUserPanel = new UpdateUserPanel(mockAdminApp, mockControlPanel);
  }
  
  @Test
  public void testUpdateModel() {
    updateUserPanel.updateModel();
    
    // Assert that the JComboBox models are set correctly
    assertTrue(updateUserPanel.jComboBox1.getModel() instanceof DefaultComboBoxModel);
    assertTrue(updateUserPanel.jComboBox2.getModel() instanceof DefaultComboBoxModel);
    
    // Assert that the reset() method is called when there are no groups or users
    assertEquals("", updateUserPanel.jTextField1.getText());
    assertEquals("", updateUserPanel.jTextField2.getText());
    assertEquals("", updateUserPanel.jTextField3.getText());
    assertEquals("", updateUserPanel.jTextField4.getText());
    assertFalse(updateUserPanel.jCheckBox1.isSelected());
    assertFalse(updateUserPanel.jCheckBox2.isSelected());
    assertFalse(updateUserPanel.jCheckBox3.isSelected());
    
    // Assert that the fillUser() method is called when there are groups and users
    updateUserPanel.jComboBox1.setSelectedIndex(0);
    updateUserPanel.updateModel();
    
    User user = (User) updateUserPanel.jComboBox2.getSelectedItem();
    assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
    assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
    assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
    assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
    assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
    assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
    assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
  }
  
  @Test
  public void testReset() {
    updateUserPanel.jTextField1.setText("Test");
    updateUserPanel.jTextField2.setText("test@test.com");
    updateUserPanel.jTextField3.setText("1234567890");
    updateUserPanel.jTextField4.setText("Test Job");
    updateUserPanel.jCheckBox1.setSelected(true);
    updateUserPanel.jCheckBox2.setSelected(true);
    updateUserPanel.jCheckBox3.setSelected(true);
    
    updateUserPanel.reset();
    
    assertEquals("", updateUserPanel.jTextField1.getText());
    assertEquals("", updateUserPanel.jTextField2.getText());
    assertEquals("", updateUserPanel.jTextField3.getText());
    assertEquals("", updateUserPanel.jTextField4.getText());
    assertFalse(updateUserPanel.jCheckBox1.isSelected());
    assertFalse(updateUserPanel.jCheckBox2.isSelected());
    assertFalse(updateUserPanel.jCheckBox3.isSelected());
  }
  
  @Test
  public void testFillUser() {
    User user = new User();
    user.setName("Test");
    user.setEmail("test@test.com");
    user.setDirectPhone("1234567890");
    user.setJobTitle("Test Job");
    user.setShowMyStatus(true);
    user.setOnlyStartChat(true);
    user.setCanJoinChatRoom(true);
    
    updateUserPanel.fillUser(user);
    
    assertEquals(user.getName(), updateUserPanel.jTextField1.getText());
    assertEquals(user.getEmail(), updateUserPanel.jTextField2.getText());
    assertEquals(user.getDirectPhone(), updateUserPanel.jTextField3.getText());
    assertEquals(user.getJobTitle(), updateUserPanel.jTextField4.getText());
    assertEquals(user.isShowMyStatus(), updateUserPanel.jCheckBox1.isSelected());
    assertEquals(user.isOnlyStartChat(), updateUserPanel.jCheckBox2.isSelected());
    assertEquals(user.isCanJoinChatRoom(), updateUserPanel.jCheckBox3.isSelected());
  }
  
  // Additional test methods for other methods in the UpdateUserPanel class
  
}