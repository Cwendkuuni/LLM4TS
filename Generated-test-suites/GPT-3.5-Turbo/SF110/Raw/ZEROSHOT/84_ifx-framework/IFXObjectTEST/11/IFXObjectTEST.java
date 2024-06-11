
import org.junit.*;
import static org.junit.Assert.*;
import java.lang.reflect.*;
import org.sourceforge.ifx.basetypes.*;

public class IFXObjectTest {
    private IFXObject obj;
    
    @Before
    public void setUp() {
        obj = new IFXObject();
    }
    
    @After
    public void tearDown() {
        obj = null;
    }
    
    @Test
    public void testEquals() {
        Object otherObj = new Object();
        assertFalse(obj.equals(otherObj));
        
        IFXObject that = new IFXObject();
        assertTrue(obj.equals(that));
    }
    
    @Test
    public void testToString() {
        assertNotNull(obj.toString());
    }
    
    @Test
    public void testStringify() {
        assertNotNull(obj.stringify(obj, 0));
    }
    
    @Test
    public void testDoCheckEquals() {
        IFXObject thisObj = new IFXObject();
        IFXObject thatObj = new IFXObject();
        assertTrue(obj.doCheckEquals(thisObj, thatObj));
        
        IFXObject nullThisObj = null;
        IFXObject nullThatObj = null;
        assertTrue(obj.doCheckEquals(nullThisObj, nullThatObj));
    }
    
    @Test
    public void testTab() {
        assertNotNull(obj.tab(0));
    }
    
    @Test
    public void testPrivateMethods() throws Exception {
        Method equalsMethod = IFXObject.class.getDeclaredMethod("doCheckEquals", IFXObject.class, IFXObject.class);
        equalsMethod.setAccessible(true);
        assertNotNull(equalsMethod.invoke(obj, new IFXObject(), new IFXObject()));
        
        Method stringifyMethod = IFXObject.class.getDeclaredMethod("stringify", IFXObject.class, int.class);
        stringifyMethod.setAccessible(true);
        assertNotNull(stringifyMethod.invoke(obj, new IFXObject(), 0));
        
        Method tabMethod = IFXObject.class.getDeclaredMethod("tab", int.class);
        tabMethod.setAccessible(true);
        assertNotNull(tabMethod.invoke(obj, 0));
    }
}
