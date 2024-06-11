import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox simpleComboBox;

    @Before
    public void setUp() {
        simpleComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        simpleComboBox.setModel(model);
        assertEquals(model, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
        simpleComboBox.setSelectedValue("Test");
        assertEquals("Test", simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        simpleComboBox.setSelectedValue(5);
        assertEquals(5, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedInt());
    }

    @Test
    public void testGetSelectedLong() {
        simpleComboBox.setSelectedValue(100L);
        assertEquals(100L, simpleComboBox.getSelectedLong());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_Exception() {
        simpleComboBox.setSelectedValue("String");
        simpleComboBox.getSelectedInt();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_Exception() {
        simpleComboBox.setSelectedValue("String");
        simpleComboBox.getSelectedLong();
    }
}