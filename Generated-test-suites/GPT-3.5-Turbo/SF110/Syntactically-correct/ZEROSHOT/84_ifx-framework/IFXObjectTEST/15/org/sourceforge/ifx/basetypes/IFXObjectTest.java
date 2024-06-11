import org.junit.*;

public class IFXObjectTest {

    @Test
    public void testEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        boolean result = obj1.equals(obj2);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testToString() {
        IFXObject obj = new IFXObject();
        String result = obj.toString();
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {}";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testStringify() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String result = obj.stringify(obj, level);
        String expected = "org.sourceforge.ifx.basetypes.IFXObject = {\n}";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject obj1 = new IFXObject();
        IFXObject obj2 = new IFXObject();
        boolean result = obj1.doCheckEquals(obj1, obj2);
        Assert.assertFalse(result);
    }
    
    @Test
    public void testTab() {
        IFXObject obj = new IFXObject();
        int level = 0;
        String result = obj.tab(level);
        String expected = "";
        Assert.assertEquals(expected, result);
    }
}