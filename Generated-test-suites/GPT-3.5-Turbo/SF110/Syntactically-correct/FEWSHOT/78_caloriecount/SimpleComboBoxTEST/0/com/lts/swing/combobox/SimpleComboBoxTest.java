import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import javax.swing.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel simpleModel;

    @Before
    public void setUp() {
        simpleModel = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(simpleModel);
    }

    @Test
    public void testSetModel() {
        ComboBoxModel model = new DefaultComboBoxModel();
        simpleComboBox.setModel(model);

        assertEquals(model, simpleComboBox.getModel());
        assertNull(simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());

        Object value = new Object();
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleModel.getSelectedValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntWithInvalidType() {
        Object value = new Object();
        simpleModel.setSelectedValue(value);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedIntWithNullValue() {
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithIntegerValue() {
        Integer value = 10;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithLongValue() {
        Long value = 10L;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithShortValue() {
        Short value = 10;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntWithByteValue() {
        Byte value = 10;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongWithInvalidType() {
        Object value = new Object();
        simpleModel.setSelectedValue(value);
        simpleComboBox.getSelectedLong();
    }

    @Test
    public void testGetSelectedLongWithNullValue() {
        assertNull(simpleComboBox.getSelectedValue());
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithIntegerValue() {
        Integer value = 10;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithLongValue() {
        Long value = 10L;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithShortValue() {
        Short value = 10;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongWithByteValue() {
        Byte value = 10;
        simpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
}