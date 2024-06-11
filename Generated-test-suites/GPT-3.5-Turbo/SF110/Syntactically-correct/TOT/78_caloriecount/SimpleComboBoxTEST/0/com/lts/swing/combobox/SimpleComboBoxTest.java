import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        assertEquals(model, simpleComboBox.getModel());
    }

    @Test
    public void testGetSelectedValueNullModel() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedValueNonNullModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Test");
        simpleComboBox.setModel(model);
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedIntNullModel() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100);
        simpleComboBox.setModel(model);
        assertEquals(100, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        simpleComboBox.setModel(model);
        assertEquals(100, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 100);
        simpleComboBox.setModel(model);
        assertEquals(100, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedIntByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 100);
        simpleComboBox.setModel(model);
        assertEquals(100, simpleComboBox.getSelectedInt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedIntInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Invalid");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLongNullModel() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongIntegerValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100);
        simpleComboBox.setModel(model);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongLongValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(100L);
        simpleComboBox.setModel(model);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongShortValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((short) 100);
        simpleComboBox.setModel(model);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test
    public void testGetSelectedLongByteValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue((byte) 100);
        simpleComboBox.setModel(model);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLongInvalidValue() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("Invalid");
        simpleComboBox.setModel(model);
        simpleComboBox.getSelectedLong();
    }
}