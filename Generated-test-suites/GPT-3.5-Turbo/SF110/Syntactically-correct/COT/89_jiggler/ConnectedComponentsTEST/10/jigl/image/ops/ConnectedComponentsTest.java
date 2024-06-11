import org.junit.Test;
import static org.junit.Assert.*;

public class ConnectedComponentsTest {
    
    @Test
    public void testConstructor() throws InvalidArgumentException {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        assertNotNull(cc);
    }
    
    @Test
    public void testApply() {
        ConnectedComponents cc = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
        BinaryImage inputImage = new BinaryImage(10, 10);
        GrayImage resultImage = cc.apply(inputImage);
        assertNotNull(resultImage);
        assertEquals(10, resultImage.X());
        assertEquals(10, resultImage.Y());
    }
    
}