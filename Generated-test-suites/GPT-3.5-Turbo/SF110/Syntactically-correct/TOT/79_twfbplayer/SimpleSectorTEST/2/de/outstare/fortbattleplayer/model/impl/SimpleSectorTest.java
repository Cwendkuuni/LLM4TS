
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertNull(sector.getOccupier());
        assertFalse(sector.isOccupied());
        
        sector.free();
        assertEquals(0, sector.getOccupierCount());
    }
    
    @Test
    public void testGetOccupier() {
        assertNull(sector.getOccupier());
        
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(10, sector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertFalse(sector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(8, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
        
        bonus = sector.getBonus(CharacterClass.MAGE);
        assertEquals(0, bonus.getAttackBonus());
        assertEquals(0, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }
    
    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
        assertTrue(sector.getAreas().isEmpty());
    }
    
    @Test
    public void testAddArea() {
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }
    
    @Test
    public void testHashCode() {
        int hashCode = sector.hashCode();
        assertNotNull(hashCode);
        assertEquals(hashCode, sector.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(sector.equals(sector));
        assertTrue(sector.equals(new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(null));
        assertFalse(sector.equals(new SimpleSector(20, true, false, 2, 3, true, 5, CharacterClass.WARRIOR)));
        assertFalse(sector.equals(new SimpleSector(10, false, false, 2, 3, true, 5, CharacterClass.WARRIOR)));
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserverImpl();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertFalse(sector.getObservers().contains(observer));
    }
    
    private class SectorObserverImpl implements SectorObserver {
        // implementation
    }
}
