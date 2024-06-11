package de.outstare.fortbattleplayer.model.impl;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {
    private SimpleSector simpleSector;

    @Before
    public void setUp() {
        simpleSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
    }

    @Test
    public void testFree() {
        // test initial occupierCount
        assertEquals(0, simpleSector.occupierCount);

        // test if occupier changes when occupierCount becomes 0
        simpleSector.gainControl(CombatantSide.ATTACKER);
        simpleSector.free();
        assertNull(simpleSector.getOccupier());

        // test if occupierCount decreases by 1 when free() is called
        simpleSector.gainControl(CombatantSide.DEFENDER);
        simpleSector.free();
        assertEquals(0, simpleSector.occupierCount);
    }

    @Test
    public void testOccupierChanged() {
        // test if all observers are notified when occupier changes
        SimpleSectorObserverMock observer1 = new SimpleSectorObserverMock();
        SimpleSectorObserverMock observer2 = new SimpleSectorObserverMock();
        simpleSector.addObserver(observer1);
        simpleSector.addObserver(observer2);

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(1, observer1.numCalls);
        assertEquals(1, observer2.numCalls);

        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(2, observer1.numCalls);
        assertEquals(2, observer2.numCalls);
    }

    @Test
    public void testGainControl() {
        // test if occupierCount increases by 1 when gainControl() is called
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(1, simpleSector.occupierCount);

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(2, simpleSector.occupierCount);

        // test if occupier changes when gainControl() is called with different side
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testGetOccupier() {
        // test initial occupier
        assertNull(simpleSector.getOccupier());

        // test occupier after gainControl() is called
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertEquals(CombatantSide.ATTACKER, simpleSector.getOccupier());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // test initial occupied state
        assertFalse(simpleSector.isOccupied());

        // test occupied state after gainControl() is called
        simpleSector.gainControl(CombatantSide.ATTACKER);
        assertTrue(simpleSector.isOccupied());

        simpleSector.gainControl(CombatantSide.DEFENDER);
        assertTrue(simpleSector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        // test getHeight() method
        assertEquals(10, simpleSector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        // test isDefenderSpawn() method
        assertTrue(simpleSector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        // test isAttackerSpawn() method
        assertTrue(simpleSector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        // test isFlag() method
        assertTrue(simpleSector.isFlag());
    }

    @Test
    public void testGetBonus() {
        // test getBonus() method
        SectorBonus expectedBonus = new SectorBonus(5, 3, 0);
        assertEquals(expectedBonus, simpleSector.getBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAttackBonus() {
        // test getAttackBonus() method
        int expectedBonus = 7;
        assertEquals(expectedBonus, simpleSector.getAttackBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetDefenseBonus() {
        // test getDefenseBonus() method
        int expectedBonus = 5;
        assertEquals(expectedBonus, simpleSector.getDefenseBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testClassBonus() {
        // test classBonus() method
        int expectedBonus = 2;
        assertEquals(expectedBonus, simpleSector.classBonus(CharacterClass.WARRIOR));

        expectedBonus = 0;
        assertEquals(expectedBonus, simpleSector.classBonus(CharacterClass.ARCHER));
    }

    @Test
    public void testGetAreas() {
        // test getAreas() method
        assertNotNull(simpleSector.getAreas());
        assertEquals(0, simpleSector.getAreas().size());

        // test _addArea() method
        Area area1 = new Area();
        simpleSector._addArea(area1);
        assertEquals(1, simpleSector.getAreas().size());

        Area area2 = new Area();
        simpleSector._addArea(area2);
        assertEquals(2, simpleSector.getAreas().size());
        assertTrue(simpleSector.getAreas().contains(area2));
    }

    @Test
    public void testHashCode() {
        SimpleSector anotherSimpleSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);

        // test hashCode() method
        assertEquals(simpleSector.hashCode(), anotherSimpleSector.hashCode());
    }

    @Test
    public void testEquals() {
        SimpleSector anotherSimpleSector = new SimpleSector(10, true, true, 5, 3, true, 2, CharacterClass.WARRIOR);
        SimpleSector differentSimpleSector = new SimpleSector(5, true, true, 2, 1, false, 1, CharacterClass.ARCHER);

        // test equals() method
        assertTrue(simpleSector.equals(anotherSimpleSector));
        assertFalse(simpleSector.equals(differentSimpleSector));
    }

    @Test
    public void testAddObserver() {
        // test addObserver() method
        SimpleSectorObserverMock observer1 = new SimpleSectorObserverMock();
        simpleSector.addObserver(observer1);
        assertEquals(1, simpleSector.observers.size());

        SimpleSectorObserverMock observer2 = new SimpleSectorObserverMock();
        simpleSector.addObserver(observer2);
        assertEquals(2, simpleSector.observers.size());
    }

    @Test
    public void testRemoveObserver() {
        SimpleSectorObserverMock observer1 = new SimpleSectorObserverMock();
        SimpleSectorObserverMock observer2 = new SimpleSectorObserverMock();

        // test removeObserver() method
        simpleSector.addObserver(observer1);
        simpleSector.addObserver(observer2);
        assertEquals(2, simpleSector.observers.size());

        simpleSector.removeObserver(observer1);
        assertEquals(1, simpleSector.observers.size());

        simpleSector.removeObserver(observer2);
        assertEquals(0, simpleSector.observers.size());
    }

    // Helper class for testing
    private class SimpleSectorObserverMock implements SectorObserver {
        int numCalls = 0;

        @Override
        public void occupierChanged() {
            numCalls++;
        }
    }
}