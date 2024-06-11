
package com.lts.swing.combobox;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
  
  private SimpleComboBoxModel model;
  private SimpleComboBox comboBox;
  
  @Before
  public void setUp() {
    model = new SimpleComboBoxModel();
    comboBox = new SimpleComboBox(model);
  }
  
  @Test
  public void testSetModel() {
    DefaultComboBoxModel<String> defaultModel = new DefaultComboBoxModel<>();
    comboBox.setModel(defaultModel);
    assertEquals(defaultModel, comboBox.getModel());
    
    SimpleComboBoxModel simpleModel = new SimpleComboBoxModel();
    comboBox.setModel(simpleModel);
    assertEquals(simpleModel, comboBox.getModel());
  }
  
  @Test
  public void testGetSelectedValue() {
    assertNull(comboBox.getSelectedValue());
    
    String value = "Test";
    model.addElement(value);
    comboBox.setSelectedItem(value);
    assertEquals(value, comboBox.getSelectedValue());
  }
  
  @Test
  public void testSetSelectedValue() {
    String value = "Test";
    comboBox.setSelectedValue(value);
    assertEquals(value, comboBox.getSelectedValue());
  }
  
  @Test
  public void testGetSelectedInt() {
    assertEquals(-1, comboBox.getSelectedInt());
    
    int value = 10;
    model.addElement(value);
    comboBox.setSelectedItem(value);
    assertEquals(value, comboBox.getSelectedInt());
  }
  
  @Test
  public void testGetSelectedLong() {
    assertEquals(-1L, comboBox.getSelectedLong());
    
    long value = 10L;
    model.addElement(value);
    comboBox.setSelectedItem(value);
    assertEquals(value, comboBox.getSelectedLong());
  }
}
