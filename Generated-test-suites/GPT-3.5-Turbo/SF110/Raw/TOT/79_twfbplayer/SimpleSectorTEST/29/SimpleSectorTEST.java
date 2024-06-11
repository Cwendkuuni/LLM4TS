import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.impl.SimpleSector;
import de.outstare.fortbattleplayer.model.impl.SectorObserverImpl;

public class SimpleSectorTest {

    private SimpleSector sector;

    @Before
    public void setUp() {
        sector = new SimpleSector(5, true, false, 3, 2, false, 1, CharacterClass.WARRIOR);
        sector.addObserver(new SectorObserverImpl());
    }

    // Test for free method
    @Test
    public void testFree() {
        // add a combatant
        sector.gainControl(CombatantSide.ATTACKER);
        // test free when there is only one combatant
        sector.free();
        assertFalse(sector.isOccupied());
        // add another combatant
        sector.gainControl(CombatantSide.DEFENDER);
        // test free when there are multiple combatants
        sector.free();
        assertTrue(sector.isOccupied());
    }

    // Test for gainControl method
    @Test
    public void testGainControl() {
        // test when no previous occupier
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        // test when previous occupier is same as new
        sector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
        // test when previous occupier is different from new
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    // Test for getOccupier method
    @Test
    public void testGetOccupier() {
        // test when there is no occupier
        assertNull(sector.getOccupier());
        // test when there is an occupier
        sector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
    }

    // Test for isOccupied method
    @Test
    public void testIsOccupied() {
        // test when there is no occupier
        assertFalse(sector.isOccupied());
        // test when there is an occupier
        sector.gainControl(CombatantSide.ATTACKER);
        assertTrue(sector.isOccupied());
    }

    // Test for getHeight method
    @Test
    public void testGetHeight() {
        assertEquals(5, sector.getHeight());
    }

    // Test for isDefenderSpawn method
    @Test
    public void testIsDefenderSpawn() {
        assertTrue(sector.isDefenderSpawn());
    }

    // Test for isAttackerSpawn method
    @Test
    public void testIsAttackerSpawn() {
        assertFalse(sector.isAttackerSpawn());
    }

    // Test for isFlag method
    @Test
    public void testIsFlag() {
        assertFalse(sector.isFlag());
    }

    // Test for getBonus method
    @Test
    public void testGetBonus() {
        // test when classBonus is 0
        assertEquals(new SectorBonus(3, 2, 0), sector.getBonus(CharacterClass.ARCHER));
        // test when classBonus is not 0
        assertEquals(new SectorBonus(3, 2, 1), sector.getBonus(CharacterClass.WARRIOR));
    }

    // Test for getAreas method
    @Test
    public void testGetAreas() {
        // test when areas list is empty
        assertTrue(sector.getAreas().isEmpty());
        // test when areas list is not empty
        sector._addArea(new Area());
        assertFalse(sector.getAreas().isEmpty());
    }

    // Test for addArea method
    @Test
    public void testAddArea() {
        // test when adding a new area
        Area area = new Area();
        sector._addArea(area);
        assertTrue(sector.getAreas().contains(area));
    }

    // Test for hashCode method
    @Test
    public void testHashCode() {
        // test when two sectors are the same
        SimpleSector sector2 = new SimpleSector(5, true, false, 3, 2, false, 1, CharacterClass.WARRIOR);
        assertEquals(sector.hashCode(), sector2.hashCode());
        // test when two sectors are different
        SimpleSector sector3 = new SimpleSector(3, true, false, 2, 1, false, 1, CharacterClass.WIZARD);
        assertNotEquals(sector.hashCode(), sector3.hashCode());
    }

    // Test for equals method
    @Test
    public void testEquals() {
        // test when comparing with itself
        assertTrue(sector.equals(sector));
        // test when comparing with null
        assertFalse(sector.equals(null));
        // test when comparing with a different object
        assertFalse(sector.equals("not a sector"));
        // test when two sectors are the same
        SimpleSector sector2 = new SimpleSector(5, true, false, 3, 2, false, 1, CharacterClass.WARRIOR);
        assertTrue(sector.equals(sector2));
        // test when two sectors are different
        SimpleSector sector3 = new SimpleSector(3, true, false, 2, 1, false, 1, CharacterClass.WIZARD);
        assertFalse(sector.equals(sector3));
    }

    // Test for addObserver method
    @Test
    public void testAddObserver() {
        // test when adding a new observer
        SectorObserverImpl observer = new SectorObserverImpl();
        sector.addObserver(observer);
        assertTrue(sector.getObservers().contains(observer));
    }
    
    // Test for removeObserver method
    @Test
    public void testRemoveObserver() {
        // prepare observers
        SectorObserverImpl observer1 = new SectorObserverImpl();
        SectorObserverImpl observer2 = new SectorObserverImpl();
        SectorObserverImpl observer3 = new SectorObserverImpl();
        // add observers
        sector.addObserver(observer1);
        sector.addObserver(observer2);
        sector.addObserver(observer3);
        // remove observer2
        sector.removeObserver(observer2);
        // verify observer2 is removed
        assertFalse(sector.getObservers().contains(observer2));
        // verify observer1 and observer3 are still in the observers list
        assertTrue(sector.getObservers().contains(observer1));
        assertTrue(sector.getObservers().contains(observer3));
    }

}