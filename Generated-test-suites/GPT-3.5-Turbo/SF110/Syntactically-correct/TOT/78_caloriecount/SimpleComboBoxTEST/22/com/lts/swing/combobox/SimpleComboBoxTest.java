import org.junit.*;
import static org.junit.Assert.*;

public class SimpleComboBoxTest {
    
    private SimpleComboBox simpleComboBox;
    private SimpleComboBoxModel model;
    
    @Before
    public void setUp() {
        model = new SimpleComboBoxModel();
        simpleComboBox = new SimpleComboBox(model);
    }
    
    @After
    public void tearDown() {
        model = null;
        simpleComboBox = null;
    }
    
    @Test
    public void testSetModel_WhenModelIsSimpleComboBoxModel_ShouldSetMySimpleModel() {
        // Arrange
        SimpleComboBoxModel newModel = new SimpleComboBoxModel();
        
        // Act
        simpleComboBox.setModel(newModel);
        
        // Assert
        assertEquals(newModel, simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testSetModel_WhenModelIsNotSimpleComboBoxModel_ShouldNotSetMySimpleModel() {
        // Arrange
        DefaultComboBoxModel newModel = new DefaultComboBoxModel();
        
        // Act
        simpleComboBox.setModel(newModel);
        
        // Assert
        assertNull(simpleComboBox.mySimpleModel);
    }
    
    @Test
    public void testGetSelectedValue_WhenMySimpleModelIsNull_ShouldReturnNull() {
        // Act
        Object selectedValue = simpleComboBox.getSelectedValue();
        
        // Assert
        assertNull(selectedValue);
    }
    
    @Test
    public void testGetSelectedValue_WhenMySimpleModelIsNotNull_ShouldReturnSelectedValue() {
        // Arrange
        Object selectedValue = "TestValue";
        model.setSelectedValue(selectedValue);
        
        // Act
        Object result = simpleComboBox.getSelectedValue();
        
        // Assert
        assertEquals(selectedValue, result);
    }
    
    @Test
    public void testSetSelectedValue_ShouldSetSelectedValueInMySimpleModel() {
        // Arrange
        Object selectedValue = "TestValue";
        
        // Act
        simpleComboBox.setSelectedValue(selectedValue);
        
        // Assert
        assertEquals(selectedValue, model.getSelectedValue());
    }
    
    @Test
    public void testGetSelectedInt_WhenSelectedValueIsNull_ShouldReturnNegativeOne() {
        // Act
        int selectedInt = simpleComboBox.getSelectedInt();
        
        // Assert
        assertEquals(-1, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_WhenSelectedValueIsInteger_ShouldReturnIntegerValue() {
        // Arrange
        Object selectedValue = 5;
        model.setSelectedValue(selectedValue);
        
        // Act
        int selectedInt = simpleComboBox.getSelectedInt();
        
        // Assert
        assertEquals((int)selectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_WhenSelectedValueIsLong_ShouldReturnIntegerValue() {
        // Arrange
        Object selectedValue = 500L;
        model.setSelectedValue(selectedValue);
        
        // Act
        int selectedInt = simpleComboBox.getSelectedInt();
        
        // Assert
        assertEquals(((Long)selectedValue).intValue(), selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_WhenSelectedValueIsShort_ShouldReturnShortValue() {
        // Arrange
        Object selectedValue = (short) 1;
        model.setSelectedValue(selectedValue);
        
        // Act
        int selectedInt = simpleComboBox.getSelectedInt();
        
        // Assert
        assertEquals((short)selectedValue, selectedInt);
    }
    
    @Test
    public void testGetSelectedInt_WhenSelectedValueIsByte_ShouldReturnByteValue() {
        // Arrange
        Object selectedValue = (byte) 10;
        model.setSelectedValue(selectedValue);
        
        // Act
        int selectedInt = simpleComboBox.getSelectedInt();
        
        // Assert
        assertEquals((byte)selectedValue, selectedInt);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedInt_WhenSelectedValueIsInvalidType_ShouldThrowIllegalArgumentException() {
        // Arrange
        Object selectedValue = "InvalidType";
        model.setSelectedValue(selectedValue);
        
        // Act
        simpleComboBox.getSelectedInt();
        
        // Assert (Expected to throw IllegalArgumentException)
    }
    
    @Test
    public void testGetSelectedLong_WhenSelectedValueIsNull_ShouldReturnNegativeOne() {
        // Act
        long selectedLong = simpleComboBox.getSelectedLong();
        
        // Assert
        assertEquals(-1L, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_WhenSelectedValueIsInteger_ShouldReturnLongValue() {
        // Arrange
        Object selectedValue = 500;
        model.setSelectedValue(selectedValue);
        
        // Act
        long selectedLong = simpleComboBox.getSelectedLong();
        
        // Assert
        assertEquals((long)selectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_WhenSelectedValueIsLong_ShouldReturnLongValue() {
        // Arrange
        Object selectedValue = 500L;
        model.setSelectedValue(selectedValue);
        
        // Act
        long selectedLong = simpleComboBox.getSelectedLong();
        
        // Assert
        assertEquals((long)selectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_WhenSelectedValueIsShort_ShouldReturnLongValue() {
        // Arrange
        Object selectedValue = (short) 5;
        model.setSelectedValue(selectedValue);
        
        // Act
        long selectedLong = simpleComboBox.getSelectedLong();
        
        // Assert
        assertEquals((short)selectedValue, selectedLong);
    }
    
    @Test
    public void testGetSelectedLong_WhenSelectedValueIsByte_ShouldReturnLongValue() {
        // Arrange
        Object selectedValue = (byte) 10;
        model.setSelectedValue(selectedValue);
        
        // Act
        long selectedLong = simpleComboBox.getSelectedLong();
        
        // Assert
        assertEquals((byte)selectedValue, selectedLong);
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetSelectedLong_WhenSelectedValueIsInvalidType_ShouldThrowIllegalArgumentException() {
        // Arrange
        Object selectedValue = "InvalidType";
        model.setSelectedValue(selectedValue);
        
        // Act
        simpleComboBox.getSelectedLong();
        
        // Assert (Expected to throw IllegalArgumentException)
    }
}