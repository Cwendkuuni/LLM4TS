import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SimpleComboBoxTest {

    SimpleComboBoxModel mockModel = new SimpleComboBoxModel();
    SimpleComboBox simpleComboBox = new SimpleComboBox(mockModel);

    @Test
    public void testSetModel() {
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        simpleComboBox.setModel(newModel);
        assertEquals(newModel, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        assertNull(simpleComboBox.getSelectedValue());
        
        mockModel.addElement(5);
        simpleComboBox.setSelectedValue(5);
        assertEquals(5, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        mockModel.addElement(10);
        simpleComboBox.setSelectedValue(10);
        assertEquals(10, simpleComboBox.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        mockModel.addElement(15);
        simpleComboBox.setSelectedValue(15);
        assertEquals(15, simpleComboBox.getSelectedInt());

        mockModel.addElement(20L);
        simpleComboBox.setSelectedValue(20L);
        assertEquals(20, simpleComboBox.getSelectedInt());

        mockModel.addElement((short)25);
        simpleComboBox.setSelectedValue((short)25);
        assertEquals(25, simpleComboBox.getSelectedInt());

        mockModel.addElement((byte)30);
        simpleComboBox.setSelectedValue((byte)30);
        assertEquals(30, simpleComboBox.getSelectedInt());

        assertThrows(IllegalArgumentException.class, () -> {
            simpleComboBox.setSelectedValue("invalid");
            simpleComboBox.getSelectedInt();
        });
    }

    @Test
    public void testGetSelectedLong() {
        mockModel.addElement(35);
        simpleComboBox.setSelectedValue(35);
        assertEquals(35, simpleComboBox.getSelectedLong());

        mockModel.addElement(40L);
        simpleComboBox.setSelectedValue(40L);
        assertEquals(40L, simpleComboBox.getSelectedLong());

        mockModel.addElement((short)45);
        simpleComboBox.setSelectedValue((short)45);
        assertEquals(45, simpleComboBox.getSelectedLong());

        mockModel.addElement((byte)50);
        simpleComboBox.setSelectedValue((byte)50);
        assertEquals(50, simpleComboBox.getSelectedLong());

        assertThrows(IllegalArgumentException.class, () -> {
            simpleComboBox.setSelectedValue("invalid");
            simpleComboBox.getSelectedLong();
        });
    }
}