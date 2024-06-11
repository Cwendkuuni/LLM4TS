package de.outstare.fortbattleplayer.model.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorObserver;

public class SimpleSectorTest {
	private SimpleSector sector;
	
	@Before
	public void setup() {
		sector = new SimpleSector(5, true, false, 10, 5, true, 3, CharacterClass.ATTACKER);
	}
	
	@Test
	public void testFree() {
		sector.gainControl(CombatantSide.ATTACKER);
		sector.free();
		assertNull(sector.getOccupier());
		assertFalse(sector.isOccupied());
	}
	
	@Test
	public void testGainControl() {
		sector.gainControl(CombatantSide.DEFENDER);
		assertEquals(CombatantSide.DEFENDER, sector.getOccupier());
		assertTrue(sector.isOccupied());
	}
	
	@Test
	public void testIsOccupied() {
		assertFalse(sector.isOccupied());
		sector.gainControl(CombatantSide.ATTACKER);
		assertTrue(sector.isOccupied());
	}
	
	@Test
	public void testGetHeight() {
		assertEquals(5, sector.getHeight());
	}
	
	@Test
	public void testIsDefenderSpawn() {
		assertFalse(sector.isDefenderSpawn());
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
		SectorBonus bonus = sector.getBonus(CharacterClass.DEFENDER);
		assertEquals(15, bonus.getAttackBonus());
		assertEquals(8, bonus.getDefenseBonus());
		assertEquals(0, bonus.getDamageBonus());
	}
	
	@Test
	public void testGetAreas() {
		assertEquals(0, sector.getAreas().size());
		Area area = new Area();
		sector._addArea(area);
		assertEquals(1, sector.getAreas().size());
		assertEquals(area, sector.getAreas().iterator().next());
	}
	
	@Test
	public void testAddObserver() {
		SectorObserver observer = new SectorObserver() {
			@Override
			public void occupierChanged() {
				// do nothing
			}
		};
		sector.addObserver(observer);
		assertEquals(1, sector.observers.size());
		assertEquals(observer, sector.observers.iterator().next());
	}
	
	@Test
	public void testRemoveObserver() {
		SectorObserver observer = new SectorObserver() {
			@Override
			public void occupierChanged() {
				// do nothing
			}
		};
		sector.addObserver(observer);
		assertEquals(1, sector.observers.size());
		assertEquals(observer, sector.observers.iterator().next());
		sector.removeObserver(observer);
		assertEquals(0, sector.observers.size());
	}
	
	@Test
	public void testHashCode() {
		assertEquals(67326990, sector.hashCode());
	}
	
	@Test
	public void testEquals() {
		SimpleSector equalSector = new SimpleSector(5, true, false, 10, 5, true,
				3, CharacterClass.ATTACKER);
		assertEquals(sector, equalSector);
		
		SimpleSector differentSector = new SimpleSector(4, true, false, 10, 5, true,
				3, CharacterClass.ATTACKER);
		assertFalse(sector.equals(differentSector));
	}
}