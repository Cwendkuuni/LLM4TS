import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.event.ActionEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class FoxHuntFrameTest {

    private FoxHuntFrame frame;

    @BeforeEach
    public void setup() {
        frame = new FoxHuntFrame();
    }

    @Test
    public void testAddPoint() {
        // Typical Use Case
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");

        ActionEvent mockedEvent = mock(ActionEvent.class);
        frame.actionPerformed(mockedEvent);

        Assertions.assertEquals(1, frame.points.size());
        Assertions.assertEquals(0, frame.fox.size());
        Assertions.assertEquals(1, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointWithTwoPoints() {
        // Typical Use Case
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");

        ActionEvent mockedEvent = mock(ActionEvent.class);
        frame.actionPerformed(mockedEvent);

        frame.theta.setText("90");
        frame.lat.setText("38.9072");
        frame.lon.setText("-77.0369");
        frame.actionPerformed(mockedEvent);

        Assertions.assertEquals(2, frame.points.size());
        Assertions.assertEquals(1, frame.fox.size());
        Assertions.assertEquals(2, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointInvalidAngle() {
        // Error Scenario - Angle is not a number
        frame.theta.setText("invalid");
        frame.lat.setText("37.7749");
        frame.lon.setText("-122.4194");

        ActionEvent mockedEvent = mock(ActionEvent.class);
        frame.actionPerformed(mockedEvent);

        Assertions.assertEquals(0, frame.points.size());
        Assertions.assertEquals(0, frame.fox.size());
        Assertions.assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointInvalidLatitude() {
        // Error Scenario - Latitude is not a number
        frame.theta.setText("45");
        frame.lat.setText("invalid");
        frame.lon.setText("-122.4194");

        ActionEvent mockedEvent = mock(ActionEvent.class);
        frame.actionPerformed(mockedEvent);

        Assertions.assertEquals(0, frame.points.size());
        Assertions.assertEquals(0, frame.fox.size());
        Assertions.assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointInvalidLongitude() {
        // Error Scenario - Longitude is not a number
        frame.theta.setText("45");
        frame.lat.setText("37.7749");
        frame.lon.setText("invalid");

        ActionEvent mockedEvent = mock(ActionEvent.class);
        frame.actionPerformed(mockedEvent);

        Assertions.assertEquals(0, frame.points.size());
        Assertions.assertEquals(0, frame.fox.size());
        Assertions.assertEquals(0, frame.posModel.getRowCount());
    }

    @Test
    public void testAddPointWithNoCoordinates() {
        // Edge Case - No Coordinates provided
        frame.theta.setText("");
        frame.lat.setText("");
        frame.lon.setText("");

        ActionEvent mockedEvent = mock(ActionEvent.class);
        frame.actionPerformed(mockedEvent);

        Assertions.assertEquals(0, frame.points.size());
        Assertions.assertEquals(0, frame.fox.size());
        Assertions.assertEquals(0, frame.posModel.getRowCount());
    }
}