import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertTrue(obj1.equals(obj2));
    }

    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        assertNotNull(obj.toString());
    }

    @Test
    public void testStringify() throws Exception {
        IFXObject obj = new IFXObject();
        String expectedString = obj.stringify(obj, 0);
        assertNotNull(expectedString);
    }

    @Test
    public void testDoCheckEquals() throws Exception {
        IFXObject thisObj = new IFXObject();
        IFXObject thatObj = new IFXObject();
        boolean result = thisObj.doCheckEquals(thisObj, thatObj);
        assertTrue(result);
    }

    @Test
    public void testTab() throws Exception {
        IFXObject obj = new IFXObject();
        String result = obj.tab(4);
        assertEquals("        ", result);
    }

}