package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

  private SimpleComboBox simpleComboBox;

  @Before
  public void setup() {
    SimpleComboBoxModel model = new SimpleComboBoxModel();
    simpleComboBox = new SimpleComboBox(model);
  }

  @Test
  public void testSetModel() {
    SimpleComboBoxModel model = new SimpleComboBoxModel();
    simpleComboBox.setModel(model);

    assertEquals(model, simpleComboBox.mySimpleModel);
  }

  @Test
  public void testGetSelectedValue() {
    SimpleComboBoxModel model = new SimpleComboBoxModel();
    simpleComboBox.setModel(model);
    model.addElement("Item 1");
    model.addElement("Item 2");
    model.addElement("Item 3");

    simpleComboBox.setSelectedItem("Item 2");

    assertEquals("Item 2", simpleComboBox.getSelectedValue());
  }

  @Test
  public void testSetSelectedValue() {
    SimpleComboBoxModel model = new SimpleComboBoxModel();
    simpleComboBox.setModel(model);
    model.addElement("Item 1");
    model.addElement("Item 2");
    model.addElement("Item 3");

    simpleComboBox.setSelectedValue("Item 2");

    assertEquals("Item 2", simpleComboBox.getSelectedValue());
  }

  @Test
  public void testGetSelectedInt() {
    SimpleComboBoxModel model = new SimpleComboBoxModel();
    simpleComboBox.setModel(model);
    model.addElement(10);
    model.addElement(20);
    model.addElement(30);

    simpleComboBox.setSelectedValue(20);

    assertEquals(20, simpleComboBox.getSelectedInt());
  }

  @Test
  public void testGetSelectedLong() {
    SimpleComboBoxModel model = new SimpleComboBoxModel();
    simpleComboBox.setModel(model);
    model.addElement(100L);
    model.addElement(200L);
    model.addElement(300L);

    simpleComboBox.setSelectedValue(200L);

    assertEquals(200L, simpleComboBox.getSelectedLong());
  }

}