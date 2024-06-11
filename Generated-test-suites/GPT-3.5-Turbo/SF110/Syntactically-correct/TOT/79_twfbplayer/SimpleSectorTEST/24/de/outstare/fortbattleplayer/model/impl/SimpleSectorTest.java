import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

public class SimpleSectorTest {

    private SimpleSector simpleSector;
    private Area area1;
    private Area area2;
    private CombatantSide side;

    @Before
    public void setUp() {
        int height = 10;
        boolean defenderSpawn = true;
        boolean attackerSpawn = false;
        int attackBonus = 5;
        int defendBonus = 3;
        boolean flag = false;
        int classBonus = 2;
        CharacterClass bonusClass = CharacterClass.WARRIOR;
        simpleSector = new SimpleSector(height, defenderSpawn, attackerSpawn, attackBonus, defendBonus, flag, classBonus, bonusClass);
        area1 = mock(Area.class);
        area2 = mock(Area.class);
        side = mock(CombatantSide.class);
    }

    @Test
    public void testFree_WhenOccupierCountGreaterThanZero_ShouldDecreaseOccupierCountByOne() {
        simpleSector.gainControl(side);
        simpleSector.free();
        assertEquals(0, simpleSector.getOccupierCount());
    }

    @Test
    public void testFree_WhenOccupierCountIsZero_ShouldNotDecreaseOccupierCount() {
        simpleSector.free();
        assertEquals(0, simpleSector.getOccupierCount());
    }

    @Test
    public void testFree_WhenOccupierCountIsZero_ShouldSetOccupierToNull() {
        simpleSector.free();
        assertEquals(null, simpleSector.getOccupier());
    }

    @Test
    public void testGainControl_WhenOccupierNotEqualSide_ShouldIncreaseOccupierCountByOne() {
        simpleSector.gainControl(side);
        assertEquals(1, simpleSector.getOccupierCount());
    }

    @Test
    public void testGainControl_WhenOccupierNotEqualSide_ShouldSetOccupierToSide() {
        simpleSector.gainControl(side);
        assertEquals(side, simpleSector.getOccupier());
    }

    @Test
    public void testGetOccupier_WhenCalled_ShouldReturnOccupier() {
        simpleSector.gainControl(side);
        assertEquals(side, simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied_WhenOccupierNotNull_ShouldReturnTrue() {
        simpleSector.gainControl(side);
        assertTrue(simpleSector.isOccupied());
    }

    @Test
    public void testIsOccupied_WhenOccupierNull_ShouldReturnFalse() {
        assertFalse(simpleSector.isOccupied());
    }

    @Test
    public void testGetHeight_WhenCalled_ShouldReturnHeight() {
        int height = 10;
        assertEquals(height, simpleSector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn_WhenCalled_ShouldReturnDefenderSpawn() {
        boolean defenderSpawn = true;
        assertEquals(defenderSpawn, simpleSector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn_WhenCalled_ShouldReturnAttackerSpawn() {
        boolean attackerSpawn = false;
        assertEquals(attackerSpawn, simpleSector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag_WhenCalled_ShouldReturnFlag() {
        boolean flag = false;
        assertEquals(flag, simpleSector.isFlag());
    }

    @Test
    public void testGetBonus_WhenCalled_ShouldReturnSectorBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        SectorBonus expectedBonus = new SectorBonus(7, 5, 0);
        assertEquals(expectedBonus, simpleSector.getBonus(charClass));
    }

    @Test
    public void testGetDamageBonus_WhenCalled_ShouldReturnBonusFromWeapons() {
        int expectedBonus = 0;
        assertEquals(expectedBonus, simpleSector.getDamageBonus());
    }

    @Test
    public void testGetAttackBonus_WhenCalled_ShouldReturnAttackBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        int expectedBonus = 7;
        assertEquals(expectedBonus, simpleSector.getAttackBonus(charClass));
    }

    @Test
    public void testGetDefenseBonus_WhenCalled_ShouldReturnDefenseBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        int expectedBonus = 5;
        assertEquals(expectedBonus, simpleSector.getDefenseBonus(charClass));
    }

    @Test
    public void testClassBonus_WhenCharClassNotEqualClassType_ShouldReturnZero() {
        CharacterClass charClass = CharacterClass.THIEF;
        int expectedBonus = 0;
        assertEquals(expectedBonus, simpleSector.classBonus(charClass));
    }

    @Test
    public void testClassBonus_WhenCharClassEqualClassType_ShouldReturnClassBonus() {
        CharacterClass charClass = CharacterClass.WARRIOR;
        int expectedBonus = 2;
        assertEquals(expectedBonus, simpleSector.classBonus(charClass));
    }

    @Test
    public void testGetAreas_WhenCalled_ShouldReturnAreas() {
        Set<Area> expectedAreas = new HashSet<>();
        expectedAreas.add(area1);
        expectedAreas.add(area2);
        simpleSector._addArea(area1);
        simpleSector._addArea(area2);
        assertEquals(expectedAreas, simpleSector.getAreas());
    }

    @Test
    public void test_addArea_WhenCalled_ShouldAddAreaToAreas() {
        Set<Area> expectedAreas = new HashSet<>();
        expectedAreas.add(area1);
        simpleSector._addArea(area1);
        assertEquals(expectedAreas, simpleSector.getAreas());
    }

    @Test
    public void testHashCode_WhenCalled_ShouldReturnHashCode() {
        int expectedHashCode = 2123704132;
        assertEquals(expectedHashCode, simpleSector.hashCode());
    }

    @Test
    public void testEquals_WhenObjectsAreTheSame_ShouldReturnTrue() {
        assertTrue(simpleSector.equals(simpleSector));
    }

    @Test
    public void testEquals_WhenObjectsAreEqual_ShouldReturnTrue() {
        SimpleSector other = new SimpleSector(10, true, false, 5, 3, false, 2, CharacterClass.WARRIOR);
        assertTrue(simpleSector.equals(other));
    }

    @Test
    public void testEquals_WhenObjectsAreNotEqual_ShouldReturnFalse() {
        SimpleSector other = new SimpleSector(5, true, false, 3, 2, false, 1, CharacterClass.THIEF);
        assertFalse(simpleSector.equals(other));
    }

    @Test
    public void testEquals_WhenObjectIsNull_ShouldReturnFalse() {
        assertFalse(simpleSector.equals(null));
    }

    @Test
    public void testEquals_WhenObjectIsNotSimpleSector_ShouldReturnFalse() {
        assertFalse(simpleSector.equals(area1));
    }

    @Test
    public void testAddObserver_WhenCalled_ShouldAddObserverToObservers() {
        SectorObserver observer = mock(SectorObserver.class);
        simpleSector.addObserver(observer);
        assertTrue(simpleSector.getObservers().contains(observer));
    }

    @Test
    public void testRemoveObserver_WhenCalled_ShouldRemoveObserverFromObservers() {
        SectorObserver observer = mock(SectorObserver.class);
        simpleSector.addObserver(observer);
        simpleSector.removeObserver(observer);
        assertFalse(simpleSector.getObservers().contains(observer));
    }

}