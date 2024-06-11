import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Collection;
import java.util.Iterator;


public class VassalRebellionTest {
    VassalRebellion vassalRebellion = new VassalRebellion();
    
    //Test case for perform method
    @Test
    public void testPerform() {
        //Creating a fiefdom object for testing
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setName("Test Fiefdom");
        fiefdomObj.setLoyalVassals(new ArrayList<String>());
        fiefdomObj.setPersonalCastles(5);
        fiefdomObj.setFiefdomCastles(3);
        
        //Adding the fiefdom object to map
        Map.map.put("Test Fiefdom", fiefdomObj);
        
        //Creating a collection for testing
        Collection<String> col = new ArrayList<String>();
        col.add("Test Fiefdom");
        
        //Testing the perform method
        vassalRebellion.perform(col);
        
        //Assertions
        assertEquals(7, fiefdomObj.getRebelliousVassals());
        assertFalse(fiefdomObj.isKingdom());
        
        //Cleaning up the map after the test
        Map.map.remove("Test Fiefdom");
    }
}