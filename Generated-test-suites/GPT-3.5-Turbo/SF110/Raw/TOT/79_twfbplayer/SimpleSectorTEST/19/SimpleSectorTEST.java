import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleSectorTest {

    // Test case for free() method
    @Test
    public void testFree() {
        // Test when occupierCount is greater than 0
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 5, true, 3, CharacterClass.WARRIOR);
        sector1.gainControl(CombatantSide.ATTACKER);
        sector1.free();
        assertFalse(sector1.isOccupied());
        
        // Test when occupierCount is 0
        SimpleSector sector2 = new SimpleSector(10, false, true, 2, 2, false, 1, CharacterClass.ARCHER);
        sector2.free();
        assertFalse(sector2.isOccupied());
    }
    
    // Test case for gainControl() method
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(8, true, true, 4, 4, false, 2, CharacterClass.MAGE);
        
        // Test when occupierCount is incremented and occupier changes
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        
        // Test when occupierCount is incremented but occupier doesn't change
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, sector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    // Test case for getOccupier() method
    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(6, true, false, 3, 3, true, 1, CharacterClass.WARRIOR);
        
        // Test when sector is not occupied
        assertNull(sector.getOccupier());
        
        // Test when sector is occupied
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    // Test case for isOccupied() method
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(4, false, true, 2, 2, false, 1, CharacterClass.ARCHER);
        
        // Test when sector is not occupied
        assertFalse(sector.isOccupied());
        
        // Test when sector is occupied
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }
    
    // Test case for getHeight() method
    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(12, false, false, 4, 4, true, 2, CharacterClass.MAGE);
        
        assertEquals(12, sector.getHeight());
    }
    
    // Test case for isDefenderSpawn() method
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(8, true, false, 3, 3, false, 1, CharacterClass.WARRIOR);
        
        assertTrue(sector.isDefenderSpawn());
    }
    
    // Test case for isAttackerSpawn() method
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, false, true, 5, 5, true, 3, CharacterClass.WARRIOR);
        
        assertTrue(sector.isAttackerSpawn());
    }
    
    // Test case for isFlag() method
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(6, false, true, 3, 3, true, 1, CharacterClass.ARCHER);
        
        assertTrue(sector.isFlag());
    }
    
    // Test case for getBonus() method
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(6, true, false, 4, 4, false, 2, CharacterClass.MAGE);
        CharacterClass charClass = CharacterClass.ARCHER;
        
        SectorBonus bonus = sector.getBonus(charClass);

        assertNotNull(bonus);
        assertEquals(6, bonus.getAttackBonus());
        assertEquals(6, bonus.getDefenseBonus());
        assertEquals(4, bonus.getDamageBonus());
    }
    
    // Test case for getAreas() method
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(8, true, true, 2, 2, true, 1, CharacterClass.MAGE);
        Area area1 = new Area();
        Area area2 = new Area();
        Area area3 = new Area();
        
        sector._addArea(area1);
        sector._addArea(area2);
        sector._addArea(area3);
        
        Set<Area> areas = sector.getAreas();
        
        assertNotNull(areas);
        assertEquals(3, areas.size());
        assertTrue(areas.contains(area1));
        assertTrue(areas.contains(area2));
        assertTrue(areas.contains(area3));
    }
    
    // Test case for _addArea() method
    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(4, false, false, 2, 2, false, 1, CharacterClass.ARCHER);
        Area area = new Area();
        
        sector._addArea(area);
        
        Set<Area> areas = sector.getAreas();
        
        assertNotNull(areas);
        assertEquals(1, areas.size());
        assertTrue(areas.contains(area));
    }
    
    // Test case for hashCode() method
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 5, true, 3, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 5, true, 3, CharacterClass.WARRIOR);
        
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }
    
    // Test case for equals() method
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(12, false, false, 4, 4, true, 2, CharacterClass.MAGE);
        SimpleSector sector2 = new SimpleSector(12, false, false, 4, 4, true, 2, CharacterClass.MAGE);
        
        assertTrue(sector1.equals(sector2));
    }
    
    // Test case for addObserver() and removeObserver() methods
    @Test
    public void testAddAndRemoveObserver() {
        SimpleSector sector = new SimpleSector(6, false, true, 3, 3, true, 1, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver();
        
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
        
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
}