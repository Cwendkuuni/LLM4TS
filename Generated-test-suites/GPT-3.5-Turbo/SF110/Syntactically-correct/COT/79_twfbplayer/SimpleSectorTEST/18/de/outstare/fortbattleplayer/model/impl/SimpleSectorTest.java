import de.outstare.fortbattleplayer.model.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class SimpleSectorTest {

    

    @Test
    public void testFree() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Test the initial state of the occupierCount
        assertEquals(0, sector.occupierCount);
        
        // Call the free() method
        sector.free();
        
        // Check if the occupierCount has decreased by 1
        assertEquals(-1, sector.occupierCount);
        
        // Check if the occupier has been set to null
        assertNull(sector.occupier);
    }

    @Test
    public void testGainControl() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Test the initial state of the occupierCount
        assertEquals(0, sector.occupierCount);
        
        // Call the gainControl() method
        sector.gainControl(CombatantSide.DEFENDER);
        
        // Check if the occupierCount has increased by 1
        assertEquals(1, sector.occupierCount);
        
        // Check if the occupier has been set to DEFENDER
        assertEquals(CombatantSide.DEFENDER, sector.occupier);
    }

    @Test
    public void testGetOccupier() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Test the initial state of the occupier
        assertNull(sector.occupier);
        
        // Call the gainControl() method to set the occupier
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Check if the getOccupier() method returns the correct occupier
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Test the initial state of the isOccupied() method
        assertFalse(sector.isOccupied());
        
        // Call the gainControl() method to set the occupier
        sector.gainControl(CombatantSide.DEFENDER);
        
        // Check if the isOccupied() method returns true
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Check if the getHeight() method returns the correct height
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Check if the isDefenderSpawn() method returns the correct value
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Check if the isAttackerSpawn() method returns the correct value
        assertTrue(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Check if the isFlag() method returns the correct value
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Call the getBonus() method
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        
        // Check if the getAttackBonus(), getDefenseBonus(), and getDamageBonus() methods return the correct values
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Test the initial state of the areas set
        assertTrue(sector.getAreas().isEmpty());
        
        // Create an Area object
        Area area = new Area();
        
        // Call the _addArea() method to add the area to the areas set
        sector._addArea(area);
        
        // Check if the getAreas() method returns the correct set of areas
        assertEquals(1, sector.getAreas().size());
        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        // Create two SimpleSector objects with the same attributes
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Check if the hashCode() method returns the same value for both objects
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        // Create two SimpleSector objects with the same attributes
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Check if the equals() method returns true for both objects
        assertTrue(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Create a mock observer object
        SectorObserverMock observer = new SectorObserverMock();
        
        // Call the addObserver() method to add the observer
        sector.addObserver(observer);
        
        // Check if the observer has been added to the observers set
        assertTrue(sector.observers.contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Create a mock observer object
        SectorObserverMock observer = new SectorObserverMock();
        
        // Add the observer to the observers set
        sector.observers.add(observer);
        
        // Call the removeObserver() method to remove the observer
        sector.removeObserver(observer);
        
        // Check if the observer has been removed from the observers set
        assertFalse(sector.observers.contains(observer));
    }

    
}