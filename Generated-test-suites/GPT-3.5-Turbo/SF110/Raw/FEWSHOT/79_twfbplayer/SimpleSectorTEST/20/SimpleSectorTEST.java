
package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotEquals;
import static org.mockito.Mockito.mock;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;

public class SimpleSectorTest {
	
	private SimpleSector sector;

	@Before
	public void setUp() {
		int height = 10;
		boolean defenderSpawn = true;
		boolean attackerSpawn = true;
		int attackBonus = 5;
		int defendBonus = 4;
		boolean flag = true;
		int classBonus = 2;
		CharacterClass bonusClass = CharacterClass.SOLDIER;
		
		sector = new SimpleSector(height, defenderSpawn, attackerSpawn, attackBonus, defendBonus, flag, classBonus, bonusClass);
	}
	
	@Test
	public void testFree() {
		sector.free();
		assertEquals(0, sector.getOccupierCount());
		assertNull(sector.getOccupier());
	}

	@Test
	public void testFree_OneOccupier() {
		sector.gainControl(CombatantSide.ATTACKER);
		sector.free();
		assertEquals(0, sector.getOccupierCount());
		assertNull(sector.getOccupier());
	}

	@Test
	public void testGainControl() {
		sector.gainControl(CombatantSide.ATTACKER);
		assertEquals(CombatantSide.ATTACKER, sector.getOccupier());
		assertEquals(1, sector.getOccupierCount());
	}

	@Test
	public void testGainControl_SameSide() {
		sector.gainControl(CombatantSide.DEFENDER);
		sector.gainControl(CombatantSide.DEFENDER);
		assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
		assertEquals(2, sector.getOccupierCount());
	}

	@Test
	public void testGetOccupier() {
		assertNull(sector.getOccupier());
	}

	@Test
	public void testIsOccupied() {
		assertFalse(sector.isOccupied());
		sector.gainControl(CombatantSide.ATTACKER);
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
		assertTrue(sector.isAttackerSpawn());
	}

	@Test
	public void testIsFlag() {
		assertTrue(sector.isFlag());
	}

    @Test
    public void testGetBonus() {
        SectorBonus bonus = sector.getBonus(CharacterClass.SOLDIER);
        assertNotNull(bonus);
        assertEquals(7, bonus.getAttackBonus());
        assertEquals(6, bonus.getDefenseBonus());
        assertEquals(0, bonus.getDamageBonus());
    }

	@Test
	public void testGetAreas() {
		Set<Area> areas = sector.getAreas();
		assertNotNull(areas);
		assertTrue(areas.isEmpty());
	}

	@Test
	public void testAddArea() {
		Area area = mock(Area.class);
		sector._addArea(area);
		Set<Area> areas = sector.getAreas();
		assertTrue(areas.contains(area));
	}

	@Test
	public void testHashCode() {
		SimpleSector other = new SimpleSector(10, true, true, 5, 4, true, 2, CharacterClass.SOLDIER);
		assertEquals(other.hashCode(), sector.hashCode());
	}

	@Test
	public void testEquals() {
		SimpleSector other = new SimpleSector(10, true, true, 5, 4, true, 2, CharacterClass.SOLDIER);
		assertEquals(other, sector);
	}

	@Test
	public void testEquals_Same() {
		assertEquals(sector, sector);
	}

	@Test
	public void testEquals_Null() {
		assertNotEquals(sector, null);
	}

	@Test
	public void testEquals_DifferentObject() {
		assertNotEquals(sector, new Object());
	}

	@Test
	public void testEquals_DifferentClass() {
		assertNotEquals(sector, new SimpleSectorTest());
	}

	@Test
	public void testEquals_DifferentHeight() {
		SimpleSector other = new SimpleSector(20, true, true, 5, 4, true, 2, CharacterClass.SOLDIER);
		assertNotEquals(other, sector);
	}

	@Test
	public void testEquals_DifferentDefenderSpawn() {
		SimpleSector other = new SimpleSector(10, false, true, 5, 4, true, 2, CharacterClass.SOLDIER);
		assertNotEquals(other, sector);
	}

	@Test
	public void testEquals_DifferentAttackerSpawn() {
		SimpleSector other = new SimpleSector(10, true, false, 5, 4, true, 2, CharacterClass.SOLDIER);
		assertNotEquals(other, sector);
	}

	@Test
	public void testEquals_DifferentAttackBonus() {
		SimpleSector other = new SimpleSector(10, true, true, 10, 4, true, 2, CharacterClass.SOLDIER);
		assertNotEquals(other, sector);
	}

	@Test
	public void testEquals_DifferentDefendBonus() {
		SimpleSector other = new SimpleSector(10, true, true, 5, 10, true, 2, CharacterClass.SOLDIER);
		assertNotEquals(other, sector);
	}

	@Test
	public void testEquals_DifferentFlag() {
		SimpleSector other = new SimpleSector(10, true, true, 5, 4, false, 2, CharacterClass.SOLDIER);
		assertNotEquals(other, sector);
	}

	@Test
	public void testEquals_DifferentClassBonus() {
		SimpleSector other = new SimpleSector(10, true, true, 5, 4, true, 5, CharacterClass.SOLDIER);
		assertNotEquals(other, sector);
	}

	@Test
	public void testEquals_DifferentClassType() {
		SimpleSector other = new SimpleSector(10, true, true, 5, 4, true, 2, CharacterClass.DOC);
		assertNotEquals(other, sector);
	}

	@Test
	public void testAddObserver() {
		SectorObserver observer = mock(SectorObserver.class);
		sector.addObserver(observer);
	}

	@Test
	public void testRemoveObserver() {
		SectorObserver observer = mock(SectorObserver.class);
		sector.addObserver(observer);
		sector.removeObserver(observer);
	}
}
