import static org.junit.Assert.*;
import org.junit.*;

public class SimpleSectorTest {
  
  @Test
  public void testFree() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    sector.free();
    assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    assertEquals(0, sector.getOccupierCount());
  }
  
  @Test
  public void testGainControl() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    assertEquals(1, sector.getOccupierCount());
  }
  
  @Test
  public void testGetOccupier() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
  }
  
  @Test
  public void testIsOccupied() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertTrue(sector.isOccupied());
  }
  
  @Test
  public void testGetHeight() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    assertEquals(10, sector.getHeight());
  }
  
  @Test
  public void testIsDefenderSpawn() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    assertTrue(sector.isDefenderSpawn());
  }
  
  @Test
  public void testIsAttackerSpawn() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    assertFalse(sector.isAttackerSpawn());
  }
  
  @Test
  public void testIsFlag() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    assertTrue(sector.isFlag());
  }
  
  @Test
  public void testGetBonus() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
    assertEquals(3, bonus.getDefenseBonus());
    assertEquals(2, bonus.getAttackBonus());
  }
  
  @Test
  public void testGetAreas() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    assertNotNull(sector.getAreas());
  }
  
  @Test
  public void test_addArea() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    Area area = new Area();
    sector._addArea(area);
    assertTrue(sector.getAreas().contains(area));
  }
  
  @Test
  public void testHashCode() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    
    assertEquals(sector1.hashCode(), sector2.hashCode());
  }
  
  @Test
  public void testEquals() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    SimpleSector sector3 = new SimpleSector(10, false, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    
    assertTrue(sector1.equals(sector2));
    assertFalse(sector1.equals(sector3));
  }
  
  @Test
  public void testAddObserver() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    SectorObserver observer = new SectorObserver();
    sector.addObserver(observer);
    assertTrue(sector.getObservers().contains(observer));
  }
  
  @Test
  public void testRemoveObserver() {
    SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 1, CharacterClass.WARRIOR);
    SectorObserver observer = new SectorObserver();
    sector.addObserver(observer);
    sector.removeObserver(observer);
    assertFalse(sector.getObservers().contains(observer));
  }

}