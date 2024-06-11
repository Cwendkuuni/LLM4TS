import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
import java.util.Set;
import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }
  
    @Test
    public void testFree() {
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals("Occupier should be null after freeing the sector", null, sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals("Occupier should be ATTACKER after gaining control by ATTACKER", CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertFalse("Initial state of sector should be unoccupied", sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals("Height should be 10", 10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        assertTrue("Sector should be a defender spawn", sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        assertFalse("Sector should not be an attacker spawn", sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertTrue("Sector should have a flag", sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertNotNull("Bonus should not be null", bonus);
    }

    @Test
    public void testGetAreas() {
        Set<Area> areas = sector.getAreas();
        assertEquals("Number of areas should be 0", 0, areas.size());
    }

    @Test
    public void testAddArea() {
        Area area = new Area("Test Area");
        sector._addArea(area);
        Set<Area> areas = sector.getAreas();
        assertTrue("Area should be added to areas", areas.contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals("HashCodes should match", sector.hashCode(), other.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue("Secors should be equal", sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        DummyObserver observer = new DummyObserver();
        sector.addObserver(observer);
        assertEquals("Number of observers should be 1", 1, sector.observers.size());
    }

    @Test
    public void testRemoveObserver() {
        DummyObserver observer = new DummyObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertEquals("Number of observers should be 0 after removal", 0, sector.observers.size());
    }

    static class DummyObserver implements SectorObserver {
        @Override
        public void occupierChanged() {
            // Do nothing
        }
    }
}