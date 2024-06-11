
import org.junit.Assert;
import org.junit.Test;

public class IFXObjectTest {
    
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        Assert.assertTrue(obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String str = obj.toString();
        Assert.assertNotNull(str);
    }
}
