import static org.junit.Assert.*;
import org.junit.Test;

public class IFXObjectTest {
    
    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        assertEquals(true, obj1.equals(obj2));
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String expectedOutput = "org.sourceforge.ifx.basetypes.IFXObject = {\n" +
                                "  }\n";
        assertEquals(expectedOutput, obj.toString());
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = null;
        assertEquals(false, obj1.doCheckEquals(obj1, obj2));
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        assertEquals("  ", obj.tab(1));
    }
}