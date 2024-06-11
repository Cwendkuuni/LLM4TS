package src;
import org.junit.*;

public class VassalRebellionTest {

    @Test
    public void testPerform() {
        //Test case 1
        VassalRebellion vassalRebellion = new VassalRebellion();
        Fiefdoms fiefdomObj = new Fiefdoms();
        vassalRebellion.perform(fiefdomObj.getLoyalVassals());
        //Perform assertions
        
        //Test case 2
        //Add more test cases as needed
        
        //...
        
        //Test case n
        //Add more test cases as needed
    }
}