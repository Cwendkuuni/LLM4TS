import org.junit.*;
import static org.junit.Assert.*;

public class RenamePrototypesTest {

    @Test
    public void testProcess_SingleProperty() {
        // Arrange
        RenamePrototypes rp = new RenamePrototypes();
        rp.addProperty("name");
        
        // Act
        rp.process();
        
        // Assert
        assertTrue(rp.getProperty("name").isNewNameGenerated());
    }
    
    @Test
    public void testProcess_MultipleProperties() {
        // Arrange
        RenamePrototypes rp = new RenamePrototypes();
        rp.addProperty("name");
        rp.addProperty("age");
        rp.addProperty("address");
        
        // Act
        rp.process();
        
        // Assert
        assertTrue(rp.getProperty("name").isNewNameGenerated());
        assertTrue(rp.getProperty("age").isNewNameGenerated());
        assertTrue(rp.getProperty("address").isNewNameGenerated());
    }
    
    @Test
    public void testProcess_ReserveExistingName() {
        // Arrange
        RenamePrototypes rp = new RenamePrototypes();
        rp.addProperty("name");
        rp.addProperty("age");
        rp.addProperty("address");
        rp.reserveName("age");
        
        // Act
        rp.process();
        
        // Assert
        assertFalse(rp.getProperty("name").isNewNameGenerated());
        assertFalse(rp.getProperty("age").isNewNameGenerated());
        assertTrue(rp.getProperty("address").isNewNameGenerated());
    }
    
    @Test
    public void testProcess_AggressiveRenaming() {
        // Arrange
        RenamePrototypes rp = new RenamePrototypes();
        rp.setAggressiveRenaming(true);
        rp.addProperty("name");
        rp.addProperty("age");
        rp.addProperty("address");
        
        // Act
        rp.process();
        
        // Assert
        assertTrue(rp.getProperty("name").isNewNameGenerated());
        assertTrue(rp.getProperty("age").isNewNameGenerated());
        assertTrue(rp.getProperty("address").isNewNameGenerated());
    }
    
    @Test
    public void testProcess_NotAggressiveRenaming() {
        // Arrange
        RenamePrototypes rp = new RenamePrototypes();
        rp.setAggressiveRenaming(false);
        rp.addProperty("name");
        rp.addProperty("age");
        rp.addProperty("address");
        
        // Act
        rp.process();
        
        // Assert
        assertFalse(rp.getProperty("name").isNewNameGenerated());
        assertFalse(rp.getProperty("age").isNewNameGenerated());
        assertFalse(rp.getProperty("address").isNewNameGenerated());
    }
}