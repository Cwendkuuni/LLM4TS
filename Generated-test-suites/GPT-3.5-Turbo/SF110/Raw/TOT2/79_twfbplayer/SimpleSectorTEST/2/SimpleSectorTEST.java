import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {

    private SimpleSector sector;
    
    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        sector.free();
        assertEquals(0, sector.getOccupierCount());
    }
    
    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, sector.getOccupierCount());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(5, sector.getHeight());
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
        assertNotNull(bonus);
    }
    
    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
    }
    
    @Test
    public void testHashCode() {
        assertEquals(sector.hashCode(), sector.hashCode());
    }
    
    @Test
    public void testEquals() {
        assertTrue(sector.equals(sector));
    }
    
    @Test
    public void testAddAndRemoveObserver() {
        SectorObserver observer = new TestObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertTrue(true); // Just checking that the method is called without exception
    }

    private class TestObserver implements SectorObserver {
        @Override
        public void occupierChanged() {
            // do nothing
        }
    }
}