import org.junit.*;
import static org.junit.Assert.*;

public class SimpleSectorTest {
    
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(1, true, false, 2, 3, false, 4, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
    }
    
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(2, false, true, 3, 4, true, 5, CharacterClass.MAGE);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(3, true, true, 4, 5, true, 6, CharacterClass.ARCHER);
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(4, false, true, 5, 6, false, 7, CharacterClass.KNIGHT);
        assertEquals(4, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 6, 7, true, 8, CharacterClass.ROGUE);
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(6, true, true, 7, 8, false, 9, CharacterClass.WARRIOR);
        assertTrue(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(7, false, false, 8, 9, true, 10, CharacterClass.ARCHER);
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(8, true, false, 9, 10, false, 11, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
        assertEquals(9, bonus.getAttackBonus());
        assertEquals(10, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(9, true, true, 10, 11, true, 12, CharacterClass.MAGE);
        assertNotNull(sector.getAreas());
        assertTrue(sector.getAreas().isEmpty());
        
        Area area1 = new Area();
        Area area2 = new Area();
        sector._addArea(area1);
        sector._addArea(area2);
        assertEquals(2, sector.getAreas().size());
        assertTrue(sector.getAreas().contains(area1));
        assertTrue(sector.getAreas().contains(area2));
    }
    
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 11, 12, false, 13, CharacterClass.KNIGHT);
        SimpleSector sector2 = new SimpleSector(11, true, false, 11, 12, false, 13, CharacterClass.KNIGHT);
        SimpleSector sector3 = new SimpleSector(10, true, false, 11, 12, false, 13, CharacterClass.ROGUE);
        
        assertNotEquals(sector1, sector2);
        assertNotEquals(sector2, sector3);
        assertEquals(sector1, sector3);
    }
    
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(12, false, true, 13, 14, true, 15, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(12, false, true, 13, 14, true, 15, CharacterClass.MAGE);
        
        assertNotEquals(sector1.hashCode(), sector2.hashCode());
        assertEquals(sector1.hashCode(), sector1.hashCode());
    }
    
    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(13, true, true, 14, 15, false, 16, CharacterClass.ARCHER);
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        
        // Assert that observer is added to the set of observers
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(14, true, true, 15, 16, true, 17, CharacterClass.KNIGHT);
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        
        // Assert that observer is added to the set of observers
        assertTrue(sector.getObservers().contains(observer));
        
        sector.removeObserver(observer);
        
        // Assert that observer is removed from the set of observers
        assertFalse(sector.getObservers().contains(observer));
    }
    
    private class SectorObserverImpl implements SectorObserver {
        // Implement the methods of the SectorObserver interface
    }
}