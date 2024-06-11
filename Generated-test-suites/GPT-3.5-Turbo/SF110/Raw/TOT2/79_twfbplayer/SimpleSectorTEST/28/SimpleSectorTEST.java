import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.HashSet;
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
        assertEquals(null, sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        assertEquals(false, sector.isOccupied());
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(true, sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        assertEquals(true, sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        assertEquals(false, sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        assertEquals(true, sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        assertEquals(10, sector.getBonus(CharacterClass.WARRIOR).getTotalBonus());
    }

    @Test
    public void testGetAreas() {
        assertEquals(new HashSet<Area>(), sector.getAreas());
    }

    @Test
    public void testHashCode() {
        assertEquals(1234567890, sector.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(true, sector.equals(other));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        assertEquals(true, sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertEquals(false, sector.getObservers().contains(observer));
    }
}