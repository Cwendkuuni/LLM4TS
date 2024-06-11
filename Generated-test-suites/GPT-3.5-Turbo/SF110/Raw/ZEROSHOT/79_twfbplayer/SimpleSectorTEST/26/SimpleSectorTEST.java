import org.junit.*;
import static org.junit.Assert.*;
import de.outstare.fortbattleplayer.model.*;
import java.util.*;

public class SimpleSectorTest {
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Initialize occupier and occupierCount
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);
        
        // Test that occupierCount decreases when free() is called
        sector.free();
        assertEquals(2, sector.getOccupierCount());     
    }
    
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Test that occupier is set when gainControl() is called
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Test that isOccupied() returns false when no occupier is set
        assertFalse(sector.isOccupied());
        
        // Test that isOccupied() returns true when an occupier is set
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Test getHeight() returns the correct height value
        assertEquals(5, sector.getHeight());   
    }
    
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Test isDefenderSpawn() returns the correct boolean value
        assertTrue(sector.isDefenderSpawn());   
    }
    
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Test isAttackerSpawn() returns the correct boolean value
        assertFalse(sector.isAttackerSpawn());   
    }
    
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Test isFlag() returns the correct boolean value
        assertTrue(sector.isFlag());   
    }
    
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        
        // Test that getBonus() returns a non-null value
        assertNotNull(sector.getBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        Area area1 = new Area();
        Area area2 = new Area();
        
        // Test that getAreas() returns a set containing the added areas
        sector._addArea(area1);
        sector._addArea(area2);
        Set<Area> areas = sector.getAreas();
        assertTrue(areas.contains(area1));
        assertTrue(areas.contains(area2));
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(10, true, true, 5, 2, false, 1, CharacterClass.MAGE);
        
        // Test that sectors with same attributes have the same hash code
        assertEquals(sector1.hashCode(), sector2.hashCode());
        
        // Test that sectors with different attributes have different hash codes
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(10, true, true, 5, 2, false, 1, CharacterClass.MAGE);
        
        // Test that sectors with same attributes are equal
        assertEquals(sector1, sector2);
        
        // Test that sectors with different attributes are not equal
        assertNotEquals(sector1, sector3);
    }
    
    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        MockObserver observer = new MockObserver();
        
        // Test that addObserver() adds the observer to the observers set
        sector.addObserver(observer);
        assertTrue(sector.observers.contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
        MockObserver observer = new MockObserver();
        sector.addObserver(observer);
        
        // Test that removeObserver() removes the observer from the observers set
        sector.removeObserver(observer);
        assertFalse(sector.observers.contains(observer));
    }
    
    // Helper class for testing
    private class MockObserver implements SectorObserver {
        public void occupierChanged() {
            // Do nothing
        }
    }
}