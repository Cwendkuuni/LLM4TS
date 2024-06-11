
import org.junit.Test;
import static org.junit.Assert.*;

public class ComboLegTest {

    @Test
    public void testEquals() {
        // Arrange
        ComboLeg leg1 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        ComboLeg leg2 = new ComboLeg(1, 1, "BUY", "NYSE", 1);
        
        // Act
        boolean result = leg1.equals(leg2);
        
        // Assert
        assertTrue(result);
    }
}
