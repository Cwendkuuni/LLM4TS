import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {

    @Test
    public void testPerform() {
        // Arrange
        Collection args = new ArrayList<>();
        
        // Act
        Purchase purchase = new Purchase();
        purchase.perform(args);

        // Assert
        // Add assertions here
    }
    
    @Test
    public void testVassal() {
        // Arrange
        Iterator arg = new ArrayList<>().iterator();
        Purchase purchase = new Purchase();
        
        // Act
        purchase.vassal(arg);

        // Assert
        // Add assertions here
    }
    
    @Test
    public void testCastleFiefdom() {
        // Arrange
        Iterator arg = new ArrayList<>().iterator();
        Purchase purchase = new Purchase();
        
        // Act
        purchase.castleFiefdom(arg);

        // Assert
        // Add assertions here
    }
    
    @Test
    public void testCastlePersonal() {
        // Arrange
        Iterator arg = new ArrayList<>().iterator();
        Purchase purchase = new Purchase();
        
        // Act
        purchase.castlePersonal(arg);

        // Assert
        // Add assertions here
    }
    
    @Test
    public void testMercenary() {
        // Arrange
        Iterator arg = new ArrayList<>().iterator();
        Purchase purchase = new Purchase();
        
        // Act
        purchase.mercenary(arg);

        // Assert
        // Add assertions here
    }
}