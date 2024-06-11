
import org.junit.Test;

public class DefaultPhdReadTagTest {
    
    private DefaultPhdReadTag defaultPhdReadTag;

    @Test
    public void testGetType() {
        // Arrange
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");

        // Act
        String result = defaultPhdReadTag.getType();

        // Assert
        assertEquals("type", result);
    }

    @Test
    public void testGetSource() {
        // Arrange
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        
        // Act
        String result = defaultPhdReadTag.getSource();
        
        // Assert
        assertEquals("source", result);
    }
    
    @Test
    public void testGetUngappedRange() {
        // Arrange
        Range ungappedRange = new Range(0, 10);
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", ungappedRange, new Date(), "comment", "freeFormData");
        
        // Act
        Range result = defaultPhdReadTag.getUngappedRange();
        
        // Assert
        assertEquals(ungappedRange, result);
    }
    
    @Test
    public void testGetDate() {
        // Arrange
        Date date = new Date();
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), date, "comment", "freeFormData");
        
        // Act
        Date result = defaultPhdReadTag.getDate();
        
        // Assert
        assertEquals(date, result);
    }
    
    @Test
    public void testGetComment() {
        // Arrange
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        
        // Act
        String result = defaultPhdReadTag.getComment();
        
        // Assert
        assertEquals("comment", result);
    }
    
    @Test
    public void testGetFreeFormData() {
        // Arrange
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        
        // Act
        String result = defaultPhdReadTag.getFreeFormData();
        
        // Assert
        assertEquals("freeFormData", result);
    }
    
    // Additional test cases for edge cases and exception handling scenarios
    
    @Test
    public void testEquals_SameObject() {
        // Arrange
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        
        // Act
        boolean result = defaultPhdReadTag.equals(defaultPhdReadTag);
        
        // Assert
        assertTrue(result);
    }
    
    @Test
    public void testEquals_NullObject() {
        // Arrange
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        
        // Act
        boolean result = defaultPhdReadTag.equals(null);
        
        // Assert
        assertFalse(result);
    }
    
    @Test
    public void testEquals_DifferentTypeObject() {
        // Arrange
        defaultPhdReadTag = new DefaultPhdReadTag("type", "source", new Range(), new Date(), "comment", "freeFormData");
        String differentTypeObject = "type";
        
        // Act
        boolean result = defaultPhdReadTag.equals(differentTypeObject);
        
        // Assert
        assertFalse(result);
    }
    
    // Add more test cases for other methods if needed
    
}
