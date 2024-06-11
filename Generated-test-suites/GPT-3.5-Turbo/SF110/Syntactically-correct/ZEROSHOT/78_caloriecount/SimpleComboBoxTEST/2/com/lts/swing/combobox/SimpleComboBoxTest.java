import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.getModel());
        assertNull(simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
        Object value = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = new Object();
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        assertEquals(-1, simpleComboBox.getSelectedInt());
        Integer value = 123;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);
        assertEquals(value.intValue(), simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        assertEquals(-1L, simpleComboBox.getSelectedLong());
        Long value = 456L;
        simpleComboBox.mySimpleModel = new SimpleComboBoxModel();
        simpleComboBox.mySimpleModel.setSelectedValue(value);
        assertEquals(value.longValue(), simpleComboBox.getSelectedLong());
    }
}