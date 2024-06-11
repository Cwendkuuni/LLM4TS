import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.*;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 5, true, 10, CharacterClass.WARRIOR);
    }
    
    // Test case for free() method
    @Test
    public void testFree() {
        // Add a combatant to the sector
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        
        // Assert that the occupier count is 0
        assertEquals(0, sector.occupierCount);
        
        // Assert that the occupier is null
        assertNull(sector.getOccupier());
    }
    
    // Test case for gainControl() method
    @Test
    public void testGainControl() {
        // Gain control of the sector for attackers
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Assert that the occupier count is 1
        assertEquals(1, sector.occupierCount);
        
        // Assert that the occupier is attackers
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    // Test case for getOccupier() method
    @Test
    public void testGetOccupier() {
        // Gain control of the sector for defenders
        sector.gainControl(CombatantSide.DEFENDER);
        
        // Assert that the occupier is defenders
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    // Test case for isOccupied() method
    @Test
    public void testIsOccupied() {
        // Assert that the sector is not occupied initially
        assertFalse(sector.isOccupied());
        
        // Gain control of the sector for attackers
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Assert that the sector is occupied
        assertTrue(sector.isOccupied());
    }
    
    // Test case for getHeight() method
    @Test
    public void testGetHeight() {
        // Assert that the height is 10
        assertEquals(10, sector.getHeight());
    }
    
    // Test case for isDefenderSpawn() method
    @Test
    public void testIsDefenderSpawn() {
        // Assert that the sector is not a defender spawn initially
        assertFalse(sector.isDefenderSpawn());
        
        // Create a new sector as defender spawn
        SimpleSector defenderSpawnSector = new SimpleSector(5, true, false, 5, 5, true, 10, CharacterClass.MAGE);
        
        // Assert that the new sector is a defender spawn
        assertTrue(defenderSpawnSector.isDefenderSpawn());
    }
    
    // Test case for isAttackerSpawn() method
    @Test
    public void testIsAttackerSpawn() {
        // Assert that the sector is not an attacker spawn initially
        assertFalse(sector.isAttackerSpawn());
        
        // Create a new sector as attacker spawn
        SimpleSector attackerSpawnSector = new SimpleSector(8, false, true, 5, 5, true, 10, CharacterClass.WARRIOR);
        
        // Assert that the new sector is an attacker spawn
        assertTrue(attackerSpawnSector.isAttackerSpawn());
    }
    
    // Test case for isFlag() method
    @Test
    public void testIsFlag() {
        // Assert that the sector has a flag
        assertTrue(sector.isFlag());
        
        // Create a new sector without a flag
        SimpleSector noFlagSector = new SimpleSector(7, true, false, 5, 5, false, 10, CharacterClass.ROGUE);
        
        // Assert that the new sector does not have a flag
        assertFalse(noFlagSector.isFlag());
    }
    
    // Test case for getBonus() method
    @Test
    public void testGetBonus() {
        // Create a new character class with required attack and defense bonuses
        CharacterClass charClass = new CharacterClass(15, 20);
        
        // Get the sector bonus for the character class
        SectorBonus bonus = sector.getBonus(charClass);
        
        // Assert that the attack bonus is correct
        assertEquals(15, bonus.getAttackBonus());
        
        // Assert that the defense bonus is correct
        assertEquals(20, bonus.getDefenseBonus());
    }
    
    // Test case for getAreas() method
    @Test
    public void testGetAreas() {
        // Assert that the number of areas is initially 0
        assertEquals(0, sector.getAreas().size());
        
        // Create two new areas and add them to the sector
        Area area1 = new Area();
        Area area2 = new Area();
        sector._addArea(area1);
        sector._addArea(area2);
        
        // Assert that the number of areas is now 2
        assertEquals(2, sector.getAreas().size());
        
        // Assert that both areas are present in the set of areas
        assertTrue(sector.getAreas().contains(area1));
        assertTrue(sector.getAreas().contains(area2));
    }
    
    // Test case for addObserver() and removeObserver() methods
    @Test
    public void testAddAndRemoveObserver() {
        // Create a new observer
        SectorObserver observer = new SimpleSectorObserver();
        
        // Add the observer to the sector
        sector.addObserver(observer);
        
        // Assert that the observer is added successfully
        assertTrue(sector.observers.contains(observer));
        
        // Remove the observer from the sector
        sector.removeObserver(observer);
        
        // Assert that the observer is removed successfully
        assertFalse(sector.observers.contains(observer));
    }
    
    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        // Create a new sector with the same attributes
        SimpleSector sameSector = new SimpleSector(10, true, false, 5, 5, true, 10, CharacterClass.WARRIOR);
        
        // Assert that the hash codes are equal
        assertEquals(sector.hashCode(), sameSector.hashCode());
    }
    
    // Test case for equals() method
    @Test
    public void testEquals() {
        // Create a new sector with the same attributes
        SimpleSector sameSector = new SimpleSector(10, true, false, 5, 5, true, 10, CharacterClass.WARRIOR);
        
        // Assert that the sectors are equal
        assertEquals(sector, sameSector);
        
        // Create a new sector with different attributes
        SimpleSector differentSector = new SimpleSector(8, false, true, 5, 5, true, 10, CharacterClass.WARRIOR);
        
        // Assert that the sectors are not equal
        assertNotEquals(sector, differentSector);
    }
}