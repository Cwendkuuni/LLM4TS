import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

    @Test
    public void testIsCounting_WhenCounting_ReturnsTrue() {
        // Arrange
        Timer timer = new Timer(5000);
        timer.start();

        // Act
        boolean result = timer.isCounting();

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testIsCounting_WhenNotCounting_ReturnsFalse() {
        // Arrange
        Timer timer = new Timer(5000);

        // Act
        boolean result = timer.isCounting();

        // Assert
        Assert.assertFalse(result);
    }

    @Test
    public void testStart_ValidStartTimeSet() {
        // Arrange
        Timer timer = new Timer(5000);

        // Act
        timer.start();

        // Assert
        Assert.assertNotEquals(-1, timer.getStartTime());
    }
}