import org.junit.Test;
import java.util.*;
import static org.junit.Assert.*;

public class VassalRebellionTest {
    
    @Test
    public void testPerform_EmptyCollection() {
        VassalRebellion rebellion = new VassalRebellion();
        Collection<String> emptyCol = new ArrayList<>();
        
        rebellion.perform(emptyCol);
        
        // Assert expected behavior
    }
    
    @Test
    public void testPerform_NullValuesInCollection() {
        VassalRebellion rebellion = new VassalRebellion();
        Collection<String> colWithNullValues = new ArrayList<>();
        colWithNullValues.add("Fiefdom1");
        colWithNullValues.add(null);
        
        rebellion.perform(colWithNullValues);
        
        // Assert expected behavior
    }
    
    @Test(expected = NullPointerException.class)
    public void testPerform_NotPresentInMap() {
        VassalRebellion rebellion = new VassalRebellion();
        Collection<String> colNotPresentInMap = new ArrayList<>();
        colNotPresentInMap.add("FiefdomNotPresent");
        
        rebellion.perform(colNotPresentInMap);
        
        // Assert expected exception
    }
    
    @Test(expected = NullPointerException.class)
    public void testPerform_NullFiefdomObj() {
        VassalRebellion rebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        rebellion.setFiefdomObj(null);
        
        rebellion.perform(col);
        
        // Assert expected exception
    }
    
    @Test
    public void testPerform_PersonalAndFiefdomCastlesZero() {
        VassalRebellion rebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        rebellion.setFiefdomObj(new Fiefdoms());
        
        rebellion.perform(col);
        
        // Assert expected behavior
    }
    
    @Test
    public void testPerform_LoyalVassalsSizeZero() {
        VassalRebellion rebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setLoyalVassals(new ArrayList<>());
        rebellion.setFiefdomObj(fiefdomObj);
        
        rebellion.perform(col);
        
        // Assert expected behavior
    }
    
    @Test
    public void testPerform_LoyalVassalsSizeLessThanPersonalAndFiefdomCastles() {
        VassalRebellion rebellion = new VassalRebellion();
        Collection<String> col = new ArrayList<>();
        col.add("Fiefdom1");
        Fiefdoms fiefdomObj = new Fiefdoms();
        fiefdomObj.setLoyalVassals(Arrays.asList("Vassal1", "Vassal2"));
        rebellion.setFiefdomObj(fiefdomObj);
        
        rebellion.perform(col);
        
        // Assert expected behavior
    }
}