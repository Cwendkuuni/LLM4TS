package com.lts.swing.combobox;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import javax.swing.ComboBoxModel;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {
    
    private SimpleComboBoxModel mockModel;
    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        mockModel = mock(SimpleComboBoxModel.class);
        simpleComboBox = new SimpleComboBox(mockModel);
    }
    
    @Test
    public void testSetModel() {
        ComboBoxModel model = mock(ComboBoxModel.class);
        simpleComboBox.setModel(model);
        
        verify(mockModel, never()).setSelectedValue(any());
        verify(mockModel, never()).getSelectedValue();
        verify(mockModel, never()).setSelectedValue(any());
        verify(mockModel, never()).setSelectedValue(anyInt());
        verify(mockModel, never()).setSelectedValue(anyLong());
        verify(mockModel, never()).setSelectedValue(anyShort());
        verify(mockModel, never()).setSelectedValue(anyByte());
        verifyNoMoreInteractions(mockModel);
        
        assertEquals(model, simpleComboBox.getModel());
    }
    
    @Test
    public void testGetSelectedValueWithNullModel() {
        when(mockModel.getSelectedValue()).thenReturn(null);
        
        assertNull(simpleComboBox.getSelectedValue());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedValue() {
        Object value = new Object();
        when(mockModel.getSelectedValue()).thenReturn(value);
        
        assertEquals(value, simpleComboBox.getSelectedValue());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        
        verify(mockModel).setSelectedValue(value);
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedIntWithNullModel() {
        when(mockModel.getSelectedValue()).thenReturn(null);
        
        assertEquals(-1, simpleComboBox.getSelectedInt());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedIntWithIntegerValue() {
        int intValue = 10;
        when(mockModel.getSelectedValue()).thenReturn(intValue);
        
        assertEquals(intValue, simpleComboBox.getSelectedInt());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedIntWithLongValue() {
        long longValue = 10L;
        when(mockModel.getSelectedValue()).thenReturn(longValue);
        
        assertEquals((int) longValue, simpleComboBox.getSelectedInt());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedIntWithShortValue() {
        short shortValue = 10;
        when(mockModel.getSelectedValue()).thenReturn(shortValue);
        
        assertEquals(shortValue, simpleComboBox.getSelectedInt());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedIntWithByteValue() {
        byte byteValue = 10;
        when(mockModel.getSelectedValue()).thenReturn(byteValue);
        
        assertEquals(byteValue, simpleComboBox.getSelectedInt());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidValue() {
        when(mockModel.getSelectedValue()).thenReturn(new Object());
        
        simpleComboBox.getSelectedInt();
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedLongWithNullModel() {
        when(mockModel.getSelectedValue()).thenReturn(null);
        
        assertEquals(-1L, simpleComboBox.getSelectedLong());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedLongWithIntegerValue() {
        int intValue = 10;
        when(mockModel.getSelectedValue()).thenReturn(intValue);
        
        assertEquals(intValue, simpleComboBox.getSelectedLong());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedLongWithLongValue() {
        long longValue = 10L;
        when(mockModel.getSelectedValue()).thenReturn(longValue);
        
        assertEquals(longValue, simpleComboBox.getSelectedLong());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedLongWithShortValue() {
        short shortValue = 10;
        when(mockModel.getSelectedValue()).thenReturn(shortValue);
        
        assertEquals((long) shortValue, simpleComboBox.getSelectedLong());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test
    public void testGetSelectedLongWithByteValue() {
        byte byteValue = 10;
        when(mockModel.getSelectedValue()).thenReturn(byteValue);
        
        assertEquals((long) byteValue, simpleComboBox.getSelectedLong());
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidValue() {
        when(mockModel.getSelectedValue()).thenReturn(new Object());
        
        simpleComboBox.getSelectedLong();
        
        verify(mockModel).getSelectedValue();
        verifyNoMoreInteractions(mockModel);
    }
}