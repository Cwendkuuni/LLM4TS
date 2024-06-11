import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        sector.gainControl(CombatantSide.ATTACKER);
        sector.free();
        assertFalse(sector.isOccupied());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        sector.gainControl(CombatantSide.DEFENDER);
        assertTrue(sector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        assertEquals(10, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WIZARD);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        SectorBonus bonus = sector.getBonus(CharacterClass.WIZARD);
        assertNotNull(bonus);
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WIZARD);
        assertNotNull(sector.getAreas());
    }
}