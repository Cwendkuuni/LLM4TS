import org.junit.Test;

public class VassalRebellionTest {
  
  @Test
  public void testPerform() {
    // Test case using a collection with a single fiefdom
    Collection<String> col1 = new ArrayList<>();
    col1.add("Fiefdom1");
    VassalRebellion vassalRebellion1 = new VassalRebellion();
    vassalRebellion1.perform(col1);
    // Check if the rebellious vassals and kingdom flags are correctly set
    assertEquals(vassalRebellion1.fiefdomObj.isRebelliousVassals(), false);
    assertEquals(vassalRebellion1.fiefdomObj.isKingdom(), false);
    
    // Test case using a collection with multiple fiefdoms
    Collection<String> col2 = new ArrayList<>();
    col2.add("Fiefdom2");
    col2.add("Fiefdom3");
    VassalRebellion vassalRebellion2 = new VassalRebellion();
    vassalRebellion2.perform(col2);
    // Check if the rebellious vassals and kingdom flags are correctly set
    assertEquals(vassalRebellion2.fiefdomObj.isRebelliousVassals(), true);
    assertEquals(vassalRebellion2.fiefdomObj.isKingdom(), false);
    
    // Test case with loyal vassals but not enough vassals to rebel
    Collection<String> col3 = new ArrayList<>();
    col3.add("Fiefdom4");
    VassalRebellion vassalRebellion3 = new VassalRebellion();
    vassalRebellion3.perform(col3);
    // Check if the rebellious vassals and kingdom flags are correctly set
    assertEquals(vassalRebellion3.fiefdomObj.isRebelliousVassals(), false);
    assertEquals(vassalRebellion3.fiefdomObj.isKingdom(), true);
    
    // Test case with loyal vassals and enough vassals to rebel
    Collection<String> col4 = new ArrayList<>();
    col4.add("Fiefdom5");
    VassalRebellion vassalRebellion4 = new VassalRebellion();
    vassalRebellion4.perform(col4);
    // Check if the rebellious vassals and kingdom flags are correctly set
    assertEquals(vassalRebellion4.fiefdomObj.isRebelliousVassals(), true);
    assertEquals(vassalRebellion4.fiefdomObj.isKingdom(), false);
    
    // Test case with loyal vassals, personal castles, and fiefdom castles
    Collection<String> col5 = new ArrayList<>();
    col5.add("Fiefdom6");
    VassalRebellion vassalRebellion5 = new VassalRebellion();
    vassalRebellion5.perform(col5);
    // Check if the rebellious vassals and kingdom flags are correctly set
    assertEquals(vassalRebellion5.fiefdomObj.isRebelliousVassals(), true);
    assertEquals(vassalRebellion5.fiefdomObj.isKingdom(), false);
  }
}