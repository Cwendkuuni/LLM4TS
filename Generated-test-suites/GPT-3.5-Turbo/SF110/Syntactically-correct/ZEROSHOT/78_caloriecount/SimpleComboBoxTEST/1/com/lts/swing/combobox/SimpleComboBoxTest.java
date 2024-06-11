package com.lts.swing.combobox;

import org.junit.*;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;
    
    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }
    
    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        Assert.assertEquals(newModel, simpleComboBox.mySimpleModel);
        Assert.assertEquals(newModel, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValue() {
        Object selectedValue = simpleComboBox.getSelectedValue();
        Assert.assertNull(selectedValue);
        
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        selectedValue = simpleComboBox.getSelectedValue();
        Assert.assertNull(selectedValue);
        
        Object value = new Object();
        simpleComboBox.mySimpleModel.setSelectedValue(value);
        selectedValue = simpleComboBox.getSelectedValue();
        Assert.assertEquals(value, selectedValue);
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        Assert.assertEquals(value, simpleComboBox.mySimpleModel.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt() {
        simpleComboBox.selectedValue = null;
        int selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(-1, selectedInt);
        
        simpleComboBox.selectedValue = 10;
        selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(10, selectedInt);
        
        simpleComboBox.selectedValue = (long) 20;
        selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(20, selectedInt);
        
        simpleComboBox.selectedValue = (short) 30;
        selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(30, selectedInt);
        
        simpleComboBox.selectedValue = (byte) 40;
        selectedInt = simpleComboBox.getSelectedInt();
        Assert.assertEquals(40, selectedInt);
        
        Object value = new Object();
        simpleComboBox.selectedValue = value;
        try {
            simpleComboBox.getSelectedInt();
            Assert.fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }
    
    @Test
    public void testGetSelectedLong() {
        simpleComboBox.selectedValue = null;
        long selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(-1L, selectedLong);
        
        simpleComboBox.selectedValue = 10;
        selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(10L, selectedLong);
        
        simpleComboBox.selectedValue = (long) 20;
        selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(20L, selectedLong);
        
        simpleComboBox.selectedValue = (short) 30;
        selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(30L, selectedLong);
        
        simpleComboBox.selectedValue = (byte) 40;
        selectedLong = simpleComboBox.getSelectedLong();
        Assert.assertEquals(40L, selectedLong);
        
        Object value = new Object();
        simpleComboBox.selectedValue = value;
        try {
            simpleComboBox.getSelectedLong();
            Assert.fail("Expected IllegalArgumentException to be thrown.");
        } catch (IllegalArgumentException e) {
            Assert.assertTrue(true);
        }
    }
    
    @After
    public void tearDown() {
        simpleComboBox = null;
        simpleComboBoxModel = null;
    }
}