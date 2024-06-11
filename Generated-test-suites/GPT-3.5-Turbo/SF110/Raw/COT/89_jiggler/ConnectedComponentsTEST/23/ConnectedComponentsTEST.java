
import org.junit.Test;

public class ConnectedComponentsTest {

    @Test
    public void testApply_basicFunctionality() {
        // Test the basic functionality of apply() method
        ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(1, 0, 1);
        image.set(2, 0, 0);
        image.set(0, 1, 0);
        image.set(1, 1, 0);
        image.set(2, 1, 0);
        image.set(0, 2, 1);
        image.set(1, 2, 1);
        image.set(2, 2, 0);
        GrayImage result = connectedComponents.apply(image);
        // Check if the connected components are correctly labeled
        // TODO: Write assertion
    }
    
    @Test
    public void testApply_edgeCases() {
        // Test edge case where inputs are at their minimum allowable values
        ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage image = new BinaryImage(1, 1);
        image.set(0, 0, 1);
        GrayImage result = connectedComponents.apply(image);
        // Check if the connected components are correctly labeled
        // TODO: Write assertion

        // Test edge case where inputs are at their maximum allowable values
        ConnectedComponents connectedComponents = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 1);
        BinaryImage image = new BinaryImage(1000, 1000);
        // Set all pixels to the background color
        // TODO: Write code to set all pixels to background color
        image.set(0, 0, 1);
        result = connectedComponents.apply(image);
        // Check if the connected components are correctly labeled
        // TODO: Write assertion
    }

    @Test
    public void testApply_exceptions() {
        // Test exception case where number of neighbors is not 0 or 1
        try {
            ConnectedComponents connectedComponents = new ConnectedComponents(2, 0);
            BinaryImage image = new BinaryImage(3, 3);
            image.set(0, 0, 1);
            image.set(1, 0, 1);
            image.set(2, 0, 0);
            image.set(0, 1, 0);
            image.set(1, 1, 0);
            image.set(2, 1, 0);
            image.set(0, 2, 1);
            image.set(1, 2, 1);
            image.set(2, 2, 0);
            GrayImage result = connectedComponents.apply(image);
            // Should not reach this point, exception should be thrown
            // TODO: Write assertion for expected exception
        } catch (InvalidArgumentException e) {
            // Catch the expected exception
        }
    }
}
