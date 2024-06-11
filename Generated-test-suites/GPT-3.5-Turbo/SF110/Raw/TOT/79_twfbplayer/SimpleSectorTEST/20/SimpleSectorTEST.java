import org.junit.Test;
import static org.junit.Assert.*;
import java.util.*;

public class SimpleSectorTest {

  @Test
  public void testFree() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    sector.free();
    assertFalse(sector.isOccupied());
    assertEquals(sector.getOccupierCount(), 0);
  }

  @Test
  public void testFree_InvalidCount() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    sector.free();
    sector.free(); // try freeing when no combatant is present
    assertFalse(sector.isOccupied());
    assertEquals(sector.getOccupierCount(), 0);
  }

  @Test
  public void testGainControl() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertTrue(sector.isOccupied());
    assertEquals(sector.getOccupier(), CombatantSide.ATTACKER);
    assertEquals(sector.getOccupierCount(), 1);
  }

  @Test
  public void testOccupierChanged() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SectorObserverStub observer = new SectorObserverStub();
    sector.addObserver(observer);
    sector.gainControl(CombatantSide.DEFENDER);
    assertTrue(observer.isOccupierChangedCalled());
  }

  @Test
  public void testGetOccupier() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.DEFENDER);
    assertEquals(sector.getOccupier(), CombatantSide.DEFENDER);
  }

  @Test
  public void testIsOccupied() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isOccupied());
    sector.gainControl(CombatantSide.ATTACKER);
    assertTrue(sector.isOccupied());
  }

  @Test
  public void testGetHeight() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector.getHeight(), 10);
  }

  @Test
  public void testIsDefenderSpawn() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertTrue(sector.isDefenderSpawn());
  }

  @Test
  public void testIsAttackerSpawn() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isAttackerSpawn());
  }

  @Test
  public void testIsFlag() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertTrue(sector.isFlag());
  }

  @Test
  public void testGetBonus() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
    assertNotNull(bonus);
    assertEquals(bonus.getAttackBonus(), 7);
    assertEquals(bonus.getDefenseBonus(), 5);
    assertEquals(bonus.getDamageBonus(), 0);
  }

  @Test
  public void testGetBonus_InvalidClass() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SectorBonus bonus = sector.getBonus(CharacterClass.MAGE); // Invalid class, should return default bonus
    assertNotNull(bonus);
    assertEquals(bonus.getAttackBonus(), 5);
    assertEquals(bonus.getDefenseBonus(), 3);
    assertEquals(bonus.getDamageBonus(), 0);
  }

  @Test
  public void testGetDamageBonus() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector.getDamageBonus(), 0);
  }

  @Test
  public void testGetAttackBonus() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector.getAttackBonus(CharacterClass.WARRIOR), 7);
  }

  @Test
  public void testGetDefenseBonus() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector.getDefenseBonus(CharacterClass.WARRIOR), 5);
  }

  @Test
  public void testClassBonus() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector.classBonus(CharacterClass.WARRIOR), 2);
  }

  @Test
  public void testClassBonus_InvalidClass() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector.classBonus(CharacterClass.MAGE), 0);
  }

  @Test
  public void testGetAreas() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    Set<Area> areas = sector.getAreas();
    assertNotNull(areas);
    assertTrue(areas.isEmpty());
  }

  @Test
  public void test_addArea() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    Area area = new Area();
    sector._addArea(area);
    Set<Area> areas = sector.getAreas();
    assertNotNull(areas);
    assertTrue(areas.contains(area));
  }

  @Test
  public void testHashCode() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector1.hashCode(), sector2.hashCode());
  }

  @Test
  public void testHashCode_DifferentObjects() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(12, false, false, 6, 2, false, 3, CharacterClass.MAGE);
    assertNotEquals(sector1.hashCode(), sector2.hashCode());
  }

  @Test
  public void testEquals() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector1, sector2);
  }

  @Test
  public void testEquals_SameObjects() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector, sector);
  }

  @Test
  public void testEquals_DifferentObjects() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(12, false, false, 6, 2, false, 3, CharacterClass.MAGE);
    assertNotEquals(sector1, sector2);
  }

  @Test
  public void testAddObserver() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    sector.addObserver(new SectorObserverStub());
  }

  @Test
  public void testRemoveObserver() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    SectorObserverStub observer = new SectorObserverStub();
    sector.addObserver(observer);
    sector.removeObserver(observer);
  }

  // Helper class to check if occupierChanged() method is called
  class SectorObserverStub implements SectorObserver {
    private boolean called = false;

    @Override
    public void occupierChanged() {
      this.called = true;
    }

    public boolean isOccupierChangedCalled() {
      return this.called;
    }
  }
}