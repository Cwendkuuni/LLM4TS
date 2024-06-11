package com.lts.swing.combobox;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setModel(model);
        
        assertSame(model, comboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue_NullModel_ReturnNull() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        
        assertNull(comboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_NotNullModel_ReturnSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertSame("Value", comboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.setSelectedValue("Value");
        
        assertSame("Value", comboBox.mySimpleModel.getSelectedItem());
    }
    
    @Test
    public void testGetSelectedInt_NullValue_ReturnNegativeOne() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        
        assertEquals(-1, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_IntegerValue_ReturnIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(123);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_LongValue_ReturnConvertedIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(123L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ShortValue_ReturnShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 123);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123, comboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ByteValue_ReturnByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 123);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123, comboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidType_ThrowException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_NullValue_ReturnNegativeOne() {
        SimpleComboBox comboBox = new SimpleComboBox(null);
        
        assertEquals(-1L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_IntegerValue_ReturnConvertedLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(123);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_LongValue_ReturnLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(123L);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ShortValue_ReturnShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((short) 123);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123L, comboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ByteValue_ReturnByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement((byte) 123);
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        assertEquals(123L, comboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidType_ThrowException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("Value");
        SimpleComboBox comboBox = new SimpleComboBox(model);
        
        comboBox.getSelectedLong();
    }

}