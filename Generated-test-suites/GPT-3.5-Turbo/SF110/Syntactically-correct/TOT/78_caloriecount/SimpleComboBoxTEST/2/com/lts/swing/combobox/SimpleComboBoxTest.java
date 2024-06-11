import static org.junit.Assert.*;
import org.junit.*;

public class SimpleComboBoxTest {

    private SimpleComboBox testComboBox;

    @Before
    public void setUp() {
        testComboBox = new SimpleComboBox(new SimpleComboBoxModel());
    }

    @Test
    public void testSetModel() {
        // Test with null model
        testComboBox.setModel(null);
        assertNull(testComboBox.getModel());
        
        // Test with SimpleComboBoxModel
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        testComboBox.setModel(model);
        assertEquals(model, testComboBox.getModel());
    }

    @Test
    public void testGetSelectedValue() {
        // Test with null SimpleComboBoxModel
        assertNull(testComboBox.getSelectedValue());
        
        // Test with SimpleComboBoxModel with selected value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue("test");
        testComboBox.setModel(model);
        assertEquals("test", testComboBox.getSelectedValue());
    }

    @Test
    public void testSetSelectedValue() {
        // Test with null value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        testComboBox.setModel(model);
        testComboBox.setSelectedValue(null);
        assertNull(model.getSelectedValue());
        
        // Test with non-null value
        testComboBox.setSelectedValue("test");
        assertEquals("test", model.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt() {
        // Test with null SimpleComboBoxModel
        assertEquals(-1, testComboBox.getSelectedInt());
        
        // Test with SimpleComboBoxModel with Integer value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        testComboBox.setModel(model);
        assertEquals(10, testComboBox.getSelectedInt());
        
        // Test with SimpleComboBoxModel with Long value
        model.setSelectedValue(100000L);
        testComboBox.setModel(model);
        assertEquals(100000, testComboBox.getSelectedInt());
        
        // Test with SimpleComboBoxModel with Short value
        model.setSelectedValue((short) 5);
        testComboBox.setModel(model);
        assertEquals(5, testComboBox.getSelectedInt());
        
        // Test with SimpleComboBoxModel with Byte value
        model.setSelectedValue((byte) 2);
        testComboBox.setModel(model);
        assertEquals(2, testComboBox.getSelectedInt());

        // Test with invalid type
        model.setSelectedValue("test");
        testComboBox.setModel(model);
        try {
            testComboBox.getSelectedInt();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

    @Test
    public void testGetSelectedLong() {
        // Test with null SimpleComboBoxModel
        assertEquals(-1L, testComboBox.getSelectedLong());
        
        // Test with SimpleComboBoxModel with Integer value
        SimpleComboBoxModel model = new SimpleComboBoxModel();
        model.setSelectedValue(10);
        testComboBox.setModel(model);
        assertEquals(10, testComboBox.getSelectedLong());
        
        // Test with SimpleComboBoxModel with Long value
        model.setSelectedValue(100000L);
        testComboBox.setModel(model);
        assertEquals(100000L, testComboBox.getSelectedLong());
        
        // Test with SimpleComboBoxModel with Short value
        model.setSelectedValue((short) 5);
        testComboBox.setModel(model);
        assertEquals(5L, testComboBox.getSelectedLong());
        
        // Test with SimpleComboBoxModel with Byte value
        model.setSelectedValue((byte) 2);
        testComboBox.setModel(model);
        assertEquals(2L, testComboBox.getSelectedLong());

        // Test with invalid type
        model.setSelectedValue("test");
        testComboBox.setModel(model);
        try {
            testComboBox.getSelectedLong();
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception
        }
    }

}