import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.impl.*;

public class SimpleSectorTest {
    
    private SimpleSector simpleSector;
    
    @Before
    public void setUp() {
        simpleSector = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
    }
    
    @Test
    public void testFree() {
        simpleSector.free();
        assertTrue(simpleSector.getOccupierCount() == 0);
        assertNull(simpleSector.getOccupier());
    }
    
    @Test
    public void testGainControl() {
        CombatantSide side = CombatantSide.ATTACKER;
        simpleSector.gainControl(side);
        assertEquals(side, simpleSector.getOccupier());
    }
    
    @Test
    public void testIsOccupied() {
        assertFalse(simpleSector.isOccupied());
    }
    
    @Test
    public void testGetHeight() {
        assertEquals(10, simpleSector.getHeight());
    }
    
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(simpleSector.isDefenderSpawn());
    }
    
    @Test
    public void testIsAttackerSpawn() {
        assertFalse(simpleSector.isAttackerSpawn());
    }
    
    @Test
    public void testIsFlag() {
        assertTrue(simpleSector.isFlag());
    }
    
    @Test
    public void testGetBonus() {
        SectorBonus sectorBonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(sectorBonus);
    }
    
    @Test
    public void testGetAreas() {
        assertEquals(0, simpleSector.getAreas().size());
    }
    
    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
        assertEquals(simpleSector.hashCode(), other.hashCode());
    }
    
    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 20, 15, true, 5, CharacterClass.WARRIOR);
        assertTrue(simpleSector.equals(other));
    }
    
    @Test
    public void testAddObserver() {
        SectorObserver observer = new TestSectorObserver();
        simpleSector.addObserver(observer);
        assertEquals(1, simpleSector.getObservers().size());
    }
    
    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new TestSectorObserver();
        simpleSector.addObserver(observer);
        simpleSector.removeObserver(observer);
        assertEquals(0, simpleSector.getObservers().size());
    }
    
    private class TestSectorObserver implements SectorObserver {
        @Override
        public void occupierChanged() {
            // do nothing for now
        }
    }
}