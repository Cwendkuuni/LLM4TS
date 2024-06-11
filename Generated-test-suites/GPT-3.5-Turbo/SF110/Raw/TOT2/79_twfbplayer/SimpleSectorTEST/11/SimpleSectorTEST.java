import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleSectorTest {

    private SimpleSector simpleSector;

    @Before
    public void setUp() {
        simpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.free();
        assertEquals(null, simpleSector.getOccupier());
    }

    @Test
    public void testGainControl() {
        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertTrue(simpleSector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        assertEquals(10, simpleSector.getHeight());
    }

    //edge case scenario
    @Test
    public void testGetBonus() {
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.WARRIOR);
        assertNotNull(bonus);
    }

    //exception handling scenario
    @Test(expected = IllegalArgumentException.class)
    public void testGetBonus_Exception() {
        simpleSector.getBonus(null);
    }

    @Test
    public void testGetAreas() {
        assertNotNull(simpleSector.getAreas());
    }

    @Test
    public void testHashCode() {
        assertEquals(simpleSector.hashCode(), simpleSector.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector otherSimpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WIZARD);
        assertFalse(simpleSector.equals(otherSimpleSector));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new MockSectorObserver();
        simpleSector.addObserver(observer);
        assertEquals(1, simpleSector.observers.size());
    }

    @Test
    public void testRemoveObserver() {
        SectorObserver observer = new MockSectorObserver();
        simpleSector.addObserver(observer);
        simpleSector.removeObserver(observer);
        assertTrue(simpleSector.observers.isEmpty());
    }

    private class MockSectorObserver implements SectorObserver {
        @Override
        public void occupierChanged() {
            // Do nothing
        }
    }
}