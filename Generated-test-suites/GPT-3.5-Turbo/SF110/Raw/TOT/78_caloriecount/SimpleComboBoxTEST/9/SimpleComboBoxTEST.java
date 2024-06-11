import org.junit.Assert;
import org.junit.Test;

public class SimpleComboBoxTest {

    private SimpleComboBoxModel simpleComboBoxModel;
    private SimpleComboBox simpleComboBox;

    @Test
    public void testSetModel() {
        // Create a new instance of SimpleComboBoxModel
        simpleComboBoxModel = new SimpleComboBoxModel();

        // Set the model using SimpleComboBox's setModel method
        simpleComboBox.setModel(simpleComboBoxModel);

        // Verify that the model is set correctly
        Assert.assertEquals(simpleComboBoxModel, simpleComboBox.mySimpleModel);
    }

    @Test
    public void testGetSelectedValue_NullModel() {
        // Set the model to null
        simpleComboBox.mySimpleModel = null;

        // Call getSelectedValue method
        Object selectedValue = simpleComboBox.getSelectedValue();

        // Verify that selectedValue is null
        Assert.assertNull(selectedValue);
    }

    @Test
    public void testGetSelectedValue_ValidModel() {
        // Create a new instance of SimpleComboBoxModel
        simpleComboBoxModel = new SimpleComboBoxModel();

        // Set a selected value in the model
        simpleComboBoxModel.setSelectedValue("Test");

        // Set the model in SimpleComboBox
        simpleComboBox.mySimpleModel = simpleComboBoxModel;

        // Call getSelectedValue method
        Object selectedValue = simpleComboBox.getSelectedValue();

        // Verify that the selectedValue is the same as the one set in the model
        Assert.assertEquals("Test", selectedValue);
    }

    @Test
    public void testSetSelectedValue() {
        // Create a new instance of SimpleComboBoxModel
        simpleComboBoxModel = new SimpleComboBoxModel();

        // Set the model in SimpleComboBox
        simpleComboBox.mySimpleModel = simpleComboBoxModel;

        // Set a selected value using setSelectedValue method
        simpleComboBox.setSelectedValue("Test");

        // Verify that the selectedValue is set correctly in the model
        Assert.assertEquals("Test", simpleComboBoxModel.getSelectedValue());
    }

    @Test
    public void testGetSelectedInt_NullValue() {
        // Set the selected value in the model to null
        simpleComboBox.mySimpleModel.setSelectedValue(null);

        // Call getSelectedInt method
        int selectedInt = simpleComboBox.getSelectedInt();

        // Verify that the selectedInt is -1
        Assert.assertEquals(-1, selectedInt);
    }

    @Test
    public void testGetSelectedInt_IntegerValue() {
        // Set an Integer value in the model
        simpleComboBox.mySimpleModel.setSelectedValue(10);

        // Call getSelectedInt method
        int selectedInt = simpleComboBox.getSelectedInt();

        // Verify that the selectedInt is the same as the Integer value set in the model
        Assert.assertEquals(10, selectedInt);
    }

    @Test
    public void testGetSelectedInt_LongValue() {
        // Set a Long value in the model
        simpleComboBox.mySimpleModel.setSelectedValue(100L);

        // Call getSelectedInt method
        int selectedInt = simpleComboBox.getSelectedInt();

        // Verify that the selectedInt is the same as the Long value set in the model
        Assert.assertEquals(100, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ShortValue() {
        // Set a Short value in the model
        simpleComboBox.mySimpleModel.setSelectedValue((short) 5);

        // Call getSelectedInt method
        int selectedInt = simpleComboBox.getSelectedInt();

        // Verify that the selectedInt is the same as the Short value set in the model
        Assert.assertEquals(5, selectedInt);
    }

    @Test
    public void testGetSelectedInt_ByteValue() {
        // Set a Byte value in the model
        simpleComboBox.mySimpleModel.setSelectedValue((byte) 2);

        // Call getSelectedInt method
        int selectedInt = simpleComboBox.getSelectedInt();

        // Verify that the selectedInt is the same as the Byte value set in the model
        Assert.assertEquals(2, selectedInt);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_InvalidValue() {
        // Set an invalid value in the model
        simpleComboBox.mySimpleModel.setSelectedValue("Invalid");

        // Call getSelectedInt method, expect IllegalStateException to be thrown
        simpleComboBox.getSelectedInt();
    }

    @Test
    public void testGetSelectedLong_NullValue() {
        // Set the selected value in the model to null
        simpleComboBox.mySimpleModel.setSelectedValue(null);

        // Call getSelectedLong method
        long selectedLong = simpleComboBox.getSelectedLong();

        // Verify that the selectedLong is -1L
        Assert.assertEquals(-1L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_IntegerValue() {
        // Set an Integer value in the model
        simpleComboBox.mySimpleModel.setSelectedValue(10);

        // Call getSelectedLong method
        long selectedLong = simpleComboBox.getSelectedLong();

        // Verify that the selectedLong is the same as the Integer value set in the model
        Assert.assertEquals(10L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_LongValue() {
        // Set a Long value in the model
        simpleComboBox.mySimpleModel.setSelectedValue(100L);

        // Call getSelectedLong method
        long selectedLong = simpleComboBox.getSelectedLong();

        // Verify that the selectedLong is the same as the Long value set in the model
        Assert.assertEquals(100L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ShortValue() {
        // Set a Short value in the model
        simpleComboBox.mySimpleModel.setSelectedValue((short) 5);

        // Call getSelectedLong method
        long selectedLong = simpleComboBox.getSelectedLong();

        // Verify that the selectedLong is the same as the Short value set in the model
        Assert.assertEquals(5L, selectedLong);
    }

    @Test
    public void testGetSelectedLong_ByteValue() {
        // Set a Byte value in the model
        simpleComboBox.mySimpleModel.setSelectedValue((byte) 2);

        // Call getSelectedLong method
        long selectedLong = simpleComboBox.getSelectedLong();

        // Verify that the selectedLong is the same as the Byte value set in the model
        Assert.assertEquals(2L, selectedLong);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_InvalidValue() {
        // Set an invalid value in the model
        simpleComboBox.mySimpleModel.setSelectedValue("Invalid");

        // Call getSelectedLong method, expect IllegalStateException to be thrown
        simpleComboBox.getSelectedLong();
    }
}