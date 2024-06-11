
import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleSectorTest {
  
  @Test
  public void testFree() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertEquals(0, sector.occupierCount);
    assertFalse(sector.isOccupied());
    
    // Occupying the sector once
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(1, sector.occupierCount);
    assertTrue(sector.isOccupied());
    
    // Freeing the sector
    sector.free();
    assertEquals(0, sector.occupierCount);
    assertFalse(sector.isOccupied());
  }
  
  @Test
  public void testGainControl() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertNull(sector.occupier);
    assertEquals(0, sector.occupierCount);
    
    // Occupying the sector once
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(1, sector.occupierCount);
    assertEquals(CombatantSide.ATTACKER, sector.occupier);
    
    // Re-occupying the sector
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(2, sector.occupierCount);
    assertEquals(CombatantSide.ATTACKER, sector.occupier);
  }
  
  @Test
  public void testGetOccupier() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertNull(sector.getOccupier());
    
    // Occupying the sector
    sector.gainControl(CombatantSide.DEFENDER);
    assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
  }
  
  @Test
  public void testIsOccupied() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isOccupied());
    
    // Occupying the sector
    sector.gainControl(CombatantSide.ATTACKER);
    assertTrue(sector.isOccupied());
  }
  
  @Test
  public void testGetHeight() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertEquals(10, sector.getHeight());
  }
  
  @Test
  public void testIsDefenderSpawn() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertTrue(sector.isDefenderSpawn());
  }
  
  @Test
  public void testIsAttackerSpawn() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertFalse(sector.isAttackerSpawn());
  }
  
  @Test
  public void testIsFlag() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    assertTrue(sector.isFlag());
  }
  
  @Test
  public void testGetBonus() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
    assertNotNull(bonus);
  }
  
  @Test
  public void testGetAreas() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertNotNull(sector.getAreas());
    assertEquals(0, sector.getAreas().size());
  }
  
  @Test
  public void test_addArea() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    Area area = new Area();
    sector._addArea(area);
    assertTrue(sector.getAreas().contains(area));
  }
  
  @Test
  public void testHashCode() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertEquals(sector1.hashCode(), sector2.hashCode());
  }
  
  @Test
  public void testEquals() {
    SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    assertTrue(sector1.equals(sector2));
  }
  
  @Test
  public void testAddObserver() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    SectorObserver observer = new SectorObserver() {
      public void occupierChanged() {
        // do nothing
      }
    };
    sector.addObserver(observer);
    assertTrue(sector.observers.contains(observer));
  }
  
  @Test
  public void testRemoveObserver() {
    SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    SectorObserver observer = new SectorObserver() {
      public void occupierChanged() {
        // do nothing
      }
    };
    sector.addObserver(observer);
    sector.removeObserver(observer);
    assertFalse(sector.observers.contains(observer));
  }
  
}
