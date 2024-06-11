import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {

    private SimpleComboBox testComboBox;

    @Before
    public void setUp() {
        testComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        // Typical case: model is an instance of SimpleComboBoxModel
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        testComboBox.setModel(model);
        assertSame(model, testComboBox.mySimpleModel);

        // Edge case: model is null
        model = null;
        testComboBox.setModel(model);
        assertNull(testComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue() {
        // Typical case: mySimpleModel is not null
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("value");
        testComboBox.setModel(model);
        assertEquals("value", testComboBox.getSelectedValue());

        // Typical case: mySimpleModel is null
        testComboBox.setModel(null);
        assertNull(testComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        // Typical case: value is in the model
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement("value");
        testComboBox.setModel(model);
        testComboBox.setSelectedValue("value");
        assertEquals("value", testComboBox.mySimpleModel.getSelectedValue());

        // Error scenario: value is not in the model
        testComboBox.setSelectedValue("invalidValue");
        assertNull(testComboBox.mySimpleModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        // Typical case: value is Integer
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(10);
        testComboBox.setModel(model);
        assertEquals(10, testComboBox.getSelectedInt());

        // Typical case: value is Short
        model = new SimpleComboBoxModel();
        model.addElement((short) 5);
        testComboBox.setModel(model);
        assertEquals(5, testComboBox.getSelectedInt());

        // Error scenario: value is not Integer or Short
        model = new SimpleComboBoxModel();
        model.addElement("value");
        testComboBox.setModel(model);
        try {
            testComboBox.getSelectedInt();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    @Test
    public void testGetSelectedLong() {
        // Typical case: value is Long
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.addElement(100L);
        testComboBox.setModel(model);
        assertEquals(100L, testComboBox.getSelectedLong());

        // Typical case: value is Byte
        model = new SimpleComboBoxModel();
        model.addElement((byte) 2);
        testComboBox.setModel(model);
        assertEquals(2L, testComboBox.getSelectedLong());

        // Error scenario: value is not Long or Byte
        model = new SimpleComboBoxModel();
        model.addElement("value");
        testComboBox.setModel(model);
        try {
            testComboBox.getSelectedLong();
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }
}