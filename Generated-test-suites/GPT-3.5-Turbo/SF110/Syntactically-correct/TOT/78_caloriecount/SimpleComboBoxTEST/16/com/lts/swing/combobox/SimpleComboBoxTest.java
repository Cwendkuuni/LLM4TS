import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.lts.swing.combobox.SimpleComboBox;
import com.lts.swing.combobox.SimpleComboBoxModel;

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
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        comboBox.setModel(newModel);

        assertSame(newModel, comboBox.mySimpleModel);
        assertSame(newModel, comboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NullModel() {
        comboBox.mySimpleModel = null;

        assertNull(comboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue() {
        Object selectedValue = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertSame(selectedValue, comboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object selectedValue = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel();

        comboBox.setSelectedValue(selectedValue);

        assertSame(selectedValue, comboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_Integer() {
        Integer selectedValue = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_Long() {
        Long selectedValue = 10L;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_Short() {
        Short selectedValue = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.intValue(), comboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_Byte() {
        Byte selectedValue = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.intValue(), comboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_IllegalArgumentException() {
        Object selectedValue = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        comboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_Integer() {
        Integer selectedValue = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_Long() {
        Long selectedValue = 10L;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_Short() {
        Short selectedValue = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.longValue(), comboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_Byte() {
        Byte selectedValue = 10;
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        assertEquals(selectedValue.longValue(), comboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_IllegalArgumentException() {
        Object selectedValue = new Object();
        comboBox.mySimpleModel = new SimpleComboBoxModel();
        comboBox.mySimpleModel.setSelectedValue(selectedValue);

        comboBox.getSelectedLong();
    }

}