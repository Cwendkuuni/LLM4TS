import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {
    
    @Test
    public void testFree() {
        // Test case for free method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Initial occupier count should be 0
        assertEquals(0, sector.getOccupierCount());
        
        // Free method should not have any effect if occupier count is already 0
        sector.free();
        
        // After freeing, occupier count should remain 0
        assertEquals(0, sector.getOccupierCount());
        
        // Add an occupier
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Occupier count should be 1
        assertEquals(1, sector.getOccupierCount());
        
        // Free method should decrement the occupier count
        sector.free();
        
        // After freeing, occupier count should be 0
        assertEquals(0, sector.getOccupierCount());
    }
    
    @Test
    public void testGainControl() {
        // Test case for gainControl method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Initial occupier count should be 0
        assertEquals(0, sector.getOccupierCount());
        
        // Occupier should be null
        assertNull(sector.getOccupier());
        
        // Add an occupier
        sector.gainControl(CombatantSide.DEFENDER);
        
        // Occupier count should be 1
        assertEquals(1, sector.getOccupierCount());
        
        // Occupier should be DEFENDER
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        
        // Add another occupier
        sector.gainControl(CombatantSide.ATTACKER);
        
        // Occupier count should be 2
        assertEquals(2, sector.getOccupierCount());
        
        // Occupier should still be DEFENDER
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        // Test case for isOccupied method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Initially, the sector should not be occupied
        assertFalse(sector.isOccupied());
        
        // Add an occupier
        sector.gainControl(CombatantSide.ATTACKER);
        
        // The sector should now be occupied
        assertTrue(sector.isOccupied());
        
        // Free the sector
        sector.free();
        
        // The sector should not be occupied now
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        // Test case for getHeight method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // The height should be 10
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        // Test case for isDefenderSpawn method
        
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, false, true, 5, 2, false, 1, CharacterClass.ARCHER);
        
        // The first sector should be defender spawn
        assertTrue(sector1.isDefenderSpawn());
        
        // The second sector should not be defender spawn
        assertFalse(sector2.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        // Test case for isAttackerSpawn method
        
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, false, true, 5, 2, false, 1, CharacterClass.ARCHER);
        
        // The first sector should be attacker spawn
        assertTrue(sector1.isAttackerSpawn());
        
        // The second sector should be attacker spawn
        assertTrue(sector2.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        // Test case for isFlag method
        
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, false, true, 5, 2, false, 1, CharacterClass.ARCHER);
        
        // The first sector should have a flag
        assertTrue(sector1.isFlag());
        
        // The second sector should not have a flag
        assertFalse(sector2.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        // Test case for getBonus method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // The bonus should be (5 + 2, 3 + 2, 0)
        assertEquals(new SectorBonus(7, 5, 0), sector.getBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetAreas() {
        // Test case for getAreas method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Initially, there should be no areas
        assertEquals(new HashSet<Area>(), sector.getAreas());
        
        // Add an area
        Area area = new Area();
        sector._addArea(area);
        
        // The set of areas should contain the added area
        assertEquals(new HashSet<Area>(Arrays.asList(area)), sector.getAreas());
    }
    
    @Test
    public void testHashCode() {
        // Test case for hashCode method
        
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // The hash codes of two identical sectors should be equal
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }
    
    @Test
    public void testEquals() {
        // Test case for equals method
        
        SimpleSector sector1 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(10, false, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Two identical sectors should be equal
        assertTrue(sector1.equals(sector2));
        
        // Two different sectors should not be equal
        assertFalse(sector1.equals(sector3));
    }
    
    @Test
    public void testAddObserver() {
        // Test case for addObserver method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // The set of observers should initially be empty
        assertEquals(new HashSet<SectorObserver>(), sector.getObservers());
        
        // Add an observer
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        
        // The set of observers should contain the added observer
        assertEquals(new HashSet<SectorObserver>(Arrays.asList(observer)), sector.getObservers());
    }
    
    @Test
    public void testRemoveObserver() {
        // Test case for removeObserver method
        
        SimpleSector sector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        
        // Add two observers
        SectorObserver observer1 = new SectorObserver();
        SectorObserver observer2 = new SectorObserver();
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        
        // The set of observers should contain both observers
        assertEquals(new HashSet<SectorObserver>(Arrays.asList(observer1, observer2)), sector.getObservers());
        
        // Remove one observer
        sector.removeObserver(observer1);
        
        // The set of observers should contain the remaining observer
        assertEquals(new HashSet<SectorObserver>(Arrays.asList(observer2)), sector.getObservers());
    }
    
}