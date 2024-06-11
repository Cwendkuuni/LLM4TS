package de.outstare.fortbattleplayer.model.impl;

/*
 Copyright (c) 2010 Daniel Raap

 Permission is hereby granted, free of charge, to any person obtaining a copy
 of this software and associated documentation files (the "Software"), to deal
 in the Software without restriction, including without limitation the rights
 to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 copies of the Software, and to permit persons to whom the Software is
 furnished to do so, subject to the following conditions:

 The above copyright notice and this permission notice shall be included in
 all copies or substantial portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 THE SOFTWARE.
 */

import java.util.HashSet;
import java.util.Set;

import de.outstare.fortbattleplayer.model.Area;
import de.outstare.fortbattleplayer.model.CharacterClass;
import de.outstare.fortbattleplayer.model.Combatant;
import de.outstare.fortbattleplayer.model.CombatantSide;
import de.outstare.fortbattleplayer.model.Sector;
import de.outstare.fortbattleplayer.model.SectorBonus;
import de.outstare.fortbattleplayer.model.SectorBonus.BonusType;
import de.outstare.fortbattleplayer.model.SectorObserver;
import de.outstare.fortbattleplayer.model.Weapon;

/**
 * 
 * @author daniel
 */
public class SimpleSector implements Sector {
	private final int height;
	private final boolean defenderSpawn;
	private final boolean attackerSpawn;
	private final int attackBonus;
	private final int defendBonus;
	private final boolean flag;
	private final int classBonus;
	private final CharacterClass classType;

	private CombatantSide occupier = null;
	public int occupierCount = 0;
	private final Set<Area> areas = new HashSet<Area>();
	public final Set<SectorObserver> observers = new HashSet<SectorObserver>();

	/**
	 * @param height
	 * @param defenderSpawn
	 * @param attackerSpawn
	 * @param attackerBonus
	 * @param defenderBonus
	 * @param flag
	 * @param classBonus
	 * @param bonusClass
	 */
	public SimpleSector(final int height, final boolean defenderSpawn, final boolean attackerSpawn,
	        final int attackerBonus, final int defenderBonus, final boolean flag, final int classBonus,
	        final CharacterClass bonusClass) {
		this.height = height;
		this.defenderSpawn = defenderSpawn;
		this.attackerSpawn = attackerSpawn;
		attackBonus = attackerBonus;
		defendBonus = defenderBonus;
		this.flag = flag;
		this.classBonus = classBonus;
		classType = bonusClass;
	}

	/**
	 * @see Sector#free()
	 */
	public void free() {
		if (occupierCount > 0) {
			occupierCount--;
		}
		assert occupierCount >= 0 : "more combatants lefts this sector than enter it";

		if (occupierCount == 0) {
			occupier = null;
			occupierChanged();
		}
	}

	private void occupierChanged() {
		// update borders to show change of occupier
		for (final SectorObserver observer : observers) {
			observer.occupierChanged();
		}
	}

	/**
	 * @see Sector#gainControl(CombatantSide)
	 */
	public void gainControl(final CombatantSide side) {
		occupierCount++;
		if (occupier != side) {
			occupier = side;
			occupierChanged();
		}
	}

	/**
	 * @see Sector#getOccupier()
	 */
	public CombatantSide getOccupier() {
		return occupier;
	}

	/**
	 * @see Sector#isOccupied()
	 */
	public boolean isOccupied() {
		return occupier != null;
	}

	/**
	 * @see Sector#getHeight()
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * @return the defenderSpawn
	 */
	public boolean isDefenderSpawn() {
		return defenderSpawn;
	}

	/**
	 * @return the attackerSpawn
	 */
	public boolean isAttackerSpawn() {
		return attackerSpawn;
	}

	/**
	 * @return the flag
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @see Sector#getBonus(CharacterClass)
	 */
	public SectorBonus getBonus(final CharacterClass charClass) {
		return new SectorBonus(getAttackBonus(charClass), getDefenseBonus(charClass), getDamageBonus());
	}

	/**
	 * @return
	 */
	protected int getDamageBonus() {
		return getBonusFromWeapons(BonusType.DAMAGE);
	}

	private int getBonusFromWeapons(final BonusType type) {
		int bonus = 0;
		for (final Area area : areas) {
			if (area.isOccupied()) {
				final Combatant combatant = area.getOccupier();
				final Weapon weapon = combatant.getWeapon();
				final int sectorBonus = weapon.getSectorBonus(type);
				if (sectorBonus != 0) {
					// only count first Golden Gun
					bonus = sectorBonus;
					break;
				}
			}
		}
		return bonus;
	}

	/**
	 * @param charClass
	 * @return
	 */
	public int getAttackBonus(final CharacterClass charClass) {
		int bonus = attackBonus + classBonus(charClass);
		bonus += getBonusFromWeapons(BonusType.ATTACK);
		return bonus;
	}

	/**
	 * @param charClass
	 * @return
	 */
	protected int getDefenseBonus(final CharacterClass charClass) {
		int bonus = defendBonus + classBonus(charClass);
		bonus += getBonusFromWeapons(BonusType.DEFENSE);
		return bonus;
	}

	/**
	 * @param charClass
	 * @return
	 */
	protected int classBonus(final CharacterClass charClass) {
		if (classType != charClass) {
			return 0;
		}
		return classBonus;
	}

	/**
	 * @see Sector#getAreas()
	 */
	public Set<Area> getAreas() {
		return areas;
	}

	/**
	 * @see Sector#_addArea(Area)
	 */
	public void _addArea(final Area area) {
		areas.add(area);
	}

	/**
	 * @see Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((areas == null) ? 0 : areas.hashCode());
		result = prime * result + attackBonus;
		result = prime * result + (attackerSpawn ? 1231 : 1237);
		result = prime * result + classBonus;
		result = prime * result + ((classType == null) ? 0 : classType.hashCode());
		result = prime * result + defendBonus;
		result = prime * result + (defenderSpawn ? 1231 : 1237);
		result = prime * result + (flag ? 1231 : 1237);
		result = prime * result + height;
		return result;
	}

	/**
	 * @see Object#equals(Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof SimpleSector)) {
			return false;
		}
		final SimpleSector other = (SimpleSector) obj;
		if (areas == null) {
			if (other.areas != null) {
				return false;
			}
		} else if (!areas.equals(other.areas)) {
			return false;
		}
		if (attackBonus != other.attackBonus) {
			return false;
		}
		if (attackerSpawn != other.attackerSpawn) {
			return false;
		}
		if (classBonus != other.classBonus) {
			return false;
		}
		if (classType != other.classType) {
			return false;
		}
		if (defendBonus != other.defendBonus) {
			return false;
		}
		if (defenderSpawn != other.defenderSpawn) {
			return false;
		}
		if (flag != other.flag) {
			return false;
		}
		if (height != other.height) {
			return false;
		}
		return true;
	}

	/**
	 * @see Sector#addObserver(SectorObserver)
	 */
	public void addObserver(final SectorObserver observer) {
		observers.add(observer);
	}

	/**
	 * @see Sector#removeObserver(SectorObserver)
	 */
	public void removeObserver(final SectorObserver observer) {
		observers.remove(observer);
	}
}
