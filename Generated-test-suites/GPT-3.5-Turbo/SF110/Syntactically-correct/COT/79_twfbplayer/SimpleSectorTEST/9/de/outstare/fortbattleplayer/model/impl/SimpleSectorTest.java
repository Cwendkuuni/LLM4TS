import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Set;
import de.outstare.fortbattleplayer.model.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CombatantSide side = new CombatantSide();

        sector.gainControl(side);
        sector.free();

        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CombatantSide side = new CombatantSide();

        sector.gainControl(side);

        assertEquals(side, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CombatantSide side = new CombatantSide();

        sector.gainControl(side);

        assertEquals(side, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CombatantSide side = new CombatantSide();

        sector.gainControl(side);

        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;

        assertNotNull(sector.getBonus(charClass));
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        assertNotNull(sector.getAreas());
    }

    @Test
    public void test_addArea() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        Area area = new Area();

        sector._addArea(area);

        assertTrue(sector.getAreas().contains(area));
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        assertTrue(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver();

        sector.addObserver(observer);

        assertTrue(sector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver();

        sector.addObserver(observer);
        sector.removeObserver(observer);

        assertFalse(sector.getObservers().contains(observer));
    }

    // Edge Cases

    @Test
    public void testFreeNoOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);

        sector.free();
	
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControlMultipleTimes() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CombatantSide side = new CombatantSide();

        sector.gainControl(side);
	sector.gainControl(side);

        assertEquals(side, sector.getOccupier());
    }

    @Test
    public void testGetAttackBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;

        assertNotNull(sector.getAttackBonus(charClass));
    }

    @Test
    public void testGetDefenseBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;

        assertNotNull(sector.getDefenseBonus(charClass));
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.WARRIOR;

        assertEquals(5, sector.classBonus(charClass));
    }

    @Test
    public void testClassBonusDifferentClass() {
        SimpleSector sector = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CharacterClass charClass = CharacterClass.MAGE;

        assertEquals(0, sector.classBonus(charClass));
    }

    @Test
    public void testEqualsDifferentOccupier() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 2, 3, true, 5, CharacterClass.WARRIOR);
        CombatantSide side = new CombatantSide();

        sector1.gainControl(side);

        assertFalse(sector1.equals(sector2));
    }
}