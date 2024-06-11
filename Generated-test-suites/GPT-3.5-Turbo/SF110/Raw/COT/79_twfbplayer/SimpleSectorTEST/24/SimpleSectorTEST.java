import de.outstare.fortbattleplayer.model.*;
import de.outstare.fortbattleplayer.model.impl.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {
    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        assertEquals(null, sector.getOccupier());
        assertFalse(sector.isOccupied());

        sector.gainControl(CombatantSide.ATTACKER);
        sector.gainControl(CombatantSide.DEFENDER);

        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());

        sector.free();

        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());

        sector.free();

        assertEquals(null, sector.getOccupier());
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        assertEquals(null, sector.getOccupier());
        assertFalse(sector.isOccupied());

        sector.gainControl(CombatantSide.ATTACKER);

        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());

        sector.gainControl(CombatantSide.DEFENDER);

        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetOccupier() {
        assertEquals(null, sector.getOccupier());
        assertFalse(sector.isOccupied());

        sector.gainControl(CombatantSide.ATTACKER);

        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testIsOccupied() {
        assertFalse(sector.isOccupied());

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
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);

        assertEquals(7, bonus.getAttackBonus());
        assertEquals(5, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        assertNotNull(sector.getAreas());
        assertTrue(sector.getAreas().isEmpty());
    }

    @Test
    public void test_addArea() {
        Area area = new Area("Area1");
      
        sector._addArea(area);

        assertTrue(sector.getAreas().contains(area));
    }
    
    // Add more test cases for edge cases and exception handling scenarios

}

