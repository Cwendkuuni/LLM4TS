import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class SimpleSectorTest {
    
    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        sector.free();
        assertTrue(sector.getOccupier() == null);
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertFalse(sector.getOccupier() == null);
    }
    
    @Test
    public void testIsOccupied() {
        sector.gainControl(CombatantSide.DEFENDER);
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
        assertFalse(sector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        assertNotNull(sector.getBonus(CharacterClass.WARRIOR));
    }
    
    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
    }
    
    @Test
    public void testHashCode() {
        SimpleSector newSector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), newSector.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector newSector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertTrue(sector.equals(newSector));
    }
}