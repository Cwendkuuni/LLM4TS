
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import javax.swing.*;

public class SimpleComboBoxTest {
    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        ComboBoxModel model = new DefaultComboBoxModel();
        simpleComboBox.setModel(model);

        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue_NoSimpleModel_ReturnsNull() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValue_WithSimpleModel_ReturnsSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        simpleComboBox.setModel(model);

        assertEquals("test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);

        simpleComboBox.setSelectedValue("test");

        assertEquals("test", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_NoSelectedValue_ReturnsMinusOne() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_WithInteger_ReturnsIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        simpleComboBox.setModel(model);

        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_WithLong_ReturnsLongValueAsInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        simpleComboBox.setModel(model);

        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_WithShort_ReturnsShortValueAsInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        simpleComboBox.setModel(model);

        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedInt_WithByte_ReturnsByteValueAsInt() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        simpleComboBox.setModel(model);

        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_WithInvalidType_ThrowsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        simpleComboBox.setModel(model);

        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_NoSelectedValue_ReturnsMinusOne() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_WithInteger_ReturnsIntegerValueAsLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        simpleComboBox.setModel(model);

        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_WithLong_ReturnsLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10L);
        simpleComboBox.setModel(model);

        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_WithShort_ReturnsShortValueAsLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 10);
        simpleComboBox.setModel(model);

        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLong_WithByte_ReturnsByteValueAsLong() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 10);
        simpleComboBox.setModel(model);

        assertEquals(10L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_WithInvalidType_ThrowsIllegalArgumentException() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        simpleComboBox.setModel(model);

        simpleComboBox.getSelectedLong();
    }
}
