import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.Timer;

public class TimerTest {

    private Timer timer;

    @Before
    public void setup() {
        timer = new Timer(5000); // Initializing the timer with a duration of 5 seconds
    }

    @Test
    public void testIsCounting_WhenTimerNotStarted_ReturnsFalse() {
        // Arrange

        // Act
        boolean isCounting = timer.isCounting();

        // Assert
        Assert.assertFalse(isCounting);
    }

    @Test
    public void testIsCounting_WhenDurationIsZero_ReturnsFalse() {
        // Arrange
        timer = new Timer(0);

        // Act
        boolean isCounting = timer.isCounting();

        // Assert
        Assert.assertFalse(isCounting);
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndNotYetExpired_ReturnsTrue() {
        // Arrange
        timer.start();

        // Act
        boolean isCounting = timer.isCounting();

        // Assert
        Assert.assertTrue(isCounting);
    }

    @Test
    public void testIsCounting_WhenTimerStartedAndExpired_ReturnsFalse() throws InterruptedException {
        // Arrange
        timer.start();
        Thread.sleep(6000); // Wait for more than 5 seconds

        // Act
        boolean isCounting = timer.isCounting();

        // Assert
        Assert.assertFalse(isCounting);
    }
    
    @Test
    public void testStart_WhenCalled_StartTimeIsSet() {
        // Arrange

        // Act
        timer.start();

        // Assert
        Assert.assertNotEquals(-1, timer.getStartTime());
    }
    
    @Test
    public void testStart_WhenCalled_StartTimeIsSetToCurrentTime() {
        // Arrange

        // Act
        timer.start();

        // Assert
        Assert.assertEquals(System.currentTimeMillis(), timer.getStartTime());
    }
}