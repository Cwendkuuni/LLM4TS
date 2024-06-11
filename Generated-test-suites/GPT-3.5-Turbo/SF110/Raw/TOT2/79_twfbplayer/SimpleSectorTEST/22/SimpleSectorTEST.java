import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
  
  private SimpleSector simpleSector;
  
  @Before
  public void setUp() {
    simpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
  }
  
  @Test
  public void testFree() {
    // Testing when occupierCount is greater than 0
    simpleSector.gainControl(CombatantSide.DEFENDER);
    simpleSector.free();
    assertEquals(0, simpleSector.getOccupierCount());
    
    // Testing when occupierCount is 0
    simpleSector.free();
    assertEquals(0, simpleSector.getOccupierCount());
  }
  
  @Test
  public void testGainControl() {
    simpleSector.gainControl(CombatantSide.ATTACKER);
    assertEquals(1, simpleSector.getOccupierCount());
    assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
    
    // Testing when the same side gains control
    simpleSector.gainControl(CombatantSide.ATTACKER);
    assertEquals(1, simpleSector.getOccupierCount());
    
  }
  
  @Test
  public void testIsOccupied() {
    assertFalse(simpleSector.isOccupied());
    
    simpleSector.gainControl(CombatantSide.ATTACKER);
    assertTrue(simpleSector.isOccupied());
  }
  
  @Test
  public void testGetBonus() {
    SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
    assertNotNull(bonus);
    assertEquals(7, bonus.getAttackBonus());
    assertEquals(5, bonus.getDefenseBonus());
    assertEquals(0, bonus.getDamageBonus());
  }
  
  // Other test cases for remaining methods
  
}