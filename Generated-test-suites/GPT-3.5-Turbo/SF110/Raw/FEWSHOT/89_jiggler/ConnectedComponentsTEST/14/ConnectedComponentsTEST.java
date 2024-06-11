package jigl.image.ops;
import jigl.image.BinaryImage;

public class ConnectedComponentsTest{
    @Test
    public void testApply(){
        ConnectedComponents cc = new ConnectedComponents(0, 1);
        
        BinaryImage image = new BinaryImage(3, 3);
        image.set(0, 0, 1);
        image.set(0, 1, 1);
        image.set(0, 2, 0);
        image.set(1, 0, 1);
        image.set(1, 1, 0);
        image.set(1, 2, 1);
        image.set(2, 0, 0);
        image.set(2, 1, 1);
        image.set(2, 2, 0);
        
        GrayImage result = cc.apply(image);
        
        assertEquals(1, result.get(0, 0));
        assertEquals(1, result.get(0, 1));
        assertEquals(2, result.get(0, 2));
        assertEquals(1, result.get(1, 0));
        assertEquals(0, result.get(1, 1));
        assertEquals(3, result.get(1, 2));
        assertEquals(4, result.get(2, 0));
        assertEquals(5, result.get(2, 1));
        assertEquals(6, result.get(2, 2));
    }
}