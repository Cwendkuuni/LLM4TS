import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleSectorTest {
  
  @Test
  public void testFree() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    sector.gainControl(CombatantSide.DEFENDER);
    assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    sector.free();
    assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    sector.free();
    assertNull(sector.getOccupier());
  }
  
  @Test
  public void testGainControl() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
  }
  
  @Test
  public void testGetOccupier() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
  }
  
  @Test
  public void testIsOccupied() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isOccupied());
    sector.gainControl(CombatantSide.ATTACKER);
    assertTrue(sector.isOccupied());
  }
  
  @Test
  public void testGetHeight() {
    SimpleSector sector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);
    assertEquals(10, sector.getHeight());
  }
  
  @Test
  public void testIsDefenderSpawn() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertTrue(sector.isDefenderSpawn());
    sector = new SimpleSector(1, false, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isDefenderSpawn());
  }
  
  @Test
  public void testIsAttackerSpawn() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertTrue(sector.isAttackerSpawn());
    sector = new SimpleSector(1, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isAttackerSpawn());
  }
  
  @Test
  public void testIsFlag() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertTrue(sector.isFlag());
    sector = new SimpleSector(1, true, true, 10, 5, false, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isFlag());
  }
  
  @Test
  public void testGetBonus() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
    assertEquals(12, bonus.getAttackBonus());
    assertEquals(7, bonus.getDefenseBonus());
    assertEquals(0, bonus.getDamageBonus());
  }
  
  @Test
  public void testGetAreas() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertEquals(0, sector.getAreas().size());
    sector._addArea(new Area());
    assertEquals(1, sector.getAreas().size());
  }
  
  @Test
  public void testHashCode() {
    SimpleSector sector1 = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertEquals(sector1.hashCode(), sector2.hashCode());
  }
  
  @Test
  public void testEquals() {
    SimpleSector sector1 = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    assertTrue(sector1.equals(sector2));
    assertFalse(sector1.equals(null));
    assertFalse(sector1.equals(new Object()));
  }
  
  @Test
  public void testAddObserver() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    sector.addObserver(new SectorObserver() {
      public void occupierChanged() { }
    });
  }
  
  @Test
  public void testRemoveObserver() {
    SimpleSector sector = new SimpleSector(1, true, true, 10, 5, true, 2, CharacterClass.WARRIOR);
    SectorObserver observer = new SectorObserver() {
      public void occupierChanged() { }
    };
    sector.addObserver(observer);
    sector.removeObserver(observer);
  }
}