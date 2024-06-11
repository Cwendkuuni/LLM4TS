import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SimpleSectorTest {

  @Test
  public void testFree() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.DEFENDER);
    sector.free();
    assertFalse(sector.isOccupied());
  }
  
  @Test
  public void testGainControl() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertTrue(sector.isOccupied());
    assertEquals(sector.getOccupier(), CombatantSide.ATTACKER);
  }
  
  @Test
  public void testGetOccupier() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(sector.getOccupier(), CombatantSide.ATTACKER);
  }
  
  @Test
  public void testIsOccupied() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.DEFENDER);
    assertTrue(sector.isOccupied());
  }
  
  @Test
  public void testGetHeight() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    assertEquals(sector.getHeight(), 5);
  }
  
  @Test
  public void testIsDefenderSpawn() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    assertTrue(sector.isDefenderSpawn());
  }
  
  @Test
  public void testIsAttackerSpawn() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    assertTrue(sector.isAttackerSpawn());
  }
  
  @Test
  public void testIsFlag() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    assertTrue(sector.isFlag());
  }
  
  @Test
  public void testGetBonus() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
    assertNotNull(bonus);
    assertEquals(bonus.getAttackBonus(), 13);
    assertEquals(bonus.getDefenseBonus(), 8);
    assertEquals(bonus.getDamageBonus(), 0);
  }
  
  @Test
  public void testGetAreas() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    Set<Area> areas = sector.getAreas();
    assertNotNull(areas);
    assertTrue(areas.isEmpty());
  }
  
  @Test
  public void test_addArea() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    Area area = new Area();
    sector._addArea(area);
    Set<Area> areas = sector.getAreas();
    assertTrue(areas.contains(area));
  }
  
  @Test
  public void testEquals() {
    SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    assertTrue(sector1.equals(sector2));
  }
  
  @Test
  public void testNotEquals() {
    SimpleSector sector1 = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.MAGE);
    assertFalse(sector1.equals(sector2));
  }
  
  @Test
  public void testAddObserver() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    SectorObserver observer = new SectorObserver();
    sector.addObserver(observer);
    assertTrue(sector.getObservers().contains(observer));
  }
  
  @Test
  public void testRemoveObserver() {
    SimpleSector sector = new SimpleSector(5, true, true, 10, 5, true, 3, CharacterClass.WARRIOR);
    SectorObserver observer = new SectorObserver();
    sector.addObserver(observer);
    sector.removeObserver(observer);
    assertFalse(sector.getObservers().contains(observer));
  }
}