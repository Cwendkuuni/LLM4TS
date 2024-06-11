import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
  
  private SimpleSector sector;
  
  @Before
  public void setUp() {
    sector = new SimpleSector(5, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
  }
  
  @Test
  public void testFree() {
    // Test case for free method when the occupier count is greater than zero
    sector.gainControl(CombatantSide.DEFENDER);
    sector.free();
    assertEquals(0, sector.getOccupierCount());
    assertNull(sector.getOccupier());
  }
  
  @Test
  public void testGainControl() {
    // Test case for gainControl method when the occupier is different from the current occupier
    sector.gainControl(CombatantSide.ATTACKER);
    assertEquals(1, sector.getOccupierCount());
    assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
  }
  
  @Test
  public void testGetOccupier() {
    // Test case for getOccupier method when the occupier is null
    assertNull(sector.getOccupier());
  }
  
  @Test
  public void testIsOccupied() {
    // Test case for isOccupied method when the occupier is null
    assertFalse(sector.isOccupied());
  }
  
  @Test
  public void testGetHeight() {
    // Test case for getHeight method
    assertEquals(5, sector.getHeight());
  }
  
  @Test
  public void testIsDefenderSpawn() {
    // Test case for isDefenderSpawn method
    assertTrue(sector.isDefenderSpawn());
  }
  
  @Test
  public void testIsAttackerSpawn() {
    // Test case for isAttackerSpawn method
    assertFalse(sector.isAttackerSpawn());
  }
  
  @Test
  public void testIsFlag() {
    // Test case for isFlag method
    assertTrue(sector.isFlag());
  }
  
  @Test
  public void testGetBonus() {
    // Test case for getBonus method
    CharacterClass charClass = CharacterClass.WARRIOR;
    SectorBonus bonus = sector.getBonus(charClass);
    assertNotNull(bonus);
    assertEquals(4, bonus.getAttackBonus());
    assertEquals(3, bonus.getDefenseBonus());
    assertEquals(0, bonus.getDamageBonus());
  }
  
  @Test
  public void testGetAreas() {
    // Test case for getAreas method
    assertTrue(sector.getAreas().isEmpty());
  }
  
  @Test
  public void testAddArea() {
    // Test case for _addArea method
    Area area = new Area();
    sector._addArea(area);
    assertTrue(sector.getAreas().contains(area));
  }
  
  @Test
  public void testHashCode() {
    // Test case for hashCode method
    SimpleSector other = new SimpleSector(5, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
    assertEquals(sector.hashCode(), other.hashCode());
  }
  
  @Test
  public void testEquals() {
    // Test case for equals method when both objects are equal
    SimpleSector other = new SimpleSector(5, true, false, 3, 2, true, 1, CharacterClass.WARRIOR);
    assertTrue(sector.equals(other));
    
    // Test case for equals method when the other object is null
    assertFalse(sector.equals(null));
    
    // Test case for equals method when the other object is not an instance of SimpleSector
    assertFalse(sector.equals(new String()));
    
    // Test case for equals method when the areas of the current object are null and the areas of the other object are not null
    sector._addArea(new Area());
    assertFalse(sector.equals(other));
    
    // Test case for equals method when the areas of the current object are not null and the areas of the other object are null
    assertTrue(other.equals(new SimpleSector(5, true, false, 3, 2, true, 1, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the areas of both objects are not null but are not equal
    assertFalse(sector.equals(new SimpleSector(5, true, false, 3, 2, true, 1, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the attackBonus of the current object is different from the attackBonus of the other object
    assertFalse(sector.equals(new SimpleSector(5, true, false, 2, 2, true, 1, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the attackerSpawn of the current object is different from the attackerSpawn of the other object
    assertFalse(sector.equals(new SimpleSector(5, false, false, 3, 2, true, 1, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the classBonus of the current object is different from the classBonus of the other object
    assertFalse(sector.equals(new SimpleSector(5, true, false, 3, 2, true, 2, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the classType of the current object is different from the classType of the other object
    assertFalse(sector.equals(new SimpleSector(5, true, false, 3, 2, true, 1, CharacterClass.MAGE)));
    
    // Test case for equals method when the defendBonus of the current object is different from the defendBonus of the other object
    assertFalse(sector.equals(new SimpleSector(5, true, false, 3, 1, true, 1, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the defenderSpawn of the current object is different from the defenderSpawn of the other object
    assertFalse(sector.equals(new SimpleSector(5, true, true, 3, 2, true, 1, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the flag of the current object is different from the flag of the other object
    assertFalse(sector.equals(new SimpleSector(5, true, false, 3, 2, false, 1, CharacterClass.WARRIOR)));
    
    // Test case for equals method when the height of the current object is different from the height of the other object
    assertFalse(sector.equals(new SimpleSector(4, true, false, 3, 2, true, 1, CharacterClass.WARRIOR)));
  }
  
  @Test
  public void testAddObserver() {
    // Test case for addObserver method
    SectorObserver observer = new SectorObserver();
    sector.addObserver(observer);
    assertTrue(sector.getObservers().contains(observer));
  }
  
  @Test
  public void testRemoveObserver() {
    // Test case for removeObserver method
    SectorObserver observer = new SectorObserver();
    sector.addObserver(observer);
    sector.removeObserver(observer);
    assertFalse(sector.getObservers().contains(observer));
  }
}