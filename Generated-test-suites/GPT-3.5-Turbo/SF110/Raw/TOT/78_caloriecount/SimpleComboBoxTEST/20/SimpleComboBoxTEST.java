
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleComboBoxModel;
    
    @Before
    public void setUp() {
        simpleComboBoxModel = new SimpleComboBoxModel();
        simpleComboBoxModel.addElement("Option 1");
        simpleComboBoxModel.addElement("Option 2");
        simpleComboBoxModel.addElement("Option 3");
        simpleComboBox = new SimpleComboBox(simpleComboBoxModel);
    }
    
    @Test
    public void testSetModel_ValidModel_SetNewModel() {
        ComboBoxModel newModel = new DefaultComboBoxModel();
        simpleComboBox.setModel(newModel);
        Assert.assertEquals(newModel, simpleComboBox.getModel());
    }
    
    @Test
    public void testSetModel_InstanceOfSimpleComboBoxModel_AssignToMySimpleModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        Assert.assertEquals(newModel, simpleComboBox.getModel());
        Assert.assertEquals(newModel, simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue_SimpleComboBoxModel_ReturnSelectedValue() {
        simpleComboBox.setSelectedIndex(1);
        Assert.assertEquals("Option 2", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedValue_NullSimpleComboBoxModel_ReturnNull() {
        simpleComboBox.mySimpleModel = null;
        Assert.assertNull(simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testSetSelectedValue_ValidValue_SetSelectedValue() {
        simpleComboBox.setSelectedIndex(0);
        simpleComboBox.setSelectedValue("Option 3");
        Assert.assertEquals("Option 3", simpleComboBox.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_IntegerValue_ReturnIntValue() {
        simpleComboBox.setSelectedIndex(1);
        Assert.assertEquals(1, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_LongValueCastToInt_ReturnIntValue() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue(10000000000L);
        Assert.assertEquals(10000000000, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ShortValue_ReturnShortValueAsInt() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue((short) 100);
        Assert.assertEquals(100, simpleComboBox.getSelectedInt());
    }
    
    @Test
    public void testGetSelectedInt_ByteValue_ReturnByteValueAsInt() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue((byte) 10);
        Assert.assertEquals(10, simpleComboBox.getSelectedInt());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidClass_ThrowIllegalArgumentException() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue(2.5);
        simpleComboBox.getSelectedInt();
    }
    
    @Test
    public void testGetSelectedLong_IntegerValue_ReturnLongValue() {
        simpleComboBox.setSelectedIndex(1);
        Assert.assertEquals(1L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_LongValue_ReturnLongValue() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue(10000000000L);
        Assert.assertEquals(10000000000L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ShortValue_ReturnShortValueAsLong() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue((short) 100);
        Assert.assertEquals(100L, simpleComboBox.getSelectedLong());
    }
    
    @Test
    public void testGetSelectedLong_ByteValue_ReturnByteValueAsLong() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue((byte) 10);
        Assert.assertEquals(10L, simpleComboBox.getSelectedLong());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidClass_ThrowIllegalArgumentException() {
        simpleComboBox.setSelectedIndex(1);
        simpleComboBox.mySimpleModel.setSelectedValue(2.5);
        simpleComboBox.getSelectedLong();
    }
}

