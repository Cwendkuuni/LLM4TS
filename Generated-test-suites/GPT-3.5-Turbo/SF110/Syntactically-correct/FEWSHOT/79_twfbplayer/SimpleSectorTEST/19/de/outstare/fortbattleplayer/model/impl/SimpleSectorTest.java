import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleSectorTest {
    @Test
    public void testFree() {
        // Test when occupierCount is greater than 0
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.DEFENDER);
        sector.free();
        assertNull(sector.getOccupier());
        
        // Test when occupierCount is 0
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.MAGE);
        sector2.free();
        assertNull(sector2.getOccupier());
    }

    @Test
    public void testGainControl() {
        // Test when occupier is null
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());

        // Test when occupier is not null
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.MAGE);
        sector2.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector2.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Test when occupier is null
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isOccupied());

        // Test when occupier is not null
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.MAGE);
        sector2.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector2.isOccupied());
    }

    @Test
    public void testGetHeight() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertEquals(5, sector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertFalse(sector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector.isFlag());
    }

    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        SectorBonus bonus = sector.getBonus(CharacterClass.WARRIOR);
        assertEquals(12, bonus.getAttackBonus());
        assertEquals(7, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertEquals(0, sector.getAreas().size());
    }

    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        Area area = new Area();
        sector._addArea(area);
        assertEquals(1, sector.getAreas().size());
    }

    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertEquals(sector1.hashCode(), sector2.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        assertTrue(sector1.equals(sector2));
    }

    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        assertEquals(1, sector.getObservers().size());
    }

    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(5, true, false, 10, 5, true, 2, CharacterClass.WARRIOR);
        SectorObserver observer = new SectorObserver();
        sector.addObserver(observer);
        sector.removeObserver(observer);
        assertEquals(0, sector.getObservers().size());
    }
}