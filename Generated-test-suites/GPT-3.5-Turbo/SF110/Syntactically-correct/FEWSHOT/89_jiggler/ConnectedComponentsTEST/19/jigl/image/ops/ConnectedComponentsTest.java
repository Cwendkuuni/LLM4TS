package jigl.image.ops;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import jigl.image.BinaryImage;
import jigl.image.GrayImage;
import jigl.image.exceptions.InvalidArgumentException;

public class ConnectedComponentsTest {
    private ConnectedComponents component;
    
    @Before
    public void setUp() throws InvalidArgumentException {
        component = new ConnectedComponents(ConnectedComponents.FOUR_CONNECTED, 0);
    }
    
    @Test(expected = InvalidArgumentException.class)
    public void testConstructorInvalidArgumentException() throws InvalidArgumentException {
        new ConnectedComponents(2, 0);
        new ConnectedComponents(0, 2);
    }
    
    @Test
    public void testApply() {
        BinaryImage mockedBinaryImage = mock(BinaryImage.class);
        GrayImage result = component.apply(mockedBinaryImage);
        // test the result
    }
}