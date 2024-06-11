import org.junit.*;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    // Test case for constructor
    @Test
    public void testConstructor() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertEquals(10, sector.getHeight());
        assertTrue(sector.isDefenderSpawn());
        assertFalse(sector.isAttackerSpawn());
        assertTrue(sector.isFlag());
        assertEquals(5, sector.getBonus(CharacterClass.WARRIOR).getAttackBonus());
        assertEquals(3, sector.getBonus(CharacterClass.WARRIOR).getDefenseBonus());
        assertEquals(2, sector.getBonus(CharacterClass.WARRIOR).getDamageBonus());
        assertEquals(CharacterClass.WARRIOR, sector.getBonus(CharacterClass.WARRIOR).getCharacterClass());
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
        assertFalse(sector.getAreas().isEmpty());
    }

    // Test case for free method
    @Test
    public void testFree() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        
        sector.free();
        
        assertFalse(sector.isOccupied());
        assertNull(sector.getOccupier());
    }

    // Test case for gainControl method
    @Test
    public void testGainControl() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertTrue(sector.isOccupied());
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    // Test case for getOccupier method
    @Test
    public void testGetOccupier() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertNull(sector.getOccupier());
        
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
    }

    // Test case for isOccupied method
    @Test
    public void testIsOccupied() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertFalse(sector.isOccupied());
        
        sector.gainControl(CombatantSide.ATTACKER);
        
        assertTrue(sector.isOccupied());
    }

    // Test case for isDefenderSpawn method
    @Test
    public void testIsDefenderSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertTrue(sector.isDefenderSpawn());
    }

    // Test case for isAttackerSpawn method
    @Test
    public void testIsAttackerSpawn() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertFalse(sector.isAttackerSpawn());
    }

    // Test case for isFlag method
    @Test
    public void testIsFlag() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertTrue(sector.isFlag());
    }

    // Test case for getBonus method
    @Test
    public void testGetBonus() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertEquals(5, sector.getBonus(CharacterClass.WARRIOR).getAttackBonus());
        assertEquals(3, sector.getBonus(CharacterClass.WARRIOR).getDefenseBonus());
        assertEquals(2, sector.getBonus(CharacterClass.WARRIOR).getDamageBonus());
    }

    // Test case for getAreas method
    @Test
    public void testGetAreas() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        assertFalse(sector.getAreas().isEmpty());
    }

    // Test case for _addArea method
    @Test
    public void testAddArea() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        Area area = new Area();

        sector._addArea(area);
        
        assertTrue(sector.getAreas().contains(area));
    }

    // Test case for hashCode method
    @Test
    public void testHashCode() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(5, false, true, 3, 0, false, 1, CharacterClass.MAGE);

        assertEquals(sector1.hashCode(), sector2.hashCode());
        assertNotEquals(sector1.hashCode(), sector3.hashCode());
    }

    // Test case for equals method
    @Test
    public void testEquals() {
        SimpleSector sector1 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector2 = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector sector3 = new SimpleSector(5, false, true, 3, 0, false, 1, CharacterClass.MAGE);

        assertTrue(sector1.equals(sector2));
        assertFalse(sector1.equals(sector3));
    }

    // Test case for addObserver method
    @Test
    public void testAddObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        SectorObserver observer = new SectorObserver();

        sector.addObserver(observer);
        
        assertTrue(sector.getObservers().contains(observer));
    }

    // Test case for removeObserver method
    @Test
    public void testRemoveObserver() {
        SimpleSector sector = new SimpleSector(10, true, false, 5, 3, true, 2, CharacterClass.WARRIOR);

        SectorObserver observer = new SectorObserver();

        sector.addObserver(observer);
        
        assertTrue(sector.getObservers().contains(observer));
        
        sector.removeObserver(observer);
        
        assertFalse(sector.getObservers().contains(observer));
    }

}