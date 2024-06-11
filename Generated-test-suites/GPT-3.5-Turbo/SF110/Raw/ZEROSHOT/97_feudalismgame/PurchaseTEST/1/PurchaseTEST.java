import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {
    private Purchase purchase;
    
    @Before
    public void setUp() {
        purchase = new Purchase();
    }
    
    @Test
    public void testPerform() {
        Collection<Object> args = new ArrayList<>();
        purchase.perform(args);
        // Add assertions here
    }
    
    @Test
    public void testVassal() {
        Iterator<Object> arg = new ArrayList<>().iterator();
        purchase.vassal(arg);
        // Add assertions here
    }
    
    @Test
    public void testCastleFiefdom() {
        Iterator<Object> arg = new ArrayList<>().iterator();
        purchase.castleFiefdom(arg);
        // Add assertions here
    }
    
    @Test
    public void testCastlePersonal() {
        Iterator<Object> arg = new ArrayList<>().iterator();
        purchase.castlePersonal(arg);
        // Add assertions here
    }
    
    @Test
    public void testMercenary() {
        Iterator<Object> arg = new ArrayList<>().iterator();
        purchase.mercenary(arg);
        // Add assertions here
    }
    
}