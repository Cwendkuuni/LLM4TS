import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
  
  private SimpleSector sector;
  
  @Before
  public void setUp() {
      sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
  }
  
  @Test
  public void testFree() {
      // Test when occupier count is more than 0
      sector.gainControl(CombatantSide.DEFENDER);
      sector.free();
      assertEquals(0, sector.getOccupierCount());
      
      // Test when occupier count is 0
      sector.free();
      assertEquals(null, sector.getOccupier());
  }
  
  @Test
  public void testGainControl() {
      // Test gaining control by defender
      sector.gainControl(CombatantSide.DEFENDER);
      assertEquals(1, sector.getOccupierCount());
      assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
      
      // Test changing control from attacker to defender
      sector.gainControl(CombatantSide.ATTACKER);
      sector.gainControl(CombatantSide.DEFENDER);
      assertEquals(1, sector.getOccupierCount());
      assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
  }
  
  // Additional test cases for other public methods can be added here
  
}