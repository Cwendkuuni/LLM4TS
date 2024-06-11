import org.junit.Assert;
import org.junit.Test;
import java.lang.reflect.Array;

public class IFXObjectTest {
  
  @Test
  public void testEquals() {
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    boolean result = obj1.equals(obj2);
    Assert.assertEquals(true, result);
  }
  
  @Test
  public void testToString() {
    IFXObject obj = new IFXObject();
    String result = obj.toString();
    Assert.assertNotNull(result);
  }
  
  @Test
  public void testStringify() {
    IFXObject obj = new IFXObject();
    String result = obj.stringify(obj, 1);
    Assert.assertNotNull(result);
  }
  
  @Test
  public void testDoCheckEquals() {
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    boolean result = obj1.doCheckEquals(obj1, obj2);
    Assert.assertEquals(true, result);
  }
  
  @Test
  public void testTab() {
    IFXObject obj = new IFXObject();
    String result = obj.tab(1);
    Assert.assertNotNull(result);
  }
}