import org.junit.*;
import java.util.*;

public class VassalRebellionTest {

  @Test
  public void testPerform_typicalCase() {
    // Arrange
    VassalRebellion vr = new VassalRebellion();
    Map<String, Fiefdoms> map = new Map<>();
    Fiefdoms fiefdom1 = new Fiefdoms();
    fiefdom1.setName("Fiefdom1");
    fiefdom1.setLoyalVassals(new ArrayList<String>());
    fiefdom1.setRebelliousVassals(new ArrayList<String>());
    fiefdom1.setValue(10);
    map.put("Fiefdom1", fiefdom1);
    vr.fiefdomObj = fiefdom1;
    Collection<String> col = new ArrayList<String>();
    col.add("Fiefdom1");
    
    // Act
    vr.perform(col);
    
    // Assert
    Assert.assertTrue(fiefdom1.isKingdom());
    Assert.assertEquals(10, fiefdom1.getRebelliousVassals().size());
  }

  @Test
  public void testPerform_emptyLoyalVassals() {
    // Arrange
    VassalRebellion vr = new VassalRebellion();
    Map<String, Fiefdoms> map = new Map<>();
    Fiefdoms fiefdom1 = new Fiefdoms();
    fiefdom1.setName("Fiefdom1");
    fiefdom1.setLoyalVassals(new ArrayList<String>());
    fiefdom1.setRebelliousVassals(new ArrayList<String>());
    fiefdom1.setValue(0);
    map.put("Fiefdom1", fiefdom1);
    vr.fiefdomObj = fiefdom1;
    Collection<String> col = new ArrayList<String>();
    col.add("Fiefdom1");
    
    // Act
    vr.perform(col);
    
    // Assert
    Assert.assertFalse(fiefdom1.isKingdom());
    Assert.assertTrue(fiefdom1.getRebelliousVassals().isEmpty());
  }

  @Test
  public void testPerform_noPersonalCastles() {
    // Arrange
    VassalRebellion vr = new VassalRebellion();
    Map<String, Fiefdoms> map = new Map<>();
    Fiefdoms fiefdom1 = new Fiefdoms();
    fiefdom1.setName("Fiefdom1");
    List<String> loyalVassals = new ArrayList<String>();
    loyalVassals.add("Vassal1");
    loyalVassals.add("Vassal2");
    fiefdom1.setLoyalVassals(loyalVassals);
    fiefdom1.setRebelliousVassals(new ArrayList<String>());
    fiefdom1.setValue(5);
    map.put("Fiefdom1", fiefdom1);
    vr.fiefdomObj = fiefdom1;
    Collection<String> col = new ArrayList<String>();
    col.add("Fiefdom1");
    
    // Act
    vr.perform(col);
    
    // Assert
    Assert.assertFalse(fiefdom1.isKingdom());
    Assert.assertEquals(2, fiefdom1.getRebelliousVassals().size());
  }

  @Test
  public void testPerform_noFiefdomCastles() {
    // Arrange
    VassalRebellion vr = new VassalRebellion();
    Map<String, Fiefdoms> map = new Map<>();
    Fiefdoms fiefdom1 = new Fiefdoms();
    fiefdom1.setName("Fiefdom1");
    List<String> loyalVassals = new ArrayList<String>();
    loyalVassals.add("Vassal1");
    loyalVassals.add("Vassal2");
    fiefdom1.setLoyalVassals(loyalVassals);
    fiefdom1.setRebelliousVassals(new ArrayList<String>());
    fiefdom1.setValue(20);
    map.put("Fiefdom1", fiefdom1);
    vr.fiefdomObj = fiefdom1;
    Collection<String> col = new ArrayList<String>();
    col.add("Fiefdom1");
    
    // Act
    vr.perform(col);
    
    // Assert
    Assert.assertFalse(fiefdom1.isKingdom());
    Assert.assertEquals(1, fiefdom1.getRebelliousVassals().size());
  }

  @Test
  public void testPerform_noLoyalVassals() {
    // Arrange
    VassalRebellion vr = new VassalRebellion();
    Map<String, Fiefdoms> map = new Map<>();
    Fiefdoms fiefdom1 = new Fiefdoms();
    fiefdom1.setName("Fiefdom1");
    fiefdom1.setRebelliousVassals(new ArrayList<String>());
    map.put("Fiefdom1", fiefdom1);
    vr.fiefdomObj = fiefdom1;
    Collection<String> col = new ArrayList<String>();
    col.add("Fiefdom1");
    
    // Act
    vr.perform(col);
    
    // Assert
    // Assert that a RollEvent is created
  }
}