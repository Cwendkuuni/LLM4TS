import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 0, null);
        // Test free method when occupierCount is greater than 0
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertEquals(null, sector.getOccupier());
        
        // Test free method when occupierCount is 0
        sector.occupierCount = 0;
        sector.free();
        sector.free();
        assertEquals(null, sector.getOccupier());
    }

    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 0, null);
        // Test gainControl method when occupier is null
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());

        // Test gainControl method when occupier is not null
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 0, null);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 0, null);
        // Test isOccupied method when occupier is null
        boolean result = sector.isOccupied();
        assertFalse(result);

        // Test isOccupied method when occupier is not null
        sector.gainControl(CombatantSide.DEFENDER);
        result = sector.isOccupied();
        assertTrue(result);
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, false, false, 0, 0, false, 0, null);
        int result = sector.getHeight();
        assertEquals(5, result);
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(0, true, false, 0, 0, false, 0, null);
        boolean result = sector.isDefenderSpawn();
        assertTrue(result);

        sector = new SimpleSector(0, false, true, 0, 0, false, 0, null);
        result = sector.isDefenderSpawn();
        assertFalse(result);
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(0, false, true, 0, 0, false, 0, null);
        boolean result = sector.isAttackerSpawn();
        assertTrue(result);

        sector = new SimpleSector(0, true, false, 0, 0, false, 0, null);
        result = sector.isAttackerSpawn();
        assertFalse(result);
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, true, 0, null);
        boolean result = sector.isFlag();
        assertTrue(result);

        sector = new SimpleSector(0, false, false, 0, 0, false, 0, null);
        result = sector.isFlag();
        assertFalse(result);
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 5, CharacterClass.WARRIOR);
        SectorBonus result = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(5, result.getAttackBonus());
        assertEquals(0, result.getDefenseBonus());
        assertEquals(0, result.getDamageBonus());
    }

    @Test
    public void testClassBonus() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 5, CharacterClass.WARRIOR);
        int result = sector.classBonus(CharacterClass.WARRIOR);
        assertEquals(5, result);

        result = sector.classBonus(CharacterClass.ARCHER);
        assertEquals(0, result);
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 0, null);
        assertNotNull(sector.getAreas());
    }

    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(0, false, false, 0, 0, false, 0, null);
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

}
