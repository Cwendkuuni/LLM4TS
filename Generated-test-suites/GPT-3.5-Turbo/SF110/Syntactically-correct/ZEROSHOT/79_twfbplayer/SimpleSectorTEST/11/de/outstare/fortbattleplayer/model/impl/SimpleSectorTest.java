import static org.junit.Assert.*;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    
    @Test
    public void testFree() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Add a combatant to the sector
        sector.gainControl(CombatantSide.DEFENDER);
        
        // Free the sector
        sector.free();
        
        // Check if the occupier and occupierCount are null/0
        assertNull(sector.getOccupier());
        assertEquals(0, sector.occupierCount);
    }
    
    @Test
    public void testOccupierChanged() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Add an observer to the sector
        SectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        
        // Trigger occupierChanged()
        sector.occupierChanged();
        
        // Check if the observer's method was called
        assertTrue(((TestSectorObserver)observer).occupierChangedCalled);
    }
    
    @Test
    public void testGainControl() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Gain control for a combatant side
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Check if the occupier is the same as the side gained control
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testGetOccupier() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Gain control for a combatant side
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Check if the occupier is the same as the side gained control
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Check if the sector is occupied (should be false)
        assertFalse(sector.isOccupied());
        
        // Gain control for a combatant side
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Check if the sector is occupied (should be true)
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Check if the height matches the initialized value
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Check if isDefenderSpawn matches the initialized value
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Check if isAttackerSpawn matches the initialized value
        assertTrue(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Check if isFlag matches the initialized value
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Get the bonus for a character class
        SectorBonus bonus = sector.getBonus(CharacterClass.SOLDIER);
        
        // Check if the attack bonus, defense bonus, and damage bonus are correct
        assertEquals(15, bonus.getAttackBonus());
        assertEquals(25, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Create an area and add it to the sector
        Area area = new TestArea();
        sector._addArea(area);
        
        // Get the areas from the sector
        Set<Area> areas = sector.getAreas();
        
        // Check if the set contains the added area
        assertTrue(areas.contains(area));
    }
    
    @Test
    public void testHashCode() {
        // Create a SimpleSector object
        SimpleSector sector1 = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Create another SimpleSector object with the same parameters
        SimpleSector sector2 = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Check if the hash codes are equal
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Create a SimpleSector object
        SimpleSector sector1 = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Create another SimpleSector object with the same parameters
        SimpleSector sector2 = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Check if the objects are equal
        assertTrue(sector1.equals(sector2));
    }
    
    @Test
    public void testAddObserver() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Add an observer to the sector
        SectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        
        // Check if the observer is present in the observers set
        assertTrue(sector.observers.contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        // Create a SimpleSector object
        SimpleSector sector = new SimpleSector(10, true, true, 10, 20, true, 5, CharacterClass.SOLDIER);
        
        // Add an observer to the sector
        SectorObserver observer = new TestSectorObserver();
        sector.addObserver(observer);
        
        // Remove the observer from the sector
        sector.removeObserver(observer);
        
        // Check if the observer is not present in the observers set
        assertFalse(sector.observers.contains(observer));
    }
    
    // Test helper classes
    
    private class TestSectorObserver implements SectorObserver {
        public boolean occupierChangedCalled = false;
        
        public void occupierChanged() {
            occupierChangedCalled = true;
        }
    }
    
    private class TestArea implements Area {
        public Combatant getOccupier() {
            return null;
        }
        
        public boolean isOccupied() {
            return false;
        }
    }
}