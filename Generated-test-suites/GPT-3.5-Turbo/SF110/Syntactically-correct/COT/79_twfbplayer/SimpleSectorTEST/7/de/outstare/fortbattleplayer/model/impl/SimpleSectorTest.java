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
        // Assuming this.occupier is null initially
        simpleSector.free();
        assertNull(simpleSector.getOccupier());
    }

    @Test
    public void testGainControl() {
        // Assuming this.occupier is null initially
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        // Assuming this.occupier is null initially
        assertNull(simpleSector.getOccupier());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Assuming this.occupier is null initially
        assertFalse(simpleSector.isOccupied());

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
        SectorBonus bonus = simpleSector.getBonus(CharacterClass.MAGE);

        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());

        bonus = simpleSector.getBonus(CharacterClass.WARRIOR);

        assertEquals(9, bonus.getAttackBonus());
        assertEquals(7, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        assertEquals(0, simpleSector.getAreas().size());

        Area area1 = new Area();
        Area area2 = new Area();

        simpleSector._addArea(area1);
        simpleSector._addArea(area2);

        assertEquals(2, simpleSector.getAreas().size());
        assertTrue(simpleSector.getAreas().contains(area1));
        assertTrue(simpleSector.getAreas().contains(area2));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(20, false, true, 7, 4, false, 3, CharacterClass.MAGE);

        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    @Test
    public void testAddObserver() {
        SectorObserver observer = new SectorObserverImpl();
        simpleSector.addObserver(observer);

        assertTrue(simpleSector.removeObserver(observer));
    }

    private class SectorObserverImpl implements SectorObserver {

    }

}

