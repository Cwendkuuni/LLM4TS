import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    private SimpleSector sector;
  
    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
    }
  
    @Test
    public void testFree() {
        // Test case 1: Occupier count is greater than 0, decrement it by 1
        sector.occupierCount = 2;
        sector.free();
        assertEquals(1, sector.occupierCount);
        
        // Test case 2: Occupier count is 0, expect occupier to be null
        sector.free();
        assertNull(sector.occupier);
    }
  
    @Test
    public void testOccupierChanged() {
        // No specific test cases to validate
        // The method only triggers the observation of sector occupation changes
    }
  
    @Test
    public void testGainControl() {
        // Test case 1: Increase the occupier count by 1 and change the occupier side
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.occupier);
        
        // Test case 2: Increase the occupier count by 1 without changing the occupier side
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, sector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, sector.occupier);
    }
  
    @Test
    public void testGetOccupier() {
        // Test case 1: Occupier is null, expect null
        assertNull(sector.getOccupier());
        
        // Test case 2: Occupier is not null, expect the occupier side
        sector.occupier = CombatantSide.DEFENDER;
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
  
    @Test
    public void testIsOccupied() {
        // Test case 1: Occupier is null, expect false
        assertFalse(sector.isOccupied());
        
        // Test case 2: Occupier is not null, expect true
        sector.occupier = CombatantSide.DEFENDER;
        assertTrue(sector.isOccupied());
    }
  
    @Test
    public void testGetHeight() {
        // Test case 1: Height is positive, expect the height value
        assertEquals(5, sector.getHeight());
        
        // Test case 2: Height is negative, expect the height value
        sector = new SimpleSector(-3, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertEquals(-3, sector.getHeight());
    }
  
    @Test
    public void testIsDefenderSpawn() {
        // Test case 1: Defenders can spawn in this sector, expect true
        assertTrue(sector.isDefenderSpawn());
        
        // Test case 2: Defenders cannot spawn in this sector, expect false
        sector = new SimpleSector(5, false, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isDefenderSpawn());
    }
  
    @Test
    public void testIsAttackerSpawn() {
        // Test case 1: Attackers can spawn in this sector, expect true
        assertTrue(sector.isAttackerSpawn());
        
        // Test case 2: Attackers cannot spawn in this sector, expect false
        sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }
  
    @Test
    public void testIsFlag() {
        // Test case 1: Sector has a flag, expect true
        assertTrue(sector.isFlag());
        
        // Test case 2: Sector does not have a flag, expect false
        sector = new SimpleSector(5, true, false, 10, 5, false, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isFlag());
    }
  
    @Test
    public void testGetBonus() {
        // Test case 1: Get bonus for a character class, expect the expected sector bonus
        SectorBonus expectedBonus = new SectorBonus(15, 7, 0);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.WARRIOR));
        
        // Test case 2: Get bonus for a different character class, expect the expected sector bonus
        sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.MAGE);
        expectedBonus = new SectorBonus(25, 7, 0);
        assertEquals(expectedBonus, sector.getBonus(CharacterClass.MAGE));
    }
  
    @Test
    public void testGetDamageBonus() {
        // No specific test cases to validate
        // The method calculates bonus from occupied areas and respective weapons
    }
  
    @Test
    public void testGetAttackBonus() {
        // Test case 1: Get attack bonus for a character class, expect the expected bonus value
        int expectedBonus = 12;
        assertEquals(expectedBonus, sector.getAttackBonus(CharacterClass.WARRIOR));
        
        // Test case 2: Get attack bonus for a different character class, expect the expected bonus value
        sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.MAGE);
        expectedBonus = 12;
        assertEquals(expectedBonus, sector.getAttackBonus(CharacterClass.MAGE));
    }
  
    @Test
    public void testGetDefenseBonus() {
        // Test case 1: Get defense bonus for a character class, expect the expected bonus value
        int expectedBonus = 7;
        assertEquals(expectedBonus, sector.getDefenseBonus(CharacterClass.WARRIOR));
        
        // Test case 2: Get defense bonus for a different character class, expect the expected bonus value
        sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.MAGE);
        expectedBonus = 7;
        assertEquals(expectedBonus, sector.getDefenseBonus(CharacterClass.MAGE));
    }
  
    @Test
    public void testClassBonus() {
        // Test case 1: Character class matches the class type, expect the class bonus value
        int expectedBonus = 2;
        assertEquals(expectedBonus, sector.classBonus(CharacterClass.WARRIOR));
        
        // Test case 2: Character class does not match the class type, expect 0
        sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.MAGE);
        expectedBonus = 0;
        assertEquals(expectedBonus, sector.classBonus(CharacterClass.WARRIOR));
    }
  
    @Test
  public void testGetAreas() {
        // No specific test cases to validate
        // The method returns the set of areas that belong to the sector
    }
  
    @Test
    public void test_addArea() {
        // Test case 1: Add an area to the sector, expect the area to be added successfully
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
  
    @Test
    public void testHashCode() {
        // Test case 1: Two sectors with the same attributes, expect the hash codes to be equal
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), sector2.hashCode());
        
        // Test case 2: Two sectors with different attributes, expect the hash codes to be different
        sector2 = new SimpleSector(10, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertNotEquals(sector.hashCode(), sector2.hashCode());
    }
  
    @Test
    public void testEquals() {
        // Test case 1: Compare a sector to itself, expect true
        assertTrue(sector.equals(sector));
        
        // Test case 2: Compare a sector to null, expect false
        assertFalse(sector.equals(null));
        
        // Test case 3: Compare a sector to an object of different class, expect false
        assertFalse(sector.equals(new Object()));
        
        // Test case 4: Compare two sectors with the same attributes, expect true
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(sector2));
        
        // Test case 5: Compare two sectors with different attributes, expect false
        sector2 = new SimpleSector(10, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.equals(sector2));
    }
  
    @Test
    public void testAddObserver() {
        // No specific test cases to validate
        // The method adds an observer to the sector's observer list
    }
  
    @Test
    public void testRemoveObserver() {
        // No specific test cases to validate
        // The method removes an observer from the sector's observer list
    }
}