import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    private SimpleSector simpleSector;

    @Before
    public void setUp() {
        simpleSector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        // Test case 1: test when occupierCount is greater than 0
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.free();
        assertEquals(0, simpleSector.getOccupierCount());
        assertNull(simpleSector.getOccupier());

        // Test case 2: test when occupierCount is already 0
        simpleSector.free();
        assertEquals(0, simpleSector.getOccupierCount());
        assertNull(simpleSector.getOccupier());
    }

    @Test
    public void testGainControl() {
        // Test case 1: test when occupier is null and occupierCount is 0
        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, simpleSector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());

        // Test case 2: test when occupier is already assigned to the same side as the new side
        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, simpleSector.getOccupierCount());
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        // Test case 1: test when occupier is null
        assertNull(simpleSector.getOccupier());

        // Test case 2: test when occupier is assigned to a side
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Test case 1: test when occupier is null
        assertFalse(simpleSector.isOccupied());

        // Test case 2: test when occupier is assigned to a side
        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertTrue(simpleSector.isOccupied());
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
        // Test case 1: test when charClass is not the same as the classType of the sector
        CharacterClass charClass = CharacterClass.ROGUE;
        SectorBonus expectedBonus = new SectorBonus(5, 3, 0);
        assertEquals(expectedBonus, simpleSector.getBonus(charClass));

        // Test case 2: test when charClass is the same as the classType of the sector
        charClass = CharacterClass.WARRIOR;
        expectedBonus = new SectorBonus(7, 5, 0);
        assertEquals(expectedBonus, simpleSector.getBonus(charClass));
    }

    @Test
    public void testGetAreas() {
        // Test case 1: test when areas is empty
        assertTrue(simpleSector.getAreas().isEmpty());

        // Test case 2: test when areas is not empty
        Area area = new Area("Area 1");
        simpleSector._addArea(area);
        assertEquals(1, simpleSector.getAreas().size());
        assertTrue(simpleSector.getAreas().contains(area));
    }
    
    @Test
    public void test_addArea() {
        // Test case 1: test when adding a new area to the sector
        Area area = new Area("Area 1");
        simpleSector._addArea(area);
        assertTrue(simpleSector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        // Test case 1: test when two SimpleSectors have the same values
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector2.hashCode());

        // Test case 2: test when two SimpleSectors have different values
        sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ROGUE);
        assertNotEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        // Test case 1: test when comparing a SimpleSector with itself
        assertTrue(simpleSector.equals(simpleSector));

        // Test case 2: test when comparing a SimpleSector with null
        assertFalse(simpleSector.equals(null));

        // Test case 3: test when comparing a SimpleSector with a different class object
        assertFalse(simpleSector.equals(new Object()));

        // Test case 4: test when comparing two equal SimpleSector objects
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector2));

        // Test case 5: test when comparing two different SimpleSector objects
        sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.ROGUE);
        assertFalse(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        // Test case 1: test when adding a new observer
        SectorObserver observer = new SectorObserver();
        simpleSector.addObserver(observer);
        assertTrue(simpleSector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        // Test case 1: test when removing an existing observer
        SectorObserver observer = new SectorObserver();
        simpleSector.addObserver(observer);
        simpleSector.removeObserver(observer);
        assertFalse(simpleSector.getObservers().contains(observer));
    }

    private class SectorObserver implements Observer {

        private boolean occupierChangedCalled;

        @Override
        public void occupierChanged() {
            occupierChangedCalled = true;
        }
    }

}