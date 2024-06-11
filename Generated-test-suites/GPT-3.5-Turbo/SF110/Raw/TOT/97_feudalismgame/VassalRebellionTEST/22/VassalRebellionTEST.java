import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class VassalRebellionTest {
  
  private VassalRebellion vassalRebellion;
  
  @Before
  public void setUp() {
    vassalRebellion = new VassalRebellion();
  }
  
  @Test
  public void testPerformWithRebelliousVassals() {
    // Typical use case - Rebellious vassals present, enough loyalty points to rebel, and personal and fiefdom castles present
    Fiefdoms fiefdomObj = new Fiefdoms();
    fiefdomObj.setName("Test Fiefdom");
    fiefdomObj.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal 1", "Vassal 2")));
    fiefdomObj.setValue(2);
    fiefdomObj.setPersonalCastles(1);
    fiefdomObj.setFiefdomCastles(2);
    
    Map.map.put(fiefdomObj.getName(), fiefdomObj);
    
    Collection<String> col = new ArrayList<String>(Arrays.asList(fiefdomObj.getName()));
    
    vassalRebellion.perform(col);
    
    Fiefdoms updatedFiefdomObj = Map.map.get(fiefdomObj.getName());
    
    assertTrue(updatedFiefdomObj.isRebelliousVassals());
    assertFalse(updatedFiefdomObj.isKingdom());
    assertEquals(1, updatedFiefdomObj.getRebelliousVassals());
  }
  
  @Test
  public void testPerformWithLoyalVassals() {
    // Typical use case - Only loyal vassals present, no rebellious vassals formation
    Fiefdoms fiefdomObj = new Fiefdoms();
    fiefdomObj.setName("Test Fiefdom");
    fiefdomObj.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal 1", "Vassal 2", "Vassal 3")));
    fiefdomObj.setValue(3);
    
    Map.map.put(fiefdomObj.getName(), fiefdomObj);
    
    Collection<String> col = new ArrayList<String>(Arrays.asList(fiefdomObj.getName()));
    
    vassalRebellion.perform(col);
    
    Fiefdoms updatedFiefdomObj = Map.map.get(fiefdomObj.getName());
    
    assertFalse(updatedFiefdomObj.isRebelliousVassals());
    assertNull(updatedFiefdomObj.getRebelliousVassals());
  }
  
  @Test
  public void testPerformWithNoVassals() {
    // Edge case - No vassals in the fiefdom
    Fiefdoms fiefdomObj = new Fiefdoms();
    fiefdomObj.setName("Test Fiefdom");
    
    Map.map.put(fiefdomObj.getName(), fiefdomObj);
    
    Collection<String> col = new ArrayList<String>(Arrays.asList(fiefdomObj.getName()));
    
    vassalRebellion.perform(col);
    
    Fiefdoms updatedFiefdomObj = Map.map.get(fiefdomObj.getName());
    
    assertFalse(updatedFiefdomObj.isRebelliousVassals());
    assertNull(updatedFiefdomObj.getRebelliousVassals());
  }
  
  @Test
  public void testPerformWithInsufficientLoyaltyPoints() {
    // Edge case - Insufficient loyalty points to rebel
    Fiefdoms fiefdomObj = new Fiefdoms();
    fiefdomObj.setName("Test Fiefdom");
    fiefdomObj.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal 1", "Vassal 2")));
    fiefdomObj.setValue(1);
    
    Map.map.put(fiefdomObj.getName(), fiefdomObj);
    
    Collection<String> col = new ArrayList<String>(Arrays.asList(fiefdomObj.getName()));
    
    vassalRebellion.perform(col);
    
    Fiefdoms updatedFiefdomObj = Map.map.get(fiefdomObj.getName());
    
    assertFalse(updatedFiefdomObj.isRebelliousVassals());
    assertNull(updatedFiefdomObj.getRebelliousVassals());
  }
  
  @Test
  public void testPerformWithNoCastles() {
    // Edge case - No personal or fiefdom castles present
    Fiefdoms fiefdomObj = new Fiefdoms();
    fiefdomObj.setName("Test Fiefdom");
    fiefdomObj.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal 1", "Vassal 2", "Vassal 3")));
    fiefdomObj.setValue(3);
    
    Map.map.put(fiefdomObj.getName(), fiefdomObj);
    
    Collection<String> col = new ArrayList<String>(Arrays.asList(fiefdomObj.getName()));
    
    vassalRebellion.perform(col);
    
    Fiefdoms updatedFiefdomObj = Map.map.get(fiefdomObj.getName());
    
    assertFalse(updatedFiefdomObj.isRebelliousVassals());
    assertNull(updatedFiefdomObj.getRebelliousVassals());
  }
  
  @Test
  public void testPerformWithInsufficientLoyaltyPointsAndNoCastles() {
    // Error scenario - Insufficient loyalty points and no personal or fiefdom castles
    Fiefdoms fiefdomObj = new Fiefdoms();
    fiefdomObj.setName("Test Fiefdom");
    fiefdomObj.setLoyalVassals(new ArrayList<String>(Arrays.asList("Vassal 1", "Vassal 2")));
    fiefdomObj.setValue(1);
    
    Map.map.put(fiefdomObj.getName(), fiefdomObj);
    
    Collection<String> col = new ArrayList<String>(Arrays.asList(fiefdomObj.getName()));
    
    vassalRebellion.perform(col);
    
    Fiefdoms updatedFiefdomObj = Map.map.get(fiefdomObj.getName());
    
    assertFalse(updatedFiefdomObj.isRebelliousVassals());
    assertNull(updatedFiefdomObj.getRebelliousVassals());
  }
  
  @Test
  public void testPerformWithNoVassalsAndNoCastles() {
    // Error scenario - No vassals and no personal or fiefdom castles
    Fiefdoms fiefdomObj = new Fiefdoms();
    fiefdomObj.setName("Test Fiefdom");
    
    Map.map.put(fiefdomObj.getName(), fiefdomObj);
    
    Collection<String> col = new ArrayList<String>(Arrays.asList(fiefdomObj.getName()));
    
    vassalRebellion.perform(col);
    
    Fiefdoms updatedFiefdomObj = Map.map.get(fiefdomObj.getName());
    
    assertFalse(updatedFiefdomObj.isRebelliousVassals());
    assertNull(updatedFiefdomObj.getRebelliousVassals());
  }
  
}