import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class PurchaseTest {
    Purchase purchase;
  
    @Before
    public void setUp() {
        purchase = new Purchase();
    }
  
    @Test
    public void testPerform() {
        // Test 1
        Collection<String> args1 = new ArrayList<>();
        args1.add("vassal");
        Iterator<String> argIter1 = args1.iterator();
        try {
            purchase.perform(args1);
        } catch (Exception e) {
            fail("testPerform failed: " + e.getMessage());
        }
        // Assertions for Test 1
        // Add assertions here
        
        // Test 2
        Collection<String> args2 = new ArrayList<>();
        args2.add("castleFiefdom");
        Iterator<String> argIter2 = args2.iterator();
        try {
            purchase.perform(args2);
        } catch (Exception e) {
            fail("testPerform failed: " + e.getMessage());
        }
        // Assertions for Test 2
        // Add assertions here
        
        // Test 3
        Collection<String> args3 = new ArrayList<>();
        args3.add("castlePersonal");
        Iterator<String> argIter3 = args3.iterator();
        try {
            purchase.perform(args3);
        } catch (Exception e) {
            fail("testPerform failed: " + e.getMessage());
        }
        // Assertions for Test 3
        // Add assertions here
        
        // Test 4
        Collection<String> args4 = new ArrayList<>();
        args4.add("mercenary");
        Iterator<String> argIter4 = args4.iterator();
        try {
            purchase.perform(args4);
        } catch (Exception e) {
            fail("testPerform failed: " + e.getMessage());
        }
        // Assertions for Test 4
        // Add assertions here
    }
  
    @Test
    public void testVassal() {
        // Test 1
        Iterator<String> arg1 = new ArrayList<String>(Arrays.asList("1", "buyer1", "fiefdom1")).iterator();
        try {
            purchase.vassal(arg1);
        } catch (Exception e) {
            fail("testVassal failed: " + e.getMessage());
        }
        // Assertions for Test 1
        // Add assertions here
        
        // Test 2
        Iterator<String> arg2 = new ArrayList<String>(Arrays.asList("2", "buyer2", "fiefdom2")).iterator();
        try {
            purchase.vassal(arg2);
        } catch (Exception e) {
            fail("testVassal failed: " + e.getMessage());
        }
        // Assertions for Test 2
        // Add assertions here
    }
  
    @Test
    public void testCastleFiefdom() {
        // Test 1
        Iterator<String> arg1 = new ArrayList<String>(Arrays.asList("1", "buyer1", "fiefdom1")).iterator();
        try {
            purchase.castleFiefdom(arg1);
        } catch (Exception e) {
            fail("testCastleFiefdom failed: " + e.getMessage());
        }
        // Assertions for Test 1
        // Add assertions here
        
        // Test 2
        Iterator<String> arg2 = new ArrayList<String>(Arrays.asList("2", "buyer2", "fiefdom2")).iterator();
        try {
            purchase.castleFiefdom(arg2);
        } catch (Exception e) {
            fail("testCastleFiefdom failed: " + e.getMessage());
        }
        // Assertions for Test 2
        // Add assertions here
    }
  
    @Test
    public void testCastlePersonal() {
        // Test 1
        Iterator<String> arg1 = new ArrayList<String>(Arrays.asList("1", "buyer1", "fiefdom1")).iterator();
        try {
            purchase.castlePersonal(arg1);
        } catch (Exception e) {
            fail("testCastlePersonal failed: " + e.getMessage());
        }
        // Assertions for Test 1
        // Add assertions here
        
        // Test 2
        Iterator<String> arg2 = new ArrayList<String>(Arrays.asList("2", "buyer2", "fiefdom2")).iterator();
        try {
            purchase.castlePersonal(arg2);
        } catch (Exception e) {
            fail("testCastlePersonal failed: " + e.getMessage());
        }
        // Assertions for Test 2
        // Add assertions here
    }
  
    @Test
    public void testMercenary() {
        // Test 1
        Iterator<String> arg1 = new ArrayList<String>(Arrays.asList("1", "buyer1", "knight1")).iterator();
        try {
            purchase.mercenary(arg1);
        } catch (Exception e) {
            fail("testMercenary failed: " + e.getMessage());
        }
        // Assertions for Test 1
        // Add assertions here
        
        // Test 2
        Iterator<String> arg2 = new ArrayList<String>(Arrays.asList("2", "buyer2", "knight2")).iterator();
        try {
            purchase.mercenary(arg2);
        } catch (Exception e) {
            fail("testMercenary failed: " + e.getMessage());
        }
        // Assertions for Test 2
        // Add assertions here
    }
}