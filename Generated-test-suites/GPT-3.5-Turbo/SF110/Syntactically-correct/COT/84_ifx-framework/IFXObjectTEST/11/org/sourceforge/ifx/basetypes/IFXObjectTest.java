
import org.junit.Test;

public class IFXObjectTest {
  
  @Test
  public void testEquals() {
    IFXObject obj1 = new IFXObject();
    IFXObject obj2 = new IFXObject();
    
    boolean result = obj1.equals(obj2);
    
    assert(result == true);
  }
  
  @Test
  public void testToString() {
    IFXObject obj = new IFXObject();
    
    String result = obj.toString();
    
    assert(result != null);
  }
}
