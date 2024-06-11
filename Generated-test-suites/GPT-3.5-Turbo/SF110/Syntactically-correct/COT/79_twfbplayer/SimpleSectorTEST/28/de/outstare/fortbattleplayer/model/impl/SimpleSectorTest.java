
package de.outstare.fortbattleplayer.model.impl;

import org.junit.Test;
import static org.junit.Assert.*;

public class SimpleSectorTest {

    @Test
    public void testFree() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the initial state of the occupier count
        assertEquals(0, simpleSector.occupierCount);

        // Test the initial state of the occupier
        assertNull(simpleSector.occupier);

        // Test the free method when occupier count is 0
        simpleSector.free();

        assertEquals(0, simpleSector.occupierCount);
        assertNull(simpleSector.occupier);

        // Test the free method when occupier count is 1
        simpleSector.occupierCount = 1;
        simpleSector.free();

        assertEquals(0, simpleSector.occupierCount);
        assertNull(simpleSector.occupier);

        // Test the free method when occupier count is greater than 1
        simpleSector.occupierCount = 3;
        simpleSector.free();

        assertEquals(2, simpleSector.occupierCount);
        assertNull(simpleSector.occupier);
    }

    @Test
    public void testGainControl() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the initial state of the occupier count
        assertEquals(0, simpleSector.occupierCount);

        // Test the initial state of the occupier
        assertNull(simpleSector.occupier);

        // Test the gainControl method
        simpleSector.gainControl(CombatantSide.ATTACKER);

        assertEquals(1, simpleSector.occupierCount);
        assertEquals(CombatantSide.ATTACKER, simpleSector.occupier);
    }

    @Test
    public void testGetOccupier() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the initial state of the occupier count
        assertEquals(0, simpleSector.occupierCount);

        // Test the initial state of the occupier
        assertNull(simpleSector.occupier);

        // Test the getOccupier method
        simpleSector.occupier = CombatantSide.DEFENDER;

        assertEquals(CombatantSide.DEFENDER, simpleSector.getOccupier());
    }

    @Test
    public void testIsOccupied() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the initial state of the occupier count
        assertEquals(0, simpleSector.occupierCount);

        // Test the initial state of the occupier
        assertNull(simpleSector.occupier);

        // Test the isOccupied method when occupier is null
        assertFalse(simpleSector.isOccupied());

        // Test the isOccupied method when occupier is not null
        simpleSector.occupier = CombatantSide.DEFENDER;

        assertTrue(simpleSector.isOccupied());
    }

    @Test
    public void testGetHeight() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the getHeight method
        assertEquals(10, simpleSector.getHeight());
    }

    @Test
    public void testIsDefenderSpawn() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the isDefenderSpawn method
        assertTrue(simpleSector.isDefenderSpawn());
    }

    @Test
    public void testIsAttackerSpawn() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the isAttackerSpawn method
        assertTrue(simpleSector.isAttackerSpawn());
    }

    @Test
    public void testIsFlag() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the isFlag method
        assertTrue(simpleSector.isFlag());
    }

    @Test
    public void testGetBonus() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the getBonus method
        SectorBonus sectorBonus = simpleSector.getBonus(CharacterClass.WARRIOR);

        assertEquals(7, sectorBonus.getAttackBonus());
        assertEquals(7, sectorBonus.getDefenseBonus());
        assertEquals(0, sectorBonus.getDamageBonus());
    }

    @Test
    public void testGetAreas() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the getAreas method
        assertNotNull(simpleSector.getAreas());
        assertEquals(0, simpleSector.getAreas().size());
    }

    @Test
    public void test_addArea() {
        // Create a SimpleSector object
        SimpleSector simpleSector = new SimpleSector(10, true, true, 5, 5, true, 2, CharacterClass.WARRIOR);

        // Test the _addArea method
        Area area = new Area();
        simpleSector._addArea(area);

        assertEquals(1, simpleSector.getAreas().size());
        assertTrue(simpleSector.getAreas().contains(area));
    }
}
