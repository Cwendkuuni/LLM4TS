import org.junit.*;
import static org.junit.Assert.*;

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
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        Object value = "Test Value";
        simpleComboBox.setSelectedValue(value);
        assertEquals(value, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        int expected = -1;
        assertEquals(expected, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        long expected = -1L;
        assertEquals(expected, simpleComboBox.getSelectedLong());
    }

}